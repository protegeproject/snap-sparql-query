package org.semanticweb.owlapi.sparql.api;

import com.google.common.base.Objects;
import java.util.Optional;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;

import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/07/2012
 */
public class SubClassOf implements ClassAxiom {

    private ClassExpression subClass;
    
    private ClassExpression superClass;

    public SubClassOf(ClassExpression subClass, ClassExpression superClass) {
        this.subClass = checkNotNull(subClass);
        this.superClass = checkNotNull(superClass);
    }

    public <R, E extends Throwable> R accept(AxiomVisitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    public <R, E extends Throwable> R accept(Visitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    public ClassExpression getSubClass() {
        return subClass;
    }

    public ClassExpression getSuperClass() {
        return superClass;
    }

    public Set<ClassExpression> getClassExpressions() {
        return new HashSet<>(Arrays.asList(subClass, superClass));
    }

    @Override
    public Optional<SubClassOf> bind(SolutionMapping sm) {
        Optional<? extends ClassExpression> boundSub = subClass.bind(sm);
        if(!boundSub.isPresent()) {
            return Optional.empty();
        }
        Optional<? extends ClassExpression> boundSuper = superClass.bind(sm);
        if(!boundSuper.isPresent()) {
            return Optional.empty();
        }
        return Optional.of(new SubClassOf(boundSub.get(), boundSuper.get()));
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(subClass.hashCode(), superClass.hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof SubClassOf)) {
            return false;
        }
        SubClassOf other = (SubClassOf) obj;
        return this.subClass.equals(other.subClass) && this.superClass.equals(other.superClass);
    }

    @Override
    public void collectVariables(Collection<Variable> variables) {
        subClass.collectVariables(variables);
        superClass.collectVariables(variables);
    }

    @Override
    public OWLAxiom toOWLObject(OWLDataFactory df) {
        return df.getOWLSubClassOfAxiom(
                subClass.toOWLObject(df),
                superClass.toOWLObject(df)
        );
    }

    @Override
    public String toString() {
        return Objects.toStringHelper("SubClassOf")
                .add("subclass", subClass)
                .add("superClass", superClass)
                .toString();
    }
}
