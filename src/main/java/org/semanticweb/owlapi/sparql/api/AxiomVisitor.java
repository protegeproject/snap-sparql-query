package org.semanticweb.owlapi.sparql.api;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 02/08/2012
 */
public interface AxiomVisitor<R, E extends Throwable>  {

    
    R visit(SubClassOf axiom) throws E;

    
//    R visit(NegativeObjectPropertyAssertion axiom) throws E;

    
    R visit(AsymmetricObjectProperty axiom) throws E;

    
    R visit(ReflexiveObjectProperty axiom) throws E;

    
    R visit(DisjointClasses axiom) throws E;

    
    R visit(DataPropertyDomain axiom) throws E;

    
    R visit(ObjectPropertyDomain axiom) throws E;

    
    R visit(EquivalentObjectProperties axiom) throws E;

    
//    R visit(NegativeDataPropertyAssertion axiom) throws E;

    
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

    
    R visit(SubAnnotationPropertyOf axiom) throws E;

    
    R visit(AnnotationPropertyDomain axiom) throws E;

    
    R visit(AnnotationPropertyRange axiom) throws E;
}
