package org.semanticweb.owlapi.sparql.api;

import org.semanticweb.owlapi.sparql.algebra.AlgebraEvaluationContext;
import org.semanticweb.owlapi.sparql.builtin.DateTime;

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

    public EvaluationResult evaluate(Expression left, Expression right, SolutionMapping sm, AlgebraEvaluationContext evaluationContext) {
        // Numeric - Numeric
        // xsd:dateTime - xsd:dateTime
        // SimpleLiteral - SimpleLiteral
        // xsd:string - xsd:string
        // xsd:boolean - xsd:boolean

        EvaluationResult leftEval = left.evaluate(sm, evaluationContext);
        if(leftEval.isError()) {
            return EvaluationResult.getError();
        }
        EvaluationResult rightEval = right.evaluate(sm, evaluationContext);
        if(rightEval.isError()) {
            return EvaluationResult.getError();
        }

        RDFTerm leftTerm = leftEval.getResult();
        RDFTerm rightTerm = rightEval.getResult();

        if(leftTerm.isNumeric() && rightTerm.isNumeric()) {
            return evaluateNumeric(leftEval, rightEval);
        }

        if(leftTerm.isXSDDateTime() && rightTerm.isXSDDateTime()) {
            return evaluateDateTime(leftEval, rightEval);
        }

        // This case covers
        //     simple literal, simple literal
        //     xsd:string, xsd:string
        //     xsd:anyURI, xsd:anyURI (since this is promotable to xsd:string according to the XPath spec)
        //     subtype of xsd:string, subtype of xsd:string  (not clear to me whether this is correct!)
        if(leftTerm.isLiteralWithoutLangTagOrWithEmptyLangTag()
                && leftTerm.isSubTypeOfOrPromotableToRdfPlainLiteral()
                && rightTerm.isLiteralWithoutLangTagOrWithEmptyLangTag()
                && rightTerm.isSubTypeOfOrPromotableToRdfPlainLiteral()) {
            return evaluateStringLiteral(leftEval.asSimpleLiteral(), rightEval.asSimpleLiteral());
        }

        if(leftTerm.isXSDBoolean() && rightTerm.isXSDBoolean()) {
            return evaluateBoolean(leftEval, rightEval);
        }

        if(leftTerm instanceof AtomicIRI && rightTerm instanceof AtomicIRI) {
            return evaluateIri((AtomicIRI) leftTerm, (AtomicIRI) rightTerm);
        }

        if(leftTerm instanceof AnonymousIndividual && rightTerm instanceof AnonymousIndividual) {
            return evaluateAnonymousIndividual((AnonymousIndividual) leftTerm, (AnonymousIndividual) rightTerm);
        }

        if(leftTerm instanceof Literal && rightTerm instanceof Literal) {
            return evaluateLiteral((Literal) leftTerm, (Literal) rightTerm);
        }
        return EvaluationResult.getError();
    }

    private EvaluationResult evaluateLiteral(Literal left, Literal right) {

        switch (this) {
            case EQUAL:
                return EvaluationResult.getBoolean(equalLiterals(left, right));
            case NOT_EQUAL:
                return EvaluationResult.getBoolean(!equalLiterals(left, right));
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

    private boolean equalLiterals(Literal left, Literal right) {
        // The strings of the two lexical forms compare equal, character by character.
        if(!left.getLexicalForm().equals(right.getLexicalForm())) {
            return false;
        }
        // Either both or neither have language tags.
        // The language tags, if any, compare equal.
        if(!left.getLang().equals(right.getLang())) {
            return false;
        }
        // Either both or neither have datatype URIs.
        // The two datatype URIs, if any, compare equal, character by character.
        return left.getDatatype().equals(right.getDatatype());
    }

    private EvaluationResult evaluateAnonymousIndividual(AnonymousIndividual left, AnonymousIndividual right) {
        switch (this) {
            case EQUAL:
                return EvaluationResult.getBoolean(left.getIdentifier().equals(right.getIdentifier()));
            case NOT_EQUAL:
                return EvaluationResult.getBoolean(!left.getIdentifier().equals(right.getIdentifier()));
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

    private EvaluationResult evaluateIri(AtomicIRI left, AtomicIRI right) {
        switch (this) {
            case EQUAL:
                return EvaluationResult.getBoolean(left.equals(right));
            case NOT_EQUAL:
                return EvaluationResult.getBoolean(!left.equals(right));
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

    private EvaluationResult evaluateDateTime(EvaluationResult leftEval, EvaluationResult rightEval) {
        DateTime leftValue = leftEval.asDateTime();
        DateTime rightValue = rightEval.asDateTime();

        switch (this) {
            case EQUAL:
                return EvaluationResult.getBoolean(leftValue.getEpochMilli() == rightValue.getEpochMilli());
            case NOT_EQUAL:
                return EvaluationResult.getBoolean(leftValue.getEpochMilli() != rightValue.getEpochMilli());
            case LESS_THAN:
                return EvaluationResult.getBoolean(leftValue.getEpochMilli() < rightValue.getEpochMilli());
            case LESS_THAN_OR_EQUAL:
                return EvaluationResult.getBoolean(leftValue.getEpochMilli() <= rightValue.getEpochMilli());
            case GREATER_THAN:
                return EvaluationResult.getBoolean(leftValue.getEpochMilli() > rightValue.getEpochMilli());
            case GREATER_THAN_OR_EQUAL:
                return EvaluationResult.getBoolean(leftValue.getEpochMilli() >= rightValue.getEpochMilli());
            default:
                throw new RuntimeException("Unknown enum value");
        }
    }

    private EvaluationResult evaluateStringLiteral(String leftValue, String rightValue) {
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
                return EvaluationResult.getBoolean(!left && right);
            case LESS_THAN_OR_EQUAL:
                return EvaluationResult.getBoolean(!(left && !right));
            case GREATER_THAN:
                return EvaluationResult.getBoolean(left && !right);
            case GREATER_THAN_OR_EQUAL:
                return EvaluationResult.getBoolean(!(!left && right));
            default:
                throw new RuntimeException("Unknown enum value");
        }
    }
}
