
package org.semanticweb.owlapi.sparql.algebra;

import com.google.common.collect.ImmutableList;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.sparql.api.EvaluationResult;

import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class GroupKey_TestCase {

    private GroupKey groupKey;
    @Mock
    private ImmutableList<EvaluationResult> key, otherKey;

    @Before
    public void setUp()
        throws Exception
    {
        groupKey = new GroupKey(key);
    }

    @Test(expected = java.lang.NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_key_IsNull() {
        new GroupKey(null);
    }

    @Test
    public void shouldReturnSupplied_key() {
        assertThat(groupKey.getKey(), Matchers.is(this.key));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(groupKey, Matchers.is(groupKey));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        assertThat(groupKey.equals(null), Matchers.is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(groupKey, Matchers.is(new GroupKey(key)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_key() {
        assertThat(groupKey, Matchers.is(Matchers.not(new GroupKey(otherKey))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(groupKey.hashCode(), Matchers.is(new GroupKey(key).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(groupKey.toString(), Matchers.startsWith("GroupKey"));
    }
}
