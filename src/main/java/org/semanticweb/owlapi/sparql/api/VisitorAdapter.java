package org.semanticweb.owlapi.sparql.api;

import org.semanticweb.owlapi.model.SWRLRule;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 02/08/2012
 */
public class VisitorAdapter<R, E extends Throwable> implements Visitor<R, E> {

    public R visit(AtomicIRI iri) throws E {
        return null;
    }

    public R visit(DataProperty property) throws E {
        return null;
    }

    public R visit(AnonymousIndividual individual) throws E {
        return null;
    }

    public R visit(SubClassOf axiom) throws E {
        return null;
    }

    public R visit(AsymmetricObjectProperty axiom) throws E {
        return null;
    }

    public R visit(ReflexiveObjectProperty axiom) throws E {
        return null;
    }

    public R visit(DisjointClasses axiom) throws E {
        return null;
    }

    public R visit(DataPropertyDomain axiom) throws E {
        return null;
    }

    public R visit(ObjectPropertyDomain axiom) throws E {
        return null;
    }

    public R visit(EquivalentObjectProperties axiom) throws E {
        return null;
    }

    public R visit(DifferentIndividuals axiom) throws E {
        return null;
    }

    public R visit(DisjointDataProperties axiom) throws E {
        return null;
    }

    public R visit(DisjointObjectProperties axiom) throws E {
        return null;
    }

    public R visit(ObjectPropertyRange axiom) throws E {
        return null;
    }

    public R visit(ObjectPropertyAssertion axiom) throws E {
        return null;
    }

    public R visit(FunctionalObjectProperty axiom) throws E {
        return null;
    }

    public R visit(SubObjectPropertyOf axiom) throws E {
        return null;
    }

    public R visit(Declaration axiom) throws E {
        return null;
    }

    public R visit(AnnotationAssertion axiom) throws E {
        return null;
    }

    public R visit(SymmetricObjectProperty axiom) throws E {
        return null;
    }

    public R visit(DataPropertyRange axiom) throws E {
        return null;
    }

    public R visit(FunctionalDataProperty axiom) throws E {
        return null;
    }

    public R visit(EquivalentDataProperties axiom) throws E {
        return null;
    }

    public R visit(ClassAssertion axiom) throws E {
        return null;
    }

    public R visit(EquivalentClasses axiom) throws E {
        return null;
    }

    public R visit(DataPropertyAssertion axiom) throws E {
        return null;
    }

    public R visit(TransitiveObjectProperty axiom) throws E {
        return null;
    }

    public R visit(IrreflexiveObjectProperty axiom) throws E {
        return null;
    }

    public R visit(SubDataPropertyOf axiom) throws E {
        return null;
    }

    public R visit(InverseFunctionalObjectProperty axiom) throws E {
        return null;
    }

    public R visit(SameIndividual axiom) throws E {
        return null;
    }

    public R visit(InverseObjectProperties axiom) throws E {
        return null;
    }

    public R visit(SWRLRule rule) throws E {
        return null;
    }

    public R visit(SubAnnotationPropertyOf axiom) throws E {
        return null;
    }

    public R visit(AnnotationPropertyDomain axiom) throws E {
        return null;
    }

    public R visit(AnnotationPropertyRange axiom) throws E {
        return null;
    }

    public R visit(Datatype node) throws E {
        return null;
    }

    public R visit(Literal node) throws E {
        return null;
    }

    public R visit(NamedClass node) throws E {
        return null;
    }

    public R visit(NamedIndividual individual) throws E {
        return null;
    }

    public R visit(AnnotationProperty property) throws E {
        return null;
    }

    public R visit(ObjectProperty property) throws E {
        return null;
    }

    public R visit(ClassVariable variable) throws E {
        return null;
    }

    public R visit(DatatypeVariable variable) throws E {
        return null;
    }

    public R visit(ObjectPropertyVariable variable) throws E {
        return null;
    }

    public R visit(DataPropertyVariable variable) throws E {
        return null;
    }

    public R visit(AnnotationPropertyVariable variable) throws E {
        return null;
    }

    public R visit(IndividualVariable variable) throws E {
        return null;
    }

    public R visit(LiteralVariable variable) throws E {
        return null;
    }

    public R visit(UntypedVariable variable) throws E {
        return null;
    }
}
