package org.semanticweb.owlapi.sparql.sparqldl;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.sparql.api.Datatype;
import org.semanticweb.owlapi.sparql.api.Literal;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 10/03/15
 */
public class LiteralPatternTranslator {

    private final static Pattern LITERAL_PATTERN_IRI_QUOTED = Pattern.compile("\"([^@\"]+)(@([^\"]+))?\"(\\^\\^<([^>]+)>)?");
    private final static Pattern LITERAL_PATTERN = Pattern.compile("\"([^@\"]+)(@([^\"]+))?\"(\\^\\^(.+))?");

    private static final Datatype RDF_PLAIN_LITERAL = Datatype.get(OWLRDFVocabulary.RDF_PLAIN_LITERAL.getIRI());


    public Literal translateLiteralPattern(String literalPattern) {
        Matcher matcher = LITERAL_PATTERN.matcher(literalPattern);
        if(matcher.matches()) {
            String literal = matcher.group(1);
            String lang = matcher.group(3);
            String datatypeIRI = matcher.group(5);
            if (lang != null) {
                return new Literal(literal, lang);
            } else if (datatypeIRI != null) {
                return new Literal( Datatype.get(IRI.create(datatypeIRI)), literal);
            }
        }
        return new Literal(RDF_PLAIN_LITERAL, literalPattern);
    }
}
