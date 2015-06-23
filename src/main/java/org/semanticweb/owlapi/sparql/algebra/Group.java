package org.semanticweb.owlapi.sparql.algebra;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.semanticweb.owlapi.sparql.api.Expression;
import org.semanticweb.owlapi.sparql.api.GroupClause;
import org.semanticweb.owlapi.sparql.api.GroupCondition;
import org.semanticweb.owlapi.sparql.api.Variable;

import java.io.PrintWriter;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 22/06/15
 */
public class Group extends GraphPatternAlgebraExpression {


    private ImmutableList<GroupCondition> expressionList;

    private GraphPatternAlgebraExpression pattern;

    public Group(ImmutableList<GroupCondition> expressionList, GraphPatternAlgebraExpression pattern) {
        this.expressionList = expressionList;
        this.pattern = pattern;
    }

    @Override
    public GraphPatternAlgebraExpression getSimplified() {
        return this;
    }

    @Override
    public void collectVisibleVariables(ImmutableSet.Builder<Variable> variableBuilder) {
        pattern.collectVisibleVariables(variableBuilder);
    }

    @Override
    public SolutionSequence evaluate(AlgebraEvaluationContext context) {
        // Zero groups
        return pattern.evaluate(context);
    }

    @Override
    protected void prettyPrint(PrintWriter writer, int level, String indentation) {
        writer.print(indentation);
        writer.println("(Group ");
        for(GroupCondition condition : expressionList) {
            writer.print(indentation + "    ");
            writer.println(condition.asExpression());
        }
        pattern.prettyPrint(writer, level + 1);
        writer.print(indentation);
        writer.println(")");
    }

    @Override
    public <R, E extends Exception> R accept(AlgebraExpressionVisitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }
}
