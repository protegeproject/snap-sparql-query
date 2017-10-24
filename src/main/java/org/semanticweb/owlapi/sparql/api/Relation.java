package org.semanticweb.owlapi.sparql.api;

import org.semanticweb.owlapi.sparql.sparqldl.EvaluationContext;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 27/07/2012
 */
public enum Relation {

    EQUAL("="),

    NOT_EQUAL("!="),

    LESS_THAN("<"),
    
    LESS_THAN_OR_EQUAL("<="),
    
    GREATER_THAN(">"),
    
    GREATER_THAN_OR_EQUAL(">=");
    
    private String symbolicForm;

    private Relation(String symbolicForm) {
        this.symbolicForm = symbolicForm;
    }

    public String getSymbolicForm() {
        return symbolicForm;
    }

    public EvaluationResult evaluate(Expression left, Expression right, SolutionMapping sm, EvaluationContext evaluationContext) {
        EvaluationResult leftEval = left.evaluateAsNumeric(sm, evaluationContext);
        EvaluationResult rightEval = right.evaluateAsNumeric(sm, evaluationContext);
        if(!leftEval.isError() && !rightEval.isError()) {
            return evaluateNumeric(leftEval, rightEval);
        }
        EvaluationResult litLeft = left.evaluateAsSimpleLiteral(sm, evaluationContext);
        EvaluationResult litRight = right.evaluateAsSimpleLiteral(sm, evaluationContext);
        if(!litLeft.isError() && !litRight.isError()) {
            return evaluateSimpleLiteral(litLeft, litRight);
        }
        EvaluationResult boolLeft = left.evaluateAsEffectiveBooleanValue(sm, evaluationContext);
        EvaluationResult boolRight = right.evaluateAsEffectiveBooleanValue(sm, evaluationContext);
        if(!boolLeft.isError() && !boolRight.isError()) {
            return evaluateBoolean(boolLeft, boolRight);
        }
        return EvaluationResult.getError();
    }

    private EvaluationResult evaluateNumeric(EvaluationResult leftEval, EvaluationResult rightEval) {
        double leftValue = leftEval.asNumeric();
        double rightValue = rightEval.asNumeric();

        switch (this) {
            case EQUAL:
                return EvaluationResult.getBoolean(leftValue == rightValue);
            case NOT_EQUAL:
                return EvaluationResult.getBoolean(leftValue != rightValue);
            case LESS_THAN:
                return EvaluationResult.getBoolean(leftValue < rightValue);
            case LESS_THAN_OR_EQUAL:
                return EvaluationResult.getBoolean(leftValue <= rightValue);
            case GREATER_THAN:
                return EvaluationResult.getBoolean(leftValue > rightValue);
            case GREATER_THAN_OR_EQUAL:
                return EvaluationResult.getBoolean(leftValue >= rightValue);
            default:
                throw new RuntimeException("Unknown enum value");
        }
    }

    private EvaluationResult evaluateSimpleLiteral(EvaluationResult leftEval, EvaluationResult rightEval) {
        String leftValue = leftEval.asSimpleLiteral();
        String rightValue = rightEval.asSimpleLiteral();

        switch (this) {
            case EQUAL:
                return EvaluationResult.getBoolean(leftValue.equals(rightValue));
            case NOT_EQUAL:
                return EvaluationResult.getBoolean(!leftValue.equals(rightValue));
            case LESS_THAN:
                return EvaluationResult.getBoolean(leftValue.compareTo(rightValue) < 0);
            case LESS_THAN_OR_EQUAL:
                return EvaluationResult.getBoolean(leftValue.compareTo(rightValue) <= 0);
            case GREATER_THAN:
                return EvaluationResult.getBoolean(leftValue.compareTo(rightValue) > 0);
            case GREATER_THAN_OR_EQUAL:
                return EvaluationResult.getBoolean(leftValue.compareTo(rightValue) >= 0);
            default:
                throw new RuntimeException("Unknown enum value");
        }
    }

    private EvaluationResult evaluateBoolean(EvaluationResult leftEval, EvaluationResult rightEval) {
        boolean left = leftEval.isTrue();
        boolean right = rightEval.isTrue();

        switch (this) {
            case EQUAL:
                return EvaluationResult.getBoolean(left == right);
            case NOT_EQUAL:
                return EvaluationResult.getBoolean(left != right);
            case LESS_THAN:
                return EvaluationResult.getError();
            case LESS_THAN_OR_EQUAL:
                return EvaluationResult.getError();
            case GREATER_THAN:
                return EvaluationResult.getError();
            case GREATER_THAN_OR_EQUAL:
                return EvaluationResult.getError();
            default:
                throw new RuntimeException("Unknown enum value");
        }
    }
}
