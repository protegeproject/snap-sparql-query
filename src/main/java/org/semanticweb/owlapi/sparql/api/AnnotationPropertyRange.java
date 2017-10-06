package org.semanticweb.owlapi.sparql.api;

import com.google.common.base.Objects;
import java.util.Optional;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;

import java.util.Collection;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 02/08/2012
 */
public class AnnotationPropertyRange implements Axiom {

    private AtomicAnnotationProperty property;

    private AtomicIRI range;

    public AnnotationPropertyRange(AtomicAnnotationProperty property, AtomicIRI range) {
        this.property = property;
        this.range = range;
    }

    public <R, E extends Throwable> R accept(AxiomVisitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    public <R, E extends Throwable> R accept(Visitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    public AtomicAnnotationProperty getProperty() {
        return property;
    }

    public AtomicIRI getRange() {
        return range;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(property, range);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof AnnotationPropertyRange)) {
            return false;
        }
        AnnotationPropertyRange other = (AnnotationPropertyRange) obj;
        return this.property.equals(other.property) && this.range.equals(other.range);
    }

    @Override
    public void collectVariables(Collection<Variable> variables) {
        property.collectVariables(variables);
        range.collectVariables(variables);
    }

    @Override
    public Optional<AnnotationPropertyRange> bind(SolutionMapping sm) {
        Optional<? extends AtomicAnnotationProperty> property = this.property.bind(sm);
        if(!property.isPresent()) {
            return Optional.empty();
        }
        Optional<AtomicIRI> range = this.range.bind(sm);
        if(!range.isPresent()) {
            return Optional.empty();
        }
        return Optional.of(new AnnotationPropertyRange(property.get(), range.get()));
    }

    @Override
    public OWLAxiom toOWLObject(OWLDataFactory df) {
        return df.getOWLAnnotationPropertyRangeAxiom(
                getProperty().toOWLObject(df),
                getRange().toOWLObject(df)
        );
    }
}
