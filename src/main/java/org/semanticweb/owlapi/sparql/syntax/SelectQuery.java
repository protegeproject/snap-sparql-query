package org.semanticweb.owlapi.sparql.syntax;


import com.google.common.base.*;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.sparql.algebra.*;
import org.semanticweb.owlapi.sparql.api.SolutionModifier;
import org.semanticweb.owlapi.sparql.api.UntypedVariable;
import org.semanticweb.owlapi.sparql.api.Variable;
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

        ImmutableList<SelectItem> selectItems = selectClause.getSelectItems();
        GraphPatternAlgebraExpression X = G;

        Map<String, Variable> name2VariableMap = new HashMap<>();
        for(Variable visibleVariable : visibleVariables) {
            name2VariableMap.put(visibleVariable.getName(), visibleVariable);
        }

        Set<Variable> projectionVariables = new LinkedHashSet<>();
        if(selectItems.isEmpty()) {
            projectionVariables.addAll(visibleVariables);
        }
        else {
            // TODO: Convert to proper typed variables
            for(SelectItem selectItem : selectItems) {
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
                else if(selectItem instanceof SelectAs) {
                    SelectAs selectAs = (SelectAs) selectItem;
                    UntypedVariable untypedVariable = selectAs.getVariable();
                    projectionVariables.add(untypedVariable);
                    // TODO: Check that it's not already in the visible set
                    Variable typedVariable = name2VariableMap.get(untypedVariable.getName());
                    if(typedVariable != null) {
                        X = new Extend(X, typedVariable, selectAs.getExpression());
                    }
                    else {
                        X = new Extend(X, untypedVariable, selectAs.getExpression());
                    }

                }
                else {
                    throw new RuntimeException("Unknown Item");
                }
            }
        }
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
