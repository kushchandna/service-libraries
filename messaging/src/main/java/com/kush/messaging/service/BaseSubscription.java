package com.kush.messaging.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseSubscription<T> implements Subscription<T> {
	
	private final List<UpdateListener<T>> listeners = new ArrayList<>();

	@Override
	public void close() throws IOException {
		listeners.clear();
	}

	@Override
	public void addUpdateListener(UpdateListener<T> listener) {
		listeners.add(listener);
	}
	
	protected void notifyListeners(T value) {
		listeners.forEach(l -> l.onUpdate(value));
	}
}
