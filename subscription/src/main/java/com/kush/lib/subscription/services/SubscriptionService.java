package com.kush.lib.subscription.services;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

import com.kush.lib.persistence.api.PersistorOperationFailedException;
import com.kush.lib.service.server.BaseService;
import com.kush.lib.service.server.annotations.Service;
import com.kush.lib.service.server.annotations.ServiceMethod;
import com.kush.lib.service.server.authentication.AuthenticationRequired;
import com.kush.lib.subscription.entities.Subscription;
import com.kush.lib.subscription.persistors.SubscriptionPersistor;
import com.kush.utils.exceptions.ValidationFailedException;
import com.kush.utils.id.Identifier;

@Service(name = "Subscription")
public class SubscriptionService extends BaseService {

    @AuthenticationRequired
    @ServiceMethod(name = "Subscribe")
    public Subscription subscribe(Identifier userId) throws ValidationFailedException, PersistorOperationFailedException {
        Identifier currentUserId = getCurrentUser().getId();
        if (currentUserId.equals(userId)) {
            throw new ValidationFailedException("Can not subscribe to self");
        }
        SubscriptionPersistor persistor = getInstance(SubscriptionPersistor.class);
        Clock clock = getInstance(Clock.class);
        LocalDateTime dateTime = LocalDateTime.now(clock);
        return persistor.addSubscription(currentUserId, userId, dateTime);
    }

    @AuthenticationRequired
    @ServiceMethod(name = "Get Subscribed")
    public List<Subscription> getSubscribed() throws PersistorOperationFailedException {
        Identifier currentUserId = getCurrentUser().getId();
        SubscriptionPersistor persistor = getInstance(SubscriptionPersistor.class);
        return persistor.getSubscribed(currentUserId);
    }

    @AuthenticationRequired
    @ServiceMethod(name = "Get Subscribers")
    public List<Subscription> getSubscribers() throws PersistorOperationFailedException {
        Identifier currentUserId = getCurrentUser().getId();
        SubscriptionPersistor persistor = getInstance(SubscriptionPersistor.class);
        return persistor.getSubscribers(currentUserId);
    }
}
