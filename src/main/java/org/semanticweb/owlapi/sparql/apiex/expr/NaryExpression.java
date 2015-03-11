package org.semanticweb.owlapi.sparql.apiex.expr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 24/07/2012
 */
public abstract class NaryExpression implements Expression {

    private List<Expression> expressions = new ArrayList<Expression>();

    public NaryExpression(List<Expression> expressions) {
        this.expressions.addAll(expressions);
    }

    public List<Expression> getExpressions() {
        return Collections.unmodifiableList(expressions);
    }
}
