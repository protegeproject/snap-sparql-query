package org.semanticweb.owlapi.sparql.parser;

import com.google.common.collect.ImmutableList;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.sparql.api.*;
import org.semanticweb.owlapi.sparql.api.PrimitiveType;
import org.semanticweb.owlapi.sparql.builtin.BuiltInCall;
import org.semanticweb.owlapi.sparql.builtin.ArgList;
import org.semanticweb.owlapi.sparql.builtin.VarArg;
import org.semanticweb.owlapi.sparql.parser.tokenizer.*;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

import java.math.BigDecimal;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.semanticweb.owlapi.vocab.OWLRDFVocabulary.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/03/2012
 */
public class SPARQLParserImpl {

    private SPARQLTokenizer tokenizer;

    private List<String> mustBindVariables = new ArrayList<>();

    private Set<SPARQLGraphPattern> parsedBGPs = new HashSet<>();

    private Set<SPARQLGraphPattern> minusBGPs = new HashSet<>();

    private SPARQLQueryType queryType = SPARQLQueryType.SELECT;

    private List<SelectAs> selectAsList = new ArrayList<>();

    private boolean selectAll = false;

    private List<OrderCondition> orderConditions = new ArrayList<>();

    public SPARQLParserImpl(SPARQLTokenizer tokenizer) {
        this.tokenizer = checkNotNull(tokenizer);
    }

    public SPARQLQuery parseQuery() {
        parsePrologue();
        parseSelectQuery();
        return createQueryObject();
    }

    private SPARQLQuery createQueryObject() {
        List<Variable> selectVariables = new ArrayList<>();
        for (String varName : mustBindVariables) {
            Collection<VariableTokenType> types = tokenizer.getVariableManager().getTypes(varName);
            TokenType tokenType = types.iterator().next();
            if (tokenType instanceof DeclaredVariableTokenType) {
                DeclaredVariableTokenType declaredVariableTokenType = (DeclaredVariableTokenType) tokenType;
                selectVariables.add(Variable.create(varName, declaredVariableTokenType.getPrimitiveType()));
            }
            else {
                selectVariables.add(new UntypedVariable(varName));
            }
        }
        List<SPARQLGraphPattern> graphPatterns = new ArrayList<>();
        List<Variable> allVariables = new ArrayList<>();
        allVariables.addAll(tokenizer.getVariableManager().getVariables());

        for (SPARQLGraphPattern bgp : parsedBGPs) {
            allVariables.addAll(bgp.getTriplePatternVariablesVariables());
            graphPatterns.add(bgp);
            if (selectAll) {
                selectVariables.addAll(bgp.getTriplePatternVariablesVariables());
            }
        }

        SolutionModifier solutionModifier = new SolutionModifier(orderConditions);
        return new SPARQLQuery(tokenizer.getPrefixManager(), queryType, selectVariables, allVariables, selectAsList, graphPatterns, new ArrayList<>(minusBGPs), solutionModifier);
    }


    public void parsePrologue() {
        while (true) {
            if (tokenizer.peek(SPARQLTerminal.PREFIX) != null) {
                parsePrefixDeclaration();
            }
            else if (tokenizer.peek(SPARQLTerminal.BASE) != null) {
                parseBaseDeclaration();
            }
            else if (tokenizer.peek(SPARQLTerminal.SELECT) != null) {
                break;
            }
            else {
                break;
            }
        }
    }

    public void parseSelectQuery() {
        parseSelectClause();
        parseWhereClause();
        parseSolutionModifier();
        tokenizer.consume(EOFTokenType.get());
    }

    public void parseSelectClause() {
        tokenizer.consume(SPARQLTerminal.SELECT);
        if (tokenizer.peek(SPARQLTerminal.DISTINCT) != null) {
            tokenizer.consume(SPARQLTerminal.DISTINCT);
            queryType = SPARQLQueryType.SELECT_DISTINCT;
        }
        if (tokenizer.peek(SPARQLTerminal.ASTERISK) != null) {
            tokenizer.consume(SPARQLTerminal.ASTERISK);
            selectAll = true;
        }
        else if (tokenizer.peek(UndeclaredVariableTokenType.get()) != null || tokenizer.peek(SPARQLTerminal.OPEN_PAR) != null) {
            while (true) {
                parseSelectVariableOrExpressionAsVariable();
                if (tokenizer.peek(SPARQLTerminal.WHERE) != null) {
                    break;
                }
                if (tokenizer.peek(UndeclaredVariableTokenType.get()) == null && tokenizer.peek(SPARQLTerminal.OPEN_PAR) == null) {
                    break;
                }
            }
        }
        else {
            tokenizer.raiseError();
        }
    }

    public void parseSelectVariableOrExpressionAsVariable() {
        if (tokenizer.peek(SPARQLTerminal.OPEN_PAR) != null) {
            parseSelectExpressionAsVariable();
        }
        else {
            parseSelectVariable();
        }
    }

    public void parseSelectVariable() {
        SPARQLToken token = tokenizer.consume(UndeclaredVariableTokenType.get());
        mustBindVariables.add(token.getImage());

    }

    public void parseSelectExpressionAsVariable() {
        tokenizer.consume(SPARQLTerminal.OPEN_PAR);
        Expression expression = parseExpression();
        tokenizer.consume(SPARQLTerminal.AS);
        SPARQLToken token = tokenizer.consume(UndeclaredVariableTokenType.get());
        tokenizer.getVariableManager().addVariableName(token.getImage());
        selectAsList.add(new SelectAs(expression, new UntypedVariable(token.getImage())));
        tokenizer.consume(SPARQLTerminal.CLOSE_PAR);
    }


    public void parseWhereClause() {
        tokenizer.consume(SPARQLTerminal.WHERE);

        while (true) {
            SPARQLGraphPattern pattern = parseGroupGraphPattern();

            parsedBGPs.add(pattern);

            if (tokenizer.peek(SPARQLTerminal.UNION) != null) {
                tokenizer.consume();
            }
            else {
                break;
            }
        }

    }

    private SPARQLGraphPattern parseGroupGraphPattern() {
        tokenizer.consume(SPARQLTerminal.OPEN_BRACE);
        SPARQLGraphPattern currentPattern = new SPARQLGraphPattern();
        parseTriplesBlock(currentPattern);
        boolean parsedGraphPatternNotTriples = true;
        while (parsedGraphPatternNotTriples) {
            parsedGraphPatternNotTriples = parseGraphPatternNotTriples(currentPattern);
        }
        tokenizer.consume(SPARQLTerminal.CLOSE_BRACE);
        return currentPattern;
    }

    public void parseSolutionModifier() {
        if (tokenizer.peek(SPARQLTerminal.ORDER) != null) {
            parseOrderByClause();
        }
    }

    public void parseOrderByClause() {
        tokenizer.consume(SPARQLTerminal.ORDER);
        tokenizer.consume(SPARQLTerminal.BY);
        while (true) {
            if (tokenizer.peek(SPARQLTerminal.ASC) != null) {
                tokenizer.consume(SPARQLTerminal.ASC);
                tokenizer.consume(SPARQLTerminal.OPEN_PAR);
                if (peekTypedOrUntypedVariable() != null) {
                    SPARQLToken varToken = tokenizer.consume();
                    orderConditions.add(new OrderCondition(varToken.getImage(), OrderByModifier.ASC));
                }
                else {
                    tokenizer.raiseError();
                }
                tokenizer.consume(SPARQLTerminal.CLOSE_PAR);
            }
            else if (tokenizer.peek(SPARQLTerminal.DESC) != null) {
                tokenizer.consume(SPARQLTerminal.DESC);
                tokenizer.consume(SPARQLTerminal.OPEN_PAR);
                if (peekTypedOrUntypedVariable() != null) {
                    SPARQLToken varToken = tokenizer.consume();
                    orderConditions.add(new OrderCondition(varToken.getImage(), OrderByModifier.DESC));
                }
                else {
                    tokenizer.raiseError();
                }
                tokenizer.consume(SPARQLTerminal.CLOSE_PAR);
            }
            else if (peekTypedOrUntypedVariable() != null) {
                SPARQLToken varToken = tokenizer.consume();
                orderConditions.add(new OrderCondition(varToken.getImage()));
            }
            else {
                tokenizer.raiseError();
            }
            if (tokenizer.peek(SPARQLTerminal.ASC) == null && tokenizer.peek(SPARQLTerminal.DESC) == null && peekTypedOrUntypedVariable() == null) {
                break;
            }
        }
    }

    private SPARQLToken peekTypedOrUntypedVariable() {
        return tokenizer.peek(UndeclaredVariableTokenType.get(), DeclaredVariableTokenType.get(PrimitiveType.CLASS), DeclaredVariableTokenType.get(PrimitiveType.OBJECT_PROPERTY), DeclaredVariableTokenType.get(PrimitiveType.DATA_PROPERTY), DeclaredVariableTokenType.get(PrimitiveType.DATATYPE), DeclaredVariableTokenType.get(PrimitiveType.ANNOTATION_PROPERTY), DeclaredVariableTokenType.get(PrimitiveType.NAMED_INDIVIDUAL));
    }

