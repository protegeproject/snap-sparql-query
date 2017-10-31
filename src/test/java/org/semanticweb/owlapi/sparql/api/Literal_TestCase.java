
package org.semanticweb.owlapi.sparql.api;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.semanticweb.owlapi.sparql.algebra.AlgebraEvaluationContext;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;
import org.semanticweb.owlapi.vocab.XSDVocabulary;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class Literal_TestCase {

    private Literal literal;

    @Mock
    private Datatype datatype;

    @Mock
    private SolutionMapping sm;

    private String lexicalForm = "theLexicalForm";

    private String langTag = "theLangTag";

    @Before
    public void setUp()
        throws Exception
    {
        literal = new Literal(datatype, lexicalForm, langTag);
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(literal, is(literal));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        assertThat(literal.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(literal, is(new Literal(datatype, lexicalForm, langTag)));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(literal.hashCode(), is(new Literal(datatype, lexicalForm, langTag).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(literal.toString(), Matchers.startsWith("Literal"));
    }

    @Test
    public void should_getLang() {
        assertThat(literal.getLang(), is(langTag));
    }

    @Test
    public void should_getLexicalForm() {
        assertThat(literal.getLexicalForm(), is(lexicalForm));
    }

    @Test
    public void should_getDatatype() {
        assertThat(literal.getDatatype(), is(datatype));
    }

    @Test
    public void should_getVariables() {
        assertThat(literal.getVariables(), is(Collections.<Variable>emptySet()));
    }

    @Test
    public void shouldReturn_False_For_toBoolean_isFalse() {
        assertThat(literal.toBoolean(), is(false));
    }

    @Test
    public void shouldReturn_False_For_isStringLiteral() {
        assertThat(literal.isStringLiteral(), is(false));
    }

    @Test
    public void shouldReturn_False_For_isSimpleLiteral_isFalse() {
        assertThat(literal.isSimpleLiteral(), is(false));
    }

    @Test
    public void shouldReturn_False_For_isRDFPlainLiteral_isFalse() {
        assertThat(literal.isRDFPlainLiteral(), is(false));
    }

    @Test
    public void shouldReturn_False_For_isDatatypeNumeric_isFalse() {
        assertThat(literal.isDatatypeNumeric(), is(false));
    }

    @Test
    public void should_createString() {
        Literal lit = Literal.createString(lexicalForm);
        assertThat(lit.getDatatype(), is(Datatype.getXSDString()));
        assertThat(lit.getLexicalForm(), is(lexicalForm));
        assertThat(lit.getLang(), is(""));
    }

    @Test
    public void should_createBoolean() {
        Literal lit = Literal.createBoolean(false);
        assertThat(lit.getDatatype(), is(Datatype.getXSDBoolean()));
        assertThat(lit.getLexicalForm(), is("false"));
        assertThat(lit.getLang(), is(""));
    }

    @Test
    public void should_getFalse() {
        Literal lit = Literal.getFalse();
        assertThat(lit.getDatatype(), is(Datatype.getXSDBoolean()));
        assertThat(lit.getLexicalForm(), is("false"));
        assertThat(lit.getLang(), is(""));
    }
    @Test
    public void should_getTrue() {
        Literal lit = Literal.getTrue();
        assertThat(lit.getDatatype(), is(Datatype.getXSDBoolean()));
        assertThat(lit.getLexicalForm(), is("true"));
        assertThat(lit.getLang(), is(""));
    }

    @Test
    public void should_createInteger() {
        Literal lit = Literal.createInteger(33);
        assertThat(lit.getDatatype(), is(Datatype.getXSDInteger()));
        assertThat(lit.getLexicalForm(), is("33"));
        assertThat(lit.getLang(), is(""));
    }

    @Test
    public void should_createDouble() {
        Literal lit = Literal.createDouble(33.30);
        assertThat(lit.getDatatype(), is(Datatype.getXSDDouble()));
        assertThat(lit.getLexicalForm(), is("33.3"));
        assertThat(lit.getLang(), is(""));
    }

    @Test
    public void should_createFloat() {
        Literal lit = Literal.createFloat(44.4f);
        assertThat(lit.getDatatype(), is(Datatype.getXSDFloat()));
        assertThat(lit.getLexicalForm(), is("44.4"));
        assertThat(lit.getLang(), is(""));
    }

    @Test
    public void should_createDecimal() {
        Literal lit = Literal.createDecimal(BigDecimal.valueOf(55.5));
        assertThat(lit.getDatatype(), is(Datatype.getXSDDecimal()));
        assertThat(lit.getLexicalForm(), is("55.5"));
        assertThat(lit.getLang(), is(""));
    }

    @Test
    public void should_evaluateAsSelf() {
        EvaluationResult eval = literal.evaluate(sm, mock(AlgebraEvaluationContext.class));
        assertThat(eval.isError(), is(false));
        assertThat(eval.getResult(), Matchers.<RDFTerm>is(literal));
    }

    @Test
    public void should_evaluateAsStringLiteral() {
        EvaluationResult eval = literal.evaluateAsStringLiteral(sm, mock(AlgebraEvaluationContext.class));
        assertThat(eval.isError(), is(false));
        assertThat(eval.getResult(), is(instanceOf(Literal.class)));
        Literal evalLiteral = (Literal) eval.getResult();
        assertThat(evalLiteral.getLexicalForm(), is(lexicalForm));
        assertThat(evalLiteral.getLang(), is(""));
        assertThat(evalLiteral.getDatatype().getIRI(), is(XSDVocabulary.STRING.getIRI()));
    }

    @Test
    public void should_evaluateAsSimpleLiteral() {
        EvaluationResult eval = literal.evaluateAsSimpleLiteral(sm, mock(AlgebraEvaluationContext.class));
        assertThat(eval.isError(), is(false));
        assertThat(eval.getResult(), is(instanceOf(Literal.class)));
        Literal evalLiteral = (Literal) eval.getResult();
        assertThat(evalLiteral.getLexicalForm(), is(lexicalForm));
        assertThat(evalLiteral.getLang(), is(langTag));
        assertThat(evalLiteral.getDatatype().getIRI(), is(OWLRDFVocabulary.RDF_PLAIN_LITERAL.getIRI()));
    }

    @Test
    public void should_evaluateAsLiteral() {
        EvaluationResult eval = literal.evaluateAsLiteral(sm, mock(AlgebraEvaluationContext.class));
        assertThat(eval.isError(), is(false));
        assertThat(eval.getResult(), is(instanceOf(Literal.class)));
        Literal evalLiteral = (Literal) eval.getResult();
        assertThat(evalLiteral.getLexicalForm(), is(lexicalForm));
        assertThat(evalLiteral.getDatatype(), is(datatype));
        assertThat(evalLiteral.getLang(), is(langTag));
    }

    @Test
    public void should_collectVariables() {
        Set<Variable> variables = new HashSet<>();
        literal.collectVariables(variables);
        assertThat(variables, is(empty()));
    }

}
