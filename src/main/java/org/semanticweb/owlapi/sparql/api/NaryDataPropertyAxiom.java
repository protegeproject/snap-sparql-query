package org.semanticweb.owlapi.sparql.api;

import java.util.Optional;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/07/2012
 */
public abstract class NaryDataPropertyAxiom implements HasDataPropertyExpressions, Axiom  {

    private Set<DataPropertyExpression> propertyExpressions;
    
    protected NaryDataPropertyAxiom(DataPropertyExpression left, DataPropertyExpression right) {
        propertyExpressions = new HashSet<DataPropertyExpression>(3);
        propertyExpressions.add(left);
        propertyExpressions.add(right);
    }

    protected NaryDataPropertyAxiom(Set<DataPropertyExpression> propertyExpressions) {
        this.propertyExpressions = new HashSet<DataPropertyExpression>(propertyExpressions);
    }

    public Set<DataPropertyExpression> getDataProperties() {
        return Collections.unmodifiableSet(propertyExpressions);
    }

    @Override
    public void collectVariables(Collection<Variable> variables) {
        for(DataPropertyExpression pe : propertyExpressions) {
            pe.collectVariables(variables);
        }
    }

    protected Optional<Set<DataPropertyExpression>> getBoundDataProperties(SolutionMapping sm) {
        Set<DataPropertyExpression> boundProperties = new HashSet<>();
        for(DataPropertyExpression property : propertyExpressions) {
            Optional<? extends DataPropertyExpression> boundProperty = property.bind(sm);
            if(!boundProperty.isPresent()) {
                return Optional.empty();
            }
            else {
                boundProperties.add(boundProperty.get());
            }
        }
        return Optional.of(boundProperties);
    }
}
