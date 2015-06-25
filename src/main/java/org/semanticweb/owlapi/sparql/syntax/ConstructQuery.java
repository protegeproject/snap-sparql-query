package org.semanticweb.owlapi.sparql.syntax;

import com.google.common.collect.ImmutableList;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.sparql.algebra.AlgebraExpression;
import org.semanticweb.owlapi.sparql.algebra.SolutionSequence;
import org.semanticweb.owlapi.sparql.algebra.Syntax2AlgebraTranslator;
import org.semanticweb.owlapi.sparql.api.SolutionModifier;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 24/06/15
 */
public class ConstructQuery extends Query {


    private final ConstructTemplate constructTemplate;

    public ConstructQuery(PrefixManager prefixManager, ConstructTemplate constructTemplate, GroupPattern groupPattern, SolutionModifier solutionModifier) {
        super(prefixManager, groupPattern, solutionModifier);
        this.constructTemplate = constructTemplate;
    }

    public SelectQuery asSelectQuery() {
        return new SelectQuery(
                getPrefixManager(),
                new SelectClause(false, ImmutableList.of()),
                getGroupPattern(),
                getSolutionModifier()
        );
    }

    public ConstructTemplate getConstructTemplate() {
        return constructTemplate;
    }

    public AlgebraExpression<SolutionSequence> translate() {
        return new Syntax2AlgebraTranslator().translate(this);
    }
}
