package org.semanticweb.owlapi.sparql.api;

import com.google.common.base.Optional;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/07/2012
 */
public class SubDataPropertyOf implements Axiom, HasSubProperty<DataPropertyExpression>, HasSuperProperty<DataPropertyExpression>, HasDataPropertyExpressions {

    private DataPropertyExpression subProperty;

    private DataPropertyExpression superProperty;

    public SubDataPropertyOf(DataPropertyExpression subProperty, DataPropertyExpression superProperty) {
        this.subProperty = subProperty;
        this.superProperty = superProperty;
    }

    public <R, E extends Throwable> R accept(AxiomVisitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    public <R, E extends Throwable> R accept(Visitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    public DataPropertyExpression getSubProperty() {
        return subProperty;
    }

    public DataPropertyExpression getSuperProperty() {
        return superProperty;
    }

    public Set<DataPropertyExpression> getDataProperties() {
        return new HashSet<DataPropertyExpression>(Arrays.asList(subProperty, superProperty));
    }

    @Override
    public int hashCode() {
        return SubDataPropertyOf.class.getSimpleName().hashCode() + subProperty.hashCode() * 13 + superProperty.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof SubDataPropertyOf)) {
            return false;
        }
        SubDataPropertyOf other = (SubDataPropertyOf) obj;
        return this.subProperty.equals(other.subProperty) && this.superProperty.equals(other.superProperty);
    }

    @Override
    public void collectVariables(Collection<Variable> variables) {
        subProperty.collectVariables(variables);
        superProperty.collectVariables(variables);
    }

    @Override
    public Optional<SubDataPropertyOf> bind(SolutionMapping sm) {
        Optional<? extends DataPropertyExpression> subProperty = this.subProperty.bind(sm);
        if(!subProperty.isPresent()) {
            return Optional.absent();
        }
        Optional<? extends DataPropertyExpression> superProperty = this.superProperty.bind(sm);
        if(!superProperty.isPresent()) {
            return Optional.absent();
        }
        return Optional.of(new SubDataPropertyOf(subProperty.get(), superProperty.get()));
    }

    @Override
    public OWLAxiom toOWLObject(OWLDataFactory df) {
        return df.getOWLSubDataPropertyOfAxiom(
                getSubProperty().toOWLObject(df),
                getSuperProperty().toOWLObject(df)
        );
    }
}
