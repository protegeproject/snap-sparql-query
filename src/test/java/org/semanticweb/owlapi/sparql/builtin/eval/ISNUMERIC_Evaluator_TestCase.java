package org.semanticweb.owlapi.sparql.builtin.eval;

import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.sparql.api.*;
import org.semanticweb.owlapi.sparql.sparqldl.EvaluationContext;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 16 Oct 2017
 */
public class ISNUMERIC_Evaluator_TestCase {

    private ISNUMERIC_Evaluator evaluator;

    @Before
    public void setUp() throws Exception {
        evaluator = new ISNUMERIC_Evaluator();
    }

    @Test
    public void shouldEvaluateIriAsNotNumeric() {
        EvaluationResult result = evaluator.evaluate(new AtomicIRI(IRI.create("Some IRI")), SolutionMapping.emptyMapping(), mock(EvaluationContext.class));
        assertThat(result.isError(), is(false));
        assertThat(result.isFalse(), is(true));
    }

    @Test
    public void shouldEvaluateAnonymousIndividualAsNotNumeric() {
        EvaluationResult result = evaluator.evaluate(new AnonymousIndividual("TheId"), SolutionMapping.emptyMapping(), mock(EvaluationContext.class));
        assertThat(result.isError(), is(false));
        assertThat(result.isFalse(), is(true));
    }

    @Test
    public void shouldEvaluateSimpleLiteralAsNotNumeric() {
        EvaluationResult result = evaluator.evaluate(Literal.createSimpleLiteral("Lit"), SolutionMapping.emptyMapping(), mock(EvaluationContext.class));
        assertThat(result.isError(), is(false));
        assertThat(result.isFalse(), is(true));
    }

    @Test
    public void shouldEvaluatePlainLiteralAsNotNumeric() {
        EvaluationResult result = evaluator.evaluate(Literal.createRDFPlainLiteralNoLang("Lit"), SolutionMapping.emptyMapping(), mock(EvaluationContext.class));
        assertThat(result.isError(), is(false));
        assertThat(result.isFalse(), is(true));
    }

    @Test
    public void shouldEvaluateIntegerAsNumeric() {
        EvaluationResult result = evaluator.evaluate(Literal.createInteger(3), SolutionMapping.emptyMapping(), mock(EvaluationContext.class));
        assertThat(result.isError(), is(false));
        assertThat(result.isTrue(), is(true));
    }

    @Test
    public void shouldEvaluateDoubleAsNumeric() {
        EvaluationResult result = evaluator.evaluate(Literal.createDouble(3), SolutionMapping.emptyMapping(), mock(EvaluationContext.class));
        assertThat(result.isError(), is(false));
        assertThat(result.isTrue(), is(true));
    }

    @Test
    public void shouldEvaluateDecimalAsNumeric() {
        EvaluationResult result = evaluator.evaluate(Literal.createDecimal(3), SolutionMapping.emptyMapping(), mock(EvaluationContext.class));
        assertThat(result.isError(), is(false));
        assertThat(result.isTrue(), is(true));
    }

    @Test
    public void shouldEvaluateLongAsNumeric() {
        EvaluationResult result = evaluator.evaluate(Literal.createLong(3), SolutionMapping.emptyMapping(), mock(EvaluationContext.class));
        assertThat(result.isError(), is(false));
        assertThat(result.isTrue(), is(true));
    }

}
