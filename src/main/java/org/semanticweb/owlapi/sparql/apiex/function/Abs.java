package org.semanticweb.owlapi.sparql.apiex.function;

import org.semanticweb.owlapi.sparql.apiex.function.types.NumericLiteral;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 11/07/2012
 */
@BuiltInFunction("ABS")
public class Abs {

    @BuiltInFunctionCall
    public NumericLiteral call(@FunctionArg("term") NumericLiteral term) {
        return null;
    }
}
