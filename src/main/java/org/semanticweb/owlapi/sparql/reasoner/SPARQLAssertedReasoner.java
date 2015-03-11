package org.semanticweb.owlapi.sparql.reasoner;

import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.*;
import org.semanticweb.owlapi.reasoner.impl.*;
import org.semanticweb.owlapi.util.Version;

import java.util.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 14/08/2012
 */
public class SPARQLAssertedReasoner implements OWLReasoner {

    public static final OWLClassNodeSet EMPTY_CLASS_NODE_SET = new OWLClassNodeSet();

    public static final OWLClassNode EMPTY_CLASS_NODE = new OWLClassNode();

    private OWLOntology rootOntology;

    public SPARQLAssertedReasoner(OWLOntology rootOntology) {
        this.rootOntology = rootOntology;
    }

    //    private OWLModelManager owlModelManager;

//    public ProtegeSPARQLStructuralReasoner(OWLModelManager owlModelManager) {
//        this.owlModelManager = owlModelManager;
//    }

    public String getReasonerName() {
        return "Protege SPARQL Reasoner";
    }

    public Version getReasonerVersion() {
        return new Version(1, 0, 0, 0);
    }

    public BufferingMode getBufferingMode() {
        return BufferingMode.NON_BUFFERING;
    }

    public void flush() {
    }

    public List<OWLOntologyChange> getPendingChanges() {
        return Collections.emptyList();
    }

    public Set<OWLAxiom> getPendingAxiomAdditions() {
        return Collections.emptySet();
    }

    public Set<OWLAxiom> getPendingAxiomRemovals() {
        return Collections.emptySet();
    }

    public OWLOntology getRootOntology() {
        return rootOntology;
    }

    public void interrupt() {
    }

    public void precomputeInferences(InferenceType... inferenceTypes) throws ReasonerInterruptedException, TimeOutException, InconsistentOntologyException {
    }

    public boolean isPrecomputed(InferenceType inferenceType) {
        return true;
    }

    public Set<InferenceType> getPrecomputableInferenceTypes() {
        return Collections.emptySet();
    }

    public boolean isConsistent() throws ReasonerInterruptedException, TimeOutException {
        return true;
    }

    public boolean isSatisfiable(OWLClassExpression owlClassExpression) throws ReasonerInterruptedException, TimeOutException, ClassExpressionNotInProfileException, FreshEntitiesException, InconsistentOntologyException {
        return true;
    }

    public Node<OWLClass> getUnsatisfiableClasses() throws ReasonerInterruptedException, TimeOutException, InconsistentOntologyException {
        return OWLClassNode.getBottomNode();
    }

    public boolean isEntailed(OWLAxiom axiom) throws ReasonerInterruptedException, UnsupportedEntailmentTypeException, TimeOutException, AxiomNotInProfileException, FreshEntitiesException, InconsistentOntologyException {
        return getRootOntology().containsAxiom(axiom, true);
    }

    public boolean isEntailed(Set<? extends OWLAxiom> owlAxioms) throws ReasonerInterruptedException, UnsupportedEntailmentTypeException, TimeOutException, AxiomNotInProfileException, FreshEntitiesException, InconsistentOntologyException {
        for (OWLAxiom axiom : owlAxioms) {
            if (!isEntailed(axiom)) {
                return false;
            }
        }
        return true;
    }

    public boolean isEntailmentCheckingSupported(AxiomType<?> axiomType) {
        return false;
    }

    public Node<OWLClass> getTopClassNode() {
        return OWLClassNode.getTopNode();
    }

    public Node<OWLClass> getBottomClassNode() {
        return OWLClassNode.getBottomNode();
    }

