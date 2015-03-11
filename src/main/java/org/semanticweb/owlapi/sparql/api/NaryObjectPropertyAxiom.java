package org.semanticweb.owlapi.sparql.api;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/07/2012
 */
public abstract class NaryObjectPropertyAxiom implements Axiom, HasObjectPropertyExpressions {

    private Set<ObjectPropertyExpression> propertyExpressions;
    
    protected NaryObjectPropertyAxiom(ObjectPropertyExpression left, ObjectPropertyExpression right) {
        this.propertyExpressions = new HashSet<ObjectPropertyExpression>(3);
        propertyExpressions.add(left);
        propertyExpressions.add(right);
    }

    public NaryObjectPropertyAxiom(Set<ObjectPropertyExpression> propertyExpressions) {
        this.propertyExpressions = new HashSet<ObjectPropertyExpression>(propertyExpressions);
    }
    
    public Set<ObjectPropertyExpression> getObjectPropertyExpressions() {
        return Collections.unmodifiableSet(propertyExpressions);
    }

    @Override
    public void collectVariables(Collection<Variable> variables) {
        for(ObjectPropertyExpression pe : propertyExpressions) {
            pe.collectVariables(variables);
        }
    }
}
