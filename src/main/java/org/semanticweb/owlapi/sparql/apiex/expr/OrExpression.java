package org.semanticweb.owlapi.sparql.apiex.expr;

import java.util.Iterator;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 24/07/2012
 */
public class OrExpression extends NaryExpression {

    public OrExpression(List<Expression> expressions) {
        super(expressions);
    }

    public boolean isBoolean() {
        return true;
    }

    public boolean isNumeric() {
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Expression(OR ");
        for(Iterator<Expression> it = getExpressions().iterator(); it.hasNext();) {
            Expression expression = it.next();
            sb.append(expression);
            if (it.hasNext()) {
                sb.append(", ");
            }
        }
        sb.append(")");
        return sb.toString();
    }
}
