package org.semanticweb.owlapi.sparql.api;

import java.util.Collection;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/07/2012
 */
public abstract class AbstractAssertion<S extends Visitable & HasCollectVariables, P extends Visitable & HasCollectVariables, O extends Visitable & HasCollectVariables> implements Assertion<S, P, O>{

    private S subject;
    
    private P property;
    
    private O object;


    protected AbstractAssertion() {
    }

    public AbstractAssertion(P property, S subject, O object) {
        this.subject = checkNotNull(subject);
        this.property = checkNotNull(property);
        this.object = checkNotNull(object);
    }

    public S getSubject() {
        return subject;
    }

    public P getProperty() {
        return property;
    }

    public O getObject() {
        return object;
    }

    @Override
    public void collectVariables(Collection<Variable> variables) {
        subject.collectVariables(variables);
        property.collectVariables(variables);
        object.collectVariables(variables);
    }
}
