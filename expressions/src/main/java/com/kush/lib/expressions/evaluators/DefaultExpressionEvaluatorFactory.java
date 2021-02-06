package com.kush.lib.expressions.evaluators;

import com.kush.lib.expressions.Expression;
import com.kush.lib.expressions.ExpressionEvaluator;
import com.kush.lib.expressions.ExpressionEvaluatorFactory;
import com.kush.lib.expressions.ExpressionException;
import com.kush.lib.expressions.ExpressionProcessor;
import com.kush.lib.expressions.types.AndExpression;
import com.kush.lib.expressions.types.ConstantIntExpression;
import com.kush.lib.expressions.types.ConstantStringExpression;
import com.kush.lib.expressions.types.EqualsExpression;
import com.kush.lib.expressions.types.FieldExpression;
import com.kush.lib.expressions.types.NotExpression;
import com.kush.lib.expressions.types.OrExpression;

public class DefaultExpressionEvaluatorFactory<T> implements ExpressionEvaluatorFactory<T> {

    private final InternalExpressionEvaluatorFactory internalFactory;
    private final FieldExpressionEvaluatorFactory<T> fieldEvaluatorFactory;

    public DefaultExpressionEvaluatorFactory(FieldExpressionEvaluatorFactory<T> fieldEvaluatorFactory) {
        this.fieldEvaluatorFactory = fieldEvaluatorFactory;
        internalFactory = new InternalExpressionEvaluatorFactory();
    }

    @Override
    public ExpressionEvaluator<T> create(Expression expression) throws ExpressionException {
        return internalFactory.process(expression);
    }

    private class InternalExpressionEvaluatorFactory extends ExpressionProcessor<ExpressionEvaluator<T>> {

        @Override
        protected ExpressionEvaluator<T> handle(FieldExpression expression) throws ExpressionException {
            return fieldEvaluatorFactory.create(expression);
        }

        @Override
        protected ExpressionEvaluator<T> handle(AndExpression expression) throws ExpressionException {
            return new AndExpressionEvaluator<>(expression, DefaultExpressionEvaluatorFactory.this);
        }

        @Override
        protected ExpressionEvaluator<T> handle(OrExpression expression) throws ExpressionException {
            return new OrExpressionEvaluator<>(expression, DefaultExpressionEvaluatorFactory.this);
        }

        @Override
        protected ExpressionEvaluator<T> handle(NotExpression expression) throws ExpressionException {
            return new NotExpressionEvaluator<>(expression, DefaultExpressionEvaluatorFactory.this);
        }

        @Override
        protected ExpressionEvaluator<T> handle(EqualsExpression expression) throws ExpressionException {
            return new EqualsExpressionEvaluator<>(expression, DefaultExpressionEvaluatorFactory.this);
        }

        @Override
        protected ExpressionEvaluator<T> handle(ConstantStringExpression expression) {
            return new ConstantStringExpressionEvaluator<>(expression);
        }

        @Override
        protected ExpressionEvaluator<T> handle(ConstantIntExpression expression) {
            return new ConstantIntExpressionEvaluator<>(expression);
        }
    }
}
