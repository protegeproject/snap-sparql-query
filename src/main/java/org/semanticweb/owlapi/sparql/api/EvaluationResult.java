package org.semanticweb.owlapi.sparql.api;

import com.google.common.base.Objects;
import org.semanticweb.owlapi.sparql.builtin.DateTime;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 01/08/2012
 */
public final class EvaluationResult {

    private static final EvaluationResult ERROR_RESULT = new EvaluationResult();

    private static final EvaluationResult FALSE = new EvaluationResult(Literal.getFalse());

    private static final EvaluationResult TRUE = new EvaluationResult(Literal.getTrue());

    @Nullable
    private final RDFTerm result;
    
    private boolean error;


    public EvaluationResult(@Nonnull RDFTerm result) {
        this.result = checkNotNull(result);
        this.error = false;
    }

    private EvaluationResult() {
        this.result = null;
        this.error = true;
    }

    @Nonnull
    public RDFTerm getResult() {
        if(error) {
            throw new RuntimeException("getResult should not be called for an error");
        }
        if(result == null) {
            throw new IllegalStateException("Result should not be null if error is false");
        }
        return result;
    }

    @Nonnull
    public static EvaluationResult getResult(RDFTerm result) {
        return new EvaluationResult(result);
    }

    @Nonnull
    public static EvaluationResult getFalse() {
        return FALSE;
    }

    @Nonnull
    public static EvaluationResult getTrue() {
        return TRUE;
    }

    @Nonnull
    public static EvaluationResult getBoolean(boolean value) {
        if(value) {
            return getTrue();
        }
        else {
            return getFalse();
        }
    }

    @Nonnull
    public static EvaluationResult getSimpleLiteral(@Nonnull String literal) {
        return EvaluationResult.getResult(Literal.createRDFPlainLiteral(literal, ""));
    }

    @Nonnull
    public static EvaluationResult getDouble(double value) {
        return getResult(Literal.createDouble(value));
    }

    @Nonnull
    public static EvaluationResult getDecimal(double value) {
        return getResult(Literal.createDecimal(value));
    }

    @Nonnull
    public static EvaluationResult getInteger(int value) {
        return getResult(Literal.createInteger(value));
    }

    @Nonnull
    public static EvaluationResult getLong(long value) {
        return getResult(Literal.createLong(value));
    }

    @Nonnull
    public static EvaluationResult getFloat(float value) {
        return getResult(Literal.createFloat(value));
    }


    @Nonnull
    public static EvaluationResult getError() {
        return ERROR_RESULT;
    }

    /**
     * Gets this result as a numeric result or else as an error
     */
    public EvaluationResult asNumericOrElseError() {
        if(result != null && result.isNumeric()) {
            return this;
        }
        else {
            return EvaluationResult.getError();
        }
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

    public DateTime asDateTime() {
        if(result instanceof Literal) {
            return DateTime.parseDateTime(((Literal) result).getLexicalForm()).orElseThrow(() -> new RuntimeException("Could not parse datetime"));
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
        return Objects.hashCode(result, error);
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
