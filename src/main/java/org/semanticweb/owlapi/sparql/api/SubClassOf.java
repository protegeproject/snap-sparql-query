package org.semanticweb.owlapi.sparql.api;

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
public class SubClassOf implements ClassAxiom {

    private ClassExpression subClass;
    
    private ClassExpression superClass;

    public SubClassOf(ClassExpression subClass, ClassExpression superClass) {
        this.subClass = subClass;
        this.superClass = superClass;
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
        return new HashSet<ClassExpression>(Arrays.asList(subClass, superClass));
    }

    @Override
    public int hashCode() {
        return SubClassOf.class.getSimpleName().hashCode() + subClass.hashCode() * 13 + superClass.hashCode();
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
}
