
package org.semanticweb.owlapi.sparql.algebra;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.sparql.api.EvaluationResult;
import org.semanticweb.owlapi.sparql.api.Variable;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AggregationEvaluation_TestCase {

    private AggregationEvaluation aggregationEvaluation;
    @Mock
    private GroupKey groupKey;
    @Mock
    private Variable variable, otherVariable;
    @Mock
    private EvaluationResult evaluationResult;

    @Before
    public void setUp() {

        when(variable.getName()).thenReturn("x");
        aggregationEvaluation = new AggregationEvaluation(groupKey, variable, evaluationResult);
    }

    @Test(expected = java.lang.NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_groupKey_IsNull() {
        new AggregationEvaluation(null, variable, evaluationResult);
    }

    @Test
    public void shouldReturnSupplied_groupKey() {
        assertThat(aggregationEvaluation.getGroupKey(), is(this.groupKey));
    }

    @Test(expected = java.lang.NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_variable_IsNull() {
        new AggregationEvaluation(groupKey, null, evaluationResult);
    }

    @Test
    public void shouldReturnSupplied_variable() {
        assertThat(aggregationEvaluation.getVariable(), is(this.variable));
    }

    @Test(expected = java.lang.NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_evaluationResult_IsNull() {
        new AggregationEvaluation(groupKey, variable, null);
    }

    @Test
    public void shouldReturnSupplied_evaluationResult() {
        assertThat(aggregationEvaluation.getEvaluationResult(), is(this.evaluationResult));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(aggregationEvaluation, is(aggregationEvaluation));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        assertThat(aggregationEvaluation.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(aggregationEvaluation, is(new AggregationEvaluation(groupKey, variable, evaluationResult)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_groupKey() {
        assertThat(aggregationEvaluation, is(Matchers.not(new AggregationEvaluation(Mockito.mock(GroupKey.class), variable, evaluationResult))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_evaluationResult() {
        assertThat(aggregationEvaluation, is(Matchers.not(new AggregationEvaluation(groupKey, variable, Mockito.mock(EvaluationResult.class)))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(aggregationEvaluation.hashCode(), is(new AggregationEvaluation(groupKey, variable, evaluationResult).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(aggregationEvaluation.toString(), Matchers.startsWith("AggregationEvaluation"));
    }

}
