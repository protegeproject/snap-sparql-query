package org.semanticweb.owlapi.sparql.apiex.function;

import org.semanticweb.owlapi.sparql.apiex.function.types.StringLiteral;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 11/07/2012
 */
public class StrStarts extends StringFunction {

    public boolean call(StringLiteral arg1, StringLiteral arg2) {
        if(!isCompatibleWith(arg1, arg2)) {
            return false;
        }
        return arg1.getLexicalValue().startsWith(arg2.getLexicalValue());
    }
}
