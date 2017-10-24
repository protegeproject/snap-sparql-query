package org.semanticweb.owlapi.sparql.api;

import org.hamcrest.Matcher;
import org.junit.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.sparql.sparqldl.EvaluationContext;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11/03/15
 */
@RunWith(MockitoJUnitRunner.class)
public class EffectiveBooleanValue_TestCase {


    /*
     * Effective boolean value is used to calculate the arguments to the logical functions logical-and, logical-or, and
     * fn:not, as well as evaluate the result of a FILTER expression.
     *
     * The XQuery Effective Boolean Value rules rely on the definition of XPath's fn:boolean. The following rules
     * reflect the rules for fn:boolean applied to the argument types present in SPARQL queries:
     *
     * The EBV of any literal whose type is xsd:boolean or numeric is false if the lexical form is not valid for that
     * datatype (e.g. "abc"^^xsd:integer).
     *
     * If the argument is a typed literal with a datatype of xsd:boolean, and it has
     * a valid lexical form, the EBV is the value of that argument.
     *
     * If the argument is a plain literal or a typed
     * literal with a datatype of xsd:string, the EBV is false if the operand value has zero length; otherwise the EBV
     * is true.
     *
     * If the argument is a numeric type or a typed literal with a datatype derived from a numeric type, and it
     * has a valid lexical form, the EBV is false if the operand value is NaN or is numerically equal to zero; otherwise
     * the EBV is true.
     *
     *
     * All other arguments, including unbound arguments, produce a type error.
     *
     * An EBV of true is
     * represented as a typed literal with a datatype of xsd:boolean and a lexical value of "true"; an EBV of false is
     * represented as a typed literal with a datatype of xsd:boolean and a lexical value of "false".
     */

    private Literal literal;

    @Mock
    private SolutionMapping sm;

    @Before
    public void setUp() throws Exception {
    }

    private void createBooleanLiteral(String lexicalForm) {
        literal = new Literal(Datatype.getXSDBoolean(), lexicalForm, "");
    }

    private void createNumericLiteral(String lexicalForm) {
        literal = new Literal(Datatype.getXSDInteger(), lexicalForm, "");
    }

    private void createStringLiteral(String lexicalForm) {
        literal = new Literal(Datatype.getXSDString(), lexicalForm, "");
    }

    /**
     * If the argument is a typed literal with a datatype of xsd:boolean, and it has
     * a valid lexical form, the EBV is the value of that argument.
     */
    @Test
    public void shouldEvaluate_true_AsTrue() {
        createBooleanLiteral("true");
        assertThatEffectiveBooleanValueIsTrue();
    }

    /**
     * If the argument is a typed literal with a datatype of xsd:boolean, and it has
     * a valid lexical form, the EBV is the value of that argument.
     */
    @Test
    public void shouldEvaluate_false_AsFalse() {
        createBooleanLiteral("false");
        assertThatEffectiveBooleanValueIsFalse();
    }


    /**
     * If the argument is a typed literal with a datatype of xsd:boolean, and it has
     * a valid lexical form, the EBV is the value of that argument.
     */
    @Test
    public void shouldEvaluate_1_AsTrue() {
        createBooleanLiteral("true");
        assertThatEffectiveBooleanValueIsTrue();
    }



    /**
     * If the argument is a typed literal with a datatype of xsd:boolean, and it has
     * a valid lexical form, the EBV is the value of that argument.
     */
    @Test
    public void shouldEvaluate_0_AsFalse() {
        createBooleanLiteral("false");
        assertThatEffectiveBooleanValueIsFalse();
    }


    /**
     * The EBV of any literal whose type is xsd:boolean or numeric is false if the lexical form is not valid for that
     * datatype (e.g. "abc"^^xsd:integer).
     */
    @Test
    public void shouldEvaluate_TRUE_AsFalse() {
        createBooleanLiteral("TRUE");
        assertThatEffectiveBooleanValueIsFalse();
    }


