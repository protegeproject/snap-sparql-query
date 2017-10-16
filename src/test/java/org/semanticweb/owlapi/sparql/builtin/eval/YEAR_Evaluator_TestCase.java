package org.semanticweb.owlapi.sparql.builtin.eval;

import org.hamcrest.Matchers;
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
public class YEAR_Evaluator_TestCase {

    private static final String DATE_TIME = "2016-08-09T10:20:03.3Z";

    private YEAR_Evaluator yearEvaluator;

    @Before
    public void setUp() {
        yearEvaluator = new YEAR_Evaluator();
    }

    @Test
    public void shouldEvaluateYearWithoutError() throws Exception {
        EvaluationResult result = getYearEvaluationResult();
        assertThat(result.isError(), is(false));
    }

    @Test
    public void shouldEvaluateYearAsSimpleLiteral() throws Exception {
        EvaluationResult result = getYearEvaluationResult();
        assertThat(result.asSimpleLiteral(), is("2016"));
    }

    @Test
    public void shouldEvaluateYearAsNumeric() throws Exception {
        EvaluationResult result = getYearEvaluationResult();
        assertThat(result.asNumeric(), is(2016.0));
    }

    @Test
    public void shouldEvaluateYearAsLiteral() throws Exception {
        EvaluationResult result = getYearEvaluationResult();
        assertThat(result.asLiteral(), is(Literal.createInteger(2016)));
    }

    private EvaluationResult getYearEvaluationResult() {
        return yearEvaluator.evaluate(
                    DateTime.parseDateTime(DATE_TIME).get(),
                    mock(Expression.class),
                    SolutionMapping.emptyMapping());
    }
}
