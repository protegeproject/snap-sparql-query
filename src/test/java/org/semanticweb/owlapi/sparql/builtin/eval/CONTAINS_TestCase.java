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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 16/06/15
 */
@RunWith(MockitoJUnitRunner.class)
public class CONTAINS_TestCase {

    private CONTAINS_Evaluator evaluator;

    @Mock
    private SolutionMapping sm;

    @Before
    public void setUp() throws Exception {
        evaluator = new CONTAINS_Evaluator();
    }

    @Test
    public void shouldContainString() {
        Literal left = new Literal(Datatype.getRDFPlainLiteral(), "SomeString", "en");
        Literal right = new Literal(Datatype.getRDFPlainLiteral(), "String", "en");
        assertThat(evaluator.evaluateCompatibleLiterals(left, right, sm), is(EvaluationResult.getTrue()));
    }

    @Test
    public void shouldNotContainString() {
        Literal left = new Literal(Datatype.getRDFPlainLiteral(), "SomeString", "en");
        Literal right = new Literal(Datatype.getRDFPlainLiteral(), "Other", "en");
        assertThat(evaluator.evaluateCompatibleLiterals(left, right, sm), is(EvaluationResult.getFalse()));
    }
}