    public NodeSet<OWLClass> getSubClasses(OWLClassExpression owlClassExpression, boolean b) throws ReasonerInterruptedException, TimeOutException, FreshEntitiesException, InconsistentOntologyException, ClassExpressionNotInProfileException {
        if (owlClassExpression.isAnonymous()) {
            return EMPTY_CLASS_NODE_SET;
        }
        OWLClass cls = owlClassExpression.asOWLClass();
        Queue<OWLClass> queue = new LinkedList<OWLClass>();
        queue.add(cls);
        Set<OWLClassExpression> result = new HashSet<OWLClassExpression>();
        if(owlClassExpression.isOWLThing()) {
            result.addAll(rootOntology.getClassesInSignature(true));
        }
        else {
            while(!queue.isEmpty()) {
                OWLClass curCls = queue.poll();
                result.add(curCls);
                Set<OWLClassExpression> curClsSubs = curCls.getSubClasses(getRootOntology().getImportsClosure());
                for(OWLClassExpression curClsSub : curClsSubs) {
                    if (!curClsSub.isAnonymous()) {
                        if(!result.contains(curClsSub.asOWLClass())) {
                            queue.add(curClsSub.asOWLClass());
                        }
                    }
                }
            }
        }

        result.add(rootOntology.getOWLOntologyManager().getOWLDataFactory().getOWLNothing());
        return toNodeSet(result);
    }

    private OWLClassNodeSet toNodeSet(Set<OWLClassExpression> clses) {
        OWLClassNodeSet ns = new OWLClassNodeSet();
        for(OWLClassExpression ce : clses) {
            if(!ce.isAnonymous()) {
                ns.addEntity(ce.asOWLClass());
            }
        }
        return ns;
    }

//    private NodeSet<OWLClass> toNodeSet(Set<OWLClass> result) {
//        Set<Node<OWLClass>> nodes = new HashSet<Node<OWLClass>>(result.size());
//        for (OWLClass cls : result) {
//            OWLClassNode classNode = new OWLClassNode(cls);
//            nodes.add(classNode);
//        }
//        return new OWLClassNodeSet(nodes);
//    }

    public NodeSet<OWLClass> getSuperClasses(OWLClassExpression owlClassExpression, boolean b) throws InconsistentOntologyException, ClassExpressionNotInProfileException, FreshEntitiesException, ReasonerInterruptedException, TimeOutException {
        if (owlClassExpression.isAnonymous()) {
            return EMPTY_CLASS_NODE_SET;
        }
        OWLClass cls = owlClassExpression.asOWLClass();
        Queue<OWLClass> queue = new LinkedList<OWLClass>();
        queue.add(cls);
        Set<OWLClassExpression> result = new HashSet<OWLClassExpression>();
        while(!queue.isEmpty()) {
            OWLClass curCls = queue.poll();
            result.add(curCls);
            Set<OWLClassExpression> curClsSupers = curCls.getSuperClasses(getRootOntology().getImportsClosure());
            for(OWLClassExpression curClsSuper : curClsSupers) {
                if (!curClsSuper.isAnonymous()) {
                    if(!result.contains(curClsSuper.asOWLClass())) {
                        queue.add(curClsSuper.asOWLClass());
                    }
                }
            }
        }
        result.add(rootOntology.getOWLOntologyManager().getOWLDataFactory().getOWLThing());
        return toNodeSet(result);
    }

    public Node<OWLClass> getEquivalentClasses(OWLClassExpression owlClassExpression) throws InconsistentOntologyException, ClassExpressionNotInProfileException, FreshEntitiesException, ReasonerInterruptedException, TimeOutException {
        if (owlClassExpression.isAnonymous()) {
            return EMPTY_CLASS_NODE;
        }
        OWLClass cls = owlClassExpression.asOWLClass();
        OWLClassNode node = new OWLClassNode();
        Set<OWLClassExpression> result = cls.getEquivalentClasses(getRootOntology().getImportsClosure());
        for(OWLClassExpression ce : result) {
            if(!ce.isAnonymous()) {
                result.add(ce.asOWLClass());
            }
        }
        return node;
    }

