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
 * Date: 26/07/2012
 */
public class ObjectPropertyDomain implements Axiom, HasDomain<ClassExpression>, HasProperty<ObjectPropertyExpression> {

    private ObjectPropertyExpression property;

    private ClassExpression domain;

    public ObjectPropertyDomain(ObjectPropertyExpression property, ClassExpression domain) {
        this.property = property;
        this.domain = domain;
    }

    public <R, E extends Throwable> R accept(AxiomVisitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    public <R, E extends Throwable> R accept(Visitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    public ClassExpression getDomain() {
        return domain;
    }

    public ObjectPropertyExpression getProperty() {
        return property;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(property.hashCode(), domain.hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof ObjectPropertyDomain)) {
            return false;
        }
        ObjectPropertyDomain other = (ObjectPropertyDomain) obj;
        return this.property.equals(other.property) && this.domain.equals(other.domain);
    }

    @Override
    public void collectVariables(Collection<Variable> variables) {
        property.collectVariables(variables);
        domain.collectVariables(variables);
    }

    @Override
    public Optional<ObjectPropertyDomain> bind(SolutionMapping sm) {
        Optional<? extends ObjectPropertyExpression> boundProperty = property.bind(sm);
        if(!boundProperty.isPresent()) {
            return Optional.empty();
        }
        Optional<? extends ClassExpression> boundDomain = domain.bind(sm);
        if(!boundDomain.isPresent()) {
            return Optional.empty();
        }
        return Optional.of(new ObjectPropertyDomain(boundProperty.get(), boundDomain.get()));
    }

    @Override
    public OWLAxiom toOWLObject(OWLDataFactory df) {
        return df.getOWLObjectPropertyDomainAxiom(
                property.toOWLObject(df),
                domain.toOWLObject(df)
        );
    }
}
