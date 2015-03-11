package org.semanticweb.owlapi.sparql.api;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 31/07/2012
 */
public class SelectAs {

    private Expression expression;

    private Variable variable;

    public SelectAs(Expression expression, Variable variable) {
        this.expression = expression;
        this.variable = variable;
    }

    public Expression getExpression() {
        return expression;
    }

    public Variable getVariable() {
        return variable;
    }
}
