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
public class MONTH_Evaluator_TestCase {

    private static final String DATE_TIME = "2011-01-10T14:45:13.815-05:00";

    private MONTH_Evaluator monthEvaluator;

    @Before
    public void setUp() {
        monthEvaluator = new MONTH_Evaluator();
    }

    @Test
    public void shouldEvaluateMonthWithoutError() throws Exception {
        EvaluationResult result = getMonthEvaluationResult();
        assertThat(result.isError(), is(false));
    }

    @Test
    public void shouldEvaluateMonthAsSimpleLiteral() throws Exception {
        EvaluationResult result = getMonthEvaluationResult();
        assertThat(result.asSimpleLiteral(), is("1"));
    }

    @Test
    public void shouldEvaluateMonthAsNumeric() throws Exception {
        EvaluationResult result = getMonthEvaluationResult();
        assertThat(result.asNumeric(), is(1.0));
    }

    @Test
    public void shouldEvaluateMonthAsLiteral() throws Exception {
        EvaluationResult result = getMonthEvaluationResult();
        assertThat(result.asLiteral(), is(Literal.createInteger(1)));
    }

    private EvaluationResult getMonthEvaluationResult() {
        return monthEvaluator.evaluate(
                DateTime.parseDateTime(DATE_TIME).get(),
                mock(Expression.class),
                SolutionMapping.emptyMapping());
    }
}
