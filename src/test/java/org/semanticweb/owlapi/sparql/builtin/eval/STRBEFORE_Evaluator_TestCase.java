package org.semanticweb.owlapi.sparql.builtin.eval;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.sparql.api.Datatype;
import org.semanticweb.owlapi.sparql.api.EvaluationResult;
import org.semanticweb.owlapi.sparql.api.Literal;
import org.semanticweb.owlapi.sparql.api.SolutionMapping;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 10/06/15
 */
@RunWith(MockitoJUnitRunner.class)
public class STRBEFORE_Evaluator_TestCase {

    private STRBEFORE_Evaluator evaluator;

    @Mock
    private SolutionMapping sm;

    @Before
    public void setUp() throws Exception {
        evaluator = new STRBEFORE_Evaluator();
    }

    @Test
    public void shouldReturnSubString() {
        EvaluationResult result = evaluator.evaluate(
                Literal.createRDFPlainLiteralNoLang("abc"),
                Literal.createRDFPlainLiteralNoLang("bc"), sm);
        assertThat(result.isError(), is(false));
        Literal literal = result.asLiteral();
        assertThat(literal.getDatatype(), is(Datatype.getRDFPlainLiteral()));
        assertThat(literal.getLexicalForm(), is("a"));
        assertThat(literal.getLang(), is(""));
    }

    @Test
    public void shouldReturnSubStringPreservingLang() {
        EvaluationResult result = evaluator.evaluate(
                Literal.createRDFPlainLiteral("abc", "en"),
                Literal.createRDFPlainLiteralNoLang("bc"), sm);
        assertThat(result.isError(), is(false));
        Literal literal = result.asLiteral();
        assertThat(literal.getDatatype(), is(Datatype.getRDFPlainLiteral()));
        assertThat(literal.getLexicalForm(), is("a"));
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
    public void shouldReturnEmptyStringForEmptyString() {
        EvaluationResult result = evaluator.evaluate(
                Literal.createRDFPlainLiteralNoLang("abc"),
                Literal.createRDFPlainLiteralNoLang(""), sm);
        assertThat(result.isError(), is(false));
        Literal literal = result.asLiteral();
        assertThat(literal.getDatatype(), is(Datatype.getRDFPlainLiteral()));
        assertThat(literal.getLexicalForm(), is(""));
        assertThat(literal.getLang(), is(""));
    }

}
