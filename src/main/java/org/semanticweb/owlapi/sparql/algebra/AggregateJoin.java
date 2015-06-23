package org.semanticweb.owlapi.sparql.algebra;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.semanticweb.owlapi.sparql.api.SolutionMapping;
import org.semanticweb.owlapi.sparql.api.Variable;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 22/06/15
 */
public class AggregateJoin extends GraphPatternAlgebraExpression {

    private ImmutableList<Aggregation> aggregations;

    public AggregateJoin(ImmutableList<Aggregation> aggregations) {
        this.aggregations = aggregations;
    }

    public ImmutableList<Aggregation> getAggregations() {
        return aggregations;
    }

    @Override
    public GraphPatternAlgebraExpression getSimplified() {
        return this;
    }

    @Override
    public void collectVisibleVariables(ImmutableSet.Builder<Variable> variableBuilder) {

    }

    @Override
    public SolutionSequence evaluate(AlgebraEvaluationContext context) {
        ImmutableList.Builder<SolutionMapping> builder = ImmutableList.builder();
        Set<Variable> variables = new HashSet<>();
        for(Aggregation aggregation : aggregations) {
            SolutionSequence solutionSequence = aggregation.evaluate(context);
            builder.addAll(solutionSequence.getSolutionMappings());
            variables.addAll(solutionSequence.getVariableList());
        }
        return new SolutionSequence(new ArrayList<>(variables), builder.build());
    }

    @Override
    protected void prettyPrint(PrintWriter writer, int level, String indentation) {
        writer.print(indentation);
        writer.println("(AggregateJoin ");
        for(Aggregation aggregation : aggregations) {
            aggregation.prettyPrint(writer, level + 1);
        }
        writer.print(indentation);
        writer.println(")");
    }

    @Override
    public <R, E extends Exception> R accept(AlgebraExpressionVisitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }
}
