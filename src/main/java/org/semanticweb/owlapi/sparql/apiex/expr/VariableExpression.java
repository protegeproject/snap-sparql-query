package org.semanticweb.owlapi.sparql.apiex.expr;

import org.semanticweb.owlapi.sparql.apiex.tmp.SolutionMapping;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 24/07/2012
 */
public class VariableExpression implements Expression {

    private String variableName;

    public VariableExpression(String variableName) {
        this.variableName = variableName;
    }

    public boolean isBoolean() {
        return false;
    }

    public boolean isNumeric() {
        return false;
    }

    public Expression evaluate(SolutionMapping solutionMapping) {
        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Expression(VARIABLE ?");
        sb.append(variableName);
        sb.append(")");
        return sb.toString();
    }
}
