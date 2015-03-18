
package org.semanticweb.owlapi.sparql.api;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class SubClassOf_TestCase {

    private SubClassOf subClassOf;
    @Mock
    private org.semanticweb.owlapi.sparql.api.ClassExpression subClass;
    @Mock
    private org.semanticweb.owlapi.sparql.api.ClassExpression superClass;

    @Before
    public void setUp()
        throws Exception
    {
        subClassOf = new SubClassOf(subClass, superClass);
    }

    @Test(expected = java.lang.NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_subClass_IsNull() {
        new SubClassOf(null, superClass);
    }

    @Test
    public void shouldReturnSupplied_subClass() {
        MatcherAssert.assertThat(subClassOf.getSubClass(), Matchers.is(this.subClass));
    }

    @Test(expected = java.lang.NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_superClass_IsNull() {
        new SubClassOf(subClass, null);
    }

    @Test
    public void shouldReturnSupplied_superClass() {
        MatcherAssert.assertThat(subClassOf.getSuperClass(), Matchers.is(this.superClass));
    }

    @Test
    public void shouldBeEqualToSelf() {
        MatcherAssert.assertThat(subClassOf, Matchers.is(subClassOf));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        MatcherAssert.assertThat(subClassOf.equals(null), Matchers.is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        MatcherAssert.assertThat(subClassOf, Matchers.is(new SubClassOf(subClass, superClass)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_subClass() {
        MatcherAssert.assertThat(subClassOf, Matchers.is(Matchers.not(new SubClassOf(Mockito.mock(org.semanticweb.owlapi.sparql.api.ClassExpression.class), superClass))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_superClass() {
        MatcherAssert.assertThat(subClassOf, Matchers.is(Matchers.not(new SubClassOf(subClass, Mockito.mock(org.semanticweb.owlapi.sparql.api.ClassExpression.class)))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        MatcherAssert.assertThat(subClassOf.hashCode(), Matchers.is(new SubClassOf(subClass, superClass).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        MatcherAssert.assertThat(subClassOf.toString(), Matchers.startsWith("SubClassOf"));
    }

}
