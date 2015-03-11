package org.semanticweb.owlapi.sparql.apiex.filter;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/07/2012
 */
public class NotExpression extends UnaryExpression {

    public NotExpression(Expression expression) {
        super(expression);
    }
}
