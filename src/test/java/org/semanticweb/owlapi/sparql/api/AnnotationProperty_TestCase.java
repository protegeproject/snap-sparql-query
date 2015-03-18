
package org.semanticweb.owlapi.sparql.api;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.semanticweb.owlapi.model.IRI;

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

}
