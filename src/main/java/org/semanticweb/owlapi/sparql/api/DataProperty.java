package org.semanticweb.owlapi.sparql.api;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import java.util.Optional;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLObject;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/07/2012
 */
public class DataProperty extends AbstractEntity implements DataPropertyExpression, AtomicDataProperty {

    public DataProperty(@Nonnull IRI iri) {
        super(iri);
    }

    public <R, E extends Throwable> R accept(Visitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return getIRI().hashCode();
    }

    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof DataProperty)) {
            return false;
        }
        DataProperty other = (DataProperty) obj;
        return this.getIRI().equals(other.getIRI());
    }

    @Override
    public IRI getIRI() {
        return super.getIRI();
    }


    @Override
    public String toString() {
        return toStringHelper("DataProperty")
                .addValue(getIRI())
                .toString();
    }

    @Override
    public <R, E extends Throwable, C> R accept(ExpressionVisitor<R, E, C> visitor, C context) throws E {
        return visitor.visit(this, context);
    }

    @Override
    public Optional<DataProperty> bind(SolutionMapping sm) {
        return Optional.of(this);
    }

    @Override
    public OWLDataProperty toOWLObject(OWLDataFactory df) {
        return df.getOWLDataProperty(getIRI());
    }

    @Nonnull
    @Override
    public java.util.Optional<Variable> asVariable() {
        return java.util.Optional.empty();
    }
}
