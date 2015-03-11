package org.semanticweb.owlapi.sparql.apiex.function.types;

import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.sparql.parser.tokenizer.StringTokenType;
import org.semanticweb.owlapi.sparql.parser.tokenizer.TokenType;

import java.util.Arrays;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 11/07/2012
 * <p>
 *     Can be a literal that is RDF Plain Literal (i.e. with or without a language tag), or a a literal typed
 *     as xsd:string.
 * </p>
 */
public class StringLiteral extends Literal {

    public StringLiteral(OWLLiteral literal) {
        super(literal);
    }

    @Override
    public boolean isLexicalFormValid() {
        // Optimisation
        return isRDFPlainLiteral() || isString();
    }
    
    public static List<TokenType> getTokenTypes() {
        return Arrays.<TokenType>asList(StringTokenType.get());
    }

}
