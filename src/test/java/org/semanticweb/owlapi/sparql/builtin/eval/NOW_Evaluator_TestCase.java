package org.semanticweb.owlapi.sparql.builtin.eval;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.semanticweb.owlapi.sparql.api.EvaluationResult;
import org.semanticweb.owlapi.sparql.api.SolutionMapping;

import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 16 Oct 2017
 */
public class NOW_Evaluator_TestCase {

    @Test
    public void shouldReturnSameValue() throws Exception {
        NOW_Evaluator evaluator = new NOW_Evaluator();
        EvaluationResult resultA = evaluator.evaluate(Collections.emptyList(), SolutionMapping.emptyMapping());
        Thread.sleep(50);
        EvaluationResult resultB = evaluator.evaluate(Collections.emptyList(), SolutionMapping.emptyMapping());
        assertThat(resultA.isError(), is(false));
        assertThat(resultB.isError(), is(false));
        assertThat(resultA, is(resultB));
    }
}
