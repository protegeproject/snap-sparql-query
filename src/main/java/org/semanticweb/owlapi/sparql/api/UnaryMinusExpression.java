package org.semanticweb.owlapi.sparql.api;

import org.semanticweb.owlapi.sparql.algebra.AlgebraEvaluationContext;

import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 30/07/2012
 */
public class UnaryMinusExpression implements Expression {

    private Expression expression;

    public UnaryMinusExpression(Expression expression) {
        this.expression = expression;
    }

    public Expression getExpression() {
        return expression;
    }

    public Set<Variable> getVariables() {
        return expression.getVariables();
    }

    @Override
    public String toString() {
        return "Expression(- " + expression + ")";
    }

    public EvaluationResult evaluate(SolutionMapping sm, AlgebraEvaluationContext evaluationContext) {
        EvaluationResult eval = expression.evaluate(sm, evaluationContext).asNumericOrElseError();
        if(eval.isError()) {
            return eval;
        }
        return EvaluationResult.getDouble(-eval.asNumeric());
    }

    public EvaluationResult evaluateAsEffectiveBooleanValue(SolutionMapping sm, AlgebraEvaluationContext evaluationContext) {
        return EvaluationResult.getError();
    }

    @Override
    public <R, E extends Throwable, C> R accept(ExpressionVisitor<R, E, C> visitor, C context) throws E {
        return visitor.visit(this, context);
    }
}
