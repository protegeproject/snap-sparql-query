package org.semanticweb.owlapi.sparql.api;

import org.semanticweb.owlapi.model.SWRLRule;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 02/08/2012
 */
public interface Visitor<R, E extends Throwable> {

    R visit(AtomicIRI iri) throws E;
    
    R visit(DataProperty property) throws E;
    
    
    R visit(AnonymousIndividual individual) throws E;

    
    R visit(SubClassOf axiom) throws E;

    
    R visit(AsymmetricObjectProperty axiom) throws E;

    
    R visit(ReflexiveObjectProperty axiom) throws E;

    
    R visit(DisjointClasses axiom) throws E;

    
    R visit(DataPropertyDomain axiom) throws E;

    
    R visit(ObjectPropertyDomain axiom) throws E;

    
    R visit(EquivalentObjectProperties axiom) throws E;

    
    R visit(DifferentIndividuals axiom) throws E;

    
    R visit(DisjointDataProperties axiom) throws E;

    
    R visit(DisjointObjectProperties axiom) throws E;

    
    R visit(ObjectPropertyRange axiom) throws E;

    
    R visit(ObjectPropertyAssertion axiom) throws E;

    
    R visit(FunctionalObjectProperty axiom) throws E;

    
    R visit(SubObjectPropertyOf axiom) throws E;

    
    R visit(Declaration axiom) throws E;

    
    R visit(AnnotationAssertion axiom) throws E;

    
    R visit(SymmetricObjectProperty axiom) throws E;

    
    R visit(DataPropertyRange axiom) throws E;

    
    R visit(FunctionalDataProperty axiom) throws E;

    
    R visit(EquivalentDataProperties axiom) throws E;

    
    R visit(ClassAssertion axiom) throws E;

    
    R visit(EquivalentClasses axiom) throws E;

    
    R visit(DataPropertyAssertion axiom) throws E;

    
    R visit(TransitiveObjectProperty axiom) throws E;

    
    R visit(IrreflexiveObjectProperty axiom) throws E;

    
    R visit(SubDataPropertyOf axiom) throws E;

    
    R visit(InverseFunctionalObjectProperty axiom) throws E;

    
    R visit(SameIndividual axiom) throws E;

    
    R visit(InverseObjectProperties axiom) throws E;

    
    R visit(SWRLRule rule) throws E;

    
    R visit(SubAnnotationPropertyOf axiom) throws E;

    
    R visit(AnnotationPropertyDomain axiom) throws E;

    
    R visit(AnnotationPropertyRange axiom) throws E;

    
    R visit(Datatype node) throws E;

    
    R visit(Literal node) throws E;

    R visit(NamedClass node) throws E;
    
    R visit(NamedIndividual individual) throws E;

    
    R visit(AnnotationProperty property) throws E;

    
    R visit(ObjectProperty property) throws E;
    
    R visit(ClassVariable variable) throws E;

    R visit(DatatypeVariable variable) throws E;

    R visit(ObjectPropertyVariable variable) throws E;

    R visit(DataPropertyVariable variable) throws E;

    R visit(AnnotationPropertyVariable variable) throws E;

    R visit(IndividualVariable variable) throws E;

    R visit(LiteralVariable variable) throws E;
    
    R visit(UntypedVariable variable) throws E;

    R visit(IRIVariable variable) throws E;

    R visit(AnnotationValueVariable variable) throws E;
}
