
package org.semanticweb.owlapi.sparql.builtin.eval;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.semanticweb.owlapi.sparql.api.EvaluationResult;
import org.semanticweb.owlapi.sparql.api.Expression;
import org.semanticweb.owlapi.sparql.api.SolutionMapping;
import org.semanticweb.owlapi.sparql.api.Variable;
import org.semanticweb.owlapi.sparql.sparqldl.EvaluationContext;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class BOUND_Evaluator_TestCase {

    private BOUND_Evaluator evaluator;

    @Mock
    private SolutionMapping solutionMapping;

    @Mock
    private Variable variable;

    @Before
    public void setUp() throws Exception {
        evaluator = new BOUND_Evaluator();
    }

    @Test
    public void shouldReturnErrorForNonVariable() {
        assertThat(evaluator.evaluate(mock(Expression.class), solutionMapping, mock(EvaluationContext.class)), is(EvaluationResult.getError()));
    }

    @Test
    public void shouldReturnTrue() {
        when(solutionMapping.isMapped(org.mockito.Matchers.any(Variable.class))).thenReturn(true);
        assertThat(evaluator.evaluate(this.variable, solutionMapping, mock(EvaluationContext.class)), is(EvaluationResult.getTrue()));
    }

    @Test
    public void shouldReturnFalse() {
        when(solutionMapping.isMapped(org.mockito.Matchers.any(Variable.class))).thenReturn(false);
        assertThat(evaluator.evaluate(variable, solutionMapping, mock(EvaluationContext.class)), is(EvaluationResult.getFalse()));
    }
}
