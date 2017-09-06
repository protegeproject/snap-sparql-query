package org.semanticweb.owlapi.sparql.api;

import com.google.common.base.Optional;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDisjointObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLObject;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/07/2012
 */
public class DisjointObjectProperties extends NaryObjectPropertyAxiom implements Axiom {

    public DisjointObjectProperties(ObjectPropertyExpression left, ObjectPropertyExpression right) {
        super(left, right);
    }

    public DisjointObjectProperties(Set<ObjectPropertyExpression> propertyExpressions) {
        super(propertyExpressions);
    }

    public <R, E extends Throwable> R accept(AxiomVisitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    public <R, E extends Throwable> R accept(Visitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return getObjectPropertyExpressions().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof DisjointObjectProperties)) {
            return false;
        }
        DisjointObjectProperties other = (DisjointObjectProperties) obj;
        return this.getObjectPropertyExpressions().equals(other.getObjectPropertyExpressions());
    }

    @Override
    public Optional<DisjointObjectProperties> bind(SolutionMapping sm) {
        Optional<Set<ObjectPropertyExpression>> boundPropertyExpressions = getBoundPropertyExpressions(sm);
        if(!boundPropertyExpressions.isPresent()) {
            return Optional.absent();
        }
        return Optional.of(new DisjointObjectProperties(boundPropertyExpressions.get()));
    }

    @Override
    public OWLDisjointObjectPropertiesAxiom toOWLObject(OWLDataFactory df) {
        return df.getOWLDisjointObjectPropertiesAxiom(
                getObjectPropertyExpressions().stream()
                        .map(p -> p.toOWLObject(df))
                        .collect(Collectors.toSet())
        );
    }
}
