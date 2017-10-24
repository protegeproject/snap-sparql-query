package org.semanticweb.owlapi.sparql.builtin.eval;

import org.junit.Test;
import org.semanticweb.owlapi.sparql.api.EvaluationResult;
import org.semanticweb.owlapi.sparql.api.SolutionMapping;
import org.semanticweb.owlapi.sparql.sparqldl.EvaluationContext;

import java.time.ZonedDateTime;
import java.util.Collections;

import static java.util.Collections.emptyList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 16 Oct 2017
 */
public class NOW_Evaluator_TestCase {

    @Test
    public void shouldReturnSameValue() throws Exception {
        NOW_Evaluator evaluator = new NOW_Evaluator();
        EvaluationContext evaluationContext = new EvaluationContext(ZonedDateTime.now());
        EvaluationResult resultA = evaluator.evaluate(emptyList(), SolutionMapping.emptyMapping(), evaluationContext);
        Thread.sleep(50);
        EvaluationResult resultB = evaluator.evaluate(emptyList(), SolutionMapping.emptyMapping(), evaluationContext);
        assertThat(resultA.isError(), is(false));
        assertThat(resultB.isError(), is(false));
        assertThat(resultA, is(resultB));
    }
}
