package com.kush.lib.subscription.persistors;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import com.kush.lib.persistence.api.DelegatingPersister;
import com.kush.lib.persistence.api.Persister;
import com.kush.lib.persistence.api.PersistorOperationFailedException;
import com.kush.lib.subscription.entities.Subscription;
import com.kush.utils.id.Identifier;

public class DefaultSubscriptionPersistor extends DelegatingPersister<Subscription> implements SubscriptionPersister {

    public DefaultSubscriptionPersistor(Persister<Subscription> delegate) {
        super(delegate);
    }

    @Override
    public Subscription addSubscription(Identifier subscriber, Identifier subscribed, LocalDateTime subscriptionTime)
            throws PersistorOperationFailedException {
        checkSubscriptionDoesNotExist(subscriber, subscribed);
        return save(new Subscription(subscriber, subscribed, subscriptionTime));
    }

    @Override
    public List<Subscription> getSubscribed(Identifier subscriber) throws PersistorOperationFailedException {
        return fetch(s -> s.getSubscriber().equals(subscriber), new Comparator<Subscription>() {

            @Override
            public int compare(Subscription o1, Subscription o2) {
                return o1.getSubscriptionTime().compareTo(o2.getSubscriptionTime());
            }
        }, -1);
    }

    @Override
    public List<Subscription> getSubscribers(Identifier subscribed) throws PersistorOperationFailedException {
        return fetch(s -> s.getSubscribed().equals(subscribed), new Comparator<Subscription>() {

            @Override
            public int compare(Subscription o1, Subscription o2) {
                return o1.getSubscriptionTime().compareTo(o2.getSubscriptionTime());
            }
        }, -1);
    }

    private void checkSubscriptionDoesNotExist(Identifier subscriber, Identifier subscribed)
            throws PersistorOperationFailedException {
        List<Subscription> existing = fetch(s -> s.getSubscriber().equals(subscriber) && s.getSubscribed().equals(subscribed));
        if (!existing.isEmpty()) {
            throw new PersistorOperationFailedException("Already subscribed");
        }
    }
}
