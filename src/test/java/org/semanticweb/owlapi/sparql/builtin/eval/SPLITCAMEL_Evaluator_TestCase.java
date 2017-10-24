package org.semanticweb.owlapi.sparql.builtin.eval;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.sparql.api.EvaluationResult;
import org.semanticweb.owlapi.sparql.api.Literal;
import org.semanticweb.owlapi.sparql.api.SolutionMapping;
import org.semanticweb.owlapi.sparql.sparqldl.EvaluationContext;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30 Nov 2016
 */
@RunWith(MockitoJUnitRunner.class)
public class SPLITCAMEL_Evaluator_TestCase {

    private SPLITCAMEL_Evaluator evaluator;

    @Mock
    private SolutionMapping sm;

    @Before
    public void setUp() {
        evaluator = new SPLITCAMEL_Evaluator();
    }

    @Test
    public void shouldSplitCamelCaseLiteral() {
        assertSplitCamelCaseIs("CamelCase", "Camel Case");
    }

    @Test
    public void shouldSplitCamelCaseWithNumbers() {
        assertSplitCamelCaseIs("Camel44Case33", "Camel44 Case33");
    }

    @Test
    public void shouldPreserveUnderscores() {
        assertSplitCamelCaseIs("CamelCase_3", "Camel Case_3");
    }

    private void assertSplitCamelCaseIs(String input, String expected) {
        Literal camelCaseLiteral = Literal.createSimpleLiteral(input);
        EvaluationResult result = evaluator.evaluate(camelCaseLiteral, camelCaseLiteral, sm, mock(EvaluationContext.class));
        assertThat(result.isError(), is(false));
        assertThat(result.asLiteral().getLexicalForm(), is(expected));
    }
}
