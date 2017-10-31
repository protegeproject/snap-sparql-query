package org.semanticweb.owlapi.sparql.builtin.eval;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.sparql.algebra.AlgebraEvaluationContext;
import org.semanticweb.owlapi.sparql.api.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 31 Oct 2017
 */
@RunWith(MockitoJUnitRunner.class)
public class STR_Evaluator_TestCase {

    private STR_Evaluator evaluator;

    @Mock
    private SolutionMapping sm;

    @Mock
    private AlgebraEvaluationContext context;

    @Before
    public void setUp() throws Exception {
        evaluator = new STR_Evaluator();
    }

    @Test
    public void shouldEvaluateSimpleLiteral() {
        EvaluationResult result = evaluator.evaluate(Literal.createSimpleLiteral("Hello"), sm, context);
        assertThat(result.isError(), is(false));
        assertThat(result.getResult(), is(Literal.createSimpleLiteral("Hello")));
    }

    @Test
    public void shouldEvaluateXSDString() {
        EvaluationResult result = evaluator.evaluate(Literal.createString("Hello"), sm, context);
        assertThat(result.isError(), is(false));
        assertThat(result.getResult(), is(Literal.createSimpleLiteral("Hello")));
    }

    @Test
    public void shouldEvaluateIri() {
        String str = "http://hello.com";
        EvaluationResult result = evaluator.evaluate(new AtomicIRI(IRI.create(str)), sm, context);
        assertThat(result.isError(), is(false));
        assertThat(result.getResult(), is(Literal.createSimpleLiteral(str)));
    }

    @Test
    public void shouldEvaluateNumericLiteral() {
        EvaluationResult result = evaluator.evaluate(Literal.createInteger(33), sm, context);
        assertThat(result.isError(), is(false));
        assertThat(result.getResult(), is(Literal.createSimpleLiteral("33")));
    }

    @Test
    public void shouldEvaluateAnonymousIndividualAsError() {
        EvaluationResult result = evaluator.evaluate(new AnonymousIndividual("1"), sm, context);
        assertThat(result.isError(), is(true));
    }
}
