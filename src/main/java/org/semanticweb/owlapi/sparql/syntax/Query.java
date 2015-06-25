package org.semanticweb.owlapi.sparql.syntax;

import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.sparql.algebra.AlgebraExpression;
import org.semanticweb.owlapi.sparql.algebra.SolutionSequence;
import org.semanticweb.owlapi.sparql.api.SolutionModifier;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 25/06/15
 */
public abstract class Query {

    private final PrefixManager prefixManager;

    private final GroupPattern groupPattern;

    private final SolutionModifier solutionModifier;

    public Query(PrefixManager prefixManager, GroupPattern groupPattern, SolutionModifier solutionModifier) {
        this.prefixManager = prefixManager;
        this.groupPattern = groupPattern;
        this.solutionModifier = solutionModifier;
    }

    public PrefixManager getPrefixManager() {
        return prefixManager;
    }

    public GroupPattern getGroupPattern() {
        return groupPattern;
    }

    public SolutionModifier getSolutionModifier() {
        return solutionModifier;
    }

    public abstract AlgebraExpression<SolutionSequence> translate();

    public abstract SelectQuery asSelectQuery();
}
