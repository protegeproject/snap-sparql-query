package org.semanticweb.owlapi.sparql.apiex.function;

import org.semanticweb.owlapi.sparql.apiex.function.types.SimpleLiteral;
import org.semanticweb.owlapi.sparql.apiex.function.types.StringLiteral;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 11/07/2012
 */
@BuiltInFunction("REGEX")
public class RegEx implements FunctionCall {

    public String getName() {
        return "REGEX";
    }

    @BuiltInFunctionCall
    public void call(@FunctionArg("text") StringLiteral text, SimpleLiteral pattern) {

    }

    @BuiltInFunctionCall
    public void call(StringLiteral text, SimpleLiteral pattern, SimpleLiteral flags) {

    }
}
