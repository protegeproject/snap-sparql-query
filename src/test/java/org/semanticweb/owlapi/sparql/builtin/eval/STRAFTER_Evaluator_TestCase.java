package org.semanticweb.owlapi.sparql.builtin.eval;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.semanticweb.owlapi.sparql.api.Datatype;
import org.semanticweb.owlapi.sparql.api.EvaluationResult;
import org.semanticweb.owlapi.sparql.api.Literal;
import org.semanticweb.owlapi.sparql.api.SolutionMapping;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 10/06/15
 */
public class STRAFTER_Evaluator_TestCase {

    private STRAFTER_Evaluator evaluator;

    @Mock
    private SolutionMapping sm;

    @Before
    public void setUp() throws Exception {
        evaluator = new STRAFTER_Evaluator();
    }

    @Test
    public void shouldReturnSubString() {
        EvaluationResult result = evaluator.evaluate(
                Literal.createRDFPlainLiteralNoLang("abc"),
                Literal.createRDFPlainLiteralNoLang("b"), sm);
        assertThat(result.isError(), is(false));
        Literal literal = result.asLiteral();
        assertThat(literal.getDatatype(), is(Datatype.getRDFPlainLiteral()));
        assertThat(literal.getLexicalForm(), is("c"));
        assertThat(literal.getLang(), is(""));
    }

    @Test
    public void shouldReturnSubStringPreservingLang() {
        EvaluationResult result = evaluator.evaluate(
                Literal.createRDFPlainLiteral("abc", "en"),
                Literal.createRDFPlainLiteralNoLang("ab"), sm);
        assertThat(result.isError(), is(false));
        Literal literal = result.asLiteral();
        assertThat(literal.getDatatype(), is(Datatype.getRDFPlainLiteral()));
        assertThat(literal.getLexicalForm(), is("c"));
        assertThat(literal.getLang(), is("en"));
    }

    @Test
    public void shouldReturnErrorForIncompatibleLangs() {
        EvaluationResult result = evaluator.evaluate(
                Literal.createRDFPlainLiteral("abc", "en"),
                Literal.createRDFPlainLiteral("bc", "cy"), sm);
        assertThat(result.isError(), is(true));
    }

    @Test
    public void shouldReturnEmptyStringForNoMatch() {
        EvaluationResult result = evaluator.evaluate(
                Literal.createRDFPlainLiteralNoLang("abc"),
                Literal.createRDFPlainLiteralNoLang("xyz"), sm);
        assertThat(result.isError(), is(false));
        Literal literal = result.asLiteral();
        assertThat(literal.getDatatype(), is(Datatype.getRDFPlainLiteral()));
        assertThat(literal.getLexicalForm(), is(""));
        assertThat(literal.getLang(), is(""));
    }

    @Test
    public void shouldReturnEmptyStringForNoMatchWithLang() {
        EvaluationResult result = evaluator.evaluate(
                Literal.createRDFPlainLiteral("abc", "en"),
                Literal.createRDFPlainLiteral("xyz", "en"), sm);
        assertThat(result.isError(), is(false));
        Literal literal = result.asLiteral();
        assertThat(literal.getDatatype(), is(Datatype.getRDFPlainLiteral()));
        assertThat(literal.getLexicalForm(), is(""));
        assertThat(literal.getLang(), is(""));
    }

    @Test
    public void shouldReturnStringForEmptyString() {
        EvaluationResult result = evaluator.evaluate(
                Literal.createRDFPlainLiteralNoLang("abc"),
                Literal.createRDFPlainLiteralNoLang(""), sm);
        assertThat(result.isError(), is(false));
        Literal literal = result.asLiteral();
        assertThat(literal.getDatatype(), is(Datatype.getRDFPlainLiteral()));
        assertThat(literal.getLexicalForm(), is("abc"));
        assertThat(literal.getLang(), is(""));
    }
}
