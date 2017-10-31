
package org.semanticweb.owlapi.sparql.api;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.sparql.algebra.AlgebraEvaluationContext;

import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class DataProperty_TestCase {

    private DataProperty dataProperty;
    @Mock
    private IRI iri;

    @Before
    public void setUp()
        throws Exception
    {
        dataProperty = new DataProperty(iri);
    }

    @Test(expected = java.lang.NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_iri_IsNull() {
        new DataProperty(null);
    }

    @Test
    public void shouldReturnSupplied_iri() {
        MatcherAssert.assertThat(dataProperty.getIRI(), Matchers.is(this.iri));
    }

    @Test
    public void shouldBeEqualToSelf() {
        MatcherAssert.assertThat(dataProperty, Matchers.is(dataProperty));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        MatcherAssert.assertThat(dataProperty.equals(null), Matchers.is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        MatcherAssert.assertThat(dataProperty, Matchers.is(new DataProperty(iri)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_iri() {
        MatcherAssert.assertThat(dataProperty, Matchers.is(Matchers.not(new DataProperty(Mockito.mock(IRI.class)))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        MatcherAssert.assertThat(dataProperty.hashCode(), Matchers.is(new DataProperty(iri).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        MatcherAssert.assertThat(dataProperty.toString(), Matchers.startsWith("DataProperty"));
    }

    @Test
    public void should_getVariables() {
        assertThat(dataProperty.getVariables(), is(Collections.<Variable>emptySet()));
    }

    @Test
    public void should_getIRI() {
        assertThat(dataProperty.getIRI(), is(iri));
    }

    @Test
    public void should_getIdentifier() {
        assertThat(dataProperty.getIdentifier(), is(iri.toString()));
    }

    @Test
    public void shouldReturn_False_For_isLiteral_isFalse() {
        assertThat(dataProperty.isLiteral(), is(false));
    }

    @Test
    public void shouldReturn_True_For_isEntityIRI() {
        assertThat(dataProperty.isEntityIRI(), is(true));
    }

    @Test
    public void shouldReturn_False_For_isUntypedIRI_isFalse() {
        assertThat(dataProperty.isUntypedIRI(), is(false));
    }

    @Test
    public void should_evaluateAsEffectiveBooleanValue() {
        assertThat(dataProperty.evaluateAsEffectiveBooleanValue(mock(SolutionMapping.class), mock(AlgebraEvaluationContext.class)), is(EvaluationResult.getError()));
    }

    @Test
    public void should_evaluateAsSimpleLiteral() {
        assertThat(dataProperty.evaluateAsSimpleLiteral(mock(SolutionMapping.class), mock(AlgebraEvaluationContext.class)), is(EvaluationResult.getResult(Literal.createRDFPlainLiteralNoLang(dataProperty.getIRI().toString()))));
    }

    @Test
    public void should_evaluateAsLiteral() {
        assertThat(dataProperty.evaluateAsLiteral(mock(SolutionMapping.class), mock(AlgebraEvaluationContext.class)), is(EvaluationResult.getResult(Literal.createRDFPlainLiteralNoLang(iri.toString()))));
    }

    @Test
    public void should_evaluateAsIRI() {
        assertThat(dataProperty.evaluate(mock(SolutionMapping.class), mock(AlgebraEvaluationContext.class)), is(EvaluationResult.getResult(new AtomicIRI(dataProperty.getIRI()))));
    }
}
