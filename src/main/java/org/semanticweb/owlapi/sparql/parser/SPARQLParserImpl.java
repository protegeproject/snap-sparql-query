package org.semanticweb.owlapi.sparql.parser;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.sparql.api.*;
import org.semanticweb.owlapi.sparql.api.PrimitiveType;
import org.semanticweb.owlapi.sparql.builtin.BuiltInCall;
import org.semanticweb.owlapi.sparql.builtin.ArgList;
import org.semanticweb.owlapi.sparql.builtin.VarArg;
import org.semanticweb.owlapi.sparql.parser.tokenizer.*;
import org.semanticweb.owlapi.sparql.syntax.*;
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

//    private Set<SPARQLGraphPattern> parsedBGPs = new HashSet<>();

//    private Set<SPARQLGraphPattern> minusBGPs = new HashSet<>();

//    private Set<SPARQLGraphPattern> optionalBGPs = new HashSet<>();

    private SPARQLQueryType queryType = SPARQLQueryType.SELECT;

//    private List<SelectAs> selectAsList = new ArrayList<>();

    private boolean selectAll = false;

//    private List<OrderCondition> orderConditions = new ArrayList<>();

    public SPARQLParserImpl(SPARQLTokenizer tokenizer) {
        this.tokenizer = checkNotNull(tokenizer);
    }

    public SelectQuery parseQuery() {
        parsePrologue();
        return parseSelectQuery();
//        return createQueryObject();
    }

