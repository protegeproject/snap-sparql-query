package org.semanticweb.owlapi.sparql.api;

import com.google.common.base.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 01/08/2012
 */
public class EvaluationResult {

    private static final EvaluationResult ERROR_RESULT = new EvaluationResult();

    private static final EvaluationResult FALSE = new EvaluationResult(Literal.getFalse());

    private static final EvaluationResult TRUE = new EvaluationResult(Literal.getTrue());

    private Term result;
    
    private boolean error;


    public EvaluationResult(Term result) {
        this.result = checkNotNull(result);
        this.error = false;
    }

    private EvaluationResult() {
        this.result = null;
        this.error = true;
    }

    public Term getResult() {
        return result;
    }

    public static EvaluationResult getResult(Term result) {
        return new EvaluationResult(result);
    }
    
    public static EvaluationResult getFalse() {
        return FALSE;
    }
    
    public static EvaluationResult getTrue() {
        return TRUE;
    }

    public static EvaluationResult getBoolean(boolean value) {
        if(value) {
            return getTrue();
        }
        else {
            return getFalse();
        }
    }

    public static EvaluationResult getSimpleLiteral(String literal) {
        return EvaluationResult.getResult(Literal.createRDFPlainLiteral(literal, ""));
    }

    public static EvaluationResult getDouble(double value) {
        return getResult(Literal.createDouble(value));
    }

    public static EvaluationResult getInteger(int value) {
        return getResult(Literal.createInteger(value));
    }


    public static EvaluationResult getError() {
        return ERROR_RESULT;
    }
    
    public boolean isTrue() {
        return this.equals(TRUE);
    }

    public boolean isFalse() {
        return this.equals(FALSE);
    }

    public boolean isError() {
        return error;
    }

    public Literal asLiteral() {
        return (Literal) result;
    }

    public String asSimpleLiteral() {
        if(result instanceof Literal) {
            return ((Literal) result).getLexicalForm();
        }
        else {
            throw new RuntimeException("Not a literal");
        }
    }

    public double asNumeric() throws NumberFormatException {
        if(result instanceof Literal) {
            Literal literal = (Literal) result;
            if(literal.isDatatypeNumeric()) {
                return Double.parseDouble(literal.getLexicalForm().trim());
            }
            else {
                throw new RuntimeException("Not a numeric literal");
            }
        }
        else {
            throw new RuntimeException("Not a literal");
        }
    }

    public AtomicIRI asIRI() {
        if(result instanceof AtomicIRI) {
            return (AtomicIRI) result;
        }
        else {
            throw new RuntimeException("Not an IRI");
        }
    }

    
    @Override
    public int hashCode() {
        return EvaluationResult.class.getSimpleName().hashCode() + (result != null ? result.hashCode() : 7);
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof EvaluationResult)) {
            return false;
        }
        EvaluationResult other = (EvaluationResult) obj;
        if(this.error) {
            return other.error;
        }
        else {
            return !other.error && this.result.equals(other.result);
        }
    }


    @Override
    public String toString() {
        return Objects.toStringHelper("EvaluationResult")
                .add("error", error)
                .add("result", result)
                .toString();
    }
}
