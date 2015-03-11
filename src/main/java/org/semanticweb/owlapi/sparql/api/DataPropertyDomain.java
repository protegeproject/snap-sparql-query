package org.semanticweb.owlapi.sparql.api;

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
        return DataPropertyDomain.class.getSimpleName().hashCode() + property.hashCode() + domain.hashCode();
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
}
