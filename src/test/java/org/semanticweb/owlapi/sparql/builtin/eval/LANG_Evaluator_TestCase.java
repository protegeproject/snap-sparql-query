package org.semanticweb.owlapi.sparql.builtin.eval;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.sparql.algebra.AlgebraEvaluationContext;
import org.semanticweb.owlapi.sparql.api.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 31 Oct 2017
 */
public class LANG_Evaluator_TestCase {

    private LANG_Evaluator evaluator;

    @Mock
    private SolutionMapping sm;

    @Mock
    private AlgebraEvaluationContext context;

    @Before
    public void setUp() throws Exception {
        evaluator = new LANG_Evaluator();
    }

    @Test
    public void shouldEvaluateLiteralWithLang() {
        EvaluationResult result = evaluator.evaluate(Literal.createRDFPlainLiteral("Hello", "en"), sm, context);
        assertThat(result.isError(), is(false));
        assertThat(result.getResult(), is(Literal.createSimpleLiteral("en")));
    }

    @Test
    public void shouldEvaluateLiteralWithoutLang() {
        EvaluationResult result = evaluator.evaluate(Literal.createRDFPlainLiteralNoLang("Hello"), sm, context);
        assertThat(result.isError(), is(false));
        assertThat(result.getResult(), is(Literal.createSimpleLiteral("")));
    }

    @Test
    public void shouldEvaluateAnonymousIndividualAsError() {
        EvaluationResult result = evaluator.evaluate(new AnonymousIndividual("1"), sm, context);
        assertThat(result.isError(), is(true));
    }

    @Test
    public void shouldEvaluateIriAsError() {
        EvaluationResult result = evaluator.evaluate(new AtomicIRI(IRI.create("http://something.com")), sm, context);
        assertThat(result.isError(), is(true));
    }
}
