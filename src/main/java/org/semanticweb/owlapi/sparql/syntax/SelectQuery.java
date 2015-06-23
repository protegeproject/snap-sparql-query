package org.semanticweb.owlapi.sparql.syntax;


import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import de.derivo.sparqldlapi.Var;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.sparql.algebra.*;
import org.semanticweb.owlapi.sparql.api.*;
import org.semanticweb.owlapi.sparql.builtin.BuiltInCall;
import org.semanticweb.owlapi.sparql.parser.tokenizer.TokenPosition;
import org.semanticweb.owlapi.sparql.parser.tokenizer.impl.Token;
import org.semanticweb.owlapi.sparql.sparqldl.OrderByComparator;

import java.util.*;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 08/06/15
 */
public class SelectQuery {

    private PrefixManager prefixManager;

    private SelectClause selectClause;

    private GroupPattern groupPattern;

    private SolutionModifier solutionModifier;

    public SelectQuery(PrefixManager prefixManager, SelectClause selectClause, GroupPattern groupPattern, SolutionModifier solutionModifier) {
        this.prefixManager = prefixManager;
        this.selectClause = selectClause;
        this.groupPattern = groupPattern;
        this.solutionModifier = solutionModifier;
    }

    public boolean isAggregateQuery() {
        if(solutionModifier.getGroupClause().isPresent()) {
            return true;
        }
        if(selectClause.containsAggregates()) {
            return true;
        }
        return false;
    }

    public PrefixManager getPrefixManager() {
        return prefixManager;
    }

    public SelectClause getSelectClause() {
        return selectClause;
    }

    public GroupPattern getGroupPattern() {
        return groupPattern;
    }

    public SolutionModifier getSolutionModifier() {
        return solutionModifier;
    }

    public List<UntypedVariable> getSelectClauseVariables() {
        return selectClause.getVariables();
    }

    public List<Variable> getProjectedVariables() {
        List<UntypedVariable> variables = selectClause.getVariables();
        ImmutableSet.Builder<Variable> variableBuilder = ImmutableSet.builder();
        Set<Variable> visibleVariables = variableBuilder.build();
        if(variables.isEmpty()) {
            groupPattern.translate().collectVisibleVariables(variableBuilder);
            return new ArrayList<>(variableBuilder.build());
        }
        else {
            return new ArrayList<Variable>(variables);
        }
    }

    public AlgebraExpression translate() {
        GraphPatternAlgebraExpression G = groupPattern.translate().getSimplified();

        ImmutableSet.Builder<Variable> visibleVariablesBuilder = ImmutableSet.builder();
        G.collectVisibleVariables(visibleVariablesBuilder);
        Set<Variable> visibleVariables = visibleVariablesBuilder.build();


        GraphPatternAlgebraExpression X = G;

        Map<String, Variable> name2VariableMap = new HashMap<>();
        for(Variable visibleVariable : visibleVariables) {
            name2VariableMap.put(visibleVariable.getName(), visibleVariable);
        }


        final List<SelectItem> E;


        if (isAggregateQuery()) {

            E = new ArrayList<>();

            if(solutionModifier.getGroupClause().isPresent()) {
                ImmutableList<GroupCondition> groupConditions = solutionModifier.getGroupClause().get().getGroupConditions();
                X = new Group(groupConditions, X);
            }
            else {
                X = new Group(ImmutableList.of(), X);
            }


            final List<SelectItem> rewrittenSelectItems = new ArrayList<>();

            AggregateBuiltInReplacer.ReplacementContext replacementContext = new AggregateBuiltInReplacer.ReplacementContext(X);
            AggregateBuiltInReplacer aggregateBuiltInReplacer = new AggregateBuiltInReplacer(replacementContext);

            for(SelectItem selectItem : getSelectClause().getSelectItems()) {
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

            List<Aggregation> aggregations = new ArrayList<>();
            aggregations.addAll(replacementContext.getAggregations());

            for(SelectItem selectItem : rewrittenSelectItems) {
                if(selectItem instanceof SelectVariable) {
                    Aggregation aggregation = new Aggregation(
                            new BuiltInCallExpression(
                                    BuiltInCall.SAMPLE,
                                    ImmutableList.<Expression>of(selectItem.getVariable())
                            ),
                            X);
                    E.add(new SelectExpressionAsVariable(new UntypedVariable("agg_" + aggregations.size()), selectItem.getVariable()));
                    aggregations.add(aggregation);
                }
                else if(selectItem instanceof SelectExpressionAsVariable) {
                    E.add(selectItem);
                }
            }

            X = new AggregateJoin(ImmutableList.copyOf(aggregations));
        }
        else {
            E = getSelectClause().getSelectItems();
        }

        System.out.println(E);
        Set<Variable> projectionVariables = new LinkedHashSet<>();
        if(E.isEmpty()) {
            projectionVariables.addAll(visibleVariables);
        }
        else {
            // TODO: Convert to proper typed variables
            for(SelectItem selectItem : E) {
                if(selectItem instanceof SelectVariable) {
                    SelectVariable selectVariable = (SelectVariable) selectItem;
                    UntypedVariable untypedVariable = selectVariable.getVariable();
                    Variable typedVariable = name2VariableMap.get(untypedVariable.getName());
                    if(typedVariable != null) {
                        projectionVariables.add(typedVariable);
                    }
                    else {
                        projectionVariables.add(untypedVariable);
                    }
                }
                else if(selectItem instanceof SelectExpressionAsVariable) {
                    SelectExpressionAsVariable selectExpressionAsVariable = (SelectExpressionAsVariable) selectItem;
                    UntypedVariable untypedVariable = selectExpressionAsVariable.getVariable();
                    projectionVariables.add(untypedVariable);
                    // TODO: Check that it's not already in the visible set
                    Variable typedVariable = name2VariableMap.get(untypedVariable.getName());
                    if(typedVariable != null) {
                        X = new Extend(X, typedVariable, selectExpressionAsVariable.getExpression());
                    }
                    else {
                        X = new Extend(X, untypedVariable, selectExpressionAsVariable.getExpression());
                    }

                }
                else {
                    throw new RuntimeException("Unknown Item");
                }
            }
        }


        // TODO: ORDER

        AlgebraExpression M = new ToList(X);
        Optional<OrderClause> orderClause = solutionModifier.getOrderClause();
        if(orderClause.isPresent()) {
            M = new OrderBy(M, new OrderByComparator(orderClause.get().getOrderConditions()));
        }
        M = new Project(M, new ArrayList<>(projectionVariables));
        if(selectClause.isSelectDistinct()) {
            M = new Distinct(M);
        }
        return M;
    }
}
