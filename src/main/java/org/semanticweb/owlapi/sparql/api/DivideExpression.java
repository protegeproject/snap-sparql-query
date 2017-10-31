package org.semanticweb.owlapi.sparql.api;

import org.semanticweb.owlapi.sparql.algebra.AlgebraEvaluationContext;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 30/07/2012
 */
public class DivideExpression extends BinaryExpression implements Expression {

    public DivideExpression(Expression left, Expression right) {
        super(left, right);
    }

    public EvaluationResult evaluate(SolutionMapping sm, AlgebraEvaluationContext evaluationContext) {
        EvaluationResult leftEval = getLeft().evaluate(sm, evaluationContext).asNumericOrElseError();
        if(leftEval.isError()) {
            return leftEval;
        }
        EvaluationResult rightEval = getRight().evaluate(sm, evaluationContext).asNumericOrElseError();
        if(rightEval.isError()) {
            return rightEval;
        }
        double leftVal = leftEval.asNumeric();
        double rightVal = rightEval.asNumeric();
        return EvaluationResult.getDecimal(leftVal / rightVal);
    }

    public EvaluationResult evaluateAsEffectiveBooleanValue(SolutionMapping sm, AlgebraEvaluationContext evaluationContext) {
        return EvaluationResult.getError();
    }

    @Override
    public <R, E extends Throwable, C> R accept(ExpressionVisitor<R, E, C> visitor, C context) throws E {
        return visitor.visit(this, context);
    }
}
