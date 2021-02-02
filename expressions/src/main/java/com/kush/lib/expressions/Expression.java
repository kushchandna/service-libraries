package com.kush.lib.expressions;

import java.util.Collection;

public interface Expression {

    Collection<Expression> getChildren();
}