    public NodeSet<OWLClass> getDisjointClasses(OWLClassExpression owlClassExpression) throws ReasonerInterruptedException, TimeOutException, FreshEntitiesException, InconsistentOntologyException {
        if (owlClassExpression.isAnonymous()) {
            return EMPTY_CLASS_NODE_SET;
        }
        if (owlClassExpression.isAnonymous()) {
            return EMPTY_CLASS_NODE_SET;
        }
        OWLClass cls = owlClassExpression.asOWLClass();
        Set<OWLClassExpression> result = cls.getEquivalentClasses(getRootOntology().getImportsClosure());
        return toNodeSet(result);
    }

    public Node<OWLObjectPropertyExpression> getTopObjectPropertyNode() {
        return OWLObjectPropertyNode.getTopNode();
    }

    public Node<OWLObjectPropertyExpression> getBottomObjectPropertyNode() {
        return OWLObjectPropertyNode.getBottomNode();
    }

    public NodeSet<OWLObjectPropertyExpression> getSubObjectProperties(OWLObjectPropertyExpression propertyExpression, boolean b) throws InconsistentOntologyException, FreshEntitiesException, ReasonerInterruptedException, TimeOutException {
        return null;
    }

    public NodeSet<OWLObjectPropertyExpression> getSuperObjectProperties(OWLObjectPropertyExpression propertyExpression, boolean b) throws InconsistentOntologyException, FreshEntitiesException, ReasonerInterruptedException, TimeOutException {
        return null;
    }

    public Node<OWLObjectPropertyExpression> getEquivalentObjectProperties(OWLObjectPropertyExpression propertyExpression) throws InconsistentOntologyException, FreshEntitiesException, ReasonerInterruptedException, TimeOutException {
        return null;
    }

    public NodeSet<OWLObjectPropertyExpression> getDisjointObjectProperties(OWLObjectPropertyExpression propertyExpression) throws InconsistentOntologyException, FreshEntitiesException, ReasonerInterruptedException, TimeOutException {
        return null;
    }

    public Node<OWLObjectPropertyExpression> getInverseObjectProperties(OWLObjectPropertyExpression propertyExpression) throws InconsistentOntologyException, FreshEntitiesException, ReasonerInterruptedException, TimeOutException {
        return null;
    }

    public NodeSet<OWLClass> getObjectPropertyDomains(OWLObjectPropertyExpression propertyExpression, boolean b) throws InconsistentOntologyException, FreshEntitiesException, ReasonerInterruptedException, TimeOutException {
        return null;
    }

    public NodeSet<OWLClass> getObjectPropertyRanges(OWLObjectPropertyExpression propertyExpression, boolean b) throws InconsistentOntologyException, FreshEntitiesException, ReasonerInterruptedException, TimeOutException {
        return null;
    }

    public Node<OWLDataProperty> getTopDataPropertyNode() {
        return OWLDataPropertyNode.getTopNode();
    }

    public Node<OWLDataProperty> getBottomDataPropertyNode() {
        return OWLDataPropertyNode.getBottomNode();
    }

    public NodeSet<OWLDataProperty> getSubDataProperties(OWLDataProperty owlDataProperty, boolean b) throws InconsistentOntologyException, FreshEntitiesException, ReasonerInterruptedException, TimeOutException {
        return null;
    }

    public NodeSet<OWLDataProperty> getSuperDataProperties(OWLDataProperty owlDataProperty, boolean b) throws InconsistentOntologyException, FreshEntitiesException, ReasonerInterruptedException, TimeOutException {
        return null;
    }

    public Node<OWLDataProperty> getEquivalentDataProperties(OWLDataProperty owlDataProperty) throws InconsistentOntologyException, FreshEntitiesException, ReasonerInterruptedException, TimeOutException {
        return null;
    }

    public NodeSet<OWLDataProperty> getDisjointDataProperties(OWLDataPropertyExpression owlDataPropertyExpression) throws InconsistentOntologyException, FreshEntitiesException, ReasonerInterruptedException, TimeOutException {
        return null;
    }

    public NodeSet<OWLClass> getDataPropertyDomains(OWLDataProperty owlDataProperty, boolean b) throws InconsistentOntologyException, FreshEntitiesException, ReasonerInterruptedException, TimeOutException {
        return null;
    }

