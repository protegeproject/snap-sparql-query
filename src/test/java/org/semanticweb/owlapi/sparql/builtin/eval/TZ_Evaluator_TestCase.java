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
public class TZ_Evaluator_TestCase {


    private static final String DATE_TIME = "2011-01-10T14:45:13.815-05:00";

    private TZ_Evaluator tzEvaluator;

    @Before
    public void setUp() {
        tzEvaluator = new TZ_Evaluator();
    }

    @Test
    public void shouldEvaluateTzWithoutError() throws Exception {
        EvaluationResult result = getTzEvaluationResult();
        assertThat(result.isError(), is(false));
    }

    @Test
    public void shouldEvaluateTzAsSimpleLiteral() throws Exception {
        EvaluationResult result = getTzEvaluationResult();
        assertThat(result.asSimpleLiteral(), is("-05:00"));
    }

    @Test
    public void shouldEvaluateTzAsLiteral() throws Exception {
        EvaluationResult result = getTzEvaluationResult();
        assertThat(result.asLiteral(), is(Literal.createSimpleLiteral("-05:00")));
    }

    private EvaluationResult getTzEvaluationResult() {
        return tzEvaluator.evaluate(
                DateTime.parseDateTime(DATE_TIME).get(),
                mock(Expression.class),
                SolutionMapping.emptyMapping());
    }
}
