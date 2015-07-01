package org.semanticweb.owlapi.sparql.algebra;

import com.google.common.base.*;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.semanticweb.owlapi.sparql.api.*;
import org.semanticweb.owlapi.sparql.builtin.BuiltInCall;
import org.semanticweb.owlapi.sparql.sparqldl.OrderByComparator;
import org.semanticweb.owlapi.sparql.syntax.*;

import java.util.*;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 24/06/15
 */
public class Syntax2AlgebraTranslator {


    public AlgebraExpression<SolutionSequence> translate(ConstructQuery constructQuery) {
        SelectQuery selectQuery = constructQuery.asSelectQuery();
        return translate(selectQuery);
    }

    public AlgebraExpression<SolutionSequence> translate(SelectQuery selectQuery) {

        GroupPattern groupPattern = selectQuery.getGroupPattern();
        GraphPatternAlgebraExpression G = groupPattern.translate().getSimplified();



        GraphPatternAlgebraExpression X = G;

        final List<SelectItem> E;

        SolutionModifier solutionModifier = selectQuery.getSolutionModifier();


        if (selectQuery.isAggregateQuery()) {

            E = new ArrayList<>();

            if(solutionModifier.getGroupClause().isPresent()) {
                ImmutableList<GroupCondition> groupConditions = solutionModifier.getGroupClause().get().getGroupConditions();
                X = new Group(groupConditions, X);
            }
            else {
                X = new Group(ImmutableList.of(), X);
            }


            final List<SelectItem> rewrittenSelectItems = new ArrayList<>();

            AggregateBuiltInReplacer.ReplacementContext replacementContext = new AggregateBuiltInReplacer.ReplacementContext((Group) X);
            AggregateBuiltInReplacer aggregateBuiltInReplacer = new AggregateBuiltInReplacer(replacementContext);

            // (X AS Var)
            for(SelectItem selectItem : selectQuery.getSelectClause().getSelectItems()) {
                if(selectItem instanceof SelectExpressionAsVariable) {
                    SelectExpressionAsVariable expressionAsVariable = (SelectExpressionAsVariable) selectItem;
                    Expression expression = expressionAsVariable.getExpression();
                    UnaggregatedVariableReplacer replacer = new UnaggregatedVariableReplacer();
                    expression = replacer.replaceUnaggregatedVariables(expression);

                    expression = aggregateBuiltInReplacer.replaceAggregateBuiltInWithVariable(expression);

                    rewrittenSelectItems.add(new SelectExpressionAsVariable(expression, expressionAsVariable.getVariable()));
                }
                else {
                    rewrittenSelectItems.add(selectItem);
                }
            }


            // HAVING(X)
            ImmutableList.Builder<HavingCondition> rewrittenHavingConditions = ImmutableList.builder();
            Optional<HavingClause> havingClause = solutionModifier.getHavingClause();
            if(havingClause.isPresent()) {
                for(HavingCondition havingCondition : havingClause.get().getConditions()) {
                    Expression expression = havingCondition.getExpression();
//                    UnaggregatedVariableReplacer replacer = new UnaggregatedVariableReplacer();
//                    expression = replacer.replaceUnaggregatedVariables(expression);
                    expression = aggregateBuiltInReplacer.replaceAggregateBuiltInWithVariable(expression);
                    rewrittenHavingConditions.add(new HavingCondition(expression));
                }
            }

            List<Aggregation> aggregations = new ArrayList<>();
            aggregations.addAll(replacementContext.getAggregations());

            for(SelectItem selectItem : rewrittenSelectItems) {
                if(selectItem instanceof SelectVariable) {
                    UntypedVariable variable = new UntypedVariable("agg_" + aggregations.size());
                    Aggregation aggregation = new Aggregation(
                            new BuiltInCallExpression(
                                    BuiltInCall.SAMPLE,
                                    ImmutableList.<Expression>of(selectItem.getVariable())
                            ),
                            (Group) X, variable);
                    E.add(new SelectExpressionAsVariable(variable, selectItem.getVariable()));
                    aggregations.add(aggregation);
                }
                else if(selectItem instanceof SelectExpressionAsVariable) {
                    E.add(selectItem);
                }
            }

            X = new AggregateJoin(ImmutableList.copyOf(aggregations));

            for(HavingCondition havingCondition : rewrittenHavingConditions.build()) {
                X = new Filter(ImmutableList.of(havingCondition.getExpression()), X);
            }

        }
        else {
            E = selectQuery.getSelectClause().getSelectItems();
        }





        Set<Variable> projectionVariables = new LinkedHashSet<>();
        if(E.isEmpty()) {
            ImmutableSet.Builder<Variable> visibleVariablesBuilder = ImmutableSet.builder();
            G.collectVisibleVariables(visibleVariablesBuilder);
            Set<Variable> visibleVariables = visibleVariablesBuilder.build();
            projectionVariables.addAll(visibleVariables);
        }
        else {
            for(SelectItem selectItem : E) {
                if(selectItem instanceof SelectVariable) {
                    SelectVariable selectVariable = (SelectVariable) selectItem;
                    UntypedVariable untypedVariable = selectVariable.getVariable();
                    projectionVariables.add(untypedVariable);
                }
                else if(selectItem instanceof SelectExpressionAsVariable) {
                    SelectExpressionAsVariable selectExpressionAsVariable = (SelectExpressionAsVariable) selectItem;
                    UntypedVariable untypedVariable = selectExpressionAsVariable.getVariable();
                    projectionVariables.add(untypedVariable);
                    X = new Extend(X, untypedVariable, selectExpressionAsVariable.getExpression());
                }
            }
        }


        AlgebraExpression M = new ToList(X);
        com.google.common.base.Optional<OrderClause> orderClause = solutionModifier.getOrderClause();
        if(orderClause.isPresent()) {
            M = new OrderBy(M, new OrderByComparator(orderClause.get().getOrderConditions()));
        }
        M = new Project(M, new ArrayList<>(projectionVariables));
        if(selectQuery.getSelectClause().isSelectDistinct()) {
            M = new Distinct(M);
        }
        return M;
    }

}
