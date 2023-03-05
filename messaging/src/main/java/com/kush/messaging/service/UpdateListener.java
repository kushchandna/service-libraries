package com.kush.messaging.service;

public interface UpdateListener<T> {
	
	void onUpdate(T value);
}
