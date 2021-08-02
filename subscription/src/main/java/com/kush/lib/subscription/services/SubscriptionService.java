package com.kush.lib.subscription.services;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

import com.kush.commons.id.Identifier;
import com.kush.lib.persistence.api.PersistorOperationFailedException;
import com.kush.lib.subscription.entities.Subscription;
import com.kush.lib.subscription.persistors.SubscriptionPersister;
import com.kush.service.BaseService;
import com.kush.service.annotations.Service;
import com.kush.service.annotations.ServiceMethod;
import com.kush.service.auth.AuthenticationRequired;
import com.kush.utils.exceptions.ValidationFailedException;

@Service
public class SubscriptionService extends BaseService {

    @AuthenticationRequired
    @ServiceMethod
    public Subscription subscribe(Identifier userId) throws ValidationFailedException, PersistorOperationFailedException {
        Identifier currentUserId = getCurrentUser().getId();
        if (currentUserId.equals(userId)) {
            throw new ValidationFailedException("Can not subscribe to self");
        }
        SubscriptionPersister persistor = getInstance(SubscriptionPersister.class);
        Clock clock = getInstance(Clock.class);
        LocalDateTime dateTime = LocalDateTime.now(clock);
        return persistor.addSubscription(currentUserId, userId, dateTime);
    }

    @AuthenticationRequired
    @ServiceMethod
    public List<Subscription> getSubscribed() throws PersistorOperationFailedException {
        Identifier currentUserId = getCurrentUser().getId();
        SubscriptionPersister persistor = getInstance(SubscriptionPersister.class);
        return persistor.getSubscribed(currentUserId);
    }

    @AuthenticationRequired
    @ServiceMethod
    public List<Subscription> getSubscribers() throws PersistorOperationFailedException {
        Identifier currentUserId = getCurrentUser().getId();
        SubscriptionPersister persistor = getInstance(SubscriptionPersister.class);
        return persistor.getSubscribers(currentUserId);
    }

    @Override
    protected void processContext() {
        checkContextHasValueFor(SubscriptionPersister.class);
        addIfDoesNotExist(Clock.class, Clock.systemUTC());
    }
}
