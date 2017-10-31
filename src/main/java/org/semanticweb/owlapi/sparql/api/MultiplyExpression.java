package org.semanticweb.owlapi.sparql.api;

import org.semanticweb.owlapi.sparql.builtin.BasicNumericType;
import org.semanticweb.owlapi.sparql.algebra.AlgebraEvaluationContext;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 30/07/2012
 */
public class MultiplyExpression extends BinaryExpression implements Expression {

    public MultiplyExpression(Expression left, Expression right) {
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
        double leftValue = leftEval.asNumeric();
        double rightValue = rightEval.asNumeric();
        Literal leftLiteral = leftEval.asLiteral();
        Literal rightLiteral = rightEval.asLiteral();
        Datatype returnType = BasicNumericType.getMostSpecificBasicNumericType(leftLiteral.getDatatype(), rightLiteral.getDatatype());
        return EvaluationResult.getResult(BasicNumericType.getLiteralOfBasicNumericType(leftValue * rightValue, returnType));

    }

    public boolean canEvaluateAsBoolean(SolutionMapping sm) {
        return false;
    }

    public EvaluationResult evaluateAsEffectiveBooleanValue(SolutionMapping sm, AlgebraEvaluationContext evaluationContext) {
        return EvaluationResult.getError();
    }

    public boolean canEvaluateAsStringLiteral(SolutionMapping sm) {
        return false;
    }

    public boolean canEvaluateAsSimpleLiteral(SolutionMapping sm) {
        return false;
    }

    public boolean canEvaluateAsNumeric(SolutionMapping sm) {
        return true;
    }

    public boolean canEvaluateAsDateTime(SolutionMapping sm) {
        return false;
    }

    public boolean canEvaluateAsIRI(SolutionMapping sm) {
        return false;
    }

    @Override
    public EvaluationResult evaluateAsLiteral(SolutionMapping sm, AlgebraEvaluationContext evaluationContext) {
        return EvaluationResult.getError();
    }

    @Override
    public <R, E extends Throwable, C> R accept(ExpressionVisitor<R, E, C> visitor, C context) throws E {
        return visitor.visit(this, context);
    }
}
