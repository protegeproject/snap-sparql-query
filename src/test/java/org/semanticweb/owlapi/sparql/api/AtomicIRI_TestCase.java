
package org.semanticweb.owlapi.sparql.api;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.sparql.algebra.AlgebraEvaluationContext;
import org.semanticweb.owlapi.vocab.XSDVocabulary;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class AtomicIRI_TestCase {

    private AtomicIRI atomicIRI;

    @Mock
    private IRI iri;

    @Mock
    private SolutionMapping sm;

    @Before
    public void setUp() {
        atomicIRI = new AtomicIRI(iri);
    }

    @Test(expected = java.lang.NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_iri_IsNull() {
        new AtomicIRI(null);
    }

    @Test
    public void shouldReturnSupplied_iri() {
        assertThat(atomicIRI.getIRI(), is(this.iri));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(atomicIRI, is(atomicIRI));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        assertThat(atomicIRI.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(atomicIRI, is(new AtomicIRI(iri)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_iri() {
        assertThat(atomicIRI, is(Matchers.not(new AtomicIRI(Mockito.mock(IRI.class)))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(atomicIRI.hashCode(), is(new AtomicIRI(iri).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(atomicIRI.toString(), Matchers.startsWith("AtomicIRI"));
    }

    @Test
    public void shouldNotContainVariables() {
        assertThat(atomicIRI.getVariables(), is(empty()));
    }

    @Test
    public void should_getIdentifier() {
        assertThat(atomicIRI.getIdentifier(), is(iri.toString()));
    }

    @Test
    public void should_evaluateAsEffectiveBooleanValue() {
        // See EffectiveBooleanValue_TestCase
    }

    @Test
    public void should_evaluateAsSimpleLiteral() {
        assertThat(atomicIRI.evaluateAsSimpleLiteral(sm, mock(AlgebraEvaluationContext.class)).isError(), is(false));
    }

    @Test
    public void shouldReturn_False_For_isLiteral() {
        assertThat(atomicIRI.isLiteral(), is(false));
    }

    @Test
    public void shouldReturn_False_For_isEntityIRI() {
        assertThat(atomicIRI.isEntityIRI(), is(false));
    }

    @Test
    public void shouldReturn_True_For_isUntypedIRI() {
        assertThat(atomicIRI.isUntypedIRI(), is(true));
    }

    @Test
    public void should_evaluateAsIRI() {
        EvaluationResult eval = atomicIRI.evaluate(sm, mock(AlgebraEvaluationContext.class));
        assertThat(eval.isError(), is(false));
        assertThat(eval.getResult(), Matchers.<RDFTerm>is(atomicIRI));
    }

    @Test
    public void should_collectVariables() {
        Set<Variable> variables = new HashSet<>();
        atomicIRI.collectVariables(variables);
        assertThat(variables, is(empty()));
    }

}