    public boolean parseTriplesBlock(SPARQLGraphPattern currentPattern) {
        int parsed = 0;
        while (true) {
            if (tokenizer.peek(UndeclaredVariableTokenType.get()) != null) {
                SPARQLToken varNameToken = tokenizer.consume();
                parseUndeclaredVariablePropertyList(varNameToken, currentPattern);
                parsed++;
            }
            else if (tokenizer.peek(DeclaredVariableTokenType.get(PrimitiveType.CLASS)) != null || tokenizer.peek(ClassIRITokenType.get()) != null) {
                SPARQLToken subjectToken = tokenizer.consume();
                parseClassNodePropertyList(subjectToken, currentPattern);
                parsed++;
            }
            else if (tokenizer.peek(DeclaredVariableTokenType.get(PrimitiveType.OBJECT_PROPERTY)) != null || tokenizer.peek(ObjectPropertyIRITokenType.get()) != null) {
                SPARQLToken subjectToken = tokenizer.consume();
                parseObjectPropertyNodePropertyList(subjectToken, currentPattern);
                parsed++;
            }
            else if (tokenizer.peek(DeclaredVariableTokenType.get(PrimitiveType.DATA_PROPERTY)) != null || tokenizer.peek(DataPropertyIRITokenType.get()) != null) {
                SPARQLToken subjectToken = tokenizer.consume();
                parseDataPropertyNodePropertyList(subjectToken, currentPattern);
                parsed++;
            }
            else if (tokenizer.peek(DeclaredVariableTokenType.get(PrimitiveType.NAMED_INDIVIDUAL)) != null || tokenizer.peek(IndividualIRITokenType.get()) != null) {
                SPARQLToken subjectToken = tokenizer.consume();
                parseIndividualNodePropertyList(subjectToken, currentPattern);
                parsed++;
            }

            // Carry on if we see a dot because we expect further triples
            if (tokenizer.peek(SPARQLTerminal.DOT) != null) {
                tokenizer.consume(SPARQLTerminal.DOT);
            }
            else {
                break;
            }
        }
        return parsed > 0;
    }

    public boolean parseGraphPatternNotTriples(SPARQLGraphPattern currentPattern) {
        boolean parsed = false;
        if (tokenizer.peek(SPARQLTerminal.FILTER) != null) {
            parseFilterCondition(currentPattern);
            parsed = true;
        }
        else if (tokenizer.peek(SPARQLTerminal.BIND) != null) {
            parseBind(currentPattern);
            parsed = true;
        }
        else if (tokenizer.peek(SPARQLTerminal.MINUS_KW) != null) {
            parseMinusGraphPattern();
            parsed = true;
        }
        else {
            parsed = parseTriplesBlock(currentPattern);
        }
        if(tokenizer.peek(SPARQLTerminal.DOT) != null) {
            tokenizer.consume(SPARQLTerminal.DOT);
        }
        return parsed;
    }

    private void parseMinusGraphPattern() {
        tokenizer.consume(SPARQLTerminal.MINUS_KW);
        SPARQLGraphPattern minusPattern = parseGroupGraphPattern();
        minusBGPs.add(minusPattern);
    }

    private void parseFilterCondition(SPARQLGraphPattern currentPattern) {
        tokenizer.consume(SPARQLTerminal.FILTER);
        Expression expression = parseConstraint();
        currentPattern.addFilter(expression);
    }


    private Expression parseConstraint() {
        Expression expression = null;
        if (tokenizer.peek(SPARQLTerminal.OPEN_PAR) != null) {
            expression = parseBracketedExpression();
        }
        else if (tokenizer.peek(BuiltInCallTokenType.get()) != null) {
            expression = parseBuiltInCall();
        }
        else {
            tokenizer.raiseError();
        }
        System.out.println(expression);
        return expression;
    }

    private void parseBind(SPARQLGraphPattern currentPattern) {
        tokenizer.consume(SPARQLTerminal.BIND);
        tokenizer.consume(SPARQLTerminal.OPEN_PAR);
        Expression expression = parseConstraint();
        tokenizer.consume(SPARQLTerminal.AS);
        Variable variable = parseVariable();
        tokenizer.consume(SPARQLTerminal.CLOSE_PAR);
        currentPattern.addBind(new Bind(expression, variable));
    }


    /**
     * Parses a node that is an individual IRI or an individual variable. Possible predicates are
     * owl:sameAs, owl:differentFrom, rdf:type, an object property IRI, object property typed variable, data property
     * IRI, data property typed variable, annotation property IRI, annotation property variable
     * @param subjectToken The node
     */
    private void parseIndividualNodePropertyList(SPARQLToken subjectToken, SPARQLGraphPattern currentPattern) {
        while (true) {
            if (tokenizer.peek(OWL_SAME_AS) != null) {
                parseSameAs(subjectToken, currentPattern);
            }
            else if (tokenizer.peek(OWL_DIFFERENT_FROM) != null) {
                parseDifferentFrom(subjectToken, currentPattern);
            }
            else if (tokenizer.peek(RDF_TYPE) != null) {
                parseIndividualType(subjectToken, currentPattern);
            }
            else if (tokenizer.peek(ObjectPropertyIRITokenType.get()) != null || tokenizer.peek(DeclaredVariableTokenType.get(PrimitiveType.OBJECT_PROPERTY)) != null) {
                parseObjectPropertyAssertion(subjectToken, currentPattern);
            }
            else if (tokenizer.peek(DataPropertyIRITokenType.get()) != null || tokenizer.peek(DeclaredVariableTokenType.get(PrimitiveType.DATA_PROPERTY)) != null) {
                parseDataPropertyAssertions(subjectToken, currentPattern);
            }
            else if (tokenizer.peek(AnnotationPropertyIRITokenType.get()) != null || tokenizer.peek(DeclaredVariableTokenType.get(PrimitiveType.ANNOTATION_PROPERTY)) != null) {
                parseAnnotationAssertions(subjectToken, currentPattern);
            }
            else if (tokenizer.peek(UndeclaredVariableTokenType.get()) != null) {
                SPARQLToken propertyToken = tokenizer.consume();
                // Object Property, Data Property or Annotation Property?
                if (tokenizer.peek(UntypedIRITokenType.get()) != null) {
                    HasAnnotationSubject subject = getAtomicIndividualFromToken(subjectToken);
                    AtomicAnnotationProperty property = getAnnotationPropertyFromToken(propertyToken);
                    tokenizer.registerVariable(propertyToken.getImage(), PrimitiveType.ANNOTATION_PROPERTY);
                    for (AnnotationValue value : parseAnnotationValueObjectList()) {
                        currentPattern.add(new AnnotationAssertion(property, subject.toAnnotationSubject(), value));
                    }
                }
                else if (tokenizer.peek(IndividualIRITokenType.get(), DeclaredVariableTokenType.get(PrimitiveType.NAMED_INDIVIDUAL)) != null) {
                    AtomicIndividual subject = getAtomicIndividualFromToken(subjectToken);
                    AtomicObjectProperty property = getObjectPropertyFromToken(propertyToken);
                    tokenizer.registerVariable(propertyToken.getImage(), PrimitiveType.OBJECT_PROPERTY);
                    for (AtomicIndividual ind : parseIndividualNodeObjectList()) {
                        currentPattern.add(new ObjectPropertyAssertion(property, subject, ind));
                    }
                }
                else {
                    tokenizer.raiseError();
                }
            }
            else {
                tokenizer.raiseError();
            }
            if (tokenizer.peek(SPARQLTerminal.SEMI_COLON) != null) {
                tokenizer.consume();
            }
            else {
                break;
            }
        }
    }

    private void parseIndividualType(SPARQLToken subjectToken, SPARQLGraphPattern currentPattern) {
        tokenizer.consume();
        AtomicIndividual individual = getAtomicIndividualFromToken(subjectToken);
        List<AtomicClass> types = parseClassNodeObjectList();
        for (AtomicClass type : types) {
            currentPattern.add(new ClassAssertion(type, individual));
        }
    }

    private void parseDataPropertyNodePropertyList(SPARQLToken subjectToken, SPARQLGraphPattern currentPattern) {
        while (true) {

            AtomicDataProperty subject = getDataPropertyFromToken(subjectToken);
            if (tokenizer.peek(OWLRDFVocabulary.RDFS_SUB_PROPERTY_OF) != null) {
                tokenizer.consume();
                List<AtomicDataProperty> properties = parseDataPropertyNodeObjectList();
                for (AtomicDataProperty property : properties) {
                    currentPattern.add(new SubDataPropertyOf(subject, property));
                }
            }
            else if (tokenizer.peek(OWL_EQUIVALENT_PROPERTY) != null) {
                tokenizer.consume();
                List<AtomicDataProperty> properties = parseDataPropertyNodeObjectList();
                for (AtomicDataProperty property : properties) {
                    currentPattern.add(new EquivalentDataProperties(subject, property));
                }
            }
            else if (tokenizer.peek(OWL_DISJOINT_WITH) != null) {
                tokenizer.consume();
                List<AtomicDataProperty> properties = parseDataPropertyNodeObjectList();
                for (AtomicDataProperty property : properties) {
                    currentPattern.add(new DisjointDataProperties(subject, property));
                }
            }
            else if (tokenizer.peek(RDFS_DOMAIN) != null) {
                tokenizer.consume();
                List<AtomicClass> clses = parseClassNodeObjectList();
                for (AtomicClass cls : clses) {
                    currentPattern.add(new DataPropertyDomain(subject, cls));
                }
            }
            else if (tokenizer.peek(RDFS_RANGE) != null) {
                tokenizer.consume();
                List<AtomicDatatype> dataTypes = parseDataTypeNodeObjectList();
                for (AtomicDatatype datatype : dataTypes) {
                    currentPattern.add(new DataPropertyRange(subject, datatype));
                }
            }
            else if (tokenizer.peek(RDF_TYPE) != null) {
                tokenizer.consume();
                if (tokenizer.peek(OWL_FUNCTIONAL_PROPERTY) != null) {
                    tokenizer.consume();
                    currentPattern.add(new FunctionalDataProperty(subject));
                }
                else {
                    tokenizer.raiseError();
                }
            }
            else {
                tokenizer.raiseError();
            }
            if (tokenizer.peek(SPARQLTerminal.SEMI_COLON) != null) {
                tokenizer.consume();
            }
            else {
                break;
            }
        }
    }

