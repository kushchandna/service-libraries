package com.kush.lib.subscription;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.kush.lib.persistence.api.Persistor;
import com.kush.lib.persistence.helpers.InMemoryPersistor;
import com.kush.lib.service.remoting.auth.User;
import com.kush.lib.service.server.BaseServiceTest;
import com.kush.lib.subscription.entities.Subscription;
import com.kush.lib.subscription.persistors.DefaultSubscriptionPersistor;
import com.kush.lib.subscription.persistors.SubscriptionPersistor;
import com.kush.lib.subscription.services.SubscriptionService;
import com.kush.utils.id.Identifier;

public class SubscriptionServiceTest extends BaseServiceTest {

    private SubscriptionService subscriptionService;

    @Before
    public void beforeEachTest() throws Exception {
        Persistor<Subscription> subscriptionPersistor = InMemoryPersistor.forType(Subscription.class);
        addToContext(SubscriptionPersistor.class, new DefaultSubscriptionPersistor(subscriptionPersistor));

        subscriptionService = new SubscriptionService();
        registerService(subscriptionService);
    }

    @Test
    public void subscription() throws Exception {
        User user1 = getUser(0);
        User user2 = getUser(1);
        User user3 = getUser(2);

        runAuthenticatedOperation(user1, () -> {
            subscriptionService.subscribe(user2.getId());
            subscriptionService.subscribe(user3.getId());

            List<Subscription> subscribedSubscriptions = subscriptionService.getSubscribed();
            List<Identifier> subscribers = getSubscribers(subscribedSubscriptions);
            assertThat(subscribers, contains(user1.getId(), user1.getId()));
            List<Identifier> subscribed = getSubscribed(subscribedSubscriptions);
            assertThat(subscribed, contains(user2.getId(), user3.getId()));

            List<Subscription> subscribersSubscriptions = subscriptionService.getSubscribers();
            assertThat(subscribersSubscriptions, is(empty()));
        });

        runAuthenticatedOperation(user2, () -> {
            List<Subscription> subscribedSubscriptions = subscriptionService.getSubscribed();
            assertThat(subscribedSubscriptions, is(empty()));
            List<Subscription> subscribersSubscriptions = subscriptionService.getSubscribers();
            assertThat(subscribersSubscriptions, hasSize(1));
            Subscription subscribersSubscription = subscribersSubscriptions.get(0);
            assertThat(subscribersSubscription.getSubscribed(), is(equalTo(user2.getId())));
            assertThat(subscribersSubscription.getSubscriber(), is(equalTo(user1.getId())));
        });

        runAuthenticatedOperation(user3, () -> {
            List<Subscription> subscribedSubscriptions = subscriptionService.getSubscribed();
            assertThat(subscribedSubscriptions, is(empty()));
            List<Subscription> subscribersSubscriptions = subscriptionService.getSubscribers();
            assertThat(subscribersSubscriptions, hasSize(1));
            Subscription subscribersSubscription = subscribersSubscriptions.get(0);
            assertThat(subscribersSubscription.getSubscribed(), is(equalTo(user3.getId())));
            assertThat(subscribersSubscription.getSubscriber(), is(equalTo(user1.getId())));
        });
    }

    private List<Identifier> getSubscribers(List<Subscription> subscribedSubscriptions) {
        return subscribedSubscriptions.stream().map(s -> s.getSubscriber()).collect(toList());
    }

    private List<Identifier> getSubscribed(List<Subscription> subscribedSubscriptions) {
        return subscribedSubscriptions.stream().map(s -> s.getSubscribed()).collect(toList());
    }
}
