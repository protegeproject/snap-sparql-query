
package org.semanticweb.owlapi.sparql.api;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class AnnotationAssertion_TestCase {

    private AnnotationAssertion annotationAssertion;

    @Mock
    private AtomicAnnotationProperty property;

    @Mock
    private AnnotationSubject subject;

    @Mock
    private AnnotationValue object;

    @Before
    public void setUp()
        throws Exception
    {
        annotationAssertion = new AnnotationAssertion(property, subject, object);
    }

    @Test(expected = java.lang.NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_property_IsNull() {
        new AnnotationAssertion(null, subject, object);
    }

    @Test(expected = java.lang.NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_subject_IsNull() {
        new AnnotationAssertion(property, null, object);
    }

    @Test(expected = java.lang.NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_object_IsNull() {
        new AnnotationAssertion(property, subject, null);
    }

    @Test
    public void shouldBeEqualToSelf() {
        MatcherAssert.assertThat(annotationAssertion, Matchers.is(annotationAssertion));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        MatcherAssert.assertThat(annotationAssertion.equals(null), Matchers.is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        MatcherAssert.assertThat(annotationAssertion, Matchers.is(new AnnotationAssertion(property, subject, object)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_property() {
        MatcherAssert.assertThat(annotationAssertion, Matchers.is(Matchers.not(new AnnotationAssertion(Mockito.mock(AtomicAnnotationProperty.class), subject, object))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_subject() {
        MatcherAssert.assertThat(annotationAssertion, Matchers.is(Matchers.not(new AnnotationAssertion(property, Mockito.mock(AnnotationSubject.class), object))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_object() {
        MatcherAssert.assertThat(annotationAssertion, Matchers.is(Matchers.not(new AnnotationAssertion(property, subject, Mockito.mock(AnnotationValue.class)))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        MatcherAssert.assertThat(annotationAssertion.hashCode(), Matchers.is(new AnnotationAssertion(property, subject, object).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        MatcherAssert.assertThat(annotationAssertion.toString(), Matchers.startsWith("AnnotationAssertion"));
    }

}