    public NodeSet<OWLClass> getTypes(OWLNamedIndividual individual, boolean b) throws InconsistentOntologyException, FreshEntitiesException, ReasonerInterruptedException, TimeOutException {

        return null;
    }

    public NodeSet<OWLNamedIndividual> getInstances(OWLClassExpression owlClassExpression, boolean b) throws InconsistentOntologyException, ClassExpressionNotInProfileException, FreshEntitiesException, ReasonerInterruptedException, TimeOutException {
        return null;
    }

    public NodeSet<OWLNamedIndividual> getObjectPropertyValues(OWLNamedIndividual individual, OWLObjectPropertyExpression propertyExpression) throws InconsistentOntologyException, FreshEntitiesException, ReasonerInterruptedException, TimeOutException {
        OWLNamedIndividualNodeSet ns = new OWLNamedIndividualNodeSet();
        for (OWLOntology ontology : getRootOntology().getImportsClosure()) {
            for (OWLObjectPropertyAssertionAxiom ax : ontology.getObjectPropertyAssertionAxioms(individual)) {
                if (ax.getProperty().equals(propertyExpression)) {
                    OWLIndividual object = ax.getObject();
                    if (!object.isAnonymous()) {
                        ns.addEntity(object.asOWLNamedIndividual());
                    }
                }
            }
        }
        return ns;
    }

    public Set<OWLLiteral> getDataPropertyValues(OWLNamedIndividual individual, OWLDataProperty owlDataProperty) throws InconsistentOntologyException, FreshEntitiesException, ReasonerInterruptedException, TimeOutException {
        List<OWLLiteral> tempResult = new ArrayList<OWLLiteral>();
        for (OWLOntology ontology : getRootOntology().getImportsClosure()) {
            for (OWLDataPropertyAssertionAxiom ax : ontology.getDataPropertyAssertionAxioms(individual)) {
                if (ax.getProperty().equals(owlDataProperty)) {
                    OWLLiteral object = ax.getObject();
                    tempResult.add(object);
                }
            }
        }
        return new HashSet<OWLLiteral>(tempResult);
    }

    public Node<OWLNamedIndividual> getSameIndividuals(OWLNamedIndividual individual) throws InconsistentOntologyException, FreshEntitiesException, ReasonerInterruptedException, TimeOutException {
        OWLNamedIndividualNode node = new OWLNamedIndividualNode();
        for (OWLOntology ontology : getRootOntology().getImportsClosure()) {
            for (OWLSameIndividualAxiom ax : ontology.getSameIndividualAxioms(individual)) {
                for (OWLIndividual ind : ax.getIndividuals()) {
                    if (!ind.isAnonymous()) {
                        node.add(ind.asOWLNamedIndividual());
                    }
                }
            }
        }
        return node;
    }

    public NodeSet<OWLNamedIndividual> getDifferentIndividuals(OWLNamedIndividual individual) throws InconsistentOntologyException, FreshEntitiesException, ReasonerInterruptedException, TimeOutException {
        OWLNamedIndividualNodeSet ns = new OWLNamedIndividualNodeSet();
        for (OWLOntology ontology : getRootOntology().getImportsClosure()) {
            for (OWLDifferentIndividualsAxiom ax : ontology.getDifferentIndividualAxioms(individual)) {
                for (OWLIndividual ind : ax.getIndividuals()) {
                    if (!ind.isAnonymous() && !ind.equals(individual)) {
                        ns.addEntity(ind.asOWLNamedIndividual());
                    }
                }
            }
        }
        return ns;
    }

    public long getTimeOut() {
        return 0;
    }

    public FreshEntityPolicy getFreshEntityPolicy() {
        return FreshEntityPolicy.ALLOW;
    }

    public IndividualNodeSetPolicy getIndividualNodeSetPolicy() {
        return IndividualNodeSetPolicy.BY_NAME;
    }

    public void dispose() {
    }
}
