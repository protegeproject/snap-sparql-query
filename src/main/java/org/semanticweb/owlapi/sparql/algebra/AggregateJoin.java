package org.semanticweb.owlapi.sparql.algebra;

import com.google.common.collect.*;
import de.derivo.sparqldlapi.Var;
import org.semanticweb.owlapi.sparql.api.EvaluationResult;
import org.semanticweb.owlapi.sparql.api.RDFTerm;
import org.semanticweb.owlapi.sparql.api.SolutionMapping;
import org.semanticweb.owlapi.sparql.api.Variable;

import java.io.PrintWriter;
import java.util.*;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 22/06/15
 */
public class AggregateJoin extends GraphPatternAlgebraExpression<SolutionSequence> {

    private ImmutableList<Aggregation> aggregations;

    public AggregateJoin(ImmutableList<Aggregation> aggregations) {
        this.aggregations = aggregations;
    }

    @Override
    public GraphPatternAlgebraExpression<SolutionSequence> getSimplified() {
        return this;
    }

    @Override
    public void collectVisibleVariables(ImmutableSet.Builder<Variable> variableBuilder) {

    }

    @Override
    public SolutionSequence evaluate(AlgebraEvaluationContext context) {
        Set<Variable> variables = new HashSet<>();
        Table<GroupKey, Variable, EvaluationResult> table = HashBasedTable.create();
        for(Aggregation aggregation : aggregations) {
            ImmutableList<AggregationEvaluation> solutionSequence = aggregation.evaluate(context);
            for(AggregationEvaluation evaluation : solutionSequence) {
                table.put(evaluation.getGroupKey(), evaluation.getVariable(), evaluation.getEvaluationResult());
                variables.add(evaluation.getVariable());
            }
        }
        ImmutableList.Builder<SolutionMapping> builder = ImmutableList.builder();
        for(GroupKey key : table.rowKeySet()) {
            ImmutableMap.Builder<Variable, RDFTerm> termMap = ImmutableMap.builder();
            for(Variable variable : table.columnKeySet()) {
                EvaluationResult result = table.get(key, variable);
                termMap.put(variable, result.getResult());
            }
            SolutionMapping sm = new SolutionMapping(termMap.build());
            builder.add(sm);
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
