
package org.semanticweb.owlapi.sparql.api;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class AnnotationPropertyVariable_TestCase {

    private AnnotationPropertyVariable annotationPropertyVariable;
    private String variableName = "The variableName";

    @Before
    public void setUp() {
        annotationPropertyVariable = new AnnotationPropertyVariable(variableName);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = java.lang.NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_variableName_IsNull() {
        new AnnotationPropertyVariable(null);
    }

    @Test
    public void shouldBeEqualToSelf() {
        MatcherAssert.assertThat(annotationPropertyVariable, Matchers.is(annotationPropertyVariable));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        MatcherAssert.assertThat(annotationPropertyVariable.equals(null), Matchers.is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        MatcherAssert.assertThat(annotationPropertyVariable, Matchers.is(new AnnotationPropertyVariable(variableName)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_variableName() {
        MatcherAssert.assertThat(annotationPropertyVariable, Matchers.is(Matchers.not(new AnnotationPropertyVariable("String-1c4ba987-65c2-4ced-8192-8f34c72a7335"))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        MatcherAssert.assertThat(annotationPropertyVariable.hashCode(), Matchers.is(new AnnotationPropertyVariable(variableName).hashCode()));
    }

    @Test
    public void shouldReturn_true_For_isEntityVariable() {
        MatcherAssert.assertThat(annotationPropertyVariable.isEntityVariable(), Matchers.is(true));
    }
}
