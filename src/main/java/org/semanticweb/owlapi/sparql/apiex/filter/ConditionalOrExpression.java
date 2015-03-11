package org.semanticweb.owlapi.sparql.apiex.filter;

import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/07/2012
 */
public class ConditionalOrExpression extends ConditionalExpression {

    public ConditionalOrExpression(List<? extends Expression> operands) {
        super(operands);
    }
}
