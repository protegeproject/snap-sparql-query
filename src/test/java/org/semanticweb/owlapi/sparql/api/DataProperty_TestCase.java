
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
public class DataProperty_TestCase {

    private DataProperty dataProperty;
    @Mock
    private IRI iri;

    @Before
    public void setUp()
        throws Exception
    {
        dataProperty = new DataProperty(iri);
    }

    @Test(expected = java.lang.NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_iri_IsNull() {
        new DataProperty(null);
    }

    @Test
    public void shouldReturnSupplied_iri() {
        MatcherAssert.assertThat(dataProperty.getIRI(), Matchers.is(this.iri));
    }

    @Test
    public void shouldBeEqualToSelf() {
        MatcherAssert.assertThat(dataProperty, Matchers.is(dataProperty));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        MatcherAssert.assertThat(dataProperty.equals(null), Matchers.is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        MatcherAssert.assertThat(dataProperty, Matchers.is(new DataProperty(iri)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_iri() {
        MatcherAssert.assertThat(dataProperty, Matchers.is(Matchers.not(new DataProperty(Mockito.mock(IRI.class)))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        MatcherAssert.assertThat(dataProperty.hashCode(), Matchers.is(new DataProperty(iri).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        MatcherAssert.assertThat(dataProperty.toString(), Matchers.startsWith("DataProperty"));
    }

}
