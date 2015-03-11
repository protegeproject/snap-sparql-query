package org.semanticweb.owlapi.sparql.api;

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

    public EvaluationResult evaluate(Expression left, Expression right, SolutionMapping sm) {
        EvaluationResult leftEval = left.evaluateAsNumeric(sm);
        EvaluationResult rightEval = right.evaluateAsNumeric(sm);
        if(leftEval.isError()) {
            return leftEval;
        }
        if(rightEval.isError()) {
            return rightEval;
        }

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

    private EvaluationResult evaluateAsNumeric(Expression left, Expression right, SolutionMapping sm) {
        return EvaluationResult.getError();
    }


}
