
package org.semanticweb.owlapi.sparql.builtin.eval;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.semanticweb.owlapi.sparql.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class STRENDS_Evaluator_TestCase {

    public static final String THE_PREFIX = "The Prefix";

    public static final String THE_SUFFIX = "The Suffix";

    private STRENDS_Evaluator evaluator;

    private List<Expression> args;

    @Mock
    private SolutionMapping sm;

    @Before
    public void setUp() {
        args = new ArrayList<>();
        evaluator = new STRENDS_Evaluator();
    }

    @Test
    public void shouldImplementToString() {
        assertThat(evaluator.toString(), startsWith("STRENDS_Evaluator"));
    }

    @Test
    public void should_evaluateAsError_If_ArgsLengthIsNot2() {
        setUpArgs("The string");
        assertThatResultIsError();
    }


    @Test
    public void should_evaluateAsError_If_ArgsIsEmpty() {
        setUpArgs();
        assertThatResultIsError();
    }


    @Test
    public void should_evaluateAsTrue() {
        String arg0Value = THE_PREFIX + THE_SUFFIX;
        String arg1Value = THE_SUFFIX;
        setUpArgs(arg0Value, arg1Value);
        verifyExpectedResultIs(true);
    }

    @Test
    public void should_Not_EvaluateAsTrue() {
        String arg0 = "Some string";
        String arg1 = THE_SUFFIX;
        setUpArgs(arg0, arg1);
        verifyExpectedResultIs(false);
    }

    private void setUpArgs(String ... argValues) {
        for (String argValue : argValues) {
            Expression arg = mock(Expression.class);
            when(arg.evaluateAsStringLiteral(sm)).thenReturn(EvaluationResult.getResult(Literal.createString(argValue)));
            args.add(arg);
        }
    }

    private void assertThatResultIsError() {
        EvaluationResult eval = evaluator.evaluate(args, sm);
        assertThat(eval.isError(), is(true));
    }

    private void verifyExpectedResultIs(boolean expectedResult) {
        EvaluationResult eval = evaluator.evaluate(args, sm);
        assertThat(eval.isError(), is(false));
        Term result = eval.getResult();
        assertThat(result, is(instanceOf(Literal.class)));
        assertThat(((Literal) result).toBoolean(), is(expectedResult));
    }

}
