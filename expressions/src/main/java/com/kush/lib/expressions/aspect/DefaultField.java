package com.kush.lib.expressions.aspect;

import com.kush.lib.expressions.Accessor;
import com.kush.lib.expressions.types.Type;

class DefaultField<T> implements Field<T> {

    private final String name;
    private final Type type;
    private final Accessor<T> accessor;

    public DefaultField(String name, Type type, Accessor<T> accessor) {
        this.name = name;
        this.type = type;
        this.accessor = accessor;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public Accessor<T> getAccessor() {
        return accessor;
    }

}
