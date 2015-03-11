package org.semanticweb.owlapi.sparql.apiex.function;

import org.semanticweb.owlapi.sparql.apiex.function.types.SimpleLiteral;
import org.semanticweb.owlapi.sparql.apiex.function.types.StringLiteral;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 11/07/2012
 */
@BuiltInFunction("REPLACE")
public class Replace {

    @BuiltInFunctionCall
    StringLiteral call(StringLiteral arg, @FunctionArg("pattern") SimpleLiteral pattern, @FunctionArg("replacement") SimpleLiteral replacement) {
        return null;
    }
    
    @BuiltInFunctionCall
    StringLiteral call(StringLiteral arg, @FunctionArg("pattern") SimpleLiteral pattern, @FunctionArg("replacement") SimpleLiteral replacement, @FunctionArg("flags") SimpleLiteral flags) {
        return null;
    }
}
