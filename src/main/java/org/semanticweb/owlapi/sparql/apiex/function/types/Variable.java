package org.semanticweb.owlapi.sparql.apiex.function.types;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 11/07/2012
 */
public class Variable extends FunctionOperand {

    private String name;

    public Variable(String name) {
        this.name = name;
    }
}
