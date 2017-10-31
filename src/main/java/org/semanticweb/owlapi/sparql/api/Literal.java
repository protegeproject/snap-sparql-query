package org.semanticweb.owlapi.sparql.api;

import com.google.common.base.Objects;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.sparql.algebra.AlgebraEvaluationContext;
import org.semanticweb.owlapi.sparql.builtin.DateTime;
import org.semanticweb.owlapi.vocab.OWL2Datatype;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br> Stanford University<br> Bio-Medical Informatics Research Group<br> Date: 26/07/2012
 */
public class Literal implements AtomicLiteral, RDFTerm, HasAsRDFTerm {

    public static final String EMPTY_LANG_TAG = "";
    private static final Literal FALSE = new Literal(Datatype.getXSDBoolean(), "false", "");
    private static final Literal TRUE = new Literal(Datatype.getXSDBoolean(), "true", "");
    private final Datatype datatype;

    private final String lexicalForm;

    private final String langTag;

    public Literal(Datatype datatype, String lexicalForm, String langTag) {
        this.datatype = checkNotNull(datatype);
        this.lexicalForm = checkNotNull(lexicalForm);
        this.langTag = checkNotNull(langTag);
    }

    public static Literal createString(String value) {
        return new Literal(Datatype.getXSDString(), value, EMPTY_LANG_TAG);
    }

    public static Literal createBoolean(boolean value) {
        if (value) {
            return TRUE;
        } else {
            return FALSE;
        }
    }

    public static Literal getFalse() {
        return FALSE;
    }

    public static Literal getTrue() {
        return TRUE;
    }

    public static Literal createInteger(int value) {
        return new Literal(Datatype.getXSDInteger(), Integer.toString(value), EMPTY_LANG_TAG);
    }

    public static Literal createDouble(double value) {
        return new Literal(Datatype.getXSDDouble(), Double.toString(value), EMPTY_LANG_TAG);
    }

    public static Literal createFloat(float value) {
        return new Literal(Datatype.getXSDFloat(), Float.toString(value), EMPTY_LANG_TAG);
    }

    public static Literal createDecimal(BigDecimal decimal) {
        return new Literal(Datatype.getXSDDecimal(), decimal.toPlainString(), EMPTY_LANG_TAG);
    }

    public static Literal createDecimal(double decimal) {
        return new Literal(Datatype.getXSDDecimal(), Double.toString(decimal), EMPTY_LANG_TAG);
    }

    public static Literal createLong(long decimal) {
        return new Literal(Datatype.getXSDLong(), Double.toString(decimal), EMPTY_LANG_TAG);
    }

    public static Literal createDateTime(DateTime dateTime) {
        return new Literal(Datatype.getXSDDateTime(), dateTime.getFormattedDateTime(), "");
    }

    public static Literal createRDFPlainLiteral(String lexicalForm, String langTag) {
        return new Literal(Datatype.getRDFPlainLiteral(), lexicalForm, langTag);
    }

    public static Literal createRDFPlainLiteralNoLang(String lexicalForm) {
        return new Literal(Datatype.getRDFPlainLiteral(), lexicalForm, EMPTY_LANG_TAG);
    }

    /**
     * Gets a Simple Literal.  This is a literal that is a plain literal without a language tag.
     *
     * @param lexicalForm The lexical form.  Not {@code null}.
     * @return The simple literal with the specified lexical form.
     */
    public static Literal createSimpleLiteral(String lexicalForm) {
        return createRDFPlainLiteralNoLang(lexicalForm);
    }

