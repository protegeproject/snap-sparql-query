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
public abstract class ExpressionListExpression extends Expression {

    private Expression expression;
    
    private ExpressionListType listType;
 
    private List<Expression> expressionList;

    public ExpressionListExpression(Expression expression, ExpressionListType listType, List<Expression> expressionList) {
        this.expression = expression;
        this.listType = listType;
        this.expressionList = Collections.unmodifiableList(new ArrayList<Expression>(expressionList));
    }

    public Expression getExpression() {
        return expression;
    }

    public ExpressionListType getListType() {
        return listType;
    }

    public List<Expression> getExpressionList() {
        return expressionList;
    }
}
