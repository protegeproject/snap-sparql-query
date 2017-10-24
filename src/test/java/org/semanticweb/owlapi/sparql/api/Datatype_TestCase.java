
package org.semanticweb.owlapi.sparql.api;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.sparql.algebra.AlgebraEvaluationContext;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;
import org.semanticweb.owlapi.vocab.XSDVocabulary;

import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class Datatype_TestCase {

    private Datatype datatype;

    private IRI iri;

    @Before
    public void setUp()
        throws Exception
    {
        iri = IRI.create("http://stuff/datatypes#datatype");
        datatype = new Datatype(iri);
    }

    @Test
    public void shouldBeEqualToSelf() {
        MatcherAssert.assertThat(datatype, Matchers.is(datatype));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        MatcherAssert.assertThat(datatype.equals(null), Matchers.is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        MatcherAssert.assertThat(datatype, Matchers.is(new Datatype(iri)));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        MatcherAssert.assertThat(datatype.hashCode(), Matchers.is(new Datatype(iri).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        MatcherAssert.assertThat(datatype.toString(), Matchers.startsWith("Datatype"));
    }

    @Test
    public void shouldReturn_isInLexicalSpace_isTrue() {
        MatcherAssert.assertThat(datatype.isInLexicalSpace("xxx"), Matchers.is(true));
    }

    @Test
    public void should_getRDFPlainLiteral() {
        MatcherAssert.assertThat(Datatype.getRDFPlainLiteral(), Matchers.is(new Datatype(OWLRDFVocabulary.RDF_PLAIN_LITERAL.getIRI())));
    }

    @Test
    public void shouldReturn_isRDFPlainLiteral_isTrue() {
        MatcherAssert.assertThat(new Datatype(OWLRDFVocabulary.RDF_PLAIN_LITERAL.getIRI()).isRDFPlainLiteral(), Matchers.is(true));
    }

    @Test
    public void shouldReturn_isRDFPlainLiteral_isFalse() {
        MatcherAssert.assertThat(datatype.isRDFPlainLiteral(), Matchers.is(false));
    }

    @Test
    public void shouldReturn_isXSDString_isTrue() {
        MatcherAssert.assertThat(new Datatype(XSDVocabulary.STRING.getIRI()).isXSDString(), Matchers.is(true));
    }

    @Test
    public void shouldReturn_isXSDString_isFalse() {
        MatcherAssert.assertThat(datatype.isXSDString(), Matchers.is(false));
    }

    @Test
    public void shouldReturn_isXSDBoolean_isTrue() {
        MatcherAssert.assertThat(new Datatype(XSDVocabulary.BOOLEAN.getIRI()).isXSDBoolean(), Matchers.is(true));
    }

    @Test
    public void shouldReturn_isXSDBoolean_isFalse() {
        MatcherAssert.assertThat(datatype.isXSDBoolean(), Matchers.is(false));
    }

    @Test
    public void should_getXSDString() {
        MatcherAssert.assertThat(Datatype.getXSDString(), Matchers.is(new Datatype(XSDVocabulary.STRING.getIRI())));
    }

    @Test
    public void should_getXSDBoolean() {
        MatcherAssert.assertThat(Datatype.getXSDBoolean(), Matchers.is(new Datatype(XSDVocabulary.BOOLEAN.getIRI())));
    }

    @Test
    public void should_getXSDInteger() {
        MatcherAssert.assertThat(Datatype.getXSDInteger(), Matchers.is(new Datatype(XSDVocabulary.INTEGER.getIRI())));
    }

    @Test
    public void should_getXSDDouble() {
        MatcherAssert.assertThat(Datatype.getXSDDouble(), Matchers.is(new Datatype(XSDVocabulary.DOUBLE.getIRI())));
    }

    @Test
    public void should_getXSDFloat() {
        MatcherAssert.assertThat(Datatype.getXSDFloat(), Matchers.is(new Datatype(XSDVocabulary.FLOAT.getIRI())));
    }

    @Test
    public void should_getXSDDecimal() {
        MatcherAssert.assertThat(Datatype.getXSDDecimal(), Matchers.is(new Datatype(XSDVocabulary.DECIMAL.getIRI())));
    }

    @Test
    public void should_getXSDDateTime() {
        MatcherAssert.assertThat(Datatype.getXSDDateTime(), Matchers.is(new Datatype(XSDVocabulary.DATE_TIME.getIRI())));
    }

    @Test
    public void shouldReturn_isNumeric_isTrue_ForNonPositiveInteger() {
        MatcherAssert.assertThat(new Datatype(XSDVocabulary.NON_POSITIVE_INTEGER.getIRI()).isNumeric(), Matchers.is(true));
    }

    @Test
    public void shouldReturn_isNumeric_isTrue_ForNegativeInteger() {
        MatcherAssert.assertThat(new Datatype(XSDVocabulary.NEGATIVE_INTEGER.getIRI()).isNumeric(), Matchers.is(true));
    }

    @Test
    public void shouldReturn_isNumeric_isTrue_ForLong() {
        MatcherAssert.assertThat(new Datatype(XSDVocabulary.LONG.getIRI()).isNumeric(), Matchers.is(true));
    }

    @Test
    public void shouldReturn_isNumeric_isTrue_ForInt() {
        MatcherAssert.assertThat(new Datatype(XSDVocabulary.INT.getIRI()).isNumeric(), Matchers.is(true));
    }

    @Test
    public void shouldReturn_isNumeric_isTrue_ForShort() {
        MatcherAssert.assertThat(new Datatype(XSDVocabulary.SHORT.getIRI()).isNumeric(), Matchers.is(true));
    }

    @Test
    public void shouldReturn_isNumeric_isTrue_ForByte() {
        MatcherAssert.assertThat(new Datatype(XSDVocabulary.BYTE.getIRI()).isNumeric(), Matchers.is(true));
    }

    @Test
    public void shouldReturn_isNumeric_isTrue_ForNonNegativeInteger() {
        MatcherAssert.assertThat(new Datatype(XSDVocabulary.NON_NEGATIVE_INTEGER.getIRI()).isNumeric(), Matchers.is(true));
    }

    @Test
    public void shouldReturn_isNumeric_isTrue_ForUnsignedLong() {
        MatcherAssert.assertThat(new Datatype(XSDVocabulary.UNSIGNED_LONG.getIRI()).isNumeric(), Matchers.is(true));
    }

    @Test
    public void shouldReturn_isNumeric_isTrue_ForUnsignedInt() {
        MatcherAssert.assertThat(new Datatype(XSDVocabulary.UNSIGNED_INT.getIRI()).isNumeric(), Matchers.is(true));
    }

    @Test
    public void shouldReturn_isNumeric_isTrue_ForUnsignedShort() {
        MatcherAssert.assertThat(new Datatype(XSDVocabulary.UNSIGNED_SHORT.getIRI()).isNumeric(), Matchers.is(true));
    }

    @Test
    public void shouldReturn_isNumeric_isTrue_ForUnsignedByte() {
        MatcherAssert.assertThat(new Datatype(XSDVocabulary.UNSIGNED_BYTE.getIRI()).isNumeric(), Matchers.is(true));
    }

    @Test
    public void shouldReturn_isNumeric_isTrue_ForPositiveInteger() {
        MatcherAssert.assertThat(new Datatype(XSDVocabulary.POSITIVE_INTEGER.getIRI()).isNumeric(), Matchers.is(true));
    }

    @Test
    public void shouldReturn_isNumeric_isTrue_ForInteger() {
        MatcherAssert.assertThat(new Datatype(XSDVocabulary.INTEGER.getIRI()).isNumeric(), Matchers.is(true));
    }

    @Test
    public void shouldReturn_isNumeric_isTrue_ForDecimal() {
        MatcherAssert.assertThat(new Datatype(XSDVocabulary.DECIMAL.getIRI()).isNumeric(), Matchers.is(true));
    }

    @Test
    public void shouldReturn_isNumeric_isTrue_ForDouble() {
        MatcherAssert.assertThat(new Datatype(XSDVocabulary.DOUBLE.getIRI()).isNumeric(), Matchers.is(true));
    }


    @Test
    public void shouldReturn_isNumeric_isTrue_ForFloat() {
        MatcherAssert.assertThat(new Datatype(XSDVocabulary.FLOAT.getIRI()).isNumeric(), Matchers.is(true));
    }

    @Test
    public void shouldReturn_isNumeric_isFalse() {
        MatcherAssert.assertThat(datatype.isNumeric(), Matchers.is(false));
    }

    @Test
    public void shouldReturn_isXSDDateTime_isTrue() {
        MatcherAssert.assertThat(new Datatype(XSDVocabulary.DATE_TIME.getIRI()).isXSDDateTime(), Matchers.is(true));
    }

    @Test
    public void shouldReturn_isXSDDateTime_isFalse() {
        MatcherAssert.assertThat(datatype.isXSDDateTime(), Matchers.is(false));
    }

    @Test
    public void should_getVariables() {
        assertThat(datatype.getVariables(), is(Collections.<Variable>emptySet()));
    }

    @Test
    public void should_getIRI() {
        assertThat(datatype.getIRI(), is(iri));
    }

    @Test
    public void should_getIdentifier() {
        assertThat(datatype.getIdentifier(), is(iri.toString()));
    }

    @Test
    public void shouldReturn_False_For_isLiteral_isFalse() {
        assertThat(datatype.isLiteral(), is(false));
    }

    @Test
    public void shouldReturn_True_For_isEntityIRI() {
        assertThat(datatype.isEntityIRI(), is(true));
    }

    @Test
    public void shouldReturn_False_For_isUntypedIRI_isFalse() {
        assertThat(datatype.isUntypedIRI(), is(false));
    }

    @Test
    public void should_evaluateAsEffectiveBooleanValue() {
        assertThat(datatype.evaluateAsEffectiveBooleanValue(mock(SolutionMapping.class), mock(AlgebraEvaluationContext.class)), is(EvaluationResult.getError()));
    }

    @Test
    public void should_evaluateAsSimpleLiteral() {
        assertThat(datatype.evaluateAsSimpleLiteral(mock(SolutionMapping.class), mock(AlgebraEvaluationContext.class)), is(EvaluationResult.getResult(Literal.createRDFPlainLiteralNoLang(datatype.getIRI().toString()))));
    }

    @Test
    public void should_evaluateAsStringLiteral() {
        assertThat(datatype.evaluateAsStringLiteral(mock(SolutionMapping.class), mock(AlgebraEvaluationContext.class)), is(EvaluationResult.getResult(Literal.createRDFPlainLiteralNoLang(datatype.getIRI().toString()))));
    }

    @Test
    public void shouldReturn_Error_When_evaluateAsNumeric() {
        assertThat(datatype.evaluateAsNumeric(mock(SolutionMapping.class), mock(AlgebraEvaluationContext.class)), is(EvaluationResult.getError()));
    }

    @Test
    public void shouldReturn_Error_When_evaluateAsDateTime() {
        assertThat(datatype.evaluateAsDateTime(mock(SolutionMapping.class), mock(AlgebraEvaluationContext.class)), is(EvaluationResult.getError()));
    }

    @Test
    public void should_evaluateAsLiteral() {
        assertThat(datatype.evaluateAsLiteral(mock(SolutionMapping.class), mock(AlgebraEvaluationContext.class)), is(EvaluationResult.getResult(Literal.createRDFPlainLiteralNoLang(iri.toString()))));
    }

    @Test
    public void should_evaluateAsIRI() {
        assertThat(datatype.evaluateAsIRI(mock(SolutionMapping.class), mock(AlgebraEvaluationContext.class)), is(EvaluationResult.getResult(new AtomicIRI(datatype.getIRI()))));
    }

    @Test
    public void shouldReturnTrue_For_XSD_Decimal_isDecimalDerivedType() {
        assertThat(new Datatype(XSDVocabulary.DECIMAL.getIRI()).isXSDDecimalDerived(), is(true));
    }

    @Test
    public void shouldReturnTrue_For_XSD_Integer_isDecimalDerivedType() {
        assertThat(new Datatype(XSDVocabulary.INTEGER.getIRI()).isXSDDecimalDerived(), is(true));
    }

    @Test
    public void shouldReturnTrue_For_XSD_Long_isDecimalDerivedType() {
        assertThat(new Datatype(XSDVocabulary.LONG.getIRI()).isXSDDecimalDerived(), is(true));
    }

    @Test
    public void shouldReturnTrue_For_XSD_INT_isDecimalDerivedType() {
        assertThat(new Datatype(XSDVocabulary.INT.getIRI()).isXSDDecimalDerived(), is(true));
    }

    @Test
    public void shouldReturnTrue_For_XSD_SHORT_isDecimalDerivedType() {
        assertThat(new Datatype(XSDVocabulary.SHORT.getIRI()).isXSDDecimalDerived(), is(true));
    }

    @Test
    public void shouldReturnTrue_For_XSD_Byte_isDecimalDerivedType() {
        assertThat(new Datatype(XSDVocabulary.BYTE.getIRI()).isXSDDecimalDerived(), is(true));
    }

    @Test
    public void shouldReturnTrue_For_XSD_NonPositiveInteger_isDecimalDerivedType() {
        assertThat(new Datatype(XSDVocabulary.NON_POSITIVE_INTEGER.getIRI()).isXSDDecimalDerived(), is(true));
    }

    @Test
    public void shouldReturnTrue_For_XSD_NegativeInteger_isDecimalDerivedType() {
        assertThat(new Datatype(XSDVocabulary.NEGATIVE_INTEGER.getIRI()).isXSDDecimalDerived(), is(true));
    }

    @Test
    public void shouldReturnTrue_For_XSD_NonNegativeInteger_isDecimalDerivedType() {
        assertThat(new Datatype(XSDVocabulary.NON_NEGATIVE_INTEGER.getIRI()).isXSDDecimalDerived(), is(true));
    }

    @Test
    public void shouldReturnTrue_For_XSD_PositiveInteger_isDecimalDerivedType() {
        assertThat(new Datatype(XSDVocabulary.POSITIVE_INTEGER.getIRI()).isXSDDecimalDerived(), is(true));
    }

    @Test
    public void shouldReturn_True_For_Is_Float() {
        assertThat(new Datatype(XSDVocabulary.FLOAT.getIRI()).isXSDFloat(), is(true));
    }

    @Test
    public void shouldReturn_True_For_Is_Double() {
        assertThat(new Datatype(XSDVocabulary.DOUBLE.getIRI()).isXSDDouble(), is(true));
    }
}
