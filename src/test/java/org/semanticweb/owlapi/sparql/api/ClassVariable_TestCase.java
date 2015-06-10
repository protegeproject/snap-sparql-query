
package org.semanticweb.owlapi.sparql.api;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class ClassVariable_TestCase {

    private ClassVariable classVariable;
    private String variableName = "The variableName";

    @Before
    public void setUp()
        throws Exception
    {
        classVariable = new ClassVariable(variableName);
    }

    @Test(expected = java.lang.NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_variableName_IsNull() {
        new ClassVariable(null);
    }

    @Test
    public void shouldBeEqualToSelf() {
        MatcherAssert.assertThat(classVariable, Matchers.is(classVariable));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        MatcherAssert.assertThat(classVariable.equals(null), Matchers.is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        MatcherAssert.assertThat(classVariable, Matchers.is(new ClassVariable(variableName)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_variableName() {
        MatcherAssert.assertThat(classVariable, Matchers.is(Matchers.not(new ClassVariable("String-dfc4be56-1836-4efe-82f9-2a6508f2cbb1"))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        MatcherAssert.assertThat(classVariable.hashCode(), Matchers.is(new ClassVariable(variableName).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        MatcherAssert.assertThat(classVariable.toString(), Matchers.startsWith("ClassVariable"));
    }

    @Test
    public void should_getType() {
        MatcherAssert.assertThat(classVariable.getType(), Matchers.is(PrimitiveType.CLASS));
    }

}
