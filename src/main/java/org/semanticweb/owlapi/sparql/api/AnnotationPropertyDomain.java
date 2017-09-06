package org.semanticweb.owlapi.sparql.api;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;

import java.util.Collection;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 02/08/2012
 */
public class AnnotationPropertyDomain implements Axiom {

    private AtomicAnnotationProperty property;

    private AtomicIRI domain;

    public AnnotationPropertyDomain(AtomicAnnotationProperty property, AtomicIRI domain) {
        this.property = property;
        this.domain = domain;
    }

    public AtomicAnnotationProperty getProperty() {
        return property;
    }

    public <R, E extends Throwable> R accept(AxiomVisitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    public <R, E extends Throwable> R accept(Visitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    public AtomicIRI getDomain() {
        return domain;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(property, domain);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof AnnotationPropertyDomain)) {
            return false;
        }
        AnnotationPropertyDomain other = (AnnotationPropertyDomain) obj;
        return this.property.equals(other.property) && this.domain.equals(other.domain);
    }

    @Override
    public void collectVariables(Collection<Variable> variables) {
        property.collectVariables(variables);
        domain.collectVariables(variables);
    }

    @Override
    public Optional<AnnotationPropertyDomain> bind(SolutionMapping sm) {
        Optional<? extends AtomicAnnotationProperty> property = this.property.bind(sm);
        if(!property.isPresent()) {
            return Optional.absent();
        }
        Optional<AtomicIRI> domain = this.domain.bind(sm);
        if(!domain.isPresent()) {
            return Optional.absent();
        }
        return Optional.of(new AnnotationPropertyDomain(property.get(), domain.get()));
    }

    @Override
    public OWLAxiom toOWLObject(OWLDataFactory df) {
        return df.getOWLAnnotationPropertyDomainAxiom(
                getProperty().toOWLObject(df),
                getDomain().toOWLObject(df)
        );
    }
}
