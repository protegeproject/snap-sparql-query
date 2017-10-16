package org.semanticweb.owlapi.sparql.builtin.eval;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.sparql.api.*;

import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 16 Oct 2017
 */
public class ISIRI_Evaluator_TestCase {

    private final AtomicIRI iri = new AtomicIRI(IRI.create("http://stuff.com"));

    private ISIRI_Evaluator evaluator;

    @Before
    public void setUp() throws Exception {
        evaluator = new ISIRI_Evaluator();
    }

    @Test
    public void shouldEvaluateIRIAsTrue() {
        EvaluationResult result = evaluator.evaluate(Collections.singletonList(iri), SolutionMapping.emptyMapping());
        assertThat(result.isError(), is(false));
        assertThat(result.isTrue(), is(true));
    }

    @Test
    public void shouldEvaluateLiteralAsFalse() {
        EvaluationResult result = evaluator.evaluate(Collections.singletonList(Literal.createString("Lit")), SolutionMapping.emptyMapping());
        assertThat(result.isError(), is(false));
        assertThat(result.isTrue(), is(false));
    }

    @Test
    public void shouldEvaluateAnonymousIndividualAsFalse() {
        EvaluationResult result = evaluator.evaluate(Collections.singletonList(new AnonymousIndividual("TheId")), SolutionMapping.emptyMapping());
        assertThat(result.isError(), is(false));
        assertThat(result.isTrue(), is(false));
    }

    @Test
    public void shouldEvaluateMultiArgsAsError() {
        EvaluationResult result = evaluator.evaluate(Arrays.asList(iri, iri), SolutionMapping.emptyMapping());
        assertThat(result.isError(), is(true));
    }
}
