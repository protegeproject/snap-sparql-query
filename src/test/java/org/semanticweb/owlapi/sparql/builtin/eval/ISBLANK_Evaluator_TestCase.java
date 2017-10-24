package org.semanticweb.owlapi.sparql.builtin.eval;

import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.sparql.api.*;
import org.semanticweb.owlapi.sparql.sparqldl.EvaluationContext;

import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 16 Oct 2017
 */
public class ISBLANK_Evaluator_TestCase {

    private final AnonymousIndividual blank = new AnonymousIndividual("TheId");

    private ISBLANK_Evaluator evaluator;

    @Before
    public void setUp() throws Exception {
        evaluator = new ISBLANK_Evaluator();
    }

    @Test
    public void shouldEvaluateIRIAsFalse() {
        EvaluationResult result = evaluator.evaluate(Collections.singletonList(new AtomicIRI(IRI.create("http://some.iri.com/A"))), SolutionMapping.emptyMapping(), mock(EvaluationContext.class));
        assertThat(result.isError(), is(false));
        assertThat(result.isTrue(), is(false));
    }

    @Test
    public void shouldEvaluateLiteralAsFalse() {
        EvaluationResult result = evaluator.evaluate(Collections.singletonList(Literal.createString("Lit")), SolutionMapping.emptyMapping(), mock(EvaluationContext.class));
        assertThat(result.isError(), is(false));
        assertThat(result.isTrue(), is(false));
    }

    @Test
    public void shouldEvaluateAnonymousIndividualAsTrue() {
        EvaluationResult result = evaluator.evaluate(Collections.singletonList(new AnonymousIndividual("TheId")), SolutionMapping.emptyMapping(), mock(EvaluationContext.class));
        assertThat(result.isError(), is(false));
        assertThat(result.isTrue(), is(true));
    }

    @Test
    public void shouldEvaluateMultiArgsAsError() {
        EvaluationResult result = evaluator.evaluate(Arrays.asList(blank, blank), SolutionMapping.emptyMapping(), mock(EvaluationContext.class));
        assertThat(result.isError(), is(true));
    }
}
