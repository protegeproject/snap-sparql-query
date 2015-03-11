package org.semanticweb.owlapi.sparql.apiex.function.types;

import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.sparql.parser.tokenizer.IntegerTokenType;
import org.semanticweb.owlapi.sparql.parser.tokenizer.TokenType;

import java.util.Arrays;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 11/07/2012
 */
public class IntegerLiteral extends Literal {

    public IntegerLiteral(OWLLiteral literal) {
        super(literal);
    }

    @Override
    public String toString() {
        return "Integer(" + getLexicalValue() + ")";
    }
    
    public static List<TokenType> getTokenTypes() {
        return Arrays.<TokenType>asList(IntegerTokenType.get());
    }
}
