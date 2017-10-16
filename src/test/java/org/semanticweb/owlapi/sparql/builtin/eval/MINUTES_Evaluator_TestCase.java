package org.semanticweb.owlapi.sparql.builtin.eval;

import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.sparql.api.EvaluationResult;
import org.semanticweb.owlapi.sparql.api.Expression;
import org.semanticweb.owlapi.sparql.api.Literal;
import org.semanticweb.owlapi.sparql.api.SolutionMapping;
import org.semanticweb.owlapi.sparql.builtin.DateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 16 Oct 2017
 */
public class MINUTES_Evaluator_TestCase {

    private static final String DATE_TIME = "2011-01-10T14:45:13.815-05:00";

    private MINUTES_Evaluator minutesEvaluator;

    @Before
    public void setUp() {
        minutesEvaluator = new MINUTES_Evaluator();
    }

    @Test
    public void shouldEvaluateMinutesWithoutError() throws Exception {
        EvaluationResult result = getMinutesEvaluationResult();
        assertThat(result.isError(), is(false));
    }

    @Test
    public void shouldEvaluateMinutesAsSimpleLiteral() throws Exception {
        EvaluationResult result = getMinutesEvaluationResult();
        assertThat(result.asSimpleLiteral(), is("45"));
    }

    @Test
    public void shouldEvaluateMinutesAsNumeric() throws Exception {
        EvaluationResult result = getMinutesEvaluationResult();
        assertThat(result.asNumeric(), is(45.0));
    }

    @Test
    public void shouldEvaluateMinutesAsLiteral() throws Exception {
        EvaluationResult result = getMinutesEvaluationResult();
        assertThat(result.asLiteral(), is(Literal.createInteger(45)));
    }

    private EvaluationResult getMinutesEvaluationResult() {
        return minutesEvaluator.evaluate(
                DateTime.parseDateTime(DATE_TIME).get(),
                mock(Expression.class),
                SolutionMapping.emptyMapping());
    }
}
