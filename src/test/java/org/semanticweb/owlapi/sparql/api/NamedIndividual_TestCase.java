
package org.semanticweb.owlapi.sparql.api;

import jpaul.Constraints.Var;
import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.hamcrest.core.IsAnything;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.semanticweb.owlapi.model.IRI;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class NamedIndividual_TestCase {


    @Mock
    public IRI iri;

    private NamedIndividual namedIndividual;

    @Before
    public void setUp()
        throws Exception
    {
        namedIndividual = new NamedIndividual(iri);
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(namedIndividual, is(namedIndividual));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        assertThat(namedIndividual.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(namedIndividual, is(new NamedIndividual(iri)));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(namedIndividual.hashCode(), is(new NamedIndividual(iri).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(namedIndividual.toString(), Matchers.startsWith("NamedIndividual"));
    }

    @Test
    public void should_Return_IRI_When_toAnnotationSubject() {
        assertThat(namedIndividual.toAnnotationSubject(), Matchers.<AnnotationSubject>is(new AtomicIRI(iri)));
    }

    @Test
    public void should_getVariables() {
        assertThat(namedIndividual.getVariables(), is(Collections.<Variable>emptySet()));
    }

    @Test
    public void should_getIRI() {
        assertThat(namedIndividual.getIRI(), is(iri));
    }

    @Test
    public void should_getIdentifier() {
        assertThat(namedIndividual.getIdentifier(), is(iri.toString()));
    }

    @Test
    public void shouldReturn_False_For_isLiteral_isFalse() {
        assertThat(namedIndividual.isLiteral(), is(false));
    }

    @Test
    public void shouldReturn_True_For_isEntityIRI() {
        assertThat(namedIndividual.isEntityIRI(), is(true));
    }

    @Test
    public void shouldReturn_False_For_isUntypedIRI_isFalse() {
        assertThat(namedIndividual.isUntypedIRI(), is(false));
    }

    @Test
    public void should_evaluateAsEffectiveBooleanValue() {
        assertThat(namedIndividual.evaluateAsEffectiveBooleanValue(mock(SolutionMapping.class)), is(EvaluationResult.getError()));
    }

    @Test
    public void should_evaluateAsSimpleLiteral() {
        assertThat(namedIndividual.evaluateAsSimpleLiteral(mock(SolutionMapping.class)), is(EvaluationResult.getResult(new Literal(namedIndividual.getIRI().toString(), ""))));
    }

    @Test
    public void should_evaluateAsStringLiteral() {
        assertThat(namedIndividual.evaluateAsStringLiteral(mock(SolutionMapping.class)), is(EvaluationResult.getResult(new Literal(namedIndividual.getIRI().toString(), ""))));
    }

    @Test
    public void shouldReturn_Error_When_evaluateAsNumeric() {
        assertThat(namedIndividual.evaluateAsNumeric(mock(SolutionMapping.class)), is(EvaluationResult.getError()));
    }

    @Test
    public void shouldReturn_Error_When_evaluateAsDateTime() {
        assertThat(namedIndividual.evaluateAsDateTime(mock(SolutionMapping.class)), is(EvaluationResult.getError()));
    }

    @Test
    public void should_evaluateAsLiteral() {
        assertThat(namedIndividual.evaluateAsLiteral(mock(SolutionMapping.class)), is(EvaluationResult.getResult(new Literal(iri.toString(), ""))));
    }

    @Test
    public void should_evaluateAsIRI() {
        assertThat(namedIndividual.evaluateAsIRI(mock(SolutionMapping.class)), is(EvaluationResult.getResult(new AtomicIRI(namedIndividual.getIRI()))));
    }
}