    private void parseObjectPropertyNodePropertyList(SPARQLToken subjectToken, SPARQLGraphPattern currentPattern) {
        while (true) {
            if (tokenizer.peek(OWLRDFVocabulary.RDFS_SUB_PROPERTY_OF) != null) {
                tokenizer.consume();
                List<AtomicObjectProperty> objects = parseObjectPropertyNodeObjectList();
                AtomicObjectProperty subject = getObjectPropertyFromToken(subjectToken);
                for (AtomicObjectProperty object : objects) {
                    currentPattern.add(new SubObjectPropertyOf(subject, object));
                }
            }
            else if (tokenizer.peek(OWL_EQUIVALENT_PROPERTY) != null) {
                tokenizer.consume();
                List<AtomicObjectProperty> objects = parseObjectPropertyNodeObjectList();
                AtomicObjectProperty subject = getObjectPropertyFromToken(subjectToken);
                for (AtomicObjectProperty object : objects) {
                    currentPattern.add(new EquivalentObjectProperties(subject, object));
                }
            }
            else if (tokenizer.peek(OWL_DISJOINT_WITH) != null) {
                tokenizer.consume();

                List<AtomicObjectProperty> objects = parseObjectPropertyNodeObjectList();
                AtomicObjectProperty subject = getObjectPropertyFromToken(subjectToken);
                for (AtomicObjectProperty object : objects) {
                    currentPattern.add(new DisjointObjectProperties(subject, object));
                }
            }
            else if (tokenizer.peek(OWL_INVERSE_OF) != null) {
                tokenizer.consume();
                List<AtomicObjectProperty> objects = parseObjectPropertyNodeObjectList();
                AtomicObjectProperty subject = getObjectPropertyFromToken(subjectToken);
                for (AtomicObjectProperty object : objects) {
                    currentPattern.add(new InverseObjectProperties(subject, object));
                }
            }
            else if (tokenizer.peek(RDFS_DOMAIN) != null) {
                tokenizer.consume();
                List<AtomicClass> clses = parseClassNodeObjectList();
                AtomicObjectProperty subject = getObjectPropertyFromToken(subjectToken);
                for (AtomicClass cls : clses) {
                    currentPattern.add(new ObjectPropertyDomain(subject, cls));
                }
            }
            else if (tokenizer.peek(RDFS_RANGE) != null) {
                tokenizer.consume();
                List<AtomicClass> clses = parseClassNodeObjectList();
                AtomicObjectProperty subject = getObjectPropertyFromToken(subjectToken);
                for (AtomicClass cls : clses) {
                    currentPattern.add(new ObjectPropertyRange(subject, cls));
                }
            }
            else if (tokenizer.peek(RDF_TYPE) != null) {
                tokenizer.consume(RDF_TYPE);
                parseObjectPropertyTypeObjectList(subjectToken, currentPattern);
            }
            else {
                tokenizer.raiseError();
            }
            if (tokenizer.peek(SPARQLTerminal.SEMI_COLON) != null) {
                tokenizer.consume();
            }
            else {
                break;
            }
        }
    }

    private void parseObjectPropertyTypeObjectList(SPARQLToken subjectToken, SPARQLGraphPattern currentPattern) {
        while (true) {
            AtomicObjectProperty objectProperty = getObjectPropertyFromToken(subjectToken);
            if (tokenizer.peek(OWL_FUNCTIONAL_PROPERTY) != null) {
                tokenizer.consume();
                currentPattern.add(new FunctionalObjectProperty(objectProperty));
            }
            else if (tokenizer.peek(OWL_INVERSE_FUNCTIONAL_PROPERTY) != null) {
                tokenizer.consume();
                currentPattern.add(new InverseFunctionalObjectProperty(objectProperty));
            }
            else if (tokenizer.peek(OWL_SYMMETRIC_PROPERTY) != null) {
                tokenizer.consume();
                currentPattern.add(new SymmetricObjectProperty(objectProperty));
            }
            else if (tokenizer.peek(OWL_TRANSITIVE_PROPERTY) != null) {
                tokenizer.consume();
                currentPattern.add(new TransitiveObjectProperty(objectProperty));
            }
            else if (tokenizer.peek(OWL_REFLEXIVE_PROPERTY) != null) {
                tokenizer.consume();
                currentPattern.add(new ReflexiveObjectProperty(objectProperty));
            }
            else if (tokenizer.peek(OWL_IRREFLEXIVE_PROPERTY) != null) {
                tokenizer.consume();
                currentPattern.add(new IrreflexiveObjectProperty(objectProperty));
            }
            else if (tokenizer.peek(OWL_ASYMMETRIC_PROPERTY) != null) {
                tokenizer.consume();
                currentPattern.add(new AsymmetricObjectProperty(objectProperty));
            }
            else {
                tokenizer.raiseError();
            }
            if (tokenizer.peek(SPARQLTerminal.COMMA) != null) {
                tokenizer.consume();
            }
            else {
                break;
            }
        }
    }

    /**
     * Parses a node that is either a class IRI or a class variable.  Possible predicates are:
     * rdfs:subClassOf, owl:equivalentClass, owl:disjointWith, or an annotation property IRI, or an annotation property
     * variable (declared or undeclared).
     * @param subjectToken The node
     */
    private void parseClassNodePropertyList(SPARQLToken subjectToken, SPARQLGraphPattern currentPattern) {
        while (true) {
            if (tokenizer.peek(RDFS_SUBCLASS_OF) != null) {
                parseSubClassOf(subjectToken, currentPattern);
            }
            else if (tokenizer.peek(OWL_EQUIVALENT_CLASS) != null) {
                parseEquivalentClasses(subjectToken, currentPattern);
            }
            else if (tokenizer.peek(OWL_DISJOINT_WITH) != null) {
                parseDisjointWith(subjectToken, currentPattern);
            }
            else if (tokenizer.peek(AnnotationPropertyIRITokenType.get(), DeclaredVariableTokenType.get(PrimitiveType.ANNOTATION_PROPERTY)) != null) {
                parseAnnotationAssertions(subjectToken, currentPattern);
            }
            else if (tokenizer.peek(UndeclaredVariableTokenType.get()) != null) {
                tokenizer.registerVariable(tokenizer.peek().getImage(), PrimitiveType.ANNOTATION_PROPERTY);
                parseAnnotationAssertions(subjectToken, currentPattern);
            }
            else {
                tokenizer.raiseError();
            }
            if (tokenizer.peek(SPARQLTerminal.SEMI_COLON) == null) {
                break;
            }
            else {
                tokenizer.consume();
            }
        }
    }

    private void parseDisjointWith(SPARQLToken subjectToken, SPARQLGraphPattern currentPattern) {
        tokenizer.consume(OWL_DISJOINT_WITH);
        AtomicClass subject = getAtomicClassFromToken(subjectToken);
        List<AtomicClass> clses = parseClassNodeObjectList();
        for (AtomicClass object : clses) {
            currentPattern.add(new DisjointClasses(subject, object));
        }
    }

    private void parseEquivalentClasses(SPARQLToken subjectToken, SPARQLGraphPattern currentPattern) {
        tokenizer.consume(OWL_EQUIVALENT_CLASS);
        AtomicClass subject = getAtomicClassFromToken(subjectToken);
        List<AtomicClass> clses = parseClassNodeObjectList();
        for (AtomicClass object : clses) {
            currentPattern.add(new EquivalentClasses(subject, object));
        }
    }

    private void parseSubClassOf(SPARQLToken subjectToken, SPARQLGraphPattern currentPattern) {
        tokenizer.consume(RDFS_SUBCLASS_OF);
        AtomicClass subject = getAtomicClassFromToken(subjectToken);
        List<AtomicClass> clses = parseClassNodeObjectList();
        for (AtomicClass object : clses) {
            currentPattern.add(new SubClassOf(subject, object));
        }
    }

    private AtomicClass getAtomicClassFromToken(SPARQLToken token) {
        if (token.hasTokenType(DeclaredVariableTokenType.get(PrimitiveType.CLASS), UndeclaredVariableTokenType.get())) {
            return new ClassVariable(token.getImage());
        }
        else {
            IRI clsIRI = getIRIFromToken(token);
            return new NamedClass(clsIRI);
        }
    }

    private Variable parseVariable() {
        SPARQLToken token = tokenizer.consume(
                UndeclaredVariableTokenType.get(),
                DeclaredVariableTokenType.get(PrimitiveType.CLASS),
                DeclaredVariableTokenType.get(PrimitiveType.OBJECT_PROPERTY),
                DeclaredVariableTokenType.get(PrimitiveType.DATA_PROPERTY),
                DeclaredVariableTokenType.get(PrimitiveType.ANNOTATION_PROPERTY),
                DeclaredVariableTokenType.get(PrimitiveType.NAMED_INDIVIDUAL),
                DeclaredVariableTokenType.get(PrimitiveType.DATATYPE)
        );
        return new UntypedVariable(token.getImage());
    }

