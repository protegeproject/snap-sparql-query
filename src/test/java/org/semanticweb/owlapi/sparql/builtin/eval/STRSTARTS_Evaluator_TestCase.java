package org.semanticweb.owlapi.sparql.builtin.eval;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.sparql.api.EvaluationResult;
import org.semanticweb.owlapi.sparql.api.Literal;
import org.semanticweb.owlapi.sparql.api.SolutionMapping;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 10/06/15
 */
@RunWith(MockitoJUnitRunner.class)
public class STRSTARTS_Evaluator_TestCase {

    private STRSTARTS_Evaluator evaluator;

    @Mock
    private SolutionMapping sm;

    @Before
    public void setUp() throws Exception {
        evaluator = new STRSTARTS_Evaluator();
    }

    @Test
    public void shouldReturnTrueFor_SimpleLiteral_SimpleLiteral() {
        Literal left = Literal.createSimpleLiteral("foobar");
        Literal right = Literal.createSimpleLiteral("foo");
        assertThat(evaluator.evaluate(left, right, sm), is(EvaluationResult.getTrue()));
    }

    @Test
    public void shouldReturnTrueFor_PlainLiteralWithEn_PlainLiteralWithEn() {
        Literal left = Literal.createRDFPlainLiteral("foobar", "en");
        Literal right = Literal.createRDFPlainLiteral("foo", "en");
        assertThat(evaluator.evaluate(left, right, sm), is(EvaluationResult.getTrue()));
    }

    @Test
    public void shouldReturnTrueFor_StringLiteral_StringLiteral() {
        Literal left = Literal.createString("foobar");
        Literal right = Literal.createString("foo");
        assertThat(evaluator.evaluate(left, right, sm), is(EvaluationResult.getTrue()));
    }

    @Test
    public void shouldReturnTrueFor_StringLiteral_PlainLiteral() {
        Literal left = Literal.createString("foobar");
        Literal right = Literal.createRDFPlainLiteralNoLang("foo");
        assertThat(evaluator.evaluate(left, right, sm), is(EvaluationResult.getTrue()));
    }

    @Test
    public void shouldReturnTrueFor_PlainLiteral_StringLiteral() {
        Literal left = Literal.createRDFPlainLiteralNoLang("foobar");
        Literal right = Literal.createString("foo");
        assertThat(evaluator.evaluate(left, right, sm), is(EvaluationResult.getTrue()));
    }

    @Test
    public void shouldReturnTrueFor_PlainLiteralWithLang_SimpleLiteral() {
        Literal left = Literal.createRDFPlainLiteral("foobar", "en");
        Literal right = Literal.createSimpleLiteral("foo");
        assertThat(evaluator.evaluate(left, right, sm), is(EvaluationResult.getTrue()));
    }
}