//    private SPARQLQuery createQueryObject() {
//        List<Variable> selectVariables = new ArrayList<>();
//        for (String varName : mustBindVariables) {
//            Collection<VariableTokenType> types = tokenizer.getVariableManager().getTypes(varName);
//            TokenType tokenType = types.iterator().next();
//            if (tokenType instanceof DeclaredVariableTokenType) {
//                DeclaredVariableTokenType declaredVariableTokenType = (DeclaredVariableTokenType) tokenType;
//                selectVariables.add(Variable.create(varName, declaredVariableTokenType.getPrimitiveType()));
//            }
//            else {
//                selectVariables.add(new UntypedVariable(varName));
//            }
//        }
//        List<SPARQLGraphPattern> graphPatterns = new ArrayList<>();
//        List<Variable> allVariables = new ArrayList<>();
//        allVariables.addAll(tokenizer.getVariableManager().getVariables());
//
//        for (SPARQLGraphPattern bgp : parsedBGPs) {
//            allVariables.addAll(bgp.getTriplePatternVariablesVariables());
//            graphPatterns.add(bgp);
//            if (selectAll) {
//                selectVariables.addAll(bgp.getTriplePatternVariablesVariables());
//            }
//        }
//
//        SolutionModifier solutionModifier = new SolutionModifier(orderConditions);
//        return new SPARQLQuery(
//                tokenizer.getPrefixManager(),
//                queryType,
//                selectVariables,
//                allVariables,
//                selectAsList,
//                graphPatterns,
//                new ArrayList<>(minusBGPs),
//                new ArrayList<>(optionalBGPs),
//                solutionModifier);
//    }


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

    public SelectQuery parseSelectQuery() {
        SelectClause selectClause = parseSelectClause();
        GroupPattern groupPattern = parseWhereClause();
        SolutionModifier solutionModifier = parseSolutionModifier();
        tokenizer.consume(EOFTokenType.get());
        return new SelectQuery(tokenizer.getPrefixManager(),selectClause, groupPattern, solutionModifier);
    }

    public SelectClause parsePrologueAndSelectClause() {
        parsePrologue();
        return parseSelectClause();
    }

    public SelectClause parseSelectClause() {
        ImmutableList.Builder<SelectItem> selectFormBuilder = ImmutableList.builder();
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
                SelectItem selectItem = parseSelectVariableOrExpressionAsVariable();
                selectFormBuilder.add(selectItem);
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
        return new SelectClause(queryType == SPARQLQueryType.SELECT_DISTINCT, selectFormBuilder.build());
    }

    public SelectItem parseSelectVariableOrExpressionAsVariable() {
        if (tokenizer.peek(SPARQLTerminal.OPEN_PAR) != null) {
            return parseSelectExpressionAsVariable();
        }
        else {
            return parseSelectVariable();
        }
    }

    public SelectVariable parseSelectVariable() {
        SPARQLToken token = tokenizer.consume(UndeclaredVariableTokenType.get());
        mustBindVariables.add(token.getImage());
        return new SelectVariable(new UntypedVariable(token.getImage()));
    }

    public SelectAs parseSelectExpressionAsVariable() {
        tokenizer.consume(SPARQLTerminal.OPEN_PAR);
        Expression expression = parseExpression();
        tokenizer.consume(SPARQLTerminal.AS);
        SPARQLToken token = tokenizer.consume(UndeclaredVariableTokenType.get());
        tokenizer.getVariableManager().addVariableName(token.getImage());
//        selectAsList.add(new SelectAs(expression, new UntypedVariable(token.getImage())));
        tokenizer.consume(SPARQLTerminal.CLOSE_PAR);
        return new SelectAs(expression, new UntypedVariable(token.getImage()));
    }


    public GroupPattern parseWhereClause() {
        tokenizer.consume(SPARQLTerminal.WHERE);
        return parseGroupGraphPattern();
    }

    private GroupPattern parseGroupGraphPattern() {

        ImmutableList.Builder<Pattern> patternBuilder = ImmutableList.builder();

        // GRAMMAR:
        // TriplesBlock? ( GraphPatternNotTriples '.'? TriplesBlock? )*

        tokenizer.consume(SPARQLTerminal.OPEN_BRACE);

        TriplesBlockPattern triplesBlockPattern1 = parseTriplesBlock();
        if (!triplesBlockPattern1.isEmpty()) {
            patternBuilder.add(triplesBlockPattern1);
        }

        Optional<Pattern> otherPattern;
        do {
            otherPattern = parseGraphPatternNotTriples();
            if(otherPattern.isPresent()) {
                patternBuilder.add(otherPattern.get());
                if(tokenizer.peek(SPARQLTerminal.DOT) != null) {
                    tokenizer.consume(SPARQLTerminal.DOT);
                }
                TriplesBlockPattern triplesBlockPattern2 = parseTriplesBlock();
                if(!triplesBlockPattern2.isEmpty()) {
                    patternBuilder.add(triplesBlockPattern2);
                }
            }
        } while (otherPattern.isPresent());

        tokenizer.consume(SPARQLTerminal.CLOSE_BRACE);

        return new GroupPattern(patternBuilder.build());
    }

    public SolutionModifier parseSolutionModifier() {
        Optional<OrderClause> orderClause;
        if (tokenizer.peek(SPARQLTerminal.ORDER) != null) {
            orderClause = Optional.of(parseOrderByClause());
        }
        else {
            orderClause = Optional.absent();
        }
        return new SolutionModifier(orderClause);
    }

    public OrderClause parseOrderByClause() {
        tokenizer.consume(SPARQLTerminal.ORDER);
        tokenizer.consume(SPARQLTerminal.BY);
        ImmutableList.Builder<OrderCondition> conditionBuilder = ImmutableList.builder();
        while (true) {
            if(tokenizer.peek(SPARQLTerminal.ASC) != null) {
                conditionBuilder.add(parseOrderCondition());
            }
            else if(tokenizer.peek(SPARQLTerminal.DESC) != null) {
                conditionBuilder.add(parseOrderCondition());
            }
            else if(peekTypedOrUntypedVariable() != null) {
                conditionBuilder.add(parseOrderCondition());
            }
            else {
                break;
            }
        }
        return new OrderClause(conditionBuilder.build());
    }

    private OrderCondition parseOrderCondition() {
        if (tokenizer.peek(SPARQLTerminal.ASC) != null) {
            tokenizer.consume(SPARQLTerminal.ASC);
            tokenizer.consume(SPARQLTerminal.OPEN_PAR);
            if (peekTypedOrUntypedVariable() != null) {
                SPARQLToken varToken = tokenizer.consume();
                return new OrderCondition(varToken.getImage(), OrderByModifier.ASC);
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
                return new OrderCondition(varToken.getImage(), OrderByModifier.DESC);
            }
            else {
                tokenizer.raiseError();
            }
            tokenizer.consume(SPARQLTerminal.CLOSE_PAR);
        }
        else if (peekTypedOrUntypedVariable() != null) {
            SPARQLToken varToken = tokenizer.consume();
            return new OrderCondition(varToken.getImage());
        }
        else {
            tokenizer.raiseError();
        }
        throw new IllegalStateException();
    }

    private SPARQLToken peekTypedOrUntypedVariable() {
        return tokenizer.peek(UndeclaredVariableTokenType.get(), DeclaredVariableTokenType.get(PrimitiveType.CLASS), DeclaredVariableTokenType.get(PrimitiveType.OBJECT_PROPERTY), DeclaredVariableTokenType.get(PrimitiveType.DATA_PROPERTY), DeclaredVariableTokenType.get(PrimitiveType.DATATYPE), DeclaredVariableTokenType.get(PrimitiveType.ANNOTATION_PROPERTY), DeclaredVariableTokenType.get(PrimitiveType.NAMED_INDIVIDUAL));
    }

    public TriplesBlockPattern parseTriplesBlock() {
        TriplesBlockPattern.Builder builder = TriplesBlockPattern.builder();
        while (true) {
            if (tokenizer.peek(UndeclaredVariableTokenType.get()) != null) {
                SPARQLToken varNameToken = tokenizer.consume();
                parseUndeclaredVariablePropertyList(varNameToken, builder);
            }
            else if (tokenizer.peek(DeclaredVariableTokenType.get(PrimitiveType.CLASS)) != null || tokenizer.peek(ClassIRITokenType.get()) != null) {
                SPARQLToken subjectToken = tokenizer.consume();
                parseClassNodePropertyList(subjectToken, builder);
            }
            else if (tokenizer.peek(DeclaredVariableTokenType.get(PrimitiveType.OBJECT_PROPERTY)) != null || tokenizer.peek(ObjectPropertyIRITokenType.get()) != null) {
                SPARQLToken subjectToken = tokenizer.consume();
                parseObjectPropertyNodePropertyList(subjectToken, builder);
            }
            else if (tokenizer.peek(DeclaredVariableTokenType.get(PrimitiveType.DATA_PROPERTY)) != null || tokenizer.peek(DataPropertyIRITokenType.get()) != null) {
                SPARQLToken subjectToken = tokenizer.consume();
                parseDataPropertyNodePropertyList(subjectToken, builder);
            }
            else if (tokenizer.peek(DeclaredVariableTokenType.get(PrimitiveType.NAMED_INDIVIDUAL)) != null || tokenizer.peek(IndividualIRITokenType.get()) != null) {
                SPARQLToken subjectToken = tokenizer.consume();
                parseIndividualNodePropertyList(subjectToken, builder);
            }

            // Carry on if we see a dot because we expect further triples
            if (tokenizer.peek(SPARQLTerminal.DOT) != null) {
                tokenizer.consume(SPARQLTerminal.DOT);
            }
            else {
                break;
            }
        }
        return builder.build();
    }

    public Optional<Pattern> parseGraphPatternNotTriples() {
        // GRAMMAR: (For the ones we support)
        // GroupOrUnionGraphPattern | OptionalGraphPattern | MinusGraphPattern | Filter | Bind
        if(tokenizer.peek(SPARQLTerminal.OPEN_BRACE) != null) {
            return Optional.of(parseGroupGraphPatternOrUnion());
        }
        else if (tokenizer.peek(SPARQLTerminal.OPTIONAL_KW) != null) {
            return Optional.<Pattern>of(parseOptionalGraphPattern());
        }
        else if (tokenizer.peek(SPARQLTerminal.MINUS_KW) != null) {
            return Optional.<Pattern>of(parseMinusGraphPattern());
        }
        else if (tokenizer.peek(SPARQLTerminal.FILTER) != null) {
            return Optional.<Pattern>of(parseFilterCondition());
        }
        else if (tokenizer.peek(SPARQLTerminal.BIND) != null) {
            return Optional.<Pattern>of(parseBind());
        }


        return Optional.absent();
    }

    private Pattern parseGroupGraphPatternOrUnion() {
        ImmutableList.Builder<Pattern> patternsBuilder = ImmutableList.builder();
        // GRAMMAR:
        // 	GroupGraphPattern ( 'UNION' GroupGraphPattern )*
        GroupPattern groupPattern = parseGroupGraphPattern();
        patternsBuilder.add(groupPattern);
        while(tokenizer.peek(SPARQLTerminal.UNION) != null) {
            UnionPattern unionPattern = parseUnionGraphPattern();
            patternsBuilder.add(unionPattern);
        }
        ImmutableList<Pattern> patterns = patternsBuilder.build();
        return new GroupPattern(patterns);
    }

    private UnionPattern parseUnionGraphPattern() {
        tokenizer.consume(SPARQLTerminal.UNION);
        GroupPattern groupPattern = parseGroupGraphPattern();
        return new UnionPattern(groupPattern);
    }

    private MinusPattern parseMinusGraphPattern() {
        tokenizer.consume(SPARQLTerminal.MINUS_KW);
        GroupPattern groupPattern = parseGroupGraphPattern();
        return new MinusPattern(groupPattern);
    }


    private OptionalPattern parseOptionalGraphPattern() {
        tokenizer.consume(SPARQLTerminal.OPTIONAL_KW);
        GroupPattern groupPattern = parseGroupGraphPattern();
        return new OptionalPattern(groupPattern);
    }

    private FilterPattern parseFilterCondition() {
        tokenizer.consume(SPARQLTerminal.FILTER);
        Expression expression = parseConstraint();
        return new FilterPattern(expression);
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
        return expression;
    }

    private BindPattern parseBind() {
        tokenizer.consume(SPARQLTerminal.BIND);
        tokenizer.consume(SPARQLTerminal.OPEN_PAR);
        Expression expression = parseConstraint();
        tokenizer.consume(SPARQLTerminal.AS);
        Variable variable = parseVariable();
        tokenizer.consume(SPARQLTerminal.CLOSE_PAR);
        return new BindPattern(expression, variable);
    }


    /**
     * Parses a node that is an individual IRI or an individual variable. Possible predicates are
     * owl:sameAs, owl:differentFrom, rdf:type, an object property IRI, object property typed variable, data property
     * IRI, data property typed variable, annotation property IRI, annotation property variable
     * @param subjectToken The node
     */
    private void parseIndividualNodePropertyList(SPARQLToken subjectToken, TriplesBlockPattern.Builder builder) {
        while (true) {
            if (tokenizer.peek(OWL_SAME_AS) != null) {
                parseSameAs(subjectToken, builder);
            }
            else if (tokenizer.peek(OWL_DIFFERENT_FROM) != null) {
                parseDifferentFrom(subjectToken, builder);
            }
            else if (tokenizer.peek(RDF_TYPE) != null) {
                parseIndividualType(subjectToken, builder);
            }
            else if (tokenizer.peek(ObjectPropertyIRITokenType.get()) != null || tokenizer.peek(DeclaredVariableTokenType.get(PrimitiveType.OBJECT_PROPERTY)) != null) {
                parseObjectPropertyAssertion(subjectToken, builder);
            }
            else if (tokenizer.peek(DataPropertyIRITokenType.get()) != null || tokenizer.peek(DeclaredVariableTokenType.get(PrimitiveType.DATA_PROPERTY)) != null) {
                parseDataPropertyAssertions(subjectToken, builder);
            }
            else if (tokenizer.peek(AnnotationPropertyIRITokenType.get()) != null || tokenizer.peek(DeclaredVariableTokenType.get(PrimitiveType.ANNOTATION_PROPERTY)) != null) {
                parseAnnotationAssertions(subjectToken, builder);
            }
            else if (tokenizer.peek(UndeclaredVariableTokenType.get()) != null) {
                SPARQLToken propertyToken = tokenizer.consume();
                // Object Property, Data Property or Annotation Property?
                if (tokenizer.peek(UntypedIRITokenType.get()) != null) {
                    HasAnnotationSubject subject = getAtomicIndividualFromToken(subjectToken);
                    AtomicAnnotationProperty property = getAnnotationPropertyFromToken(propertyToken);
                    tokenizer.registerVariable(propertyToken.getImage(), PrimitiveType.ANNOTATION_PROPERTY);
                    for (AnnotationValue value : parseAnnotationValueObjectList()) {
                        builder.add(new AnnotationAssertion(property, subject.toAnnotationSubject(), value));
                    }
                }
                else if (tokenizer.peek(IndividualIRITokenType.get(), DeclaredVariableTokenType.get(PrimitiveType.NAMED_INDIVIDUAL)) != null) {
                    AtomicIndividual subject = getAtomicIndividualFromToken(subjectToken);
                    AtomicObjectProperty property = getObjectPropertyFromToken(propertyToken);
                    tokenizer.registerVariable(propertyToken.getImage(), PrimitiveType.OBJECT_PROPERTY);
                    for (AtomicIndividual ind : parseIndividualNodeObjectList()) {
                        builder.add(new ObjectPropertyAssertion(property, subject, ind));
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

    private void parseIndividualType(SPARQLToken subjectToken, TriplesBlockPattern.Builder builder) {
        tokenizer.consume();
        AtomicIndividual individual = getAtomicIndividualFromToken(subjectToken);
        List<AtomicClass> types = parseClassNodeObjectList();
        for (AtomicClass type : types) {
            builder.add(new ClassAssertion(type, individual));
        }
    }

    private void parseDataPropertyNodePropertyList(SPARQLToken subjectToken, TriplesBlockPattern.Builder builder) {
        while (true) {

            AtomicDataProperty subject = getDataPropertyFromToken(subjectToken);
            if (tokenizer.peek(OWLRDFVocabulary.RDFS_SUB_PROPERTY_OF) != null) {
                tokenizer.consume();
                List<AtomicDataProperty> properties = parseDataPropertyNodeObjectList();
                for (AtomicDataProperty property : properties) {
                    builder.add(new SubDataPropertyOf(subject, property));
                }
            }
            else if (tokenizer.peek(OWL_EQUIVALENT_PROPERTY) != null) {
                tokenizer.consume();
                List<AtomicDataProperty> properties = parseDataPropertyNodeObjectList();
                for (AtomicDataProperty property : properties) {
                    builder.add(new EquivalentDataProperties(subject, property));
                }
            }
            else if (tokenizer.peek(OWL_DISJOINT_WITH) != null) {
                tokenizer.consume();
                List<AtomicDataProperty> properties = parseDataPropertyNodeObjectList();
                for (AtomicDataProperty property : properties) {
                    builder.add(new DisjointDataProperties(subject, property));
                }
            }
            else if (tokenizer.peek(RDFS_DOMAIN) != null) {
                tokenizer.consume();
                List<AtomicClass> clses = parseClassNodeObjectList();
                for (AtomicClass cls : clses) {
                    builder.add(new DataPropertyDomain(subject, cls));
                }
            }
            else if (tokenizer.peek(RDFS_RANGE) != null) {
                tokenizer.consume();
                List<AtomicDatatype> dataTypes = parseDataTypeNodeObjectList();
                for (AtomicDatatype datatype : dataTypes) {
                    builder.add(new DataPropertyRange(subject, datatype));
                }
            }
            else if (tokenizer.peek(RDF_TYPE) != null) {
                tokenizer.consume();
                if (tokenizer.peek(OWL_FUNCTIONAL_PROPERTY) != null) {
                    tokenizer.consume();
                    builder.add(new FunctionalDataProperty(subject));
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

    private void parseObjectPropertyNodePropertyList(SPARQLToken subjectToken, TriplesBlockPattern.Builder builder) {
        while (true) {
            if (tokenizer.peek(OWLRDFVocabulary.RDFS_SUB_PROPERTY_OF) != null) {
                tokenizer.consume();
                List<AtomicObjectProperty> objects = parseObjectPropertyNodeObjectList();
                AtomicObjectProperty subject = getObjectPropertyFromToken(subjectToken);
                for (AtomicObjectProperty object : objects) {
                    builder.add(new SubObjectPropertyOf(subject, object));
                }
            }
            else if (tokenizer.peek(OWL_EQUIVALENT_PROPERTY) != null) {
                tokenizer.consume();
                List<AtomicObjectProperty> objects = parseObjectPropertyNodeObjectList();
                AtomicObjectProperty subject = getObjectPropertyFromToken(subjectToken);
                for (AtomicObjectProperty object : objects) {
                    builder.add(new EquivalentObjectProperties(subject, object));
                }
            }
            else if (tokenizer.peek(OWL_DISJOINT_WITH) != null) {
                tokenizer.consume();

                List<AtomicObjectProperty> objects = parseObjectPropertyNodeObjectList();
                AtomicObjectProperty subject = getObjectPropertyFromToken(subjectToken);
                for (AtomicObjectProperty object : objects) {
                    builder.add(new DisjointObjectProperties(subject, object));
                }
            }
            else if (tokenizer.peek(OWL_INVERSE_OF) != null) {
                tokenizer.consume();
                List<AtomicObjectProperty> objects = parseObjectPropertyNodeObjectList();
                AtomicObjectProperty subject = getObjectPropertyFromToken(subjectToken);
                for (AtomicObjectProperty object : objects) {
                    builder.add(new InverseObjectProperties(subject, object));
                }
            }
            else if (tokenizer.peek(RDFS_DOMAIN) != null) {
                tokenizer.consume();
                List<AtomicClass> clses = parseClassNodeObjectList();
                AtomicObjectProperty subject = getObjectPropertyFromToken(subjectToken);
                for (AtomicClass cls : clses) {
                    builder.add(new ObjectPropertyDomain(subject, cls));
                }
            }
            else if (tokenizer.peek(RDFS_RANGE) != null) {
                tokenizer.consume();
                List<AtomicClass> clses = parseClassNodeObjectList();
                AtomicObjectProperty subject = getObjectPropertyFromToken(subjectToken);
                for (AtomicClass cls : clses) {
                    builder.add(new ObjectPropertyRange(subject, cls));
                }
            }
            else if (tokenizer.peek(RDF_TYPE) != null) {
                tokenizer.consume(RDF_TYPE);
                parseObjectPropertyTypeObjectList(subjectToken, builder);
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

    private void parseObjectPropertyTypeObjectList(SPARQLToken subjectToken, TriplesBlockPattern.Builder builder) {
        while (true) {
            AtomicObjectProperty objectProperty = getObjectPropertyFromToken(subjectToken);
            if (tokenizer.peek(OWL_FUNCTIONAL_PROPERTY) != null) {
                tokenizer.consume();
                builder.add(new FunctionalObjectProperty(objectProperty));
            }
            else if (tokenizer.peek(OWL_INVERSE_FUNCTIONAL_PROPERTY) != null) {
                tokenizer.consume();
                builder.add(new InverseFunctionalObjectProperty(objectProperty));
            }
            else if (tokenizer.peek(OWL_SYMMETRIC_PROPERTY) != null) {
                tokenizer.consume();
                builder.add(new SymmetricObjectProperty(objectProperty));
            }
            else if (tokenizer.peek(OWL_TRANSITIVE_PROPERTY) != null) {
                tokenizer.consume();
                builder.add(new TransitiveObjectProperty(objectProperty));
            }
            else if (tokenizer.peek(OWL_REFLEXIVE_PROPERTY) != null) {
                tokenizer.consume();
                builder.add(new ReflexiveObjectProperty(objectProperty));
            }
            else if (tokenizer.peek(OWL_IRREFLEXIVE_PROPERTY) != null) {
                tokenizer.consume();
                builder.add(new IrreflexiveObjectProperty(objectProperty));
            }
            else if (tokenizer.peek(OWL_ASYMMETRIC_PROPERTY) != null) {
                tokenizer.consume();
                builder.add(new AsymmetricObjectProperty(objectProperty));
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
    private void parseClassNodePropertyList(SPARQLToken subjectToken, TriplesBlockPattern.Builder builder) {
        while (true) {
            if (tokenizer.peek(RDFS_SUBCLASS_OF) != null) {
                parseSubClassOf(subjectToken, builder);
            }
            else if (tokenizer.peek(OWL_EQUIVALENT_CLASS) != null) {
                parseEquivalentClasses(subjectToken, builder);
            }
            else if (tokenizer.peek(OWL_DISJOINT_WITH) != null) {
                parseDisjointWith(subjectToken, builder);
            }
            else if (tokenizer.peek(AnnotationPropertyIRITokenType.get(), DeclaredVariableTokenType.get(PrimitiveType.ANNOTATION_PROPERTY)) != null) {
                parseAnnotationAssertions(subjectToken, builder);
            }
            else if (tokenizer.peek(UndeclaredVariableTokenType.get()) != null) {
                tokenizer.registerVariable(tokenizer.peek().getImage(), PrimitiveType.ANNOTATION_PROPERTY);
                parseAnnotationAssertions(subjectToken, builder);
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

    private void parseDisjointWith(SPARQLToken subjectToken, TriplesBlockPattern.Builder builder) {
        tokenizer.consume(OWL_DISJOINT_WITH);
        AtomicClass subject = getAtomicClassFromToken(subjectToken);
        List<AtomicClass> clses = parseClassNodeObjectList();
        for (AtomicClass object : clses) {
            builder.add(new DisjointClasses(subject, object));
        }
    }

    private void parseEquivalentClasses(SPARQLToken subjectToken, TriplesBlockPattern.Builder builder) {
        tokenizer.consume(OWL_EQUIVALENT_CLASS);
        AtomicClass subject = getAtomicClassFromToken(subjectToken);
        List<AtomicClass> clses = parseClassNodeObjectList();
        for (AtomicClass object : clses) {
            builder.add(new EquivalentClasses(subject, object));
        }
    }

    private void parseSubClassOf(SPARQLToken subjectToken, TriplesBlockPattern.Builder builder) {
        tokenizer.consume(RDFS_SUBCLASS_OF);
        AtomicClass subject = getAtomicClassFromToken(subjectToken);
        List<AtomicClass> clses = parseClassNodeObjectList();
        for (AtomicClass object : clses) {
            builder.add(new SubClassOf(subject, object));
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

    private void parseUndeclaredVariablePropertyList(SPARQLToken varNameToken, TriplesBlockPattern.Builder builder) {
        while (true) {
            if (tokenizer.peek(RDF_TYPE) != null) {
                parseUndeclaredVariableTypeTriples(varNameToken, builder);
            }
            else if (tokenizer.peek(RDFS_SUBCLASS_OF) != null) {
                tokenizer.registerVariable(varNameToken.getImage(), PrimitiveType.CLASS);
                parseSubClassOf(varNameToken, builder);
            }
            else if (tokenizer.peek(OWL_EQUIVALENT_CLASS) != null) {
                tokenizer.registerVariable(varNameToken.getImage(), PrimitiveType.CLASS);
                parseEquivalentClasses(varNameToken, builder);
            }
            else if (tokenizer.peek(OWL_DISJOINT_WITH) != null) {
                tokenizer.registerVariable(varNameToken.getImage(), PrimitiveType.CLASS);
                parseDisjointWith(varNameToken, builder);
            }
            else if (tokenizer.peek(RDFS_SUB_PROPERTY_OF) != null) {
                parseUndeclaredVariableSubPropertyOf(varNameToken, builder);
            }
            else if (tokenizer.peek(OWL_EQUIVALENT_PROPERTY) != null) {
                parseUndeclaredVariableEquivalentProperty(varNameToken, builder);
            }
            // CAN'T DISAMBIGUATE!
//            else if (tokenizer.peek(RDFS_DOMAIN) != null) {
//                tokenizer.consume();
//            }
            else if (tokenizer.peek(OWLRDFVocabulary.RDFS_RANGE) != null) {
                parseUndeclaredVariableRange(varNameToken, builder);
            }
            else if (tokenizer.peek(OWLRDFVocabulary.OWL_PROPERTY_DISJOINT_WITH) != null) {
                parseUndeclaredVariablePropertyDisjointWith();
            }
            else if (tokenizer.peek(OWL_SAME_AS) != null) {
                tokenizer.registerVariable(varNameToken.getImage(), PrimitiveType.NAMED_INDIVIDUAL);
                parseSameAs(varNameToken, builder);
            }
            else if (tokenizer.peek(OWL_DIFFERENT_FROM) != null) {
                tokenizer.registerVariable(varNameToken.getImage(), PrimitiveType.NAMED_INDIVIDUAL);
                parseDifferentFrom(varNameToken, builder);
            }
            else if (tokenizer.peek(DeclaredVariableTokenType.get(PrimitiveType.OBJECT_PROPERTY)) != null) {
                tokenizer.registerVariable(varNameToken.getImage(), PrimitiveType.NAMED_INDIVIDUAL);
                parseObjectPropertyAssertion(varNameToken, builder);
            }
            else if (tokenizer.peek(ObjectPropertyIRITokenType.get()) != null) {
                tokenizer.registerVariable(varNameToken.getImage(), PrimitiveType.NAMED_INDIVIDUAL);
                parseObjectPropertyAssertion(varNameToken, builder);
            }
            else if (tokenizer.peek(DeclaredVariableTokenType.get(PrimitiveType.DATA_PROPERTY)) != null) {
                tokenizer.registerVariable(varNameToken.getImage(), PrimitiveType.NAMED_INDIVIDUAL);
                parseDataPropertyAssertions(varNameToken, builder);
            }
            else if (tokenizer.peek(DeclaredVariableTokenType.get(PrimitiveType.ANNOTATION_PROPERTY)) != null) {
                parseAnnotationAssertions(varNameToken, builder);
            }
            else if (tokenizer.peek(DataPropertyIRITokenType.get()) != null) {
                tokenizer.registerVariable(varNameToken.getImage(), PrimitiveType.NAMED_INDIVIDUAL);
                parseDataPropertyAssertions(varNameToken, builder);
            }
            else if (tokenizer.peek(AnnotationPropertyIRITokenType.get()) != null) {
//                tokenizer.registerVariable(varNameToken.getImage(), EntityType.NAMED_INDIVIDUAL);
                parseAnnotationAssertions(varNameToken, builder);
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
                        parseClassNodePropertyList(varNameToken, builder);
                    }
                    else if (types.contains(DeclaredVariableTokenType.get(PrimitiveType.OBJECT_PROPERTY))) {
                        parseObjectPropertyNodePropertyList(varNameToken, builder);
                    }
                    else if (types.contains(DeclaredVariableTokenType.get(PrimitiveType.DATA_PROPERTY))) {
                        parseDataPropertyNodePropertyList(varNameToken, builder);
                    }
                    else if (types.contains(DeclaredVariableTokenType.get(PrimitiveType.NAMED_INDIVIDUAL))) {
                        parseIndividualNodePropertyList(varNameToken, builder);
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

    private void parseUndeclaredVariableSubPropertyOf(SPARQLToken varNameToken, TriplesBlockPattern.Builder builder) {
        // TODO: Multiple Objects
        tokenizer.consume(RDFS_SUB_PROPERTY_OF);
        AtomicProperty entity = parseTypedProperty(EntityType.OBJECT_PROPERTY, EntityType.DATA_PROPERTY, EntityType.ANNOTATION_PROPERTY);
        if (entity instanceof AtomicObjectProperty) {
            tokenizer.registerVariable(varNameToken.getImage(), PrimitiveType.OBJECT_PROPERTY);
            AtomicObjectProperty subject = getObjectPropertyFromToken(varNameToken);
            builder.add(new SubObjectPropertyOf(subject, (AtomicObjectProperty) entity));
        }
        else if (entity instanceof AtomicDataProperty) {
            tokenizer.registerVariable(varNameToken.getImage(), PrimitiveType.DATA_PROPERTY);
            AtomicDataProperty subject = getDataPropertyFromToken(varNameToken);
            builder.add(new SubDataPropertyOf(subject, (AtomicDataProperty) entity));

        }
        else if (entity instanceof AtomicAnnotationProperty) {
            tokenizer.registerVariable(varNameToken.getImage(), PrimitiveType.ANNOTATION_PROPERTY);
            AtomicAnnotationProperty subject = getAnnotationPropertyFromToken(varNameToken);
            builder.add(new SubAnnotationPropertyOf(subject, (AtomicAnnotationProperty) entity));
        }
    }

    private void parseUndeclaredVariableEquivalentProperty(SPARQLToken varNameToken, TriplesBlockPattern.Builder builder) {
        // TODO: Multiple Objects?
        tokenizer.consume(OWL_EQUIVALENT_PROPERTY);
        AtomicProperty prop = parseTypedProperty(EntityType.OBJECT_PROPERTY, EntityType.DATA_PROPERTY);
        if (prop instanceof AtomicObjectProperty) {
            tokenizer.registerVariable(varNameToken.getImage(), PrimitiveType.OBJECT_PROPERTY);
            AtomicObjectProperty subject = getObjectPropertyFromToken(varNameToken);
            builder.add(new EquivalentObjectProperties(subject, (AtomicObjectProperty) prop));
        }
        else if (prop instanceof AtomicDataProperty) {
            tokenizer.registerVariable(varNameToken.getImage(), PrimitiveType.DATA_PROPERTY);
            AtomicDataProperty subject = getDataPropertyFromToken(varNameToken);
            builder.add(new EquivalentDataProperties(subject, (AtomicDataProperty) prop));

        }
    }

    private void parseUndeclaredVariableRange(SPARQLToken varNameToken, TriplesBlockPattern.Builder builder) {
        // TODO: Multiple Objects
        tokenizer.consume(OWLRDFVocabulary.RDFS_RANGE);
        if (tokenizer.peek(ClassIRITokenType.get(), DeclaredVariableTokenType.get(PrimitiveType.CLASS)) != null) {
            tokenizer.registerVariable(varNameToken.getImage(), PrimitiveType.OBJECT_PROPERTY);
            AtomicClass cls = getAtomicClassFromToken(tokenizer.consume());
            AtomicObjectProperty property = getObjectPropertyFromToken(varNameToken);
            builder.add(new ObjectPropertyRange(property, cls));
        }
        else if (tokenizer.peek(DatatypeIRITokenType.get(), DeclaredVariableTokenType.get(PrimitiveType.DATATYPE)) != null) {
            tokenizer.registerVariable(varNameToken.getImage(), PrimitiveType.DATA_PROPERTY);
            AtomicDatatype dt = getDatatypeFromToken(tokenizer.consume());
            AtomicDataProperty property = getDataPropertyFromToken(varNameToken);
            builder.add(new DataPropertyRange(property, dt));
        }
        else if (tokenizer.peek(AnnotationPropertyIRITokenType.get(), DeclaredVariableTokenType.get(PrimitiveType.ANNOTATION_PROPERTY)) != null) {
            tokenizer.registerVariable(varNameToken.getImage(), PrimitiveType.ANNOTATION_PROPERTY);
            AtomicIRI iri = new AtomicIRI(getIRIFromToken(tokenizer.consume()));
            AtomicAnnotationProperty property = getAnnotationPropertyFromToken(varNameToken);
            builder.add(new AnnotationPropertyRange(property, iri));
        }
        else {
            tokenizer.raiseError();
        }
    }

    private void parseDifferentFrom(SPARQLToken subjectToken, TriplesBlockPattern.Builder builder) {
        tokenizer.consume(OWL_DIFFERENT_FROM);
        AtomicIndividual subject = getAtomicIndividualFromToken(subjectToken);
        List<AtomicIndividual> objects = parseIndividualNodeObjectList();
        for (AtomicIndividual object : objects) {
            builder.add(new DifferentIndividuals(subject, object));
        }
    }

    private void parseSameAs(SPARQLToken subjectToken, TriplesBlockPattern.Builder builder) {
        tokenizer.consume(OWL_SAME_AS);
        AtomicIndividual subject = getAtomicIndividualFromToken(subjectToken);
        List<AtomicIndividual> objects = parseIndividualNodeObjectList();
        for (AtomicIndividual object : objects) {
            builder.add(new SameIndividual(subject, object));
        }
    }

    private void parseUndeclaredVariableTypeTriples(SPARQLToken subjectToken, TriplesBlockPattern.Builder builder) {
        tokenizer.consume(RDF_TYPE);
        if (tokenizer.peek(OWL_CLASS) != null) {
            tokenizer.registerVariable(subjectToken.getImage(), PrimitiveType.CLASS);
            tokenizer.consume(OWL_CLASS);
            AtomicClass cls = getAtomicClassFromToken(subjectToken);
            builder.add(new Declaration(cls));
        }
        else if (tokenizer.peek(OWL_OBJECT_PROPERTY) != null) {
            tokenizer.registerVariable(subjectToken.getImage(), PrimitiveType.OBJECT_PROPERTY);
            tokenizer.consume(OWL_OBJECT_PROPERTY);
            AtomicObjectProperty prop = getObjectPropertyFromToken(subjectToken);
            builder.add(new Declaration(prop));
        }
        else if (tokenizer.peek(OWL_DATA_PROPERTY) != null) {
            tokenizer.registerVariable(subjectToken.getImage(), PrimitiveType.DATA_PROPERTY);
            tokenizer.consume(OWL_DATA_PROPERTY);
            AtomicDataProperty prop = getDataPropertyFromToken(subjectToken);
            builder.add(new Declaration(prop));
        }
        else if (tokenizer.peek(OWL_NAMED_INDIVIDUAL) != null) {
            tokenizer.registerVariable(subjectToken.getImage(), PrimitiveType.NAMED_INDIVIDUAL);
            tokenizer.consume(OWL_NAMED_INDIVIDUAL);
            AtomicIndividual ind = getAtomicIndividualFromToken(subjectToken);
            builder.add(new Declaration(ind));
        }
        else if (tokenizer.peek(OWL_ANNOTATION_PROPERTY) != null) {
            tokenizer.registerVariable(subjectToken.getImage(), PrimitiveType.ANNOTATION_PROPERTY);
            tokenizer.consume(OWL_ANNOTATION_PROPERTY);
            AtomicAnnotationProperty prop = getAnnotationPropertyFromToken(subjectToken);
            builder.add(new Declaration(prop));
        }
        else if (tokenizer.peek(OWL_INVERSE_FUNCTIONAL_PROPERTY, OWL_TRANSITIVE_PROPERTY, OWL_SYMMETRIC_PROPERTY, OWL_REFLEXIVE_PROPERTY, OWL_IRREFLEXIVE_PROPERTY) != null) {
            tokenizer.registerVariable(subjectToken.getImage(), PrimitiveType.OBJECT_PROPERTY);
            parseObjectPropertyTypeObjectList(subjectToken, builder);
        }
        else if (tokenizer.peek(DeclaredVariableTokenType.get(PrimitiveType.CLASS)) != null || tokenizer.peek(ClassIRITokenType.get()) != null) {
            tokenizer.registerVariable(subjectToken.getImage(), PrimitiveType.NAMED_INDIVIDUAL);
            parseSubjectTypeClass(subjectToken, builder);
        }
        else if (tokenizer.peek(UndeclaredVariableTokenType.get()) != null) {
            tokenizer.registerVariable(subjectToken.getImage(), PrimitiveType.NAMED_INDIVIDUAL);
            SPARQLToken typeNode = tokenizer.consume();
            tokenizer.registerVariable(typeNode.getImage(), PrimitiveType.CLASS);
            AtomicIndividual subject = getAtomicIndividualFromToken(subjectToken);
            AtomicClass object = getAtomicClassFromToken(typeNode);
            builder.add(new ClassAssertion(object, subject));
        }
        else {
            tokenizer.raiseError();
        }
    }

    private void parseSubjectTypeClass(SPARQLToken subjectToken, TriplesBlockPattern.Builder builder) {
        SPARQLToken token = tokenizer.consume();
        AtomicIndividual subject = getAtomicIndividualFromToken(subjectToken);
        AtomicClass object = getAtomicClassFromToken(token);
        builder.add(new ClassAssertion(object, subject));
    }

    private void parseDataPropertyAssertions(SPARQLToken subjectToken, TriplesBlockPattern.Builder builder) {
        SPARQLToken predicateToken = tokenizer.consume();
        AtomicDataProperty property = getDataPropertyFromToken(predicateToken);
        List<AtomicLiteral> objects = parseLiteralNodeObjectList();

        AtomicIndividual subject = getAtomicIndividualFromToken(subjectToken);
        for (AtomicLiteral object : objects) {
            builder.add(new DataPropertyAssertion(property, subject, object));
        }
    }

    private void parseObjectPropertyAssertion(SPARQLToken subjectToken, TriplesBlockPattern.Builder builder) {
        SPARQLToken predicateToken = tokenizer.consume();
        AtomicIndividual subject = getAtomicIndividualFromToken(subjectToken);
        AtomicObjectProperty predicate = getObjectPropertyFromToken(predicateToken);
        List<AtomicIndividual> objects = parseIndividualNodeObjectList();
        for (AtomicIndividual object : objects) {
            builder.add(new ObjectPropertyAssertion(predicate, subject, object));
        }
    }

    private void parseAnnotationAssertions(SPARQLToken subjectToken, TriplesBlockPattern.Builder builder) {
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
            builder.add(new AnnotationAssertion(property, subject, value));
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
        boolean hasEmptyArgList = false;
        for (ArgList argList : builtInCall.getArgLists()) {
            if(argList.getArgList().isEmpty()) {
                hasEmptyArgList = true;
            }
            argListsSizes.add(argList.getArgList().size());
            if(argList.isVarArg()) {
                varArg = VarArg.VARIABLE;
            }
        }
        Collections.sort(argListsSizes);
        int maxSize = argListsSizes.get(argListsSizes.size() - 1);
        List<Expression> args = new ArrayList<>();

        if(hasEmptyArgList && tokenizer.peek(SPARQLTerminal.CLOSE_PAR) != null) {
            tokenizer.consume(SPARQLTerminal.CLOSE_PAR);
        }
        else {
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
        }



        return new BuiltInCallExpression(builtInCall, ImmutableList.copyOf(args));
    }


}
