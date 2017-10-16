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
public class DAY_Evaluator_TestCase {

    private static final String DATE_TIME = "2011-01-10T14:45:13.815-05:00";

    private DAY_Evaluator dayEvaluator;

    @Before
    public void setUp() {
        dayEvaluator = new DAY_Evaluator();
    }

    @Test
    public void shouldEvaluateDayWithoutError() throws Exception {
        EvaluationResult result = getDayEvaluationResult();
        assertThat(result.isError(), is(false));
    }

    @Test
    public void shouldEvaluateDayAsSimpleLiteral() throws Exception {
        EvaluationResult result = getDayEvaluationResult();
        assertThat(result.asSimpleLiteral(), is("10"));
    }

    @Test
    public void shouldEvaluateDayAsNumeric() throws Exception {
        EvaluationResult result = getDayEvaluationResult();
        assertThat(result.asNumeric(), is(10.0));
    }

    @Test
    public void shouldEvaluateDayAsLiteral() throws Exception {
        EvaluationResult result = getDayEvaluationResult();
        assertThat(result.asLiteral(), is(Literal.createInteger(10)));
    }

    private EvaluationResult getDayEvaluationResult() {
        return dayEvaluator.evaluate(
                DateTime.parseDateTime(DATE_TIME).get(),
                mock(Expression.class),
                SolutionMapping.emptyMapping());
    }
}
