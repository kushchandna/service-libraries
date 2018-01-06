package com.kush.lib.persistence.api;

import java.util.Collection;
import java.util.Iterator;

import com.kush.lib.service.api.Identifiable;
import com.kush.lib.service.api.Identifier;

public class DelegatingPersistor<T extends Identifiable> implements Persistor<T> {

    private final Persistor<T> delegate;

    public DelegatingPersistor(Persistor<T> delegate) {
        this.delegate = delegate;
    }

    @Override
    public T save(T object) {
        return delegate.save(object);
    }

    @Override
    public T fetch(Identifier id) {
        return delegate.fetch(id);
    }

    @Override
    public Iterator<T> fetch(Collection<Identifier> ids) {
        return delegate.fetch(ids);
    }

    @Override
    public Iterator<T> fetchAll() {
        return delegate.fetchAll();
    }

    @Override
    public boolean remove(Identifier id) {
        return delegate.remove(id);
    }
}
