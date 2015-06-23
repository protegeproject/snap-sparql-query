package org.semanticweb.owlapi.sparql.algebra;

import com.google.common.collect.ImmutableSet;
import org.semanticweb.owlapi.sparql.api.BuiltInCallExpression;
import org.semanticweb.owlapi.sparql.api.Variable;
import org.semanticweb.owlapi.sparql.builtin.BuiltInCall;
import org.semanticweb.owlapi.sparql.syntax.SelectExpressionAsVariable;

import java.io.PrintWriter;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 22/06/15
 */
public class Aggregation extends GraphPatternAlgebraExpression {

    private BuiltInCallExpression expression;

    private GraphPatternAlgebraExpression algebraExpression;

    public Aggregation(BuiltInCallExpression expression, GraphPatternAlgebraExpression algebraExpression) {
        this.expression = expression;
        this.algebraExpression = algebraExpression;
    }

    @Override
    public GraphPatternAlgebraExpression getSimplified() {
        return null;
    }

    @Override
    public void collectVisibleVariables(ImmutableSet.Builder<Variable> variableBuilder) {

    }

    @Override
    public SolutionSequence evaluate(AlgebraEvaluationContext context) {
        return algebraExpression.evaluate(context);
    }

    @Override
    protected void prettyPrint(PrintWriter writer, int level, String indentation) {
        writer.print(indentation);
        writer.println("(Aggregation ");
        writer.print(indentation + "    ");
        writer.println(expression);
        algebraExpression.prettyPrint(writer, level + 1);
        writer.print(indentation);
        writer.println(")");
    }

    @Override
    public <R, E extends Exception> R accept(AlgebraExpressionVisitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }
}