    private void parseUndeclaredVariablePropertyList(SPARQLToken varNameToken, SPARQLGraphPattern currentPattern) {
        while (true) {
            if (tokenizer.peek(RDF_TYPE) != null) {
                parseUndeclaredVariableTypeTriples(varNameToken, currentPattern);
            }
            else if (tokenizer.peek(RDFS_SUBCLASS_OF) != null) {
                tokenizer.registerVariable(varNameToken.getImage(), PrimitiveType.CLASS);
                parseSubClassOf(varNameToken, currentPattern);
            }
            else if (tokenizer.peek(OWL_EQUIVALENT_CLASS) != null) {
                tokenizer.registerVariable(varNameToken.getImage(), PrimitiveType.CLASS);
                parseEquivalentClasses(varNameToken, currentPattern);
            }
            else if (tokenizer.peek(OWL_DISJOINT_WITH) != null) {
                tokenizer.registerVariable(varNameToken.getImage(), PrimitiveType.CLASS);
                parseDisjointWith(varNameToken, currentPattern);
            }
            else if (tokenizer.peek(RDFS_SUB_PROPERTY_OF) != null) {
                parseUndeclaredVariableSubPropertyOf(varNameToken, currentPattern);
            }
            else if (tokenizer.peek(OWL_EQUIVALENT_PROPERTY) != null) {
                parseUndeclaredVariableEquivalentProperty(varNameToken, currentPattern);
            }
            // CAN'T DISAMBIGUATE!
//            else if (tokenizer.peek(RDFS_DOMAIN) != null) {
//                tokenizer.consume();
//            }
            else if (tokenizer.peek(OWLRDFVocabulary.RDFS_RANGE) != null) {
                parseUndeclaredVariableRange(varNameToken, currentPattern);
            }
            else if (tokenizer.peek(OWLRDFVocabulary.OWL_PROPERTY_DISJOINT_WITH) != null) {
                parseUndeclaredVariablePropertyDisjointWith();
            }
            else if (tokenizer.peek(OWL_SAME_AS) != null) {
                tokenizer.registerVariable(varNameToken.getImage(), PrimitiveType.NAMED_INDIVIDUAL);
                parseSameAs(varNameToken, currentPattern);
            }
            else if (tokenizer.peek(OWL_DIFFERENT_FROM) != null) {
                tokenizer.registerVariable(varNameToken.getImage(), PrimitiveType.NAMED_INDIVIDUAL);
                parseDifferentFrom(varNameToken, currentPattern);
            }
            else if (tokenizer.peek(DeclaredVariableTokenType.get(PrimitiveType.OBJECT_PROPERTY)) != null) {
                tokenizer.registerVariable(varNameToken.getImage(), PrimitiveType.NAMED_INDIVIDUAL);
                parseObjectPropertyAssertion(varNameToken, currentPattern);
            }
            else if (tokenizer.peek(ObjectPropertyIRITokenType.get()) != null) {
                tokenizer.registerVariable(varNameToken.getImage(), PrimitiveType.NAMED_INDIVIDUAL);
                parseObjectPropertyAssertion(varNameToken, currentPattern);
            }
            else if (tokenizer.peek(DeclaredVariableTokenType.get(PrimitiveType.DATA_PROPERTY)) != null) {
                tokenizer.registerVariable(varNameToken.getImage(), PrimitiveType.NAMED_INDIVIDUAL);
                parseDataPropertyAssertions(varNameToken, currentPattern);
            }
            else if (tokenizer.peek(DeclaredVariableTokenType.get(PrimitiveType.ANNOTATION_PROPERTY)) != null) {
                parseAnnotationAssertions(varNameToken, currentPattern);
            }
            else if (tokenizer.peek(DataPropertyIRITokenType.get()) != null) {
                tokenizer.registerVariable(varNameToken.getImage(), PrimitiveType.NAMED_INDIVIDUAL);
                parseDataPropertyAssertions(varNameToken, currentPattern);
            }
            else if (tokenizer.peek(AnnotationPropertyIRITokenType.get()) != null) {
//                tokenizer.registerVariable(varNameToken.getImage(), EntityType.NAMED_INDIVIDUAL);
                parseAnnotationAssertions(varNameToken, currentPattern);
            }
            else {
                tokenizer.raiseError();
            }
            if (!tokenizer.getVariableManager().getTypes(varNameToken.getImage()).isEmpty()) {
                if (tokenizer.peek(SPARQLTerminal.SEMI_COLON) != null) {
                    tokenizer.consume();
                    // Has become typed
                    Collection<VariableTokenType> types = tokenizer.getVariableManager().getTypes(varNameToken.getImage());
                    if (types.contains(DeclaredVariableTokenType.get(PrimitiveType.CLASS))) {
                        parseClassNodePropertyList(varNameToken, currentPattern);
                    }
                    else if (types.contains(DeclaredVariableTokenType.get(PrimitiveType.OBJECT_PROPERTY))) {
                        parseObjectPropertyNodePropertyList(varNameToken, currentPattern);
                    }
                    else if (types.contains(DeclaredVariableTokenType.get(PrimitiveType.DATA_PROPERTY))) {
                        parseDataPropertyNodePropertyList(varNameToken, currentPattern);
                    }
                    else if (types.contains(DeclaredVariableTokenType.get(PrimitiveType.NAMED_INDIVIDUAL))) {
                        parseIndividualNodePropertyList(varNameToken, currentPattern);
                    }
                    break;
                }
                else {
                    break;
                }

            }
            else {
                if (tokenizer.peek(SPARQLTerminal.SEMI_COLON) != null) {
                    tokenizer.consume();
                }
                else {
                    break;
                }
            }
        }
    }

    private void parseUndeclaredVariablePropertyDisjointWith() {
        tokenizer.consume();
        AtomicProperty entity = parseTypedProperty(EntityType.OBJECT_PROPERTY, EntityType.DATA_PROPERTY);
        if (entity instanceof AtomicObjectProperty) {

        }
        else if (entity instanceof AtomicDataProperty) {

        }
    }

    private void parseUndeclaredVariableSubPropertyOf(SPARQLToken varNameToken, SPARQLGraphPattern currentPattern) {
        // TODO: Multiple Objects
        tokenizer.consume(RDFS_SUB_PROPERTY_OF);
        AtomicProperty entity = parseTypedProperty(EntityType.OBJECT_PROPERTY, EntityType.DATA_PROPERTY, EntityType.ANNOTATION_PROPERTY);
        if (entity instanceof AtomicObjectProperty) {
            tokenizer.registerVariable(varNameToken.getImage(), PrimitiveType.OBJECT_PROPERTY);
            AtomicObjectProperty subject = getObjectPropertyFromToken(varNameToken);
            currentPattern.add(new SubObjectPropertyOf(subject, (AtomicObjectProperty) entity));
        }
        else if (entity instanceof AtomicDataProperty) {
            tokenizer.registerVariable(varNameToken.getImage(), PrimitiveType.DATA_PROPERTY);
            AtomicDataProperty subject = getDataPropertyFromToken(varNameToken);
            currentPattern.add(new SubDataPropertyOf(subject, (AtomicDataProperty) entity));

        }
        else if (entity instanceof AtomicAnnotationProperty) {
            tokenizer.registerVariable(varNameToken.getImage(), PrimitiveType.ANNOTATION_PROPERTY);
            AtomicAnnotationProperty subject = getAnnotationPropertyFromToken(varNameToken);
            currentPattern.add(new SubAnnotationPropertyOf(subject, (AtomicAnnotationProperty) entity));
        }
    }

    private void parseUndeclaredVariableEquivalentProperty(SPARQLToken varNameToken, SPARQLGraphPattern currentPattern) {
        // TODO: Multiple Objects?
        tokenizer.consume(OWL_EQUIVALENT_PROPERTY);
        AtomicProperty prop = parseTypedProperty(EntityType.OBJECT_PROPERTY, EntityType.DATA_PROPERTY);
        if (prop instanceof AtomicObjectProperty) {
            tokenizer.registerVariable(varNameToken.getImage(), PrimitiveType.OBJECT_PROPERTY);
            AtomicObjectProperty subject = getObjectPropertyFromToken(varNameToken);
            currentPattern.add(new EquivalentObjectProperties(subject, (AtomicObjectProperty) prop));
        }
        else if (prop instanceof AtomicDataProperty) {
            tokenizer.registerVariable(varNameToken.getImage(), PrimitiveType.DATA_PROPERTY);
            AtomicDataProperty subject = getDataPropertyFromToken(varNameToken);
            currentPattern.add(new EquivalentDataProperties(subject, (AtomicDataProperty) prop));

        }
    }

    private void parseUndeclaredVariableRange(SPARQLToken varNameToken, SPARQLGraphPattern currentPattern) {
        // TODO: Multiple Objects
        tokenizer.consume(OWLRDFVocabulary.RDFS_RANGE);
        if (tokenizer.peek(ClassIRITokenType.get(), DeclaredVariableTokenType.get(PrimitiveType.CLASS)) != null) {
            tokenizer.registerVariable(varNameToken.getImage(), PrimitiveType.OBJECT_PROPERTY);
            AtomicClass cls = getAtomicClassFromToken(tokenizer.consume());
            AtomicObjectProperty property = getObjectPropertyFromToken(varNameToken);
            currentPattern.add(new ObjectPropertyRange(property, cls));
        }
        else if (tokenizer.peek(DatatypeIRITokenType.get(), DeclaredVariableTokenType.get(PrimitiveType.DATATYPE)) != null) {
            tokenizer.registerVariable(varNameToken.getImage(), PrimitiveType.DATA_PROPERTY);
            AtomicDatatype dt = getDatatypeFromToken(tokenizer.consume());
            AtomicDataProperty property = getDataPropertyFromToken(varNameToken);
            currentPattern.add(new DataPropertyRange(property, dt));
        }
        else if (tokenizer.peek(AnnotationPropertyIRITokenType.get(), DeclaredVariableTokenType.get(PrimitiveType.ANNOTATION_PROPERTY)) != null) {
            tokenizer.registerVariable(varNameToken.getImage(), PrimitiveType.ANNOTATION_PROPERTY);
            AtomicIRI iri = new AtomicIRI(getIRIFromToken(tokenizer.consume()));
            AtomicAnnotationProperty property = getAnnotationPropertyFromToken(varNameToken);
            currentPattern.add(new AnnotationPropertyRange(property, iri));
        }
        else {
            tokenizer.raiseError();
        }
    }

