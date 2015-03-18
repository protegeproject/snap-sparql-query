
package org.semanticweb.owlapi.sparql.api;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.semanticweb.owlapi.model.IRI;

import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class ObjectProperty_TestCase {


    @Mock
    private SolutionMapping sm;

    @Mock
    private IRI iri;

    private ObjectProperty objectProperty;

    @Before
    public void setUp()
            throws Exception {
        objectProperty = new ObjectProperty(iri);
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(objectProperty, is(objectProperty));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        assertThat(objectProperty.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(objectProperty, is(new ObjectProperty(iri)));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(objectProperty.hashCode(), is(new ObjectProperty(iri).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(objectProperty.toString(), Matchers.startsWith("ObjectProperty"));
    }


    @Test
    public void should_getVariables() {
        assertThat(objectProperty.getVariables(), is(Collections.<Variable>emptySet()));
    }

    @Test
    public void should_getIRI() {
        assertThat(objectProperty.getIRI(), is(iri));
    }

    @Test
    public void should_getIdentifier() {
        assertThat(objectProperty.getIdentifier(), is(iri.toString()));
    }

    @Test
    public void shouldReturn_False_For_isLiteral_isFalse() {
        assertThat(objectProperty.isLiteral(), is(false));
    }

    @Test
    public void shouldReturn_True_For_isEntityIRI() {
        assertThat(objectProperty.isEntityIRI(), is(true));
    }

    @Test
    public void shouldReturn_False_For_isUntypedIRI_isFalse() {
        assertThat(objectProperty.isUntypedIRI(), is(false));
    }

    @Test
    public void should_evaluateAsEffectiveBooleanValue() {
        assertThat(objectProperty.evaluateAsEffectiveBooleanValue(sm), is(EvaluationResult.getError()));
    }

    @Test
    public void should_evaluateAsSimpleLiteral() {
        assertThat(objectProperty.evaluateAsSimpleLiteral(sm), is(EvaluationResult.getResult(new Literal(objectProperty.getIRI().toString(), ""))));
    }

    @Test
    public void should_evaluateAsStringLiteral() {
        assertThat(objectProperty.evaluateAsStringLiteral(sm), is(EvaluationResult.getResult(new Literal(objectProperty.getIRI().toString(), ""))));
    }

    @Test
    public void shouldReturn_Error_When_evaluateAsNumeric() {
        assertThat(objectProperty.evaluateAsNumeric(sm), is(EvaluationResult.getError()));
    }

    @Test
    public void shouldReturn_Error_When_evaluateAsDateTime() {
        assertThat(objectProperty.evaluateAsDateTime(sm), is(EvaluationResult.getError()));
    }

    @Test
    public void should_evaluateAsLiteral() {
        assertThat(objectProperty.evaluateAsLiteral(sm), is(EvaluationResult.getResult(new Literal(iri.toString(), ""))));
    }

    @Test
    public void should_evaluateAsIRI() {
        assertThat(objectProperty.evaluateAsIRI(sm), is(EvaluationResult.getResult(new AtomicIRI(objectProperty.getIRI()))));
    }
}
