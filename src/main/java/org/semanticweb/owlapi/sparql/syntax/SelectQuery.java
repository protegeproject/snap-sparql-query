package org.semanticweb.owlapi.sparql.syntax;


import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.sparql.algebra.*;
import org.semanticweb.owlapi.sparql.api.*;

import java.util.*;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 08/06/15
 */
public class SelectQuery extends Query {

    private SelectClause selectClause;

    public SelectQuery(PrefixManager prefixManager, SelectClause selectClause, GroupPattern groupPattern, SolutionModifier solutionModifier) {
        super(prefixManager, groupPattern, solutionModifier);
        this.selectClause = selectClause;
    }

    public boolean isAggregateQuery() {
        if(getSolutionModifier().getGroupClause().isPresent()) {
            return true;
        }
        if(selectClause.containsAggregates()) {
            return true;
        }
        return false;
    }

    public SelectClause getSelectClause() {
        return selectClause;
    }

    public List<UntypedVariable> getSelectClauseVariables() {
        return selectClause.getVariables();
    }

    public AlgebraExpression<SolutionSequence> translate() {
        Syntax2AlgebraTranslator translator = new Syntax2AlgebraTranslator();
        return translator.translate(this);
    }

    @Override
    public SelectQuery asSelectQuery() {
        return this;
    }
}
