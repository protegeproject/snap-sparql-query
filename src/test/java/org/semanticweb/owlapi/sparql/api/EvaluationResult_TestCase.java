
package org.semanticweb.owlapi.sparql.api;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.mockito.Mockito.when;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class EvaluationResult_TestCase {

    private EvaluationResult evaluationResult;

    @Mock
    private Term result;

    @Before
    public void setUp() {
        evaluationResult = new EvaluationResult(result);
    }

    @Test(expected = java.lang.NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_result_IsNull() {
        new EvaluationResult(null);
    }

    @Test
    public void shouldReturnSupplied_result() {
        MatcherAssert.assertThat(evaluationResult.getResult(), Matchers.is(this.result));
    }

    @Test
    public void shouldBeEqualToSelf() {
        MatcherAssert.assertThat(evaluationResult, Matchers.is(evaluationResult));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        MatcherAssert.assertThat(evaluationResult.equals(null), Matchers.is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        MatcherAssert.assertThat(evaluationResult, Matchers.is(new EvaluationResult(result)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_result() {
        MatcherAssert.assertThat(evaluationResult, Matchers.is(Matchers.not(new EvaluationResult(Mockito.mock(org.semanticweb.owlapi.sparql.api.Term.class)))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        MatcherAssert.assertThat(evaluationResult.hashCode(), Matchers.is(new EvaluationResult(result).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        MatcherAssert.assertThat(evaluationResult.toString(), Matchers.startsWith("EvaluationResult"));
    }

    @Test
    public void should_getFalse() {
        MatcherAssert.assertThat(EvaluationResult.getFalse(), Matchers.is(new EvaluationResult(Literal.getFalse())));
    }

    @Test
    public void should_getTrue() {
        MatcherAssert.assertThat(EvaluationResult.getTrue(), Matchers.is(new EvaluationResult(Literal.getTrue())));
    }

    @Test
    public void should_getBoolean() {
        MatcherAssert.assertThat(EvaluationResult.getBoolean(true), Matchers.is(new EvaluationResult(Literal.getTrue())));
    }

    @Test
    public void should_getSimpleLiteral() {
        String lexicalForm = "The Value";
        MatcherAssert.assertThat(EvaluationResult.getSimpleLiteral(lexicalForm), Matchers.is(new EvaluationResult(Literal.createRDFPlainLiteralNoLang(lexicalForm))));
    }

    @Test
    public void should_getDouble() {
        MatcherAssert.assertThat(EvaluationResult.getDouble(44.4), Matchers.is(new EvaluationResult(Literal.createDouble(44.4))));
    }

    @Test
    public void should_getInteger() {
        MatcherAssert.assertThat(EvaluationResult.getInteger(33), Matchers.is(new EvaluationResult(Literal.createInteger(33))));
    }

    @Test
    public void should_getError() {
        MatcherAssert.assertThat(EvaluationResult.getError(), Matchers.is(EvaluationResult.getError()));
    }

    @Test
    public void shouldReturn_false_For_isTrue() {
        MatcherAssert.assertThat(evaluationResult.isTrue(), Matchers.is(false));
    }

    @Test
    public void shouldReturn_false_For_isFalse() {
        MatcherAssert.assertThat(evaluationResult.isFalse(), Matchers.is(false));
    }

    @Test
    public void shouldReturn_false_For_isError() {
        MatcherAssert.assertThat(evaluationResult.isError(), Matchers.is(false));
    }
}
