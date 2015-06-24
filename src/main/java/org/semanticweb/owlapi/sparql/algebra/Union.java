package org.semanticweb.owlapi.sparql.algebra;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.sparql.api.SolutionMapping;
import org.semanticweb.owlapi.sparql.api.Variable;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 08/06/15
 */
public class Union extends GraphPatternAlgebraExpression<SolutionSequence> {

    private GraphPatternAlgebraExpression<SolutionSequence> left;

    private GraphPatternAlgebraExpression<SolutionSequence> right;

    public Union(GraphPatternAlgebraExpression<SolutionSequence> left, GraphPatternAlgebraExpression<SolutionSequence> right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public GraphPatternAlgebraExpression<SolutionSequence> getSimplified() {
        return new Union(left.getSimplified(), right.getSimplified());
    }

    @Override
    public void collectVisibleVariables(ImmutableSet.Builder<Variable> variableBuilder) {
        left.collectVisibleVariables(variableBuilder);
        right.collectVisibleVariables(variableBuilder);
    }

    @Override
    public SolutionSequence evaluate(AlgebraEvaluationContext context) {
        SolutionSequence leftSeq = left.evaluate(context);
        SolutionSequence rightSeq = right.evaluate(context);
        List<Variable> variables = new ArrayList<>();
        variables.addAll(leftSeq.getVariableList());
        variables.addAll(rightSeq.getVariableList());
        List<SolutionMapping> mappings = new ArrayList<>();
        mappings.addAll(leftSeq.getSolutionMappings());
        mappings.addAll(rightSeq.getSolutionMappings());
        return new SolutionSequence(variables, ImmutableList.copyOf(mappings));
    }

    @Override
    public String toString() {
        return Objects.toStringHelper("Union")
                .addValue(left)
                .addValue(right)
                .toString();
    }

    @Override
    protected void prettyPrint(PrintWriter writer, int level, String indentation) {
        writer.print(indentation);
        writer.println("(Union ");
        left.prettyPrint(writer, level + 1);
        writer.println();
        right.prettyPrint(writer, level + 1);
        writer.print(indentation);
        writer.println(")");
    }


    @Override
    public <R, E extends Exception> R accept(AlgebraExpressionVisitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }
}
