package org.semanticweb.owlapi.sparql.api;


import com.google.common.base.Optional;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 22/06/15
 */
public abstract class GroupCondition {

    public abstract Optional<UntypedVariable> getGroupConditionVariable();
}
