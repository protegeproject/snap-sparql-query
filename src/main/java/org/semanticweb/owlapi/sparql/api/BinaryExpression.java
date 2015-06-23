package org.semanticweb.owlapi.sparql.api;

import java.util.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/07/2012
 */
public abstract class BinaryExpression implements Expression {

    private Expression left;
    
    private Expression right;

    protected BinaryExpression(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    public Set<Variable> getVariables() {
        Set<Variable> result = new HashSet<Variable>();
        result.addAll(left.getVariables());
        result.addAll(right.getVariables());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Expression(");
        String simpleName = getClass().getSimpleName();
        sb.append(simpleName.substring(0, simpleName.indexOf("Expression")).toUpperCase());
        sb.append(" ");
        sb.append(left);
        sb.append(" ");
        sb.append(right);
        sb.append(")");
        return sb.toString();

    }

    public Expression getLeft() {
        return left;
    }

    public Expression getRight() {
        return right;
    }

//    @Override
//    public final Expression replaceSubExpressionWith(Map<? extends Expression, ? extends Expression> map) {
//        Expression replacementExpression = map.get(this);
//        if(replacementExpression == null) {
//
//        }
//        if(this.equals(subExpression)) {
//            return withExpression;
//        }
//        return new MultiplyExpression(
//                getLeft().replaceSubExpressionWith(subExpression, withExpression),
//                getRight().replaceSubExpressionWith(subExpression, withExpression)
//        );
//    }

    @Override
    public List<Expression> getSubExpressions() {
        ArrayList<Expression> expressions = new ArrayList<>();
        expressions.add(this);
        expressions.addAll(left.getSubExpressions());
        expressions.addAll(right.getSubExpressions());
        return expressions;
    }
}