    private void parseDifferentFrom(SPARQLToken subjectToken, SPARQLGraphPattern currentPattern) {
        tokenizer.consume(OWL_DIFFERENT_FROM);
        AtomicIndividual subject = getAtomicIndividualFromToken(subjectToken);
        List<AtomicIndividual> objects = parseIndividualNodeObjectList();
        for (AtomicIndividual object : objects) {
            currentPattern.add(new DifferentIndividuals(subject, object));
        }
    }

    private void parseSameAs(SPARQLToken subjectToken, SPARQLGraphPattern currentPattern) {
        tokenizer.consume(OWL_SAME_AS);
        AtomicIndividual subject = getAtomicIndividualFromToken(subjectToken);
        List<AtomicIndividual> objects = parseIndividualNodeObjectList();
        for (AtomicIndividual object : objects) {
            currentPattern.add(new SameIndividual(subject, object));
        }
    }

    private void parseUndeclaredVariableTypeTriples(SPARQLToken subjectToken, SPARQLGraphPattern currentPattern) {
        tokenizer.consume(RDF_TYPE);
        if (tokenizer.peek(OWL_CLASS) != null) {
            tokenizer.registerVariable(subjectToken.getImage(), PrimitiveType.CLASS);
            tokenizer.consume(OWL_CLASS);
            AtomicClass cls = getAtomicClassFromToken(subjectToken);
            currentPattern.add(new Declaration(cls));
        }
        else if (tokenizer.peek(OWL_OBJECT_PROPERTY) != null) {
            tokenizer.registerVariable(subjectToken.getImage(), PrimitiveType.OBJECT_PROPERTY);
            tokenizer.consume(OWL_OBJECT_PROPERTY);
            AtomicObjectProperty prop = getObjectPropertyFromToken(subjectToken);
            currentPattern.add(new Declaration(prop));
        }
        else if (tokenizer.peek(OWL_DATA_PROPERTY) != null) {
            tokenizer.registerVariable(subjectToken.getImage(), PrimitiveType.DATA_PROPERTY);
            tokenizer.consume(OWL_DATA_PROPERTY);
            AtomicDataProperty prop = getDataPropertyFromToken(subjectToken);
            currentPattern.add(new Declaration(prop));
        }
        else if (tokenizer.peek(OWL_NAMED_INDIVIDUAL) != null) {
            tokenizer.registerVariable(subjectToken.getImage(), PrimitiveType.NAMED_INDIVIDUAL);
            tokenizer.consume(OWL_NAMED_INDIVIDUAL);
            AtomicIndividual ind = getAtomicIndividualFromToken(subjectToken);
            currentPattern.add(new Declaration(ind));
        }
        else if (tokenizer.peek(OWL_ANNOTATION_PROPERTY) != null) {
            tokenizer.registerVariable(subjectToken.getImage(), PrimitiveType.ANNOTATION_PROPERTY);
            tokenizer.consume(OWL_ANNOTATION_PROPERTY);
            AtomicAnnotationProperty prop = getAnnotationPropertyFromToken(subjectToken);
            currentPattern.add(new Declaration(prop));
        }
        else if (tokenizer.peek(OWL_INVERSE_FUNCTIONAL_PROPERTY, OWL_TRANSITIVE_PROPERTY, OWL_SYMMETRIC_PROPERTY, OWL_REFLEXIVE_PROPERTY, OWL_IRREFLEXIVE_PROPERTY) != null) {
            tokenizer.registerVariable(subjectToken.getImage(), PrimitiveType.OBJECT_PROPERTY);
            parseObjectPropertyTypeObjectList(subjectToken, currentPattern);
        }
        else if (tokenizer.peek(DeclaredVariableTokenType.get(PrimitiveType.CLASS)) != null || tokenizer.peek(ClassIRITokenType.get()) != null) {
            tokenizer.registerVariable(subjectToken.getImage(), PrimitiveType.NAMED_INDIVIDUAL);
            parseSubjectTypeClass(subjectToken, currentPattern);
        }
        else if (tokenizer.peek(UndeclaredVariableTokenType.get()) != null) {
            tokenizer.registerVariable(subjectToken.getImage(), PrimitiveType.NAMED_INDIVIDUAL);
            SPARQLToken typeNode = tokenizer.consume();
            tokenizer.registerVariable(typeNode.getImage(), PrimitiveType.CLASS);
            AtomicIndividual subject = getAtomicIndividualFromToken(subjectToken);
            AtomicClass object = getAtomicClassFromToken(typeNode);
            currentPattern.add(new ClassAssertion(object, subject));
        }
        else {
            tokenizer.raiseError();
        }
    }

    private void parseSubjectTypeClass(SPARQLToken subjectToken, SPARQLGraphPattern currentPattern) {
        SPARQLToken token = tokenizer.consume();
        AtomicIndividual subject = getAtomicIndividualFromToken(subjectToken);
        AtomicClass object = getAtomicClassFromToken(token);
        currentPattern.add(new ClassAssertion(object, subject));
    }

    private void parseDataPropertyAssertions(SPARQLToken subjectToken, SPARQLGraphPattern currentPattern) {
        SPARQLToken predicateToken = tokenizer.consume();
        AtomicDataProperty property = getDataPropertyFromToken(predicateToken);
        List<AtomicLiteral> objects = parseLiteralNodeObjectList();

        AtomicIndividual subject = getAtomicIndividualFromToken(subjectToken);
        for (AtomicLiteral object : objects) {
            currentPattern.add(new DataPropertyAssertion(property, subject, object));
        }
    }

    private void parseObjectPropertyAssertion(SPARQLToken subjectToken, SPARQLGraphPattern currentPattern) {
        SPARQLToken predicateToken = tokenizer.consume();
        AtomicIndividual subject = getAtomicIndividualFromToken(subjectToken);
        AtomicObjectProperty predicate = getObjectPropertyFromToken(predicateToken);
        List<AtomicIndividual> objects = parseIndividualNodeObjectList();
        for (AtomicIndividual object : objects) {
            currentPattern.add(new ObjectPropertyAssertion(predicate, subject, object));
        }
    }

    private void parseAnnotationAssertions(SPARQLToken subjectToken, SPARQLGraphPattern currentPattern) {
        SPARQLToken predicateToken = tokenizer.consume();
        AnnotationSubject subject;
        if(subjectToken.hasTokenType(DeclaredVariableTokenType.get(PrimitiveType.CLASS))) {
            subject = new ClassVariable(subjectToken.getImage());
        }
        else if(subjectToken.hasTokenType(DeclaredVariableTokenType.get(PrimitiveType.DATATYPE))) {
            subject = new DatatypeVariable(subjectToken.getImage());
        }
        else if(subjectToken.hasTokenType(DeclaredVariableTokenType.get(PrimitiveType.OBJECT_PROPERTY))) {
            subject = new ObjectPropertyVariable(subjectToken.getImage());
        }
        else if(subjectToken.hasTokenType(DeclaredVariableTokenType.get(PrimitiveType.DATA_PROPERTY))) {
            subject = new DataPropertyVariable(subjectToken.getImage());
        }
        else if(subjectToken.hasTokenType(DeclaredVariableTokenType.get(PrimitiveType.ANNOTATION_PROPERTY))) {
            subject = new AnnotationPropertyVariable(subjectToken.getImage());
        }
        else if(subjectToken.hasTokenType(DeclaredVariableTokenType.get(PrimitiveType.NAMED_INDIVIDUAL))) {
            subject = new IndividualVariable(subjectToken.getImage());
        }
        else {
            subject = new AtomicIRI(getIRIFromToken(subjectToken));
        }
        AtomicAnnotationProperty property = getAnnotationPropertyFromToken(predicateToken);
        List<AnnotationValue> annotationValues = parseAnnotationValueObjectList();

        for (AnnotationValue value : annotationValues) {
            currentPattern.add(new AnnotationAssertion(property, subject, value));
        }
    }

    private AtomicProperty parseTypedProperty(EntityType<?>... types) {
        AtomicProperty property = null;
        List<EntityType<?>> possibleTypes = Arrays.asList(types);
        if (possibleTypes.contains(EntityType.OBJECT_PROPERTY) && tokenizer.peek(ObjectPropertyIRITokenType.get(), DeclaredVariableTokenType.get(PrimitiveType.OBJECT_PROPERTY)) != null) {
            SPARQLToken token = tokenizer.consume();
            property = getObjectPropertyFromToken(token);
        }
        else if (possibleTypes.contains(EntityType.DATA_PROPERTY) && tokenizer.peek(DataPropertyIRITokenType.get(), DeclaredVariableTokenType.get(PrimitiveType.DATA_PROPERTY)) != null) {
            SPARQLToken token = tokenizer.consume();
            property = getDataPropertyFromToken(token);
        }
        else if (possibleTypes.contains(EntityType.ANNOTATION_PROPERTY) && tokenizer.peek(AnnotationPropertyIRITokenType.get(), DeclaredVariableTokenType.get(PrimitiveType.ANNOTATION_PROPERTY)) != null) {
            SPARQLToken token = tokenizer.consume();
            property = getAnnotationPropertyFromToken(token);
        }
        else {
            tokenizer.raiseError();
        }
        return property;
    }


    public void parsePrefixDeclaration() {
        tokenizer.consume(SPARQLTerminal.PREFIX);
        SPARQLToken prefixName;
        if (!tokenizer.getPrefixManager().containsPrefixMapping(":")) {
            prefixName = tokenizer.consume(PrefixNameTokenType.get(), SPARQLTerminalTokenType.get(SPARQLTerminal.COLON));
        }
        else {
            prefixName = tokenizer.consume(PrefixNameTokenType.get());
        }
        SPARQLToken prefix = tokenizer.consume(UntypedIRITokenType.get(), PrologueDeclarationIRITokenType.get());
        String prefixImage = prefix.getImage();
        String iriString = prefixImage.substring(1, prefixImage.length() - 1);
        tokenizer.registerPrefix(prefixName.getImage(), iriString);

    }

