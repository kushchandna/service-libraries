package com.kush.lib.subscription.entities;

import java.time.LocalDateTime;

import com.kush.utils.id.Identifiable;
import com.kush.utils.id.Identifier;

public class Subscription implements Identifiable {

    private final Identifier subscriptionId;
    private final Identifier subscriber;
    private final Identifier subscribed;
    private final LocalDateTime subscriptionTime;

    public Subscription(Identifier subscriber, Identifier subscribed, LocalDateTime subscriptionTime) {
        this(Identifier.NULL, subscriber, subscribed, subscriptionTime);
    }

    public Subscription(Identifier subscriptionId, Subscription subscription) {
        this(subscriptionId, subscription.getSubscriber(), subscription.getSubscribed(), subscription.getSubscriptionTime());
    }

    public Subscription(Identifier subscriptionId, Identifier subscriber, Identifier subscribed, LocalDateTime subscriptionTime) {
        this.subscriptionId = subscriptionId;
        this.subscriber = subscriber;
        this.subscribed = subscribed;
        this.subscriptionTime = subscriptionTime;
    }

    @Override
    public Identifier getId() {
        return subscriptionId;
    }

    public Identifier getSubscriber() {
        return subscriber;
    }

    public Identifier getSubscribed() {
        return subscribed;
    }

    public LocalDateTime getSubscriptionTime() {
        return subscriptionTime;
    }
}
