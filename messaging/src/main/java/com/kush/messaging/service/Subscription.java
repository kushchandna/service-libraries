package com.kush.messaging.service;

import java.io.Closeable;

public interface Subscription<T> extends Closeable {
	
	void addUpdateListener(UpdateListener<T> listener);
}
