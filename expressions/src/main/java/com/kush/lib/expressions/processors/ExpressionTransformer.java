package com.kush.lib.expressions.processors;

import static com.kush.utils.commons.ObjectUtils.nullOrGet;

import com.kush.lib.expressions.Expression;

public interface ExpressionTransformer {

    Expression transform(Expression expression);

    default <E extends Expression> E transform(Expression expression, Class<E> resultType) {
        return nullOrGet(transform(expression), resultType::cast);
    }
}
