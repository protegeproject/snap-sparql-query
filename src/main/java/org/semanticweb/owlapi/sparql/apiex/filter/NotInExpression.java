package org.semanticweb.owlapi.sparql.apiex.filter;

import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/07/2012
 */
public class NotInExpression extends ExpressionListExpression {

    public NotInExpression(Expression expression, ExpressionListType listType, List<Expression> expressionList) {
        super(expression, listType, expressionList);
    }
}
