package com.kush.lib.subscription.persistors;

import java.time.LocalDateTime;
import java.util.List;

import com.kush.commons.id.Identifier;
import com.kush.lib.persistence.api.Persister;
import com.kush.lib.persistence.api.PersistorOperationFailedException;
import com.kush.lib.subscription.entities.Subscription;

public interface SubscriptionPersister extends Persister<Subscription> {

    Subscription addSubscription(Identifier subscriber, Identifier subscribed, LocalDateTime subscriptionTime)
            throws PersistorOperationFailedException;

    List<Subscription> getSubscribed(Identifier subscriber) throws PersistorOperationFailedException;

    List<Subscription> getSubscribers(Identifier subscribed) throws PersistorOperationFailedException;
}
