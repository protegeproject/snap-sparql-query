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
public class IRI_Evaluator_TestCase {

    private static final String STRING = "http://abc.def";

    private IRI_Evaluator evaluator;

    @Mock
    private SolutionMapping sm;

    @Mock
    private AlgebraEvaluationContext context;

    @Before
    public void setUp() throws Exception {
        evaluator = new IRI_Evaluator();
    }

    @Test
    public void shouldEvaluateSimpleLiteral() {
        EvaluationResult result = evaluator.evaluate(Literal.createSimpleLiteral(STRING), sm, context);
        assertThat(result.isError(), is(false));
        assertThat(result.getResult(), is(new AtomicIRI(IRI.create(STRING))));
    }

    @Test
    public void shouldEvaluateXsdString() {
        EvaluationResult result = evaluator.evaluate(Literal.createString(STRING), sm, context);
        assertThat(result.isError(), is(false));
        assertThat(result.getResult(), is(new AtomicIRI(IRI.create(STRING))));
    }

    @Test
    public void shouldEvaluateIri() {
        EvaluationResult result = evaluator.evaluate(AtomicIRI.create(STRING), sm, context);
        assertThat(result.isError(), is(false));
        assertThat(result.getResult(), is(new AtomicIRI(IRI.create(STRING))));
    }

    @Test
    public void shouldNotEvaluateLiteralWithLanguageTag() {
        EvaluationResult result = evaluator.evaluate(Literal.createRDFPlainLiteral(STRING, "en"), sm, context);
        assertThat(result.isError(), is(true));
    }

    @Test
    public void shouldNotEvaluateAnonymousIndividual() {
        EvaluationResult result = evaluator.evaluate(new AnonymousIndividual(STRING), sm, context);
        assertThat(result.isError(), is(true));
    }


}
