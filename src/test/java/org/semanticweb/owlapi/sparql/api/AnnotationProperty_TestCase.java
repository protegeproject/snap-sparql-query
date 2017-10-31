
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
public class AnnotationProperty_TestCase {

    private AnnotationProperty annotationProperty;
    @Mock
    private IRI iri;

    @Before
    public void setUp()
        throws Exception
    {
        annotationProperty = new AnnotationProperty(iri);
    }

    @Test(expected = java.lang.NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_iri_IsNull() {
        new AnnotationProperty(null);
    }

    @Test
    public void shouldReturnSupplied_iri() {
        MatcherAssert.assertThat(annotationProperty.getIRI(), Matchers.is(this.iri));
    }

    @Test
    public void shouldBeEqualToSelf() {
        MatcherAssert.assertThat(annotationProperty, Matchers.is(annotationProperty));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        MatcherAssert.assertThat(annotationProperty.equals(null), Matchers.is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        MatcherAssert.assertThat(annotationProperty, Matchers.is(new AnnotationProperty(iri)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_iri() {
        MatcherAssert.assertThat(annotationProperty, Matchers.is(Matchers.not(new AnnotationProperty(Mockito.mock(IRI.class)))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        MatcherAssert.assertThat(annotationProperty.hashCode(), Matchers.is(new AnnotationProperty(iri).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        MatcherAssert.assertThat(annotationProperty.toString(), Matchers.startsWith("AnnotationProperty"));
    }

    @Test
    public void should_getVariables() {
        assertThat(annotationProperty.getVariables(), is(Collections.<Variable>emptySet()));
    }

    @Test
    public void should_getIRI() {
        assertThat(annotationProperty.getIRI(), is(iri));
    }

    @Test
    public void should_getIdentifier() {
        assertThat(annotationProperty.getIdentifier(), is(iri.toString()));
    }

    @Test
    public void shouldReturn_False_For_isLiteral_isFalse() {
        assertThat(annotationProperty.isLiteral(), is(false));
    }

    @Test
    public void shouldReturn_True_For_isEntityIRI() {
        assertThat(annotationProperty.isEntityIRI(), is(true));
    }

    @Test
    public void shouldReturn_False_For_isUntypedIRI_isFalse() {
        assertThat(annotationProperty.isUntypedIRI(), is(false));
    }

    @Test
    public void should_evaluateAsEffectiveBooleanValue() {
        assertThat(annotationProperty.evaluateAsEffectiveBooleanValue(mock(SolutionMapping.class), mock(AlgebraEvaluationContext.class)), is(EvaluationResult.getError()));
    }

    @Test
    public void should_evaluateAsSimpleLiteral() {
        assertThat(annotationProperty.evaluateAsSimpleLiteral(mock(SolutionMapping.class), mock(AlgebraEvaluationContext.class)), is(EvaluationResult.getResult(Literal.createRDFPlainLiteralNoLang(annotationProperty.getIRI().toString()))));
    }

    @Test
    public void should_evaluateAsStringLiteral() {
        assertThat(annotationProperty.evaluateAsStringLiteral(mock(SolutionMapping.class), mock(AlgebraEvaluationContext.class)), is(EvaluationResult.getResult(Literal.createRDFPlainLiteralNoLang(annotationProperty.getIRI().toString()))));
    }

    @Test
    public void should_evaluateAsLiteral() {
        assertThat(annotationProperty.evaluateAsLiteral(mock(SolutionMapping.class), mock(AlgebraEvaluationContext.class)), is(EvaluationResult.getResult(Literal.createRDFPlainLiteralNoLang(iri.toString()))));
    }

    @Test
    public void should_evaluateAsIRI() {
        assertThat(annotationProperty.evaluate(mock(SolutionMapping.class), mock(AlgebraEvaluationContext.class)), is(EvaluationResult.getResult(new AtomicIRI(annotationProperty.getIRI()))));
    }
}
