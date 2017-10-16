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
public class SECONDS_Evaluator_TestCase {
    
    private static final String DATE_TIME = "2011-01-10T14:45:13.815-05:00";

    private SECONDS_Evaluator secondsEvaluator;

    @Before
    public void setUp() {
        secondsEvaluator = new SECONDS_Evaluator();
    }

    @Test
    public void shouldEvaluateSecondsWithoutError() throws Exception {
        EvaluationResult result = getSecondsEvaluationResult();
        assertThat(result.isError(), is(false));
    }

    @Test
    public void shouldEvaluateSecondsAsSimpleLiteral() throws Exception {
        EvaluationResult result = getSecondsEvaluationResult();
        assertThat(result.asSimpleLiteral(), is("13.815"));
    }

    @Test
    public void shouldEvaluateSecondsAsNumeric() throws Exception {
        EvaluationResult result = getSecondsEvaluationResult();
        assertThat(result.asNumeric(), is(13.815));
    }

    @Test
    public void shouldEvaluateSecondsAsLiteral() throws Exception {
        EvaluationResult result = getSecondsEvaluationResult();
        assertThat(result.asLiteral(), is(Literal.createDecimal(13.815)));
    }

    private EvaluationResult getSecondsEvaluationResult() {
        return secondsEvaluator.evaluate(
                DateTime.parseDateTime(DATE_TIME).get(),
                mock(Expression.class),
                SolutionMapping.emptyMapping());
    }
}
