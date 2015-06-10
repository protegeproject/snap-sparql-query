
package org.semanticweb.owlapi.sparql.api;

import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Optional;
import jpaul.Constraints.Var;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class SolutionMapping_TestCase {

    private SolutionMapping solutionMapping;

    private Map<Variable, Term> map;

    @Mock
    private Variable variable, variable2;

    @Mock
    private Term term, term2;

    @Before
    public void setUp() {
        when(variable.getName()).thenReturn("x");
        when(variable2.getName()).thenReturn("y");
        map = new HashMap<>();
        map.put(variable, term);
        map.put(variable2, term2);
        solutionMapping = new SolutionMapping(map);
    }

    @Test(expected = java.lang.NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_map_IsNull() {
        new SolutionMapping(null);
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(solutionMapping, is(solutionMapping));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        assertThat(solutionMapping.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(solutionMapping, is(new SolutionMapping(map)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_map() {
        assertThat(solutionMapping, is(Matchers.not(new SolutionMapping(mock(Map.class)))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(solutionMapping.hashCode(), is(new SolutionMapping(map).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(solutionMapping.toString(), Matchers.startsWith("SolutionMapping"));
    }

    @Test
    public void shouldReturn_true_For_isMapped() {
        assertThat(solutionMapping.isMapped(variable), is(true));
    }

    @Test
    public void shouldReturn_false_For_isMapped() {
        Variable otherVariable = mock(Variable.class);
        assertThat(solutionMapping.isMapped(otherVariable), is(false));
    }

    @Test
    public void should_bindVariableToTerm() {
        Variable otherVariable = mock(Variable.class);
        when(otherVariable.getName()).thenReturn("y");
        Term otherTerm = mock(Term.class);
        solutionMapping.bind(otherVariable, otherTerm);
        assertThat(solutionMapping.getTermForVariable(otherVariable), is(Optional.of(otherTerm)));
    }

    @Test
    public void should_Return_Map_when_asMap() {
        assertThat(solutionMapping.asMap(), is(map));
    }

    @Test
    public void should_getTermForVariable() {
        assertThat(solutionMapping.getTermForVariable(variable), is(Optional.of(term)));
    }

    @Test
    public void should_getTermForVariableName() {
        assertThat(solutionMapping.getTermForVariableName("?x"), is(Optional.of(term)));
    }

}