    public void parseBaseDeclaration() {
        tokenizer.consume(SPARQLTerminal.BASE);
        SPARQLToken token = tokenizer.consume();
        tokenizer.setBase(token.getImage());
    }

    private List<AtomicClass> parseClassNodeObjectList() {
        List<AtomicClass> result = new ArrayList<>();
        while (true) {
            AtomicClass cls = parseClassNode();
            result.add(cls);
            if (tokenizer.peek(SPARQLTerminal.COMMA) != null) {
                tokenizer.consume();
            }
            else {
                break;
            }
        }
        return result;
    }

    private AtomicClass parseClassNode() {
        AtomicClass result = null;
        if (tokenizer.peek(UndeclaredVariableTokenType.get()) != null) {
            SPARQLToken token = tokenizer.consume(UndeclaredVariableTokenType.get());
            tokenizer.registerVariable(token.getImage(), PrimitiveType.CLASS);
            result = new ClassVariable(token.getImage());
        }
        else if (tokenizer.peek(DeclaredVariableTokenType.get(PrimitiveType.CLASS)) != null) {
            SPARQLToken token = tokenizer.consume();
            result = new ClassVariable(token.getImage());
        }
        else if (tokenizer.peek(ClassIRITokenType.get()) != null) {
            SPARQLToken token = tokenizer.consume();
            result = new NamedClass(getIRIFromToken(token));
        }
//        else if(tokenizer.peek(SPARQLTerminal.OPEN_PAR) != null) {
//            parseClassExpression();
//        }
        else {
            tokenizer.raiseError();
        }
        return result;
    }

    private AtomicClass getClassFromToken(SPARQLToken token) {
        if(token.hasTokenType(DeclaredVariableTokenType.get(PrimitiveType.CLASS))) {
            return new ClassVariable(token.getImage());
        }
        else if(token.hasTokenType(UndeclaredVariableTokenType.get())) {
            return new ClassVariable(token.getImage());
        }
        else {
            IRI iri = getIRIFromToken(token);
            return new NamedClass(iri);
        }
    }

    private AtomicDatatype getDatatypeFromToken(SPARQLToken token) {
        if(token.hasTokenType(DeclaredVariableTokenType.get(PrimitiveType.DATATYPE))) {
            return new DatatypeVariable(token.getImage());
        }
        else if(token.hasTokenType(UndeclaredVariableTokenType.get())) {
            return new DatatypeVariable(token.getImage());
        }
        else {
            IRI iri = getIRIFromToken(token);
            return  Datatype.get(iri);
        }
    }
    
    private AtomicObjectProperty getObjectPropertyFromToken(SPARQLToken token) {
        if(token.hasTokenType(DeclaredVariableTokenType.get(PrimitiveType.OBJECT_PROPERTY))) {
            return new ObjectPropertyVariable(token.getImage());
        }
        else if(token.hasTokenType(UndeclaredVariableTokenType.get())) {
            return new ObjectPropertyVariable(token.getImage());
        }
        else {
            IRI iri = getIRIFromToken(token);
            return new ObjectProperty(iri);
        }
    }

    private AtomicDataProperty getDataPropertyFromToken(SPARQLToken token) {
        if(token.hasTokenType(DeclaredVariableTokenType.get(PrimitiveType.DATA_PROPERTY))) {
            return new DataPropertyVariable(token.getImage());
        }
        else if(token.hasTokenType(UndeclaredVariableTokenType.get())) {
            return new DataPropertyVariable(token.getImage());
        }
        else {
            IRI iri = getIRIFromToken(token);
            return new DataProperty(iri);
        }
    }

    private AtomicAnnotationProperty getAnnotationPropertyFromToken(SPARQLToken token) {
        if(token.hasTokenType(DeclaredVariableTokenType.get(PrimitiveType.ANNOTATION_PROPERTY))) {
            return new AnnotationPropertyVariable(token.getImage());
        }
        else if(token.hasTokenType(UndeclaredVariableTokenType.get())) {
            return new AnnotationPropertyVariable(token.getImage());
        }
        else {
            IRI iri = getIRIFromToken(token);
            return new AnnotationProperty(iri);
        }
    }

    private AtomicIndividual getAtomicIndividualFromToken(SPARQLToken token) {
        if(token.hasTokenType(DeclaredVariableTokenType.get(PrimitiveType.NAMED_INDIVIDUAL))) {
            return new IndividualVariable(token.getImage());
        }
        else if(token.hasTokenType(UndeclaredVariableTokenType.get())) {
            return new IndividualVariable(token.getImage());
        }
        else {
            IRI iri = getIRIFromToken(token);
            return new NamedIndividual(iri);
        }
    }
    
//    private <T extends OWLEntity> T getOWLEntityFromToken(SPARQLToken token, EntityType<T> entityType) {
//        IRI iri;
//        if (token.hasTokenType(DeclaredVariableTokenType.get(entityType))) {
//            iri = IRI.create(token.getImage());
//        }
//        else if (token.hasTokenType(UndeclaredVariableTokenType.get())) {
//            iri = IRI.create(token.getImage());
//        }
//        else {
//            iri = getIRIFromToken(token);
//            if(entityType == EntityType.CLASS) {
//                return new NamedClass(iri);
//            }
//        }
//        return df.getOWLEntity(entityType, iri);
//    }


    private IRI getIRIFromToken(SPARQLToken token) {
        if (token.hasTokenType(PrefixNameTokenType.get())) {
            return tokenizer.getPrefixManager().getIRI(token.getImage());
        }
        else if (token.hasTokenType(UndeclaredVariableTokenType.get(), DeclaredVariableTokenType.get(PrimitiveType.CLASS), DeclaredVariableTokenType.get(PrimitiveType.OBJECT_PROPERTY), DeclaredVariableTokenType.get(PrimitiveType.DATA_PROPERTY), DeclaredVariableTokenType.get(PrimitiveType.NAMED_INDIVIDUAL), DeclaredVariableTokenType.get(PrimitiveType.DATATYPE), DeclaredVariableTokenType.get(PrimitiveType.ANNOTATION_PROPERTY))) {
            return IRI.create(token.getImage());
        }
        else {
            String image = token.getImage();
            String rawString = image.substring(1, image.length() - 1);
            return IRI.create(rawString);
        }

    }

    private List<AtomicIndividual> parseIndividualNodeObjectList() {
        List<AtomicIndividual> result = new ArrayList<>();
        while (true) {
            AtomicIndividual individual = parseIndividualNode();
            result.add(individual);
            if (tokenizer.peek(SPARQLTerminal.COMMA) != null) {
                tokenizer.consume();
            }
            else {
                break;
            }
        }
        return result;
    }

    private AtomicIndividual parseIndividualNode() {
        AtomicIndividual result = null;
        if (tokenizer.peek(UndeclaredVariableTokenType.get()) != null) {
            SPARQLToken token = tokenizer.consume(UndeclaredVariableTokenType.get());
            tokenizer.registerVariable(token.getImage(), PrimitiveType.NAMED_INDIVIDUAL);
            result = new IndividualVariable(token.getImage());
        }
        else if (tokenizer.peek(DeclaredVariableTokenType.get(PrimitiveType.NAMED_INDIVIDUAL)) != null) {
            SPARQLToken token = tokenizer.consume();
            result = new IndividualVariable(token.getImage());
        }
        else if (tokenizer.peek(IndividualIRITokenType.get()) != null) {
            SPARQLToken token = tokenizer.consume();
            result = new NamedIndividual(getIRIFromToken(token));
        }
        else {
            tokenizer.raiseError();
        }
        return result;
    }

    private List<AnnotationValue> parseAnnotationValueObjectList() {
        List<AnnotationValue> result = new ArrayList<>();
        while (true) {
            AnnotationValue value = parseAnnotationValueNode();
            result.add(value);
            if (tokenizer.peek(SPARQLTerminal.COMMA) != null) {
                tokenizer.consume();
            }
            else {
                break;
            }
        }
        return result;
    }

    private AnnotationValue parseAnnotationValueNode() {
        AnnotationValue result = null;
        if (tokenizer.peek(UntypedIRITokenType.get()) != null) {
            SPARQLToken token = tokenizer.consume();
            result = new AtomicIRI(getIRIFromToken(token));
        }
        else if (peekEntityIRI() != null) {
            SPARQLToken token = tokenizer.consume();
            result = new AtomicIRI(getIRIFromToken(token));
        }
        else if (tokenizer.peek(UndeclaredVariableTokenType.get()) != null) {
            SPARQLToken token = tokenizer.consume();
            result = new UntypedVariable(token.getImage());
        }
        else if (tokenizer.peek(StringTokenType.get()) != null) {
            result = parseLiteralNode();
        }
        else if (tokenizer.peek(IntegerTokenType.get()) != null) {
            result = parseLiteralNode();
        }
        else if (tokenizer.peek(DecimalTokenType.get()) != null) {
            result = parseLiteralNode();
        }
        else if (tokenizer.peek(DoubleTokenType.get()) != null) {
            result = parseLiteralNode();
        }
        else if (tokenizer.peek(BooleanTokenType.get()) != null) {
            result = parseLiteralNode();
        }
        else {
            tokenizer.raiseError();
        }
        return result;
    }

    private SPARQLToken peekEntityIRI() {
        return tokenizer.peek(ClassIRITokenType.get(), ObjectPropertyIRITokenType.get(), DataPropertyIRITokenType.get(), IndividualIRITokenType.get(), AnnotationPropertyIRITokenType.get(), DatatypeIRITokenType.get());
    }

