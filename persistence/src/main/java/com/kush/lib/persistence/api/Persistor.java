package com.kush.lib.persistence.api;

import java.util.Collection;
import java.util.Iterator;

import com.kush.lib.service.api.Identifiable;
import com.kush.lib.service.api.Identifier;

public interface Persistor<T extends Identifiable> {

    T save(T object) throws PersistorOperationFailedException;

    T fetch(Identifier id) throws PersistorOperationFailedException;

    Iterator<T> fetch(Collection<Identifier> ids) throws PersistorOperationFailedException;

    Iterator<T> fetchAll() throws PersistorOperationFailedException;

    boolean remove(Identifier id) throws PersistorOperationFailedException;
}