    public <R, E extends Throwable> R accept(Visitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    /**
     * Gets the lang.
     *
     * @return The lang.  Not {@code null}.  If the lang is not present then the empty string is returned.
     */
    public String getLang() {
        return langTag;
    }

    /**
     * Gets the lexical form.
     *
     * @return The lexical form.  Not {@code null}.  May be empty.
     */
    public String getLexicalForm() {
        return lexicalForm;
    }

    /**
     * Gets the datatype.
     *
     * @return The datatype.  Not {@code null}.
     */
    public Datatype getDatatype() {
        return datatype;
    }

    public Set<Variable> getVariables() {
        return Collections.emptySet();
    }

    @Override
    public String toString() {
        if (datatype.equals(Datatype.getXSDInteger())) {
            return lexicalForm.trim();
        } else if (datatype.equals(Datatype.getXSDDouble())) {
            return lexicalForm;
        } else if (datatype.isRDFPlainLiteral()) {
            return "\"" + lexicalForm + "\"";
        }
        return "Literal(\"" + lexicalForm + "\" ^^" + datatype + " @" + langTag + ")";
    }

    public boolean toBoolean() {
        return datatype.equals(Datatype.getXSDBoolean()) && Boolean.parseBoolean(lexicalForm);
    }

    /**
     * Determines if this literal is a string literal.  A string literal is a literal that has a datatype of
     * xsd:string.
     *
     * @return {@code true} if this literal is a string literal, otherwise {@code false}.
     */
    public boolean isStringLiteral() {
        return datatype.isXSDString();
    }

    /**
     * Determines if this literal is a simple literal.  A simple literal is a plain literal that does not have a lang
     * tag.
     *
     * @return {@code true} if this literal is a simple literal, otherwise {@code false}.
     */
    public boolean isSimpleLiteral() {
        return isRDFPlainLiteral() && "".equals(langTag);
    }

    public boolean isRDFPlainLiteral() {
        return Datatype.getRDFPlainLiteral().equals(this.datatype);
    }

    public boolean isDatatypeNumeric() {
        return datatype.isNumeric();
    }

    private boolean isInBooleanLexicalSpace() {
        return datatype.isXSDBoolean() && OWL2Datatype.XSD_BOOLEAN.getPattern().matcher(lexicalForm).matches();
    }

    /**
     * Determines if this literal is numeric.  For this to be true, the datatype must be a numeric datatype and the
     * lexical value must be in the lexical space of the datatype.
     */
    public boolean isNumeric() {
        return datatype.isNumeric() && datatype.isInLexicalSpace(lexicalForm);
    }

    @Override
    public EvaluationResult evaluate(SolutionMapping sm, AlgebraEvaluationContext evaluationContext) {
        return EvaluationResult.getResult(this);
    }

    /**
     * Evaluates this expression expecting an effective boolean value (EBV).
     *
     * 17.2.2 Effective Boolean Value (EBV)
     *
     * Effective boolean value is used to calculate the arguments to the logical functions logical-and, logical-or, and
     * fn:not, as well as evaluate the result of a FILTER expression.
     *
     * The XQuery Effective Boolean Value rules rely on the definition of XPath's fn:boolean. The following rules
     * reflect the rules for fn:boolean applied to the argument types present in SPARQL queries:
     *
     * The EBV of any literal whose type is xsd:boolean or numeric is false if the lexical form is not valid for that
     * datatype (e.g. "abc"^^xsd:integer). If the argument is a typed literal with a datatype of xsd:boolean, and it has
     * a valid lexical form, the EBV is the value of that argument. If the argument is a plain literal or a typed
     * literal with a datatype of xsd:string, the EBV is false if the operand value has zero length; otherwise the EBV
     * is true. If the argument is a numeric type or a typed literal with a datatype derived from a numeric type, and it
     * has a valid lexical form, the EBV is false if the operand value is NaN or is numerically equal to zero; otherwise
     * the EBV is true. All other arguments, including unbound arguments, produce a type error. An EBV of true is
     * represented as a typed literal with a datatype of xsd:boolean and a lexical value of "true"; an EBV of false is
     * represented as a typed literal with a datatype of xsd:boolean and a lexical value of "false".
     *
     * @param sm                The solution mapping
     * @param evaluationContext
     */
    @Override
    public EvaluationResult evaluateAsEffectiveBooleanValue(SolutionMapping sm, AlgebraEvaluationContext evaluationContext) {
        if (getDatatype().isXSDBoolean()) {
            // The EBV of any literal whose type is xsd:boolean
            // is false if the lexical form is not valid
            if (isInBooleanLexicalSpace()) {
                // If the argument is a typed literal with a datatype of xsd:boolean, and it has
                // a valid lexical form, the EBV is the value of that argument.
                return EvaluationResult.getResult(this);
            } else {
                return EvaluationResult.getFalse();
            }
        } else if (isDatatypeNumeric()) {
            // The EBV of any literal whose type is numeric
            // is false if the lexical form is not valid
            if (isNumeric()) {
                try {
                    double value = Double.parseDouble(lexicalForm);
                    // return false if the value is NaN or is numerically equal to zero
                    if (value == 0) {
                        return EvaluationResult.getFalse();
                    } else if (Double.isNaN(value)) {
                        return EvaluationResult.getFalse();
                    } else {
                        // Otherwise return true
                        return EvaluationResult.getTrue();
                    }
                } catch (NumberFormatException e) {
                    return EvaluationResult.getFalse();
                }
            } else {
                return EvaluationResult.getFalse();
            }
        } else if (isRDFPlainLiteral() || isStringLiteral()) {
            // If the argument is a plain literal or a typed
            // literal with a datatype of xsd:string, the EBV is false if the operand
            // value has zero length; otherwise the EBV is true.
            return EvaluationResult.getBoolean(lexicalForm.length() > 0);
        } else {
            // All other arguments, including unbound arguments, produce a type error.
            return EvaluationResult.getError();
        }
    }

    public EvaluationResult evaluateAsStringLiteral(SolutionMapping sm, AlgebraEvaluationContext evaluationContext) {
        return EvaluationResult.getResult(Literal.createString(lexicalForm));
    }

    public EvaluationResult evaluateAsSimpleLiteral(SolutionMapping sm, AlgebraEvaluationContext evaluationContext) {
        return EvaluationResult.getResult(Literal.createRDFPlainLiteral(lexicalForm, langTag));
    }

//    public EvaluationResult evaluateAsNumeric(SolutionMapping sm, AlgebraEvaluationContext evaluationContext) {
//        if (isNumeric()) {
//            return EvaluationResult.getResult(this);
//        } else if (isStringLiteral() || isSimpleLiteral()) {
//            try {
//                double value = Double.parseDouble(lexicalForm);
//                if (value % 1 == 0) {
//                    return EvaluationResult.getInteger((int) value);
//                } else if(Double.isNaN(value)) {
//                    return EvaluationResult.getDouble(value);
//                }
//                else {
//                    return EvaluationResult.getDecimal(value);
//                }
//            } catch (NumberFormatException e2) {
//                return EvaluationResult.getError();
//            }
//        } else {
//            return EvaluationResult.getError();
//        }
//    }

    private boolean isDatatypeDateTime() {
        return datatype.isXSDDateTime();
    }

    private boolean isInDateTimeLexicalSpace() {
        return OWL2Datatype.XSD_DATE_TIME.getPattern().matcher(lexicalForm).matches();
    }

    public EvaluationResult evaluateAsDateTime(SolutionMapping sm, AlgebraEvaluationContext evaluationContext) {
        if (isDatatypeDateTime() && isInDateTimeLexicalSpace()) {
            return EvaluationResult.getResult(this);
        } else {
            return castToXSDDateTime().map(EvaluationResult::getResult).orElse(EvaluationResult.getError());
        }
    }

    @Override
    public EvaluationResult evaluateAsIRI(SolutionMapping sm, AlgebraEvaluationContext evaluationContext) {
        return EvaluationResult.getResult(new AtomicIRI(IRI.create(lexicalForm)));
    }

    @Override
    public EvaluationResult evaluateAsLiteral(SolutionMapping sm, AlgebraEvaluationContext evaluationContext) {
        return EvaluationResult.getResult(this);
    }


    @Override
    public int hashCode() {
        return Objects.hashCode(datatype.hashCode(), lexicalForm.hashCode(), langTag.hashCode());
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Literal)) {
            return false;
        }
        Literal other = (Literal) obj;
        return this.datatype.equals(other.datatype) && this.lexicalForm.equals(other.lexicalForm) && this.langTag.equals(other.langTag);
    }

    @Override
    public boolean isSameRDFTermAs(Term term) {
        return this.equals(term);
    }

    @Override
    public RDFTerm asRDFTerm() {
        return this;
    }

    @Override
    public void collectVariables(Collection<Variable> variables) {

    }

    @Override
    public <R, E extends Throwable, C> R accept(ExpressionVisitor<R, E, C> visitor, C context) throws E {
        return visitor.visit(this, context);
    }

    @Override
    public Optional<Literal> bind(SolutionMapping sm) {
        return Optional.of(this);
    }

    @Override
    public OWLLiteral toOWLObject(OWLDataFactory df) {
        return df.getOWLLiteral(lexicalForm, datatype.toOWLObject(df));
    }

    @Nonnull
    @Override
    public java.util.Optional<Variable> asVariable() {
        return java.util.Optional.empty();
    }

    @Override
    public Optional<Literal> castToXSDString() {
        if (datatype.isXSDString()) {
            return Optional.of(this);
        } else {
            return Optional.of(Literal.createString(lexicalForm));
        }
    }

    @Override
    public Optional<Literal> castToXSDFloat() {
        if (datatype.isXSDFloat()) {
            return Optional.of(this);
        } else if (isNumeric()) {
            int value = (int) Double.parseDouble(lexicalForm);
            return Optional.of(Literal.createInteger(value));
        } else if (datatype.isXSDString()) {
            try {
                float value = (float) Double.parseDouble(lexicalForm);
                return Optional.of(Literal.createFloat(value));
            } catch (NumberFormatException e) {
                return Optional.empty();
            }
        } else if (datatype.isXSDBoolean() && (lexicalForm.equalsIgnoreCase("true") || lexicalForm.equals("1"))) {
            return Optional.of(Literal.createFloat(1));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Literal> castToXSDDouble() {
        if (datatype.isXSDDouble()) {
            return Optional.of(this);
        } else if (isNumeric()) {
            int value = (int) Double.parseDouble(lexicalForm);
            return Optional.of(Literal.createDouble(value));
        } else if (datatype.isXSDString()) {
            try {
                double value = Double.parseDouble(lexicalForm);
                return Optional.of(Literal.createDouble(value));
            } catch (NumberFormatException e) {
                return Optional.empty();
            }
        } else if (datatype.isXSDBoolean() && (lexicalForm.equalsIgnoreCase("true") || lexicalForm.equals("1"))) {
            return Optional.of(Literal.createDouble(1));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Literal> castToXSDDecimal() {
        if (datatype.isXSDDecimal()) {
            return Optional.of(this);
        } else if (isNumeric()) {
            int value = (int) Double.parseDouble(lexicalForm);
            return Optional.of(Literal.createDecimal(value));
        } else if (datatype.isXSDString()) {
            try {
                double value = Double.parseDouble(lexicalForm);
                if (!Double.isNaN(value)) {
                    return Optional.of(Literal.createDecimal(value));
                }
                else {
                    return Optional.empty();
                }
            } catch (NumberFormatException e) {
                return Optional.empty();
            }
        } else if (datatype.isXSDBoolean() && (lexicalForm.equalsIgnoreCase("true") || lexicalForm.equals("1"))) {
            return Optional.of(Literal.createDouble(1));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Literal> castToXSDInteger() {
        if (datatype.isXSDInteger()) {
            return Optional.of(this);
        } else if (isNumeric()) {
            int value = (int) Double.parseDouble(lexicalForm);
            return Optional.of(Literal.createInteger(value));
        } else if (datatype.isXSDString()) {
            try {
                int value = (int) Double.parseDouble(lexicalForm);
                return Optional.of(Literal.createInteger(value));
            } catch (NumberFormatException e) {
                return Optional.empty();
            }
        } else if (datatype.isXSDBoolean() && (lexicalForm.equalsIgnoreCase("true") || lexicalForm.equals("1"))) {
            return Optional.of(Literal.createInteger(1));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Literal> castToXSDDateTime() {
        if (datatype.isXSDDateTime()) {
            return Optional.of(this);
        } else {
            return DateTime.parseDateTime(lexicalForm).map(Literal::createDateTime);
        }
    }

    @Override
    public Optional<Literal> castToXSDBoolean() {
        return null;
    }
}