    private List<AtomicObjectProperty> parseObjectPropertyNodeObjectList() {
        List<AtomicObjectProperty> result = new ArrayList<>();
        while (true) {
            AtomicObjectProperty property = parseObjectPropertyNode();
            result.add(property);
            if (tokenizer.peek(SPARQLTerminal.COMMA) != null) {
                tokenizer.consume();
            }
            else {
                break;
            }
        }
        return result;
    }

    private AtomicObjectProperty parseObjectPropertyNode() {
        AtomicObjectProperty result = null;
        if (tokenizer.peek(UndeclaredVariableTokenType.get()) != null) {
            SPARQLToken token = tokenizer.consume(UndeclaredVariableTokenType.get());
            tokenizer.registerVariable(token.getImage(), PrimitiveType.OBJECT_PROPERTY);
            result = new ObjectPropertyVariable(token.getImage());
        }
        else if (tokenizer.peek(DeclaredVariableTokenType.get(PrimitiveType.OBJECT_PROPERTY)) != null) {
            SPARQLToken token = tokenizer.consume();
            result = new ObjectPropertyVariable(token.getImage());
        }
        else if (tokenizer.peek(ObjectPropertyIRITokenType.get()) != null) {
            SPARQLToken token = tokenizer.consume();
            result = new ObjectProperty(getIRIFromToken(token));
        }
        else {
            tokenizer.raiseError();
        }
        return result;
    }

    private List<AtomicDataProperty> parseDataPropertyNodeObjectList() {
        List<AtomicDataProperty> result = new ArrayList<>();
        while (true) {
            AtomicDataProperty property = parseDataPropertyNode();
            result.add(property);
            if (tokenizer.peek(SPARQLTerminal.COMMA) != null) {
                tokenizer.consume();
            }
            else {
                break;
            }
        }
        return result;
    }

    private AtomicDataProperty parseDataPropertyNode() {
        if (tokenizer.peek(UndeclaredVariableTokenType.get()) != null) {
            SPARQLToken token = tokenizer.consume(UndeclaredVariableTokenType.get());
            tokenizer.registerVariable(token.getImage(), PrimitiveType.DATA_PROPERTY);
            return new DataPropertyVariable(token.getImage());
        }
        else if (tokenizer.peek(DeclaredVariableTokenType.get(PrimitiveType.DATA_PROPERTY)) != null) {
            SPARQLToken token = tokenizer.consume();
            return new DataPropertyVariable(token.getImage());
        }
        else if (tokenizer.peek(DataPropertyIRITokenType.get()) != null) {
            SPARQLToken token = tokenizer.consume();
            return getDataPropertyFromToken(token);
        }
        else {
            tokenizer.raiseError();
        }
        return null;
    }

    private List<AtomicLiteral> parseLiteralNodeObjectList() {
        List<AtomicLiteral> result = new ArrayList<>();
        while (true) {
            AtomicLiteral lit = parseLiteralNode();
            result.add(lit);
            if (tokenizer.peek(SPARQLTerminal.COMMA) != null) {
                tokenizer.consume();
            }
            else {
                break;
            }
        }
        return result;
    }

    private AtomicLiteral parseLiteralNode() {
        AtomicLiteral result = null;
        if (tokenizer.peek(StringTokenType.get()) != null) {
            SPARQLToken lexicalToken = tokenizer.consume();
            String lexicalValue = getStringFromLexicalToken(lexicalToken);
            if (tokenizer.peek(SPARQLTerminal.DOUBLE_CARET) != null) {
                tokenizer.consume();
                SPARQLToken token = tokenizer.consume(DatatypeIRITokenType.get());
                result = new Literal( Datatype.get(getIRIFromToken(token)), lexicalValue, "");
            }
            else {
                result = Literal.createString(lexicalValue);
            }
        }
        else if (tokenizer.peek(IntegerTokenType.get()) != null) {
            SPARQLToken token = tokenizer.consume();
            result = Literal.createInteger(Integer.parseInt(token.getImage()));
        }
        else if (tokenizer.peek(DecimalTokenType.get()) != null) {
            SPARQLToken token = tokenizer.consume();
            result = Literal.createDecimal(BigDecimal.valueOf(Double.parseDouble(token.getImage())));
        }
        else if (tokenizer.peek(DoubleTokenType.get()) != null) {
            SPARQLToken token = tokenizer.consume();
            result = Literal.createDouble(Double.parseDouble(token.getImage()));
        }
        else if (tokenizer.peek(BooleanTokenType.get()) != null) {
            SPARQLToken token = tokenizer.consume();
            result = Literal.createBoolean(Boolean.parseBoolean(token.getImage()));
        }
        else if (tokenizer.peek(UndeclaredVariableTokenType.get()) != null) {
            SPARQLToken token = tokenizer.consume();
            tokenizer.registerVariable(token.getImage(), PrimitiveType.LITERAL);
            result = new LiteralVariable(token.getImage());
        }
        else {
            tokenizer.raiseError();
        }
        return result;
    }

    private String getStringFromLexicalToken(SPARQLToken token) {
        // NASTY
        String image = token.getImage();
        if (image.startsWith("\"\"\"") && image.endsWith("\"\"\"")) {
            // TODO - Unescape
            return image.substring(3, image.length() - 3);
        }
        else {
            return image.substring(1, image.length() - 1);
        }
    }

    private List<AtomicDatatype> parseDataTypeNodeObjectList() {
        List<AtomicDatatype> result = new ArrayList<>();
        while (true) {
            AtomicDatatype dt = parseDataTypeNode();
            result.add(dt);
            if (tokenizer.peek(SPARQLTerminal.COMMA) != null) {
                tokenizer.consume();
            }
            else {
                break;
            }
        }
        return result;
    }

    private AtomicDatatype parseDataTypeNode() {
        AtomicDatatype result = null;
        if (tokenizer.peek(UndeclaredVariableTokenType.get()) != null) {
            SPARQLToken token = tokenizer.consume(UndeclaredVariableTokenType.get());
            tokenizer.registerVariable(token.getImage(), PrimitiveType.DATATYPE);
            result = new DatatypeVariable(token.getImage());
        }
        else if (tokenizer.peek(DeclaredVariableTokenType.get(PrimitiveType.DATATYPE)) != null) {
            SPARQLToken token = tokenizer.consume();
            result = new DatatypeVariable(token.getImage());
        }
        else if (tokenizer.peek(DatatypeIRITokenType.get()) != null) {
            SPARQLToken token = tokenizer.consume();
            result =  Datatype.get(getIRIFromToken(token));
        }
        else {
            tokenizer.raiseError();
        }
        return result;
    }


