package org.semanticweb.owlapi.sparql.builtin.eval;

import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.sparql.api.EvaluationResult;
import org.semanticweb.owlapi.sparql.api.Literal;
import org.semanticweb.owlapi.sparql.api.SolutionMapping;
import org.semanticweb.owlapi.sparql.algebra.AlgebraEvaluationContext;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 16 Oct 2017
 */
public class MD5_Evaluator_TestCase {

    private MD5_Evaluator evaluator;

    @Before
    public void setUp() throws Exception {
        evaluator = new MD5_Evaluator();
    }

    @Test
    public void shouldEvaluateMd5OfSimpleLiteral() {
        EvaluationResult result = evaluator.evaluate(Literal.createSimpleLiteral("abc"), SolutionMapping.emptyMapping(), mock(AlgebraEvaluationContext.class));
        assertThat(result.isError(), is(false));
        assertThat(result.asSimpleLiteral(), is("900150983cd24fb0d6963f7d28e17f72"));
    }

    @Test
    public void shouldEvaluateMd5OfStringLiteral() {
        EvaluationResult result = evaluator.evaluate(Literal.createString("abc"), SolutionMapping.emptyMapping(), mock(AlgebraEvaluationContext.class));
        assertThat(result.isError(), is(false));
        assertThat(result.asSimpleLiteral(), is("900150983cd24fb0d6963f7d28e17f72"));
    }
}
