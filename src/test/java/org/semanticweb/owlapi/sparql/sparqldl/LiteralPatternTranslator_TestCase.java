package org.semanticweb.owlapi.sparql.sparqldl;

import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.sparql.api.Datatype;
import org.semanticweb.owlapi.sparql.api.Literal;
import org.semanticweb.owlapi.vocab.XSDVocabulary;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 10/03/15
 */
public class LiteralPatternTranslator_TestCase {


    private LiteralPatternTranslator translator;

    @Before
    public void setUp() throws Exception {
        translator = new LiteralPatternTranslator();
    }

    @Test
    public void shouldParseLang() {
        String input = "\"Hello@en\"";
        Literal literal = translator.translateLiteralPattern(input);
        assertThat(literal.getLexicalForm(), is("Hello"));
        assertThat(literal.getLang(), is("en"));
    }

    @Test
    public void shouldParseDatatype() {
        String input = "\"Hello\"^^" + XSDVocabulary.STRING.getIRI().toQuotedString();
        Literal literal = translator.translateLiteralPattern(input);
        assertThat(literal.getLexicalForm(), is("Hello"));
        assertThat(literal.getDatatype(), is( Datatype.get(XSDVocabulary.STRING.getIRI())));
    }
}
