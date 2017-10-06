package org.semanticweb.owlapi.sparql.api;


import java.util.Optional;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 22/06/15
 */
public abstract class GroupCondition {

    public abstract Optional<UntypedVariable> getGroupConditionVariable();

    public abstract Expression asExpression();
}
