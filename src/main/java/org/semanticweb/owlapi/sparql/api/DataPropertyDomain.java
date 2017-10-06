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
public class DataPropertyDomain implements Axiom, HasProperty<DataPropertyExpression>, HasDomain<ClassExpression> {

    private DataPropertyExpression property;

    private ClassExpression domain;

    public DataPropertyDomain(DataPropertyExpression property, ClassExpression domain) {
        this.property = property;
        this.domain = domain;
    }

    public <R, E extends Throwable> R accept(Visitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    public <R, E extends Throwable> R accept(AxiomVisitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    public ClassExpression getDomain() {
        return domain;
    }

    public DataPropertyExpression getProperty() {
        return property;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(property, domain);
    }

    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof DataPropertyDomain)) {
            return false;
        }
        DataPropertyDomain other = (DataPropertyDomain) obj;
        return this.property.equals(other.property) && this.domain.equals(other.domain);
    }

    @Override
    public void collectVariables(Collection<Variable> variables) {
        property.collectVariables(variables);
        domain.collectVariables(variables);
    }

    @Override
    public Optional<DataPropertyDomain> bind(SolutionMapping sm) {
        Optional<? extends DataPropertyExpression> boundProperty = property.bind(sm);
        if(!boundProperty.isPresent()) {
            return Optional.empty();
        }
        Optional<? extends ClassExpression> boundDomain = domain.bind(sm);
        if(!boundDomain.isPresent()) {
            return Optional.empty();
        }
        return Optional.of(new DataPropertyDomain(boundProperty.get(), boundDomain.get()));
    }

    @Override
    public OWLAxiom toOWLObject(OWLDataFactory df) {
        return df.getOWLDataPropertyDomainAxiom(
                getProperty().toOWLObject(df),
                getDomain().toOWLObject(df)
        );
    }
}
