package org.semanticweb.owlapi.sparql.api;

import com.google.common.base.Objects;
import org.semanticweb.owlapi.sparql.algebra.EvaluationContext;

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

    public EvaluationResult evaluate(SolutionMapping sm, EvaluationContext evaluationContext) {
        return evaluateAsEffectiveBooleanValue(sm, evaluationContext);
    }

    public boolean canEvaluateAsBoolean(SolutionMapping sm) {
        return true;
    }

    public EvaluationResult evaluateAsEffectiveBooleanValue(SolutionMapping sm, EvaluationContext evaluationContext) {
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

    public boolean canEvaluateAsStringLiteral(SolutionMapping sm) {
        return false;
    }

    public EvaluationResult evaluateAsStringLiteral(SolutionMapping sm, EvaluationContext evaluationContext) {
        return EvaluationResult.getError();
    }

    public boolean canEvaluateAsSimpleLiteral(SolutionMapping sm) {
        return false;
    }

    public EvaluationResult evaluateAsSimpleLiteral(SolutionMapping sm, EvaluationContext evaluationContext) {
        return EvaluationResult.getError();
    }

    public boolean canEvaluateAsNumeric(SolutionMapping sm) {
        return false;
    }

    public EvaluationResult evaluateAsNumeric(SolutionMapping sm, EvaluationContext evaluationContext) {
        return EvaluationResult.getError();
    }

    public boolean canEvaluateAsDateTime(SolutionMapping sm) {
        return false;
    }

    public EvaluationResult evaluateAsDateTime(SolutionMapping sm, EvaluationContext evaluationContext) {
        return EvaluationResult.getError();
    }

    public boolean canEvaluateAsIRI(SolutionMapping sm) {
        return false;
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
    public EvaluationResult evaluateAsLiteral(SolutionMapping sm, EvaluationContext evaluationContext) {
        return EvaluationResult.getError();
    }


    @Override
    public EvaluationResult evaluateAsIRI(SolutionMapping sm, EvaluationContext evaluationContext) {
        return EvaluationResult.getError();
    }

    @Override
    public <R, E extends Throwable, C> R accept(ExpressionVisitor<R, E, C> visitor, C context) throws E {
        return visitor.visit(this, context);
    }
}
