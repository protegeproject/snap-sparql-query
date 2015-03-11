package org.semanticweb.owlapi.sparql.apiex.function;

import org.semanticweb.owlapi.sparql.apiex.function.types.StringLiteral;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/07/2012
 */
public class StringFunction {


    public boolean isCompatibleWith(StringLiteral arg1, StringLiteral arg2) {
//        Compatibility of two arguments is defined as:
//
//        The arguments are simple literals or literals typed as xsd:string
        if(!arg1.isLexicalFormValid()) {
            return false;
        }

        if(!arg2.isLexicalFormValid()) {
            return false;
        }
//        The arguments are plain literals with identical language tags
//        The first argument is a plain literal with language tag and the second argument is a simple literal or literal typed as xsd:string
        if(arg1.isRDFPlainLiteral() && arg2.isRDFPlainLiteral()) {
            return arg1.getLang().equals(arg2.getLang());
        }
        else {
            return arg1.isRDFPlainLiteral() && !arg1.getLang().isEmpty();
        }
    }
}
