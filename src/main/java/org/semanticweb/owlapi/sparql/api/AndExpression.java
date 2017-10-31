package org.semanticweb.owlapi.sparql.api;

import com.google.common.base.Objects;
import org.semanticweb.owlapi.sparql.algebra.AlgebraEvaluationContext;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/07/2012
 */
public class AndExpression extends BinaryExpression implements Expression {

    public AndExpression(Expression left, Expression right) {
        super(left, right);
    }

    public EvaluationResult evaluate(SolutionMapping sm, AlgebraEvaluationContext evaluationContext) {
        return evaluateAsEffectiveBooleanValue(sm, evaluationContext);
    }

    public EvaluationResult evaluateAsEffectiveBooleanValue(SolutionMapping sm, AlgebraEvaluationContext evaluationContext) {
        EvaluationResult leftEval = getLeft().evaluateAsEffectiveBooleanValue(sm, evaluationContext);
        if(leftEval.isError()) {
            return leftEval;
        }
        EvaluationResult rightEval = getRight().evaluateAsEffectiveBooleanValue(sm, evaluationContext);
        if(rightEval.isError()) {
            return rightEval;
        }
        return EvaluationResult.getBoolean(leftEval.isTrue() && rightEval.isTrue());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getLeft(), getRight());
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof AndExpression)) {
            return false;
        }
        AndExpression other = (AndExpression) obj;
        return this.getLeft().equals(other.getLeft()) && this.getRight().equals(other.getRight());
    }

    @Override
    public <R, E extends Throwable, C> R accept(ExpressionVisitor<R, E, C> visitor, C context) throws E {
        return visitor.visit(this, context);
    }
}
