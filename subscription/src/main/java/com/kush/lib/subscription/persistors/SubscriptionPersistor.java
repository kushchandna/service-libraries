package com.kush.lib.subscription.persistors;

import java.time.LocalDateTime;
import java.util.List;

import com.kush.lib.persistence.api.Persistor;
import com.kush.lib.persistence.api.PersistorOperationFailedException;
import com.kush.lib.subscription.entities.Subscription;
import com.kush.utils.id.Identifier;

public interface SubscriptionPersistor extends Persistor<Subscription> {

    Subscription addSubscription(Identifier subscriber, Identifier subscribed, LocalDateTime subscriptionTime)
            throws PersistorOperationFailedException;

    List<Subscription> getSubscribed(Identifier subscriber) throws PersistorOperationFailedException;

    List<Subscription> getSubscribers(Identifier subscribed) throws PersistorOperationFailedException;
}
