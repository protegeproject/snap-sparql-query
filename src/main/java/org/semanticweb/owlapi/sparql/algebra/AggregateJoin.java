package org.semanticweb.owlapi.sparql.algebra;

import com.google.common.collect.*;
import org.semanticweb.owlapi.sparql.api.EvaluationResult;
import org.semanticweb.owlapi.sparql.api.RDFTerm;
import org.semanticweb.owlapi.sparql.api.SolutionMapping;
import org.semanticweb.owlapi.sparql.api.Variable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

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
        for(Aggregation aggregation : aggregations) {
            aggregation.collectVisibleVariables(variableBuilder);
        }
    }

    public ImmutableList<Aggregation> getAggregations() {
        return aggregations;
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
        Set<GroupKey> groupKeys = table.rowKeySet();
        for(GroupKey key : groupKeys) {
            ImmutableMap.Builder<Variable, RDFTerm> termMap = ImmutableMap.builder();
            for(Variable variable : variables) {
                EvaluationResult result = table.get(key, variable);
                if (!result.isError()) {
                    termMap.put(variable, result.getResult());
                }
            }
            SolutionMapping sm = new SolutionMapping(termMap.build());
            builder.add(sm);
        }
        return new SolutionSequence(new ArrayList<>(variables), builder.build());
    }

    @Override
    public <R, X extends Throwable> R accept(AlgebraExpressionVisitor<R, X> visitor) throws X {
        return visitor.visit(this);
    }
}
