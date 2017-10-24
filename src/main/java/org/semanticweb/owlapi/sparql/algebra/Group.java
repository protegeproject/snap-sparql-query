package org.semanticweb.owlapi.sparql.algebra;

import com.google.common.collect.*;
import org.semanticweb.owlapi.sparql.api.EvaluationResult;
import org.semanticweb.owlapi.sparql.api.GroupCondition;
import org.semanticweb.owlapi.sparql.api.SolutionMapping;
import org.semanticweb.owlapi.sparql.api.Variable;
import org.semanticweb.owlapi.sparql.sparqldl.EvaluationContext;

import java.util.Optional;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 22/06/15
 */
public class Group extends GraphPatternAlgebraExpression<GroupEvaluation> {

    private ImmutableList<GroupCondition> expressionList;

    private GraphPatternAlgebraExpression<SolutionSequence> pattern;

    private Optional<GroupEvaluation> lastEvaluation = Optional.empty();

    public Group(ImmutableList<GroupCondition> expressionList, GraphPatternAlgebraExpression<SolutionSequence> pattern) {
        this.expressionList = expressionList;
        this.pattern = pattern;
    }

    public ImmutableList<GroupCondition> getExpressionList() {
        return expressionList;
    }

    public GraphPatternAlgebraExpression<SolutionSequence> getPattern() {
        return pattern;
    }

    @Override
    public Group getSimplified() {
        return this;
    }

    @Override
    public void collectVisibleVariables(ImmutableSet.Builder<Variable> variableBuilder) {
        pattern.collectVisibleVariables(variableBuilder);
    }

    @Override
    public GroupEvaluation evaluate(AlgebraEvaluationContext context, EvaluationContext evaluationContext) {
        if(lastEvaluation.isPresent()) {
            return lastEvaluation.get();
        }
        SolutionSequence sequence = pattern.evaluate(context, evaluationContext);
        if(expressionList.isEmpty()) {
            return new GroupEvaluation(ImmutableMap.of(GroupKey.empty(), sequence));
        }
        else {
            Multimap<GroupKey, SolutionMapping> map = HashMultimap.create();
            for (SolutionMapping sm : sequence.getSolutionMappings()) {
                ImmutableList.Builder<EvaluationResult> groupKeyBuilder = ImmutableList.builder();
                for(GroupCondition condition : expressionList) {
                    EvaluationResult eval = condition.asExpression().evaluate(sm, evaluationContext);
                    groupKeyBuilder.add(eval);
                }
                GroupKey key = new GroupKey(groupKeyBuilder.build());
                map.put(key, sm);
            }
            ImmutableMap.Builder<GroupKey, SolutionSequence> mapBuilder = ImmutableMap.builder();
            for(GroupKey key : map.keySet()) {
                mapBuilder.put(key, new SolutionSequence(sequence.getVariableList(), ImmutableList.copyOf(map.get(key))));
            }
            GroupEvaluation groupEvaluation = new GroupEvaluation(mapBuilder.build());
            lastEvaluation = Optional.of(groupEvaluation);
            return groupEvaluation;

        }
    }

    @Override
    public <R, E extends Throwable> R accept(AlgebraExpressionVisitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }
}
