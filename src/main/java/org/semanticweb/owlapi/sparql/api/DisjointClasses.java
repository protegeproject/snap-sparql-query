package org.semanticweb.owlapi.sparql.api;


import java.util.Optional;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Author: Matthew Horridge<br> Stanford University<br> Bio-Medical Informatics Research Group<br> Date: 26/07/2012
 */
public class DisjointClasses extends NaryClassAxiom implements ClassAxiom {

    public DisjointClasses(Set<ClassExpression> classExpressions) {
        super(classExpressions);
    }

    public DisjointClasses(ClassExpression first, ClassExpression second) {
        super(first, second);
    }

    public <R, E extends Throwable> R accept(Visitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    public <R, E extends Throwable> R accept(AxiomVisitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return getClassExpressions().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof DisjointClasses)) {
            return false;
        }
        DisjointClasses other = (DisjointClasses) obj;
        return this.getClassExpressions().equals(other.getClassExpressions());
    }

    @Override
    public Optional<DisjointClasses> bind(SolutionMapping sm) {
        Optional<Set<ClassExpression>> boundClassExpressions = getBoundClassExpressions(sm);
        if (!boundClassExpressions.isPresent()) {
            return Optional.empty();
        }
        return Optional.of(new DisjointClasses(boundClassExpressions.get()));
    }

    @Override
    public OWLAxiom toOWLObject(OWLDataFactory df) {
        return df.getOWLDisjointClassesAxiom(
                getClassExpressions().stream()
                        .map(ce -> ce.toOWLObject(df))
                        .collect(Collectors.toSet())
        );
    }
}
