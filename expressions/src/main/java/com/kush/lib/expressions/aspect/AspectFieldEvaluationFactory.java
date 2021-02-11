package com.kush.lib.expressions.aspect;

import static com.kush.lib.expressions.ExpressionException.exceptionWithMessage;

import java.util.Optional;

import com.kush.lib.expressions.AccessException;
import com.kush.lib.expressions.Accessor;
import com.kush.lib.expressions.ExpressionException;
import com.kush.lib.expressions.clauses.FieldExpression;
import com.kush.lib.expressions.evaluators.FieldExpressionEvaluator;
import com.kush.lib.expressions.evaluators.FieldExpressionEvaluatorFactory;
import com.kush.lib.expressions.types.Type;
import com.kush.lib.expressions.types.TypedValue;

public class AspectFieldEvaluationFactory<T> implements FieldExpressionEvaluatorFactory<T> {

    private final Aspect<T> aspect;

    public AspectFieldEvaluationFactory(Aspect<T> aspect) {
        this.aspect = aspect;
    }

    @Override
    public FieldExpressionEvaluator<T> create(FieldExpression expression) throws ExpressionException {
        String fieldName = expression.getFieldName();
        Optional<? extends Field<T>> field = aspect.getField(fieldName);
        if (!field.isPresent()) {
            throw exceptionWithMessage("No such field %s exists", fieldName);
        }
        return new AspectFieldExpressionEvaluator<>(field.get());
    }

    private static class AspectFieldExpressionEvaluator<T> implements FieldExpressionEvaluator<T> {

        private final Field<T> field;

        public AspectFieldExpressionEvaluator(Field<T> field) {
            this.field = field;
        }

        @Override
        public TypedValue evaluate(T object) throws ExpressionException {
            Accessor<T> accessor = field.getAccessor();
            try {
                return accessor.access(object);
            } catch (AccessException e) {
                throw new ExpressionException(e.getMessage(), e);
            }
        }

        @Override
        public Type evaluateType() {
            return field.getType();
        }
    }
}
