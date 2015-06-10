
package org.semanticweb.owlapi.sparql.builtin;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.semanticweb.owlapi.sparql.api.Literal;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class LiteralCompatibilityChecker_TestCase {

    private LiteralCompatibilityChecker checker;

    @Before
    public void setUp() {
        checker = new LiteralCompatibilityChecker();
    }


    @Test
    public void shouldReturnTrueFor_PlainLiteral_PlainLiteral() {
        Literal arg0 = Literal.createRDFPlainLiteralNoLang("abc");
        Literal arg1 = Literal.createRDFPlainLiteralNoLang("b");
        assertThat(checker.isCompatibleWith(arg0, arg1), is(true));
    }

    @Test
    public void shouldReturnTrueFor_PlainLiteral_StringLiteral() {
        Literal arg0 = Literal.createRDFPlainLiteralNoLang("abc");
        Literal arg1 = Literal.createString("b");
        assertThat(checker.isCompatibleWith(arg0, arg1), is(true));
    }

    @Test
    public void shouldReturnTrueFor_StringLiteral_PlainLiteral() {
        Literal arg0 = Literal.createString("abc");
        Literal arg1 = Literal.createRDFPlainLiteralNoLang("b");
        assertThat(checker.isCompatibleWith(arg0, arg1), is(true));
    }

    @Test
    public void shouldReturnTrueFor_StringLiteral_StringLiteral() {
        Literal arg0 = Literal.createString("abc");
        Literal arg1 = Literal.createString("b");
        assertThat(checker.isCompatibleWith(arg0, arg1), is(true));
    }

    @Test
    public void shouldReturnTrueFor_PlainLiteralWithLang_PlainLiteral() {
        Literal arg0 = Literal.createRDFPlainLiteral("abc", "en");
        Literal arg1 = Literal.createRDFPlainLiteralNoLang("b");
        assertThat(checker.isCompatibleWith(arg0, arg1), is(true));
    }


    @Test
    public void shouldReturnTrueFor_PlainLiteralWithLang_StringLiteral() {
        Literal arg0 = Literal.createRDFPlainLiteral("abc", "en");
        Literal arg1 = Literal.createString("b");
        assertThat(checker.isCompatibleWith(arg0, arg1), is(true));
    }

    @Test
    public void shouldReturnTrueFor_PlainLiteralWithLang_StringLiteralWithLang() {
        Literal arg0 = Literal.createRDFPlainLiteral("abc", "en");
        Literal arg1 = Literal.createRDFPlainLiteral("b", "en");
        assertThat(checker.isCompatibleWith(arg0, arg1), is(true));
    }

    @Test
    public void shouldReturnFalseFor_PlainLiteralWithLang_StringLiteralWithLang_DifferentLang() {
        Literal arg0 = Literal.createRDFPlainLiteral("abc", "fr");
        Literal arg1 = Literal.createRDFPlainLiteral("b", "ja");
        assertThat(checker.isCompatibleWith(arg0, arg1), is(true));
    }

    @Test
    public void shouldReturnFalseFor_PlainLiteral_StringLiteralWithLang() {
        Literal arg0 = Literal.createRDFPlainLiteralNoLang("abc");
        Literal arg1 = Literal.createRDFPlainLiteral("b", "ja");
        assertThat(checker.isCompatibleWith(arg0, arg1), is(true));
    }

    @Test
    public void shouldReturnFalseFor_StringLiteral_StringLiteralWithLang() {
        Literal arg0 = Literal.createString("abc");
        Literal arg1 = Literal.createRDFPlainLiteral("b", "ja");
        assertThat(checker.isCompatibleWith(arg0, arg1), is(true));
    }
}
