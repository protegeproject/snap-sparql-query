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
 */
public class SimpleLiteral extends Literal {

    public SimpleLiteral(OWLLiteral literal) {
        super(literal);
    }

    @Override
    public boolean isLexicalFormValid() {
        return isRDFPlainLiteral() && !hasLang();
    }
    
    public static List<TokenType> getTokenTypes() {
        return Arrays.<TokenType>asList(StringTokenType.get());
    }
}
