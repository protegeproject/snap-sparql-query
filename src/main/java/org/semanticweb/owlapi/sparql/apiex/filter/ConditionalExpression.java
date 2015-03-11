package org.semanticweb.owlapi.sparql.apiex.filter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/07/2012
 */
public abstract class ConditionalExpression extends Expression {

    private List<Expression> operands = new ArrayList<Expression>();

    public ConditionalExpression(List<? extends Expression> operands) {
        this.operands = Collections.unmodifiableList(new ArrayList<Expression>(operands));
    }

    public List<Expression> getOperands() {
        return operands;
    }
}
