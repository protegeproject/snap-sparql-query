package org.semanticweb.owlapi.sparql.api;

import java.util.Optional;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;

import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/07/2012
 */
public class EquivalentDataProperties extends NaryDataPropertyAxiom {

    public EquivalentDataProperties(DataPropertyExpression left, DataPropertyExpression right) {
        super(left, right);
    }

    public EquivalentDataProperties(Set<DataPropertyExpression> propertyExpressions) {
        super(propertyExpressions);
    }

    public <R, E extends Throwable> R accept(Visitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    public <R, E extends Throwable> R accept(AxiomVisitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return getDataProperties().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof EquivalentDataProperties)) {
            return false;
        }
        EquivalentDataProperties other = (EquivalentDataProperties) obj;
        return this.getDataProperties().equals(other.getDataProperties());
    }

    @Override
    public Optional<EquivalentDataProperties> bind(SolutionMapping sm) {
        Optional<Set<DataPropertyExpression>> boundDataProperties = getBoundDataProperties(sm);
        if(!boundDataProperties.isPresent()) {
            return Optional.empty();
        }
        return Optional.of(new EquivalentDataProperties(boundDataProperties.get()));
    }

    @Override
    public OWLAxiom toOWLObject(OWLDataFactory df) {
        return df.getOWLEquivalentDataPropertiesAxiom(
                getDataProperties().stream()
                .map(p -> p.toOWLObject(df))
                .collect(toSet())
        );
    }
}
