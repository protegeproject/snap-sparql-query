package org.semanticweb.owlapi.sparql.builtin.eval;

import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.sparql.api.EvaluationResult;
import org.semanticweb.owlapi.sparql.api.Literal;
import org.semanticweb.owlapi.sparql.api.SolutionMapping;
import org.semanticweb.owlapi.sparql.sparqldl.EvaluationContext;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 16 Oct 2017
 */
public class FLOOR_Evaluator_TestCase {

    private FLOOR_Evaluator evaluator;

    @Before
    public void setUp() throws Exception {
        evaluator = new FLOOR_Evaluator();
    }

    @Test
    public void shouldGetPositiveFlooring() {
        EvaluationResult result = evaluator.evaluate(Literal.createDecimal(10.5), SolutionMapping.emptyMapping(), mock(EvaluationContext.class));
        assertThat(result.getResult(), is(Literal.createDecimal(10)));
    }

    @Test
    public void shouldGetNegativeFlooring() {
        EvaluationResult result = evaluator.evaluate(Literal.createDecimal(-10.5), SolutionMapping.emptyMapping(), mock(EvaluationContext.class));
        assertThat(result.getResult(), is(Literal.createDecimal(-11)));
    }

    @Test
    public void shouldProduceError() {
        EvaluationResult result = evaluator.evaluate(Literal.createString("Hello"), SolutionMapping.emptyMapping(), mock(EvaluationContext.class));
        assertThat(result.isError(), is(true));
    }
}
