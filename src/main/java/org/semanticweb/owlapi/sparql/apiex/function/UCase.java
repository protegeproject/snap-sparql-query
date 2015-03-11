package org.semanticweb.owlapi.sparql.apiex.function;

import org.semanticweb.owlapi.sparql.apiex.function.types.StringLiteral;
import org.semanticweb.owlapi.sparql.apiex.function.types.TypeFactory;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/07/2012
 */
public class UCase {

    public StringLiteral call(StringLiteral literal) {
        String ucaseLexicalValue = literal.getLexicalValue().toUpperCase();
        if(literal.isRDFPlainLiteral()) {
            return TypeFactory.getStringLiteral(ucaseLexicalValue, literal.getLang());
        }
        else {
             return TypeFactory.getStringLiteral(ucaseLexicalValue);
        }
    }
}
