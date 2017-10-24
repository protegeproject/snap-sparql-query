package org.semanticweb.owlapi.sparql.api;

import org.semanticweb.owlapi.sparql.builtin.BasicNumericType;
import org.semanticweb.owlapi.sparql.algebra.EvaluationContext;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 30/07/2012
 */
public class MinusExpression extends BinaryExpression implements Expression {

    public MinusExpression(Expression left, Expression right) {
        super(left, right);
    }

    public EvaluationResult evaluate(SolutionMapping sm, EvaluationContext evaluationContext) {
        return evaluateAsNumeric(sm, evaluationContext);
    }

    public boolean canEvaluateAsBoolean(SolutionMapping sm) {
        return false;
    }

    public EvaluationResult evaluateAsEffectiveBooleanValue(SolutionMapping sm, EvaluationContext evaluationContext) {
        return EvaluationResult.getError();
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
        return true;
    }

    public EvaluationResult evaluateAsNumeric(SolutionMapping sm, EvaluationContext evaluationContext) {
        EvaluationResult leftEval = getLeft().evaluateAsNumeric(sm, evaluationContext);
        if(leftEval.isError()) {
            return leftEval;
        }
        EvaluationResult rightEval = getRight().evaluateAsNumeric(sm, evaluationContext);
        if(rightEval.isError()) {
            return rightEval;
        }
        double leftValue = leftEval.asNumeric();
        double rightValue = rightEval.asNumeric();
        Literal leftLiteral = leftEval.asLiteral();
        Literal rightLiteral = rightEval.asLiteral();
        Datatype returnType = BasicNumericType.getMostSpecificBasicNumericType(leftLiteral.getDatatype(), rightLiteral.getDatatype());
        return EvaluationResult.getResult(BasicNumericType.getLiteralOfBasicNumericType(leftValue - rightValue, returnType));

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