    /**
     * The EBV of any literal whose type is xsd:boolean or numeric is false if the lexical form is not valid for that
     * datatype (e.g. "abc"^^xsd:integer).
     */
    @Test
    public void shouldEvaluate_FALSE_AsFalse() {
        createBooleanLiteral("FALSE");
        assertThatEffectiveBooleanValueIsFalse();
    }


    /**
     * The EBV of any literal whose type is xsd:boolean or numeric is false if the lexical form is not valid for that
     * datatype (e.g. "abc"^^xsd:integer).
     */
    @Test
    public void shouldEvaluate_Other_As_False() {
        createBooleanLiteral("Other");
        assertThatEffectiveBooleanValueIsFalse();
    }

    /**
     * The EBV of any literal whose type is xsd:boolean or numeric is false if the lexical form is not valid for that
     * datatype (e.g. "abc"^^xsd:integer).
     */
    @Test
    public void shouldEvaluate_NumericLiteralAs_False() {
        createNumericLiteral("Other");
        assertThatEffectiveBooleanValueIsFalse();
    }

    /**
     * If the argument is a plain literal or a typed
     * literal with a datatype of xsd:string, the EBV is false if the operand value has zero length; otherwise the EBV
     * is true.
     */
    @Test
    public void shouldEvaluate_StringLiteral_ZeroLength_As_False() {
        createNumericLiteral("");
        assertThatEffectiveBooleanValueIsFalse();
    }

    /**
     * If the argument is a plain literal or a typed
     * literal with a datatype of xsd:string, the EBV is false if the operand value has zero length; otherwise the EBV
     * is true.
     */
    @Test
    public void shouldEvaluate_StringLiteral_NonZeroLength_As_True() {
        createStringLiteral("X");
        assertThatEffectiveBooleanValueIsTrue();
    }

    private void assertThatEffectiveBooleanValueIsTrue() {
        assertThat(literal.evaluateAsEffectiveBooleanValue(sm, mock(EvaluationContext.class)), isTrue());
    }

    /**
     * If the argument is a numeric type or a typed literal with a datatype derived from a numeric type, and it
     * has a valid lexical form, the EBV is false if the operand value is NaN or is numerically equal to zero; otherwise
     * the EBV is true.
     */
    @Test
    public void shouldEvaluateNumericTypeZero_As_False() {
        createNumericLiteral("0");
        assertThatEffectiveBooleanValueIsFalse();
    }

    /**
     * If the argument is a numeric type or a typed literal with a datatype derived from a numeric type, and it
     * has a valid lexical form, the EBV is false if the operand value is NaN or is numerically equal to zero; otherwise
     * the EBV is true.
     */
    @Test
    public void shouldEvaluateNumericNonZero_As_True() {
        createNumericLiteral("7");
        assertThatEffectiveBooleanValueIsTrue();
    }

    @Test
    public void shouldEvaluateAsError() {
        Datatype datatype = mock(Datatype.class);
        when(datatype.isNumeric()).thenReturn(false);
        when(datatype.isXSDString()).thenReturn(false);
        when(datatype.isRDFPlainLiteral()).thenReturn(false);
        literal = new Literal(datatype, "Some Lexical Form", "");
        assertThat(literal.evaluateAsEffectiveBooleanValue(sm, mock(EvaluationContext.class)), isError());
    }

    private void assertThatEffectiveBooleanValueIsFalse() {
        assertThat(literal.evaluateAsEffectiveBooleanValue(sm, mock(EvaluationContext.class)), isFalse());
    }



    private Matcher<EvaluationResult> isError() {
        return is(EvaluationResult.getError());
    }

    private Matcher<EvaluationResult> isTrue() {
        return is(EvaluationResult.getBoolean(true));
    }

    private Matcher<EvaluationResult> isFalse() {
        return is(EvaluationResult.getBoolean(false));
    }

}
