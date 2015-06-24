package org.semanticweb.owlapi.sparql.algebra;

import com.google.common.base.Optional;
import com.google.common.collect.*;
import org.semanticweb.owlapi.sparql.api.EvaluationResult;
import org.semanticweb.owlapi.sparql.api.GroupCondition;
import org.semanticweb.owlapi.sparql.api.SolutionMapping;
import org.semanticweb.owlapi.sparql.api.Variable;

import java.io.PrintWriter;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 22/06/15
 */
public class Group extends GraphPatternAlgebraExpression<GroupEvaluation> {

    private ImmutableList<GroupCondition> expressionList;

    private GraphPatternAlgebraExpression<SolutionSequence> pattern;

    private Optional<GroupEvaluation> lastEvaluation = Optional.absent();

    public Group(ImmutableList<GroupCondition> expressionList, GraphPatternAlgebraExpression<SolutionSequence> pattern) {
        this.expressionList = expressionList;
        this.pattern = pattern;
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
    public GroupEvaluation evaluate(AlgebraEvaluationContext context) {
        if(lastEvaluation.isPresent()) {
            return lastEvaluation.get();
        }
        SolutionSequence sequence = pattern.evaluate(context);
        if(expressionList.isEmpty()) {
            return new GroupEvaluation(ImmutableMap.of(GroupKey.empty(), sequence));
        }
        else {
            Multimap<GroupKey, SolutionMapping> map = HashMultimap.create();
            for (SolutionMapping sm : sequence.getSolutionMappings()) {
                ImmutableList.Builder<EvaluationResult> groupKeyBuilder = ImmutableList.builder();
                for(GroupCondition condition : expressionList) {
                    EvaluationResult eval = condition.asExpression().evaluate(sm);
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
