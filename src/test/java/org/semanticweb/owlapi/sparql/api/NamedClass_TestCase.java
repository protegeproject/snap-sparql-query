
package org.semanticweb.owlapi.sparql.api;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.sparql.algebra.AlgebraEvaluationContext;

import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class NamedClass_TestCase {
    
    
    @Mock
    private IRI iri;

    private NamedClass namedClass;

    @Before
    public void setUp()
        throws Exception
    {
        namedClass = new NamedClass(iri);
    }

    @Test
    public void shouldBeEqualToSelf() {
        MatcherAssert.assertThat(namedClass, Matchers.is(namedClass));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        MatcherAssert.assertThat(namedClass.equals(null), Matchers.is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        MatcherAssert.assertThat(namedClass, Matchers.is(new NamedClass(iri)));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        MatcherAssert.assertThat(namedClass.hashCode(), Matchers.is(new NamedClass(iri).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        MatcherAssert.assertThat(namedClass.toString(), Matchers.startsWith("NamedClass"));
    }

    @Test
    public void should_getVariables() {
        assertThat(namedClass.getVariables(), is(Collections.<Variable>emptySet()));
    }

    @Test
    public void should_getIRI() {
        assertThat(namedClass.getIRI(), is(iri));
    }

    @Test
    public void should_getIdentifier() {
        assertThat(namedClass.getIdentifier(), is(iri.toString()));
    }

    @Test
    public void shouldReturn_False_For_isLiteral_isFalse() {
        assertThat(namedClass.isLiteral(), is(false));
    }

    @Test
    public void shouldReturn_True_For_isEntityIRI() {
        assertThat(namedClass.isEntityIRI(), is(true));
    }

    @Test
    public void shouldReturn_False_For_isUntypedIRI_isFalse() {
        assertThat(namedClass.isUntypedIRI(), is(false));
    }

    @Test
    public void should_evaluateAsEffectiveBooleanValue() {
        assertThat(namedClass.evaluateAsEffectiveBooleanValue(mock(SolutionMapping.class), mock(AlgebraEvaluationContext.class)), is(EvaluationResult.getError()));
    }

    @Test
    public void should_evaluateAsLiteral() {
        assertThat(namedClass.evaluateAsLiteral(mock(SolutionMapping.class), mock(AlgebraEvaluationContext.class)), is(EvaluationResult.getResult(Literal.createRDFPlainLiteralNoLang(iri.toString()))));
    }

    @Test
    public void should_evaluateAsIRI() {
        assertThat(namedClass.evaluate(mock(SolutionMapping.class), mock(AlgebraEvaluationContext.class)), is(EvaluationResult.getResult(new AtomicIRI(namedClass.getIRI()))));
    }
}
