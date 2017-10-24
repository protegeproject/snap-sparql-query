
package org.semanticweb.owlapi.sparql.builtin.eval;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.semanticweb.owlapi.sparql.api.EvaluationResult;
import org.semanticweb.owlapi.sparql.api.Literal;
import org.semanticweb.owlapi.sparql.api.SolutionMapping;
import org.semanticweb.owlapi.sparql.sparqldl.EvaluationContext;

import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class SUBSTR_Evaluator_TestCase {

    public static final String THE_LEXICAL_FORM = "TheLexicalForm";

    public static final int START_INDEX = 1;

    public static final int LENGTH = 5;

    private SUBSTR_Evaluator evaluator;

    @Mock
    private SolutionMapping sm;

    private Literal literal = Literal.createRDFPlainLiteral(THE_LEXICAL_FORM, "");

    private Literal startIndex = Literal.createInteger(START_INDEX);

    private Literal length = Literal.createInteger(LENGTH);

    @Before
    public void setUp() throws Exception {
        evaluator = new SUBSTR_Evaluator();
    }

    @Test
    public void shouldGenerateSubStringFromStartIndex() {
        EvaluationResult eval = evaluator.evaluate(Arrays.asList(literal, startIndex), sm, mock(EvaluationContext.class));
        assertThat(eval.isError(), is(false));
        assertThat(eval.asSimpleLiteral(), is(THE_LEXICAL_FORM.substring(START_INDEX)));
    }

    @Test
    public void shouldGenerateSubStringFromStartIndexToLength() {
        EvaluationResult eval = evaluator.evaluate(Arrays.asList(literal, startIndex, length), sm, mock(EvaluationContext.class));
        assertThat(eval.isError(), is(false));
        assertThat(eval.asSimpleLiteral(), is(THE_LEXICAL_FORM.substring(START_INDEX, START_INDEX + 5)));
    }

    @Test
    public void shouldGenerateErrorForInvalidLength() {
        EvaluationResult eval = evaluator.evaluate(Arrays.asList(literal, startIndex, Literal.createInteger(30)), sm, mock(EvaluationContext.class));
        assertThat(eval.isError(), is(true));
    }
}
