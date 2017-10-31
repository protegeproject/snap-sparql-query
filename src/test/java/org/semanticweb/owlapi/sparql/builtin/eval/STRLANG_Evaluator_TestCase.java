package org.semanticweb.owlapi.sparql.builtin.eval;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.sparql.algebra.AlgebraEvaluationContext;
import org.semanticweb.owlapi.sparql.api.EvaluationResult;
import org.semanticweb.owlapi.sparql.api.Literal;
import org.semanticweb.owlapi.sparql.api.SolutionMapping;

import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 31 Oct 2017
 */
@RunWith(MockitoJUnitRunner.class)
public class STRLANG_Evaluator_TestCase {

    private STRLANG_Evaluator evaluator;

    @Mock
    private SolutionMapping sm;

    @Mock
    private AlgebraEvaluationContext context;

    @Before
    public void setUp() throws Exception {
        evaluator = new STRLANG_Evaluator();
    }

    @Test
    public void shouldEvaluateSimpleStringAndSimpleString() {
        EvaluationResult result = evaluator.evaluate(Arrays.asList(Literal.createSimpleLiteral("abc"), Literal.createSimpleLiteral("en")), sm, context);
        assertThat(result.getResult(), is(Literal.createRDFPlainLiteral("abc", "en")));
    }
}
