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
public class HOURS_Evaluator_TestCase {

    private static final String DATE_TIME = "2011-01-10T14:45:13.815-05:00";

    private HOURS_Evaluator hoursEvaluator;

    @Before
    public void setUp() {
        hoursEvaluator = new HOURS_Evaluator();
    }

    @Test
    public void shouldEvaluateHoursWithoutError() throws Exception {
        EvaluationResult result = getHoursEvaluationResult();
        assertThat(result.isError(), is(false));
    }

    @Test
    public void shouldEvaluateHoursAsSimpleLiteral() throws Exception {
        EvaluationResult result = getHoursEvaluationResult();
        assertThat(result.asSimpleLiteral(), is("14"));
    }

    @Test
    public void shouldEvaluateHoursAsNumeric() throws Exception {
        EvaluationResult result = getHoursEvaluationResult();
        assertThat(result.asNumeric(), is(14.0));
    }

    @Test
    public void shouldEvaluateHoursAsLiteral() throws Exception {
        EvaluationResult result = getHoursEvaluationResult();
        assertThat(result.asLiteral(), is(Literal.createInteger(14)));
    }

    private EvaluationResult getHoursEvaluationResult() {
        return hoursEvaluator.evaluate(
                DateTime.parseDateTime(DATE_TIME).get(),
                mock(Expression.class),
                SolutionMapping.emptyMapping());
    }
}
