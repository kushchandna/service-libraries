package com.kush.messaging.push.signal;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

import com.kush.utils.id.Identifier;
import com.kush.utils.signaling.SignalEmitterFactory;
import com.kush.utils.signaling.SignalSpace;

public class SignalSpaceProvider {

    private final Map<Identifier, SignalSpace> signalSpaces = new HashMap<>();

    private final Executor emitterExecutor;
    private final SignalEmitterFactory emitterFactory;

    public SignalSpaceProvider(Executor emitterExecutor, SignalEmitterFactory emitterFactory) {
        this.emitterExecutor = emitterExecutor;
        this.emitterFactory = emitterFactory;
    }

    public SignalSpace getSignalSpace(Identifier userId) {
        SignalSpace signalSpace = signalSpaces.get(userId);
        if (signalSpace == null) {
            signalSpace = new SignalSpace(emitterExecutor, emitterFactory);
            signalSpaces.put(userId, signalSpace);
        }
        return signalSpace;
    }

    public void removeSignalSpace(Identifier userId) {
        signalSpaces.remove(userId);
    }
}
