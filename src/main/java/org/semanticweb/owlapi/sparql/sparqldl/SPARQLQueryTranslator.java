/*
 * This file is part of the OWL API.
 *
 * The contents of this file are subject to the LGPL License, Version 3.0.
 *
 * Copyright (C) 2011, The University of Manchester
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 *
 *
 * Alternatively, the contents of this file may be used under the terms of the Apache License, Version 2.0
 * in which case, the provisions of the Apache License Version 2.0 are applicable instead of those above.
 *
 * Copyright 2011, The University of Manchester
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.semanticweb.owlapi.sparql.sparqldl;


import org.semanticweb.owlapi.sparql.syntax.SelectQuery;

/**
 * Author: Matthew Horridge<br> Stanford University<br> Bio-Medical Informatics Research Group<br> Date: 27/03/2012
 */
public class SPARQLQueryTranslator {

    private SelectQuery query;

    public SPARQLQueryTranslator(SelectQuery query) {
        this.query = query;
    }

//    public Query translate() {
//        List<TriplesBlockPattern> graphPatterns = this.query.getGroupPattern().getTriplesBlockPatterns();
//        return translateInternal(graphPatterns);
//    }

//    private Query translateInternal(List<TriplesBlockPattern> graphPatterns) {
//        QueryType select = query.getSelectClause().isSelectDistinct() ? QueryType.SELECT_DISTINCT : QueryType.SELECT;
//        QueryImpl query = new QueryImpl(select);
//        Set<Variable> variables = new HashSet<>();
//        variables.addAll(this.query.getGroupPattern().getVariables());
//        for (Variable v : variables) {
//            query.addResultVar(new QueryArgument(QueryArgumentType.VAR, v.getName()));
//        }
//        AxiomTemplateVisitor axiomTemplateVisitor = new AxiomTemplateVisitor();
//        for (TriplesBlockPattern gp : graphPatterns) {
//            QueryAtomGroupImpl queryAtomGroup = new QueryAtomGroupImpl();
//            for (Axiom ax : gp.getAxioms()) {
//                QueryAtom atom = ax.accept(axiomTemplateVisitor);
//                queryAtomGroup.addAtom(atom);
//            }
//            query.addAtomGroup(queryAtomGroup);
//        }
//        return query;
//    }
//
//    public Query translateMinus() {
//        return translateInternal(query.getMinusPatterns());
//    }
//
//    public Query translateOptional() {
//        return translateInternal(query.getOptionalPatterns());
//    }

//    private QueryAtom getAtom(QueryAtomType type, Visitable... args) {
//        return getAtom(type, Arrays.asList(args));
//    }
//
//    private QueryAtom getAtom(QueryAtomType type, Collection<? extends Visitable> args) {
//        ArgumentVisitor argumentVisitor = new ArgumentVisitor();
//        List<QueryArgument> queryArguments = new ArrayList<QueryArgument>();
//        for (Visitable arg : args) {
//            QueryArgument queryArgument = arg.accept(argumentVisitor);
//            if (queryArgument != null) {
//                queryArguments.add(queryArgument);
//            } else {
//                throw new IllegalArgumentException("Cannot translate: " + arg + " into a query atom argument");
//            }
//        }
//        return new QueryAtom(type, queryArguments);
//    }
//
//
//    private class AxiomTemplateVisitor implements AxiomVisitor<QueryAtom, RuntimeException> {
//
//        public QueryAtom visit(SubClassOf axiom) {
//            return getAtom(QueryAtomType.SUB_CLASS_OF, axiom.getSubClass(), axiom.getSuperClass());
//        }
//
//        public QueryAtom visit(AsymmetricObjectProperty axiom) {
//            throw new UnsupportedOperationException("Asymmetric object property queries are not supported");
//        }
//
//        public QueryAtom visit(ReflexiveObjectProperty axiom) {
//            throw new UnsupportedOperationException("Reflexive object property queries are not supported");
//        }
//
//        public QueryAtom visit(DisjointClasses axiom) {
//            return getAtom(QueryAtomType.DISJOINT_WITH, axiom.getClassExpressions());
//        }
//
//        public QueryAtom visit(DataPropertyDomain axiom) {
//            return getAtom(QueryAtomType.DOMAIN, axiom.getProperty(), axiom.getDomain());
//        }
//
//        public QueryAtom visit(ObjectPropertyDomain axiom) {
//            return getAtom(QueryAtomType.DOMAIN, axiom.getProperty(), axiom.getDomain());
//        }
//
//        public QueryAtom visit(EquivalentObjectProperties axiom) {
//            return getAtom(QueryAtomType.EQUIVALENT_PROPERTY, axiom.getObjectPropertyExpressions());
//        }
//
//        public QueryAtom visit(DifferentIndividuals axiom) {
//            return getAtom(QueryAtomType.DIFFERENT_FROM, axiom.getIndividuals());
//        }
//
//        public QueryAtom visit(DisjointDataProperties axiom) {
//            return getAtom(QueryAtomType.DISJOINT_WITH, axiom.getDataProperties());
//        }
//
//        public QueryAtom visit(DisjointObjectProperties axiom) {
//            return getAtom(QueryAtomType.DISJOINT_WITH, axiom.getObjectPropertyExpressions());
//        }
//
//        public QueryAtom visit(ObjectPropertyRange axiom) {
//            return getAtom(QueryAtomType.RANGE, axiom.getProperty(), axiom.getRange());
//        }
//
//        public QueryAtom visit(ObjectPropertyAssertion axiom) {
//            return getAtom(QueryAtomType.PROPERTY_VALUE, axiom.getSubject(), axiom.getProperty(), axiom.getObject());
//        }
//
//        public QueryAtom visit(FunctionalObjectProperty axiom) {
//            return getAtom(QueryAtomType.FUNCTIONAL, axiom.getProperty());
//        }
//
//        public QueryAtom visit(SubObjectPropertyOf axiom) {
//            return getAtom(QueryAtomType.SUB_PROPERTY_OF, axiom.getSubProperty(), axiom.getSuperProperty());
//        }
//
//        public QueryAtom visit(Declaration axiom) {
//            return axiom.getAtomic().accept(new VisitorAdapter<QueryAtom, RuntimeException>() {
//                public QueryAtom visit(NamedClass cls) {
//                    return getAtom(QueryAtomType.CLASS, cls);
//                }
//
//                public QueryAtom visit(ObjectProperty property) {
//                    return getAtom(QueryAtomType.OBJECT_PROPERTY, property);
//                }
//
//                public QueryAtom visit(DataProperty property) {
//                    return getAtom(QueryAtomType.DATA_PROPERTY, property);
//                }
//
//                public QueryAtom visit(NamedIndividual individual) {
//                    return getAtom(QueryAtomType.INDIVIDUAL, individual);
//                }
//
//                public QueryAtom visit(Datatype datatype) {
//                    throw new UnsupportedOperationException("Datatype queries are not supported");
//                }
//
//                public QueryAtom visit(AnnotationProperty property) {
//                    return getAtom(QueryAtomType.ANNOTATION, property);
//                }
//
//                @Override
//                public QueryAtom visit(ClassVariable variable) throws RuntimeException {
//                    return getAtom(QueryAtomType.CLASS, variable);
//                }
//
//                @Override
//                public QueryAtom visit(DatatypeVariable variable) throws RuntimeException {
//                    throw new RuntimeException("Datatype queries are not supported");
//                }
//
//                @Override
//                public QueryAtom visit(ObjectPropertyVariable variable) throws RuntimeException {
//                    return getAtom(QueryAtomType.OBJECT_PROPERTY, variable);
//                }
//
//                @Override
//                public QueryAtom visit(DataPropertyVariable variable) throws RuntimeException {
//                    return getAtom(QueryAtomType.DATA_PROPERTY, variable);
//                }
//
//                @Override
//                public QueryAtom visit(AnnotationPropertyVariable variable) throws RuntimeException {
//                    return getAtom(QueryAtomType.ANNOTATION, variable);
//                }
//
//                @Override
//                public QueryAtom visit(IndividualVariable variable) throws RuntimeException {
//                    return getAtom(QueryAtomType.INDIVIDUAL, variable);
//                }
//
//                @Override
//                public QueryAtom visit(LiteralVariable variable) throws RuntimeException {
//                    return getAtom(QueryAtomType.UKNOWN, variable);
//                }
//
//                @Override
//                public QueryAtom visit(UntypedVariable variable) throws RuntimeException {
//                    return getAtom(QueryAtomType.UKNOWN, variable);
//                }
//            });
//        }
//
//        public QueryAtom visit(AnnotationAssertion axiom) {
//            return getAtom(QueryAtomType.ANNOTATION, axiom.getSubject(), axiom.getProperty(), axiom.getObject());
//        }
//
//        public QueryAtom visit(SymmetricObjectProperty axiom) {
//            return getAtom(QueryAtomType.SYMMETRIC, axiom.getProperty());
//        }
//
//        public QueryAtom visit(DataPropertyRange axiom) {
//            return getAtom(QueryAtomType.RANGE, axiom.getProperty());
//        }
//
//        public QueryAtom visit(FunctionalDataProperty axiom) {
//            return getAtom(QueryAtomType.FUNCTIONAL, axiom.getProperty());
//        }
//
//        public QueryAtom visit(EquivalentDataProperties axiom) {
//            return getAtom(QueryAtomType.EQUIVALENT_PROPERTY, axiom.getDataProperties());
//        }
//
//        public QueryAtom visit(ClassAssertion axiom) {
//            return getAtom(QueryAtomType.TYPE, axiom.getIndividual(), axiom.getClassExpression());
//        }
//
//        public QueryAtom visit(EquivalentClasses axiom) {
//            return getAtom(QueryAtomType.EQUIVALENT_CLASS, axiom.getClassExpressions());
//        }
//
//        public QueryAtom visit(DataPropertyAssertion axiom) {
//            return getAtom(QueryAtomType.PROPERTY_VALUE, axiom.getSubject(), axiom.getProperty(), axiom.getObject());
//        }
//
//        public QueryAtom visit(TransitiveObjectProperty axiom) {
//            return getAtom(QueryAtomType.TRANSITIVE, axiom.getProperty());
//        }
//
//        public QueryAtom visit(IrreflexiveObjectProperty axiom) {
//            return getAtom(QueryAtomType.IRREFLEXIVE, axiom.getProperty());
//        }
//
//        public QueryAtom visit(SubDataPropertyOf axiom) {
//            return getAtom(QueryAtomType.SUB_PROPERTY_OF, axiom.getSubProperty(), axiom.getSuperProperty());
//        }
//
//        public QueryAtom visit(InverseFunctionalObjectProperty axiom) {
//            return getAtom(QueryAtomType.INVERSE_FUNCTIONAL, axiom.getProperty());
//        }
//
//        public QueryAtom visit(SameIndividual axiom) {
//            return getAtom(QueryAtomType.SAME_AS, axiom.getIndividuals());
//        }
//
//        public QueryAtom visit(InverseObjectProperties axiom) {
//            return getAtom(QueryAtomType.INVERSE_OF, axiom.getObjectPropertyExpressions());
//        }
//
//        public QueryAtom visit(SubAnnotationPropertyOf axiom) {
//            return getAtom(QueryAtomType.SUB_PROPERTY_OF, axiom.getSubProperty(), axiom.getSuperProperty());
//        }
//
//        public QueryAtom visit(AnnotationPropertyDomain axiom) {
//            return getAtom(QueryAtomType.DOMAIN, axiom.getProperty(), axiom.getDomain());
//        }
//
//        public QueryAtom visit(AnnotationPropertyRange axiom) {
//            return getAtom(QueryAtomType.RANGE, axiom.getProperty(), axiom.getRange());
//        }
//
//    }

//    private class ArgumentVisitor extends VisitorAdapter<QueryArgument, RuntimeException> {
//
//
//        public QueryArgument visit(AnonymousIndividual individual) {
//            return new QueryArgument(QueryArgumentType.BNODE, individual.getIdentifier());
//        }
//
//        public QueryArgument visit(NamedClass node) throws RuntimeException {
//            return new QueryArgument(QueryArgumentType.URI, node.getIRI().toString());
//        }
//
//        public QueryArgument visit(AtomicIRI iri) throws RuntimeException {
//            return new QueryArgument(QueryArgumentType.URI, iri.getIRI().toString());
//        }
//
//        public QueryArgument visit(DataProperty property) throws RuntimeException {
//            return new QueryArgument(QueryArgumentType.URI, property.getIRI().toString());
//        }
//
//        public QueryArgument visit(NamedIndividual individual) throws RuntimeException {
//            return new QueryArgument(QueryArgumentType.URI, individual.getIRI().toString());
//        }
//
//        public QueryArgument visit(AnnotationProperty property) throws RuntimeException {
//            return new QueryArgument(QueryArgumentType.URI, property.getIRI().toString());
//        }
//
//        public QueryArgument visit(ObjectProperty property) throws RuntimeException {
//            return new QueryArgument(QueryArgumentType.URI, property.getIRI().toString());
//        }
//
//        public QueryArgument visit(ClassVariable variable) throws RuntimeException {
//            return new QueryArgument(QueryArgumentType.VAR, variable.getName());
//        }
//
//        public QueryArgument visit(DatatypeVariable variable) throws RuntimeException {
//            return new QueryArgument(QueryArgumentType.VAR, variable.getName());
//        }
//
//        public QueryArgument visit(ObjectPropertyVariable variable) throws RuntimeException {
//            return new QueryArgument(QueryArgumentType.VAR, variable.getName());
//        }
//
//        public QueryArgument visit(DataPropertyVariable variable) throws RuntimeException {
//            return new QueryArgument(QueryArgumentType.VAR, variable.getName());
//        }
//
//        public QueryArgument visit(AnnotationPropertyVariable variable) throws RuntimeException {
//            return new QueryArgument(QueryArgumentType.VAR, variable.getName());
//        }
//
//        public QueryArgument visit(IndividualVariable variable) throws RuntimeException {
//            return new QueryArgument(QueryArgumentType.VAR, variable.getName());
//        }
//
//        public QueryArgument visit(LiteralVariable variable) throws RuntimeException {
//            return new QueryArgument(QueryArgumentType.VAR, variable.getName());
//        }
//
//        public QueryArgument visit(UntypedVariable variable) throws RuntimeException {
//            return new QueryArgument(QueryArgumentType.VAR, variable.getName());
//        }
//
//        public QueryArgument visit(Literal node) throws RuntimeException {
//            return LiteralTranslator.toQueryArgument(node.getLexicalForm(), node.getLang(), node.getDatatype().getIRI().toString());
//        }
//    }

}

