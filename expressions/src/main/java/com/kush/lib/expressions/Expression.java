package com.kush.lib.indexing.expressions;

import java.util.Collection;

public interface Expression {

    Collection<Expression> getChildren();
}
