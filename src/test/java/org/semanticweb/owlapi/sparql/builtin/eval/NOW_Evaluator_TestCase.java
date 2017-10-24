package org.semanticweb.owlapi.sparql.builtin.eval;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.sparql.api.EvaluationResult;
import org.semanticweb.owlapi.sparql.api.SolutionMapping;
import org.semanticweb.owlapi.sparql.algebra.AlgebraEvaluationContext;

import java.time.ZonedDateTime;

import static java.util.Collections.emptyList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 16 Oct 2017
 */
@RunWith(MockitoJUnitRunner.class)
public class NOW_Evaluator_TestCase {

    @Mock
    private AlgebraEvaluationContext evaluationContext;

    @Before
    public void setUp() throws Exception {
        ZonedDateTime evalTime = ZonedDateTime.now();
        when(evaluationContext.getEvaluationTime()).thenReturn(evalTime);
    }

    @Test
    public void shouldReturnSameValue() throws Exception {
        NOW_Evaluator evaluator = new NOW_Evaluator();
        EvaluationResult resultA = evaluator.evaluate(emptyList(), SolutionMapping.emptyMapping(), evaluationContext);
        Thread.sleep(50);
        EvaluationResult resultB = evaluator.evaluate(emptyList(), SolutionMapping.emptyMapping(), evaluationContext);
        assertThat(resultA.isError(), is(false));
        assertThat(resultB.isError(), is(false));
        assertThat(resultA, is(resultB));
    }
}