    public List<String> getVariables() {
        return mustBindVariables;
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public Expression parseBracketedExpression() {
        tokenizer.consume(SPARQLTerminal.OPEN_PAR);
        Expression expression = parseExpression();
        tokenizer.consume(SPARQLTerminal.CLOSE_PAR);
        return expression;
    }

    public Expression parseExpression() {
        return parseConditionalOrExpression();
    }

    public Expression parseConditionalOrExpression() {
        Expression result = null;
        while (true) {
            Expression expression = parseConditionalAndExpression();
            if (result == null) {
                result = expression;
            }
            else {
                result = new OrExpression(result, expression);
            }
            // If the parsed expression is a boolean result expression then we can parse an OR
            if (tokenizer.peek(SPARQLTerminal.OR) != null) {
                tokenizer.consume(SPARQLTerminal.OR);
            }
            else {
                break;
            }
        }
        return result;
    }

    public Expression parseConditionalAndExpression() {
        Expression result = null;
        while (true) {
            Expression expr = parseValueLogical();
            if (result == null) {
                result = expr;
            }
            else {
                result = new AndExpression(result, expr);
            }
            if (tokenizer.peek(SPARQLTerminal.AND) != null) {
                tokenizer.consume(SPARQLTerminal.AND);
            }
            else {
                break;
            }
        }
        return result;
    }

    public Expression parseValueLogical() {
        return parseRelationalExpression();
    }

    public Expression parseRelationalExpression() {
        Expression left = parseNumericExpression();
        if (tokenizer.peek(SPARQLTerminal.EQUAL) != null) {
            tokenizer.consume(SPARQLTerminal.EQUAL);
            Expression right = parseNumericExpression();
            return new RelationExpression(left, right, Relation.EQUAL);
        }
        else if (tokenizer.peek(SPARQLTerminal.NOT_EQUAL) != null) {
            tokenizer.consume(SPARQLTerminal.NOT_EQUAL);
            Expression right = parseNumericExpression();
            return new RelationExpression(left, right, Relation.NOT_EQUAL);
        }
        else if (tokenizer.peek(SPARQLTerminal.LESS_THAN) != null) {
            tokenizer.consume(SPARQLTerminal.LESS_THAN);
            Expression right = parseNumericExpression();
            return new RelationExpression(left, right, Relation.LESS_THAN);
        }
        else if (tokenizer.peek(SPARQLTerminal.LESS_THAN_OR_EQUAL) != null) {
            tokenizer.consume(SPARQLTerminal.LESS_THAN_OR_EQUAL);
            Expression right = parseNumericExpression();
            return new RelationExpression(left, right, Relation.LESS_THAN_OR_EQUAL);
        }
        else if (tokenizer.peek(SPARQLTerminal.GREATER_THAN) != null) {
            tokenizer.consume(SPARQLTerminal.GREATER_THAN);
            Expression right = parseNumericExpression();
            return new RelationExpression(left, right, Relation.GREATER_THAN);
        }
        else if (tokenizer.peek(SPARQLTerminal.GREATER_THAN_OR_EQUAL) != null) {
            tokenizer.consume(SPARQLTerminal.GREATER_THAN_OR_EQUAL);
            Expression right = parseNumericExpression();
            return new RelationExpression(left, right, Relation.GREATER_THAN_OR_EQUAL);
        }
        else {
            return left;
        }
    }

    public Expression parseNumericExpression() {
        return parseAdditiveExpression();
    }

    public Expression parseAdditiveExpression() {
//        MultiplicativeExpression ( '+' MultiplicativeExpression | '-' MultiplicativeExpression | ( NumericLiteralPositive | NumericLiteralNegative ) ( ( '*' UnaryExpression ) | ( '/' UnaryExpression ) )* )*
        Expression leftExpr = parseMultiplicativeExpression();
        while (true) {
            if (tokenizer.peek(SPARQLTerminal.PLUS) != null) {
                tokenizer.consume(SPARQLTerminal.PLUS);
                Expression rightExpr = parseMultiplicativeExpression();
                leftExpr = new PlusExpression(leftExpr, rightExpr);
            }
            else if (tokenizer.peek(SPARQLTerminal.MINUS) != null) {
                tokenizer.consume(SPARQLTerminal.MINUS);
                Expression rightExpr = parseMultiplicativeExpression();
                leftExpr = new MinusExpression(leftExpr, rightExpr);
            }
            else {
                break;
            }
        }
        return leftExpr;
    }

    public Expression parseMultiplicativeExpression() {
//        UnaryExpression ( '*' UnaryExpression | '/' UnaryExpression )*
        Expression leftExpr = parseUnaryExpression();
        while (true) {
            if (tokenizer.peek(SPARQLTerminal.ASTERISK) != null) {
                tokenizer.consume(SPARQLTerminal.ASTERISK);
                Expression rightExpr = parseUnaryExpression();
                leftExpr = new MultiplyExpression(leftExpr, rightExpr);
            }
            else if (tokenizer.peek(SPARQLTerminal.DIVIDE) != null) {
                tokenizer.consume(SPARQLTerminal.DIVIDE);
                Expression rightExpr = parseUnaryExpression();
                leftExpr = new DivideExpression(leftExpr, rightExpr);
            }
            else {
                break;
            }
        }
        return leftExpr;
    }

    public Expression parseUnaryExpression() {
//        '!' PrimaryExpression
//                |	'+' PrimaryExpression
//                |	'-' PrimaryExpression
//                |	PrimaryExpression
        if (tokenizer.peek(SPARQLTerminal.NOT) != null) {
            tokenizer.consume(SPARQLTerminal.NOT);
            return new NotExpression(parsePrimaryExpression());
        }
        else if (tokenizer.peek(SPARQLTerminal.PLUS) != null) {
            tokenizer.consume(SPARQLTerminal.PLUS);
            return parsePrimaryExpression();
        }
        else if (tokenizer.peek(SPARQLTerminal.MINUS) != null) {
            tokenizer.consume(SPARQLTerminal.MINUS);
            return new UnaryMinusExpression(parsePrimaryExpression());
        }
        else {
            return parsePrimaryExpression();
        }
    }

    public Expression parsePrimaryExpression() {
//        BrackettedExpression | BuiltInCall | iriOrFunction | RDFLiteral | NumericLiteral | BooleanLiteral | Var
        Expression expression = null;
        if (tokenizer.peek(SPARQLTerminal.OPEN_PAR) != null) {
            expression = parseBracketedExpression();
        }
        else if (tokenizer.peek(BuiltInCallTokenType.get()) != null) {
            expression = parseBuiltInCall();
        }
        else if (tokenizer.peek(ClassIRITokenType.get()) != null) {
            expression = new NamedClass(getIRIFromToken(tokenizer.consume(ClassIRITokenType.get())));
        }
        else if (tokenizer.peek(DatatypeIRITokenType.get()) != null) {
            expression =  Datatype.get(getIRIFromToken(tokenizer.consume(DatatypeIRITokenType.get())));
        }
        else if (tokenizer.peek(ObjectPropertyIRITokenType.get()) != null) {
            expression = new ObjectProperty(getIRIFromToken(tokenizer.consume(ObjectPropertyIRITokenType.get())));
        }
        else if (tokenizer.peek(DataPropertyIRITokenType.get()) != null) {
            expression = new DataProperty(getIRIFromToken(tokenizer.consume(DataPropertyIRITokenType.get())));
        }
        else if (tokenizer.peek(AnnotationPropertyIRITokenType.get()) != null) {
            expression = new AnnotationProperty(getIRIFromToken(tokenizer.consume(AnnotationPropertyIRITokenType.get())));
        }
        else if (tokenizer.peek(IndividualIRITokenType.get()) != null) {
            expression = new NamedIndividual(getIRIFromToken(tokenizer.consume(IndividualIRITokenType.get())));
        }
        else if (tokenizer.peek(UntypedIRITokenType.get()) != null) {
            expression = new AtomicIRI(getIRIFromToken(tokenizer.consume(UntypedIRITokenType.get())));
        }
        else if (tokenizer.peek(IntegerTokenType.get(), DoubleTokenType.get(), DecimalTokenType.get(), StringTokenType.get(), BooleanTokenType.get()) != null) {
            expression = parseLiteralNode();
        }
        else if (tokenizer.peek(DeclaredVariableTokenType.get(PrimitiveType.CLASS)) != null) {
            SPARQLToken token = tokenizer.consume(DeclaredVariableTokenType.get(PrimitiveType.CLASS));
            expression = new ClassVariable(token.getImage());
        }
        else if (tokenizer.peek(DeclaredVariableTokenType.get(PrimitiveType.DATATYPE)) != null) {
            SPARQLToken token = tokenizer.consume(DeclaredVariableTokenType.get(PrimitiveType.DATATYPE));
            expression = new DatatypeVariable(token.getImage());
        }
        else if (tokenizer.peek(DeclaredVariableTokenType.get(PrimitiveType.OBJECT_PROPERTY)) != null) {
            SPARQLToken token = tokenizer.consume(DeclaredVariableTokenType.get(PrimitiveType.OBJECT_PROPERTY));
            expression = new ObjectPropertyVariable(token.getImage());
        }
        else if (tokenizer.peek(DeclaredVariableTokenType.get(PrimitiveType.DATA_PROPERTY)) != null) {
            SPARQLToken token = tokenizer.consume(DeclaredVariableTokenType.get(PrimitiveType.DATA_PROPERTY));
            expression = new DataPropertyVariable(token.getImage());
        }
        else if (tokenizer.peek(DeclaredVariableTokenType.get(PrimitiveType.ANNOTATION_PROPERTY)) != null) {
            SPARQLToken token = tokenizer.consume(DeclaredVariableTokenType.get(PrimitiveType.ANNOTATION_PROPERTY));
            expression = new AnnotationPropertyVariable(token.getImage());
        }
        else if (tokenizer.peek(DeclaredVariableTokenType.get(PrimitiveType.NAMED_INDIVIDUAL)) != null) {
            SPARQLToken token = tokenizer.consume(DeclaredVariableTokenType.get(PrimitiveType.NAMED_INDIVIDUAL));
            expression = new IndividualVariable(token.getImage());
        }
        else if (tokenizer.peek(DeclaredVariableTokenType.get(PrimitiveType.LITERAL)) != null) {
            SPARQLToken token = tokenizer.consume(DeclaredVariableTokenType.get(PrimitiveType.LITERAL));
            expression = new LiteralVariable(token.getImage());
        }
        else if (tokenizer.peek(UndeclaredVariableTokenType.get()) != null) {
            SPARQLToken token = tokenizer.consume(UndeclaredVariableTokenType.get());
            expression = new UntypedVariable(token.getImage());
        }
        else if (tokenizer.peek(StringTokenType.get()) != null) {
            return parseLiteralNode();
        }
        else {
            tokenizer.raiseError();
        }
        return expression;

    }

    public Expression parseBuiltInCall() {
        SPARQLToken token = tokenizer.consume(BuiltInCallTokenType.get());
        tokenizer.consume(SPARQLTerminal.OPEN_PAR);
        String callName = token.getImage().toUpperCase();
        BuiltInCall builtInCall = BuiltInCall.valueOf(callName);
        List<Integer> argListsSizes = new ArrayList<>();
        VarArg varArg = VarArg.FIXED;
        for (ArgList argList : builtInCall.getArgLists()) {
            argListsSizes.add(argList.getArgList().size());
            if(argList.isVarArg()) {
                varArg = VarArg.VARIABLE;
            }
        }
        Collections.sort(argListsSizes);
        int maxSize = argListsSizes.get(argListsSizes.size() - 1);
        List<Expression> args = new ArrayList<>();
        for (int i = 0; ; i++) {
            Expression arg = parseExpression();
            args.add(arg);
            int numberOfParsedArgs = i + 1;
            if (varArg == VarArg.FIXED) {
                if (numberOfParsedArgs == maxSize) {
                    break;
                }
                else if (argListsSizes.indexOf(numberOfParsedArgs) != -1) {
                    if (tokenizer.peek(SPARQLTerminal.COMMA) != null) {
                        tokenizer.consume(SPARQLTerminal.COMMA);
                    }
                    else {
                        break;
                    }
                }
                else {
                    tokenizer.consume(SPARQLTerminal.COMMA);
                }
            }
            else {
                if (tokenizer.peek(SPARQLTerminal.COMMA) != null) {
                    tokenizer.consume(SPARQLTerminal.COMMA);
                }
                else {
                    break;
                }
            }
        }
        tokenizer.consume(SPARQLTerminal.CLOSE_PAR);
        return new BuiltInCallExpression(builtInCall, ImmutableList.copyOf(args));
    }


}
