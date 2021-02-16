package com.kush.lib.expressions.evaluators;

import com.kush.lib.expressions.Expression;
import com.kush.lib.expressions.ExpressionEvaluator;
import com.kush.lib.expressions.ExpressionEvaluatorFactory;
import com.kush.lib.expressions.ExpressionException;
import com.kush.lib.expressions.ExpressionProcessor;
import com.kush.lib.expressions.clauses.AndExpression;
import com.kush.lib.expressions.clauses.ConstantIntExpression;
import com.kush.lib.expressions.clauses.ConstantStringExpression;
import com.kush.lib.expressions.clauses.EqualsExpression;
import com.kush.lib.expressions.clauses.FieldExpression;
import com.kush.lib.expressions.clauses.GreaterThanEqualsExpression;
import com.kush.lib.expressions.clauses.GreaterThanExpression;
import com.kush.lib.expressions.clauses.InExpression;
import com.kush.lib.expressions.clauses.LessThanEqualsExpression;
import com.kush.lib.expressions.clauses.LessThanExpression;
import com.kush.lib.expressions.clauses.NotExpression;
import com.kush.lib.expressions.clauses.OrExpression;

public class DefaultExpressionEvaluatorFactory<T> implements ExpressionEvaluatorFactory<T> {

    private final InternalExpressionEvaluatorFactory internalFactory;

    public DefaultExpressionEvaluatorFactory(FieldExpressionEvaluatorFactory<T> fieldEvaluatorFactory) {
        internalFactory = new InternalExpressionEvaluatorFactory(fieldEvaluatorFactory);
    }

    @Override
    public ExpressionEvaluator<T> create(Expression expression) throws ExpressionException {
        return internalFactory.process(expression);
    }

    private class InternalExpressionEvaluatorFactory extends ExpressionProcessor<ExpressionEvaluator<T>> {

        private final FieldExpressionEvaluatorFactory<T> fieldEvaluatorFactory;

        public InternalExpressionEvaluatorFactory(FieldExpressionEvaluatorFactory<T> fieldEvaluatorFactory) {
            this.fieldEvaluatorFactory = fieldEvaluatorFactory;
        }

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
        protected ExpressionEvaluator<T> handle(InExpression expression) throws ExpressionException {
            return new InExpressionEvaluator<>(expression, DefaultExpressionEvaluatorFactory.this);
        }

        @Override
        protected ExpressionEvaluator<T> handle(GreaterThanExpression expression) throws ExpressionException {
            return new GreaterThanExpressionEvaluator<>(expression, DefaultExpressionEvaluatorFactory.this);
        }

        @Override
        protected ExpressionEvaluator<T> handle(GreaterThanEqualsExpression expression) throws ExpressionException {
            return new GreaterThanEqualsExpressionEvaluator<>(expression, DefaultExpressionEvaluatorFactory.this);
        }

        @Override
        protected ExpressionEvaluator<T> handle(LessThanExpression expression) throws ExpressionException {
            return new LessThanExpressionEvaluator<>(expression, DefaultExpressionEvaluatorFactory.this);
        }

        @Override
        protected ExpressionEvaluator<T> handle(LessThanEqualsExpression expression) throws ExpressionException {
            return new LessThanEqualsExpressionEvaluator<>(expression, DefaultExpressionEvaluatorFactory.this);
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
