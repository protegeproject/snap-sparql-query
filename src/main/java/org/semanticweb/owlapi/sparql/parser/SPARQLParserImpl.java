package org.semanticweb.owlapi.sparql.parser;

import java.util.Optional;
import com.google.common.collect.ImmutableList;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.sparql.api.*;
import org.semanticweb.owlapi.sparql.builtin.ArgList;
import org.semanticweb.owlapi.sparql.builtin.BuiltInCall;
import org.semanticweb.owlapi.sparql.builtin.VarArg;
import org.semanticweb.owlapi.sparql.builtin.eval.BuiltInAggregateCallEvaluator;
import org.semanticweb.owlapi.sparql.parser.tokenizer.*;
import org.semanticweb.owlapi.sparql.syntax.*;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

import java.math.BigDecimal;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.semanticweb.owlapi.sparql.parser.tokenizer.SPARQLTerminal.*;
import static org.semanticweb.owlapi.vocab.OWLRDFVocabulary.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/03/2012
 */
public class SPARQLParserImpl {

    private final SPARQLTokenizer tokenizer;

    public SPARQLParserImpl(SPARQLTokenizer tokenizer) {
        this.tokenizer = checkNotNull(tokenizer);
    }

    private boolean peek(TokenType ... tokenTypes) {
        return tokenizer.peek(tokenTypes) != null;
    }

    private boolean peek(SPARQLTerminal terminal) {
        return tokenizer.peek(terminal) != null;
    }

    private boolean peek(OWLRDFVocabulary vocabulary) {
        return tokenizer.peek(vocabulary) != null;
    }

    private boolean peek(OWLRDFVocabulary vocabulary, OWLRDFVocabulary ... vocabularies) {
        return tokenizer.peek(vocabulary, vocabularies) != null;
    }

    public void parsePrologue() {
        while (true) {
            if (peek(PREFIX)) {
                parsePrefixDeclaration();
            }
            else if (peek(BASE)) {
                parseBaseDeclaration();
            }
            else if (peek(SELECT)) {
                break;
            }
            else {
                break;
            }
        }
    }

    public Query parseQuery() {
        parsePrologue();
        if(peek(SELECT)) {
            return parseSelectQuery();
        }
        else if(peek(CONSTRUCT)) {
            return parseConstructQuery();
        }
        else {
            tokenizer.raiseError();
            return null;
        }
    }

    public SelectQuery parseSelectQuery() {
        parsePrologue();
        SelectClause selectClause = parseSelectClause();
        GroupPattern groupPattern = parseWhereClause(false);
        SolutionModifier solutionModifier = parseSolutionModifier();
        tokenizer.consume(EOFTokenType.get());
        SelectQuery selectQuery = new SelectQuery(tokenizer.getPrefixManager(), selectClause, groupPattern, solutionModifier);
        checkSelectForm(selectQuery);
        return selectQuery;
    }

    public ConstructQuery parseConstructQuery() {
        parsePrologue();
        ConstructTemplate constructTemplate = parseConstructClause();
        GroupPattern groupPattern = parseWhereClause(false);
        SolutionModifier solutionModifier = parseSolutionModifier();
        tokenizer.consume(EOFTokenType.get());
        return new ConstructQuery(tokenizer.getPrefixManager(), constructTemplate, groupPattern, solutionModifier);

    }

    private ConstructTemplate parseConstructClause() {
        tokenizer.consume(CONSTRUCT);
        return parseConstructTemplate();
    }

    private ConstructTemplate parseConstructTemplate() {
        tokenizer.consume(OPEN_BRACE);
        TriplesBlockPattern triplesBlockPattern = parseTriplesBlock(true);
        tokenizer.consume(CLOSE_BRACE);
        return new ConstructTemplate(triplesBlockPattern.getAxioms());
    }

    public Collection<? extends Variable> parseProjectedVariables() {
        parsePrologue();
        if(peek(SELECT)) {
            return parseSelectClause().getVariables();
        }
        else if(peek(CONSTRUCT)) {
            return parseConstructClause().getVariables();
        }
        else {
            return Collections.emptySet();
        }
    }

    public SelectClause parseSelectClause() {
        ImmutableList.Builder<SelectItem> selectFormBuilder = ImmutableList.builder();
        tokenizer.consume(SELECT);
        SPARQLQueryType queryType = SPARQLQueryType.SELECT;
        if (peek(DISTINCT)) {
            tokenizer.consume(DISTINCT);
            queryType = SPARQLQueryType.SELECT_DISTINCT;
        }
        if (peek(ASTERISK)) {
            tokenizer.consume(ASTERISK);
        }
        else if (peek(VariableTokenType.get()) || peek(OPEN_PAR)) {
            while (true) {
                SelectItem selectItem = parseSelectVariableOrExpressionAsVariable();
                selectFormBuilder.add(selectItem);
                if (peek(WHERE)) {
                    break;
                }
                if (tokenizer.peek(VariableTokenType.get()) == null && tokenizer.peek(OPEN_PAR) == null) {
                    break;
                }
            }
        }
        else {
            tokenizer.raiseError();
        }
        ImmutableList<SelectItem> selectForms = selectFormBuilder.build();
        return new SelectClause(queryType == SPARQLQueryType.SELECT_DISTINCT, selectForms);

    }

    private void checkSelectForm(SelectQuery selectQuery) {
        ProjectionChecker checker = new ProjectionChecker(selectQuery);
        Optional<ProjectionRestrictionViolation> error = checker.checkSelectClause();
        if(error.isPresent()) {
            throw error.get().getException();
        }
    }

    public SelectItem parseSelectVariableOrExpressionAsVariable() {
        if (peek(OPEN_PAR)) {
            return parseExpressionAsVariable();
        }
        else {
            return parseSelectVariable();
        }
    }

    public SelectVariable parseSelectVariable() {
        SPARQLToken token = tokenizer.consume(VariableTokenType.get());
        return new SelectVariable(new UntypedVariable(token.getImage()), token.getTokenPosition());
    }

    public SelectExpressionAsVariable parseExpressionAsVariable() {
        SPARQLToken startToken = tokenizer.consume(OPEN_PAR);
        Expression expression = parseExpression();
        tokenizer.consume(AS);
        SPARQLToken variableToken = tokenizer.consume(
                VariableTokenType.get()
        );
        UntypedVariable variable = new UntypedVariable(variableToken.getImage());
        tokenizer.getVariableManager().registerVariable(variable);
        SPARQLToken endToken = tokenizer.consume(CLOSE_PAR);
        return new SelectExpressionAsVariable(expression, variable, startToken.getTokenPosition(), endToken.getTokenPosition(), variableToken.getTokenPosition());
    }


    public GroupPattern parseWhereClause(boolean allowFreshDeclarations) {
        tokenizer.consume(WHERE);
        return parseGroupGraphPattern(allowFreshDeclarations);
    }

    private GroupPattern parseGroupGraphPattern(boolean allowFreshDeclarations) {

        ImmutableList.Builder<Pattern> patternBuilder = ImmutableList.builder();

        // GRAMMAR:
        // TriplesBlock? ( GraphPatternNotTriples '.'? TriplesBlock? )*

        tokenizer.consume(OPEN_BRACE);

        tokenizer.getVariableManager().pushVariableTypeScope();

        TriplesBlockPattern triplesBlockPattern1 = parseTriplesBlock(allowFreshDeclarations);
        if (!triplesBlockPattern1.isEmpty()) {
            patternBuilder.add(triplesBlockPattern1);
        }

        Optional<Pattern> otherPattern;
        do {
            otherPattern = parseGraphPatternNotTriples();
            if(otherPattern.isPresent()) {
                patternBuilder.add(otherPattern.get());
                if(peek(DOT)) {
                    tokenizer.consume(DOT);
                }
                TriplesBlockPattern triplesBlockPattern2 = parseTriplesBlock(allowFreshDeclarations);
                if(!triplesBlockPattern2.isEmpty()) {
                    patternBuilder.add(triplesBlockPattern2);
                }
            }
        } while (otherPattern.isPresent());

        tokenizer.getVariableManager().popVariableTypeScope();

        tokenizer.consume(CLOSE_BRACE);

        return new GroupPattern(patternBuilder.build());
    }

    public SolutionModifier parseSolutionModifier() {
        final Optional<GroupClause> groupClause;
        if(peek(GROUP)) {
            groupClause = Optional.of(parseGroupClause());
        }
        else {
            groupClause = Optional.empty();
        }
        Optional<HavingClause> havingClause;
        if(peek(HAVING)) {
            havingClause = Optional.of(parseHavingClause());
        }
        else {
            havingClause = Optional.empty();
        }
        final Optional<OrderClause> orderClause;
        if (peek(ORDER)) {
            orderClause = Optional.of(parseOrderByClause());
        }
        else {
            orderClause = Optional.empty();
        }
        return new SolutionModifier(groupClause, havingClause, orderClause);
    }

    public OrderClause parseOrderByClause() {
        tokenizer.consume(ORDER);
        tokenizer.consume(BY);
        ImmutableList.Builder<OrderCondition> conditionBuilder = ImmutableList.builder();
        while (true) {
            if(peek(ASC)) {
                conditionBuilder.add(parseOrderCondition());
            }
            else if(peek(DESC)) {
                conditionBuilder.add(parseOrderCondition());
            }
            else if(peekVariable() != null) {
                conditionBuilder.add(parseOrderCondition());
            }
            else {
                break;
            }
        }
        return new OrderClause(conditionBuilder.build());
    }

    private OrderCondition parseOrderCondition() {
        if (peek(ASC)) {
            tokenizer.consume(ASC);
            tokenizer.consume(OPEN_PAR);
            if (peekVariable() != null) {
                SPARQLToken varToken = tokenizer.consume();
                tokenizer.consume(CLOSE_PAR);
                return new OrderCondition(varToken.getImage(), OrderByModifier.ASC);
            }
            else {
                tokenizer.raiseError();
            }
        }
        else if (peek(DESC)) {
            tokenizer.consume(DESC);
            tokenizer.consume(OPEN_PAR);
            if (peekVariable() != null) {
                SPARQLToken varToken = tokenizer.consume();
                tokenizer.consume(CLOSE_PAR);
                return new OrderCondition(varToken.getImage(), OrderByModifier.DESC);
            }
            else {
                tokenizer.raiseError();
            }

        }
        else if (peekVariable() != null) {
            SPARQLToken varToken = tokenizer.consume();
            return new OrderCondition(varToken.getImage());
        }
        else {
            tokenizer.raiseError();
        }
        throw new IllegalStateException();
    }

    private GroupClause parseGroupClause() {
        tokenizer.consume(GROUP);
        tokenizer.consume(BY);
        List<GroupCondition> groupConditions = new ArrayList<>();
        while(true) {
            if(peek(BuiltInCallTokenType.get())) {
                BuiltInCallExpression expr = parseBuiltInCall();
                groupConditions.add(new GroupConditionBuiltInCall(expr));
            }
            else if(peek(OPEN_PAR)) {
                GroupConditionExpressionAs condition = parseExpressionWithOptionalAsVariable();
                groupConditions.add(condition);
            }
            else if(peekVariable() != null) {
                UntypedVariable variable = parseVariable();
                groupConditions.add(new GroupConditionVariable(variable));
            }
            else if(groupConditions.isEmpty()) {
                tokenizer.raiseError();
            }
            else {
                break;
            }
        }
        return new GroupClause(ImmutableList.copyOf(groupConditions));
    }

    private HavingClause parseHavingClause() {
        tokenizer.consume(HAVING);
        ImmutableList.Builder<HavingCondition> havingConditionBuilder = ImmutableList.builder();
        Expression expression = parseConstraint();
        havingConditionBuilder.add(new HavingCondition(expression));
        while(peek(OPEN_PAR) || peek(BuiltInCallTokenType.get())) {
            havingConditionBuilder.add(new HavingCondition(expression));
        }
        return new HavingClause(havingConditionBuilder.build());
    }

    private GroupConditionExpressionAs parseExpressionWithOptionalAsVariable() {
        tokenizer.consume(OPEN_PAR);
        Expression expr = parseExpression();
        final Optional<UntypedVariable> optionalVariable;
        if(peek(AS)) {
            tokenizer.consume(AS);
            UntypedVariable variable = parseVariable();
            tokenizer.getVariableManager().registerVariable(variable);
            optionalVariable = Optional.of(variable);
        }
        else {
            optionalVariable = Optional.empty();
        }
        tokenizer.consume(CLOSE_PAR);
        return new GroupConditionExpressionAs(expr, optionalVariable);
    }

    private SPARQLToken peekVariable() {
        return tokenizer.peek(
                VariableTokenType.get());
    }

    private boolean isVariableOfTypeOrIndirectType(SPARQLToken token, PrimitiveType primitiveType) {
        if(!token.getTokenTypes().contains(VariableTokenType.get())) {
            return false;
        }
        return tokenizer.getVariableManager().getVariableTypeIndirect(new UntypedVariable(token.getImage())).equals(Optional.of(primitiveType));
    }

    private SPARQLToken peekUntypedVariable() {
        SPARQLToken token = tokenizer.peek(VariableTokenType.get());
        if(token == null) {
            return null;
        }
        if(!tokenizer.getVariableManager().getVariableType(new UntypedVariable(token.getImage())).isPresent()) {
            return token;
        }
        else {
            return null;
        }
    }

    private SPARQLToken peekTypedVariable(PrimitiveType primitiveType) {
        SPARQLToken token = tokenizer.peek(VariableTokenType.get());
        if(token == null) {
            return null;
        }
        UntypedVariable variable = new UntypedVariable(token.getImage());
        Optional<PrimitiveType> type = tokenizer.getVariableManager().getVariableType(variable);
        if(!type.isPresent()) {
            return null;
        }
        if(type.get().equals(primitiveType)) {
            return token;
        }
        else {
            return null;
        }
    }

    public TriplesBlockPattern parseTriplesBlock(boolean allowFreshDeclarations) {
        TriplesBlockPattern.Builder builder = TriplesBlockPattern.builder();
        while (true) {
            if (peekUntypedVariable() != null) {
                SPARQLToken varNameToken = tokenizer.consume();
                parseUndeclaredVariablePropertyList(varNameToken, builder);
            }
            else if (peekTypedVariable(PrimitiveType.CLASS) != null || peek(ClassIRITokenType.get())) {
                SPARQLToken subjectToken = tokenizer.consume();
                parseClassNodePropertyList(subjectToken, builder);
            }
            else if (peekTypedVariable(PrimitiveType.OBJECT_PROPERTY) != null || peek(ObjectPropertyIRITokenType.get())) {
                SPARQLToken subjectToken = tokenizer.consume();
                parseObjectPropertyNodePropertyList(subjectToken, builder);
            }
            else if (peekTypedVariable(PrimitiveType.DATA_PROPERTY) != null || peek(DataPropertyIRITokenType.get())) {
                SPARQLToken subjectToken = tokenizer.consume();
                parseDataPropertyNodePropertyList(subjectToken, builder);
            }
            else if (peekTypedVariable(PrimitiveType.NAMED_INDIVIDUAL) != null || peek(IndividualIRITokenType.get())) {
                SPARQLToken subjectToken = tokenizer.consume();
                parseIndividualNodePropertyList(subjectToken, builder);
            }
            else if (peekTypedVariable(PrimitiveType.ANNOTATION_PROPERTY) != null || peek(AnnotationPropertyIRITokenType.get())) {
                SPARQLToken token = tokenizer.consume();
                parseAnnotationPropertyNode(token, builder);
            }
            else if (allowFreshDeclarations && peek(UntypedIRITokenType.get())) {
                // Should be allowed in construct queries
                SPARQLToken token = tokenizer.peek(UntypedIRITokenType.get());
                try {
                    IRI iri = getIRIFromToken(token);
                    tokenizer.consume();
                    tokenizer.consume(RDF_TYPE);
                    if(tokenizer.peek(OWL_CLASS) != null) {
                        tokenizer.consume();
                        builder.add(new Declaration(new NamedClass(iri)));
                        tokenizer.registerType(iri, ClassIRITokenType.get());
                    }
                    else if(tokenizer.peek(OWL_OBJECT_PROPERTY) != null) {
                        tokenizer.consume();
                        builder.add(new Declaration(new ObjectProperty(iri)));
                        tokenizer.registerType(iri, ObjectPropertyIRITokenType.get());
                    }
                    else if(tokenizer.peek(OWL_DATA_PROPERTY) != null) {
                        tokenizer.consume();
                        builder.add(new Declaration(new ObjectProperty(iri)));
                        tokenizer.registerType(iri, DataPropertyIRITokenType.get());
                    }
                    else if(tokenizer.peek(OWL_ANNOTATION_PROPERTY) != null) {
                        tokenizer.consume();
                        builder.add(new Declaration(new AnnotationProperty(iri)));
                        tokenizer.registerType(iri, AnnotationPropertyIRITokenType.get());
                    }
                    else if(tokenizer.peek(OWL_NAMED_INDIVIDUAL) != null) {
                        tokenizer.consume();
                        builder.add(new Declaration(new NamedIndividual(iri)));
                        tokenizer.registerType(iri, IndividualIRITokenType.get());
                    }
                    else if(tokenizer.peek(OWL_DATATYPE) != null) {
                        tokenizer.consume();
                        builder.add(new Declaration(new Datatype(iri)));
                        tokenizer.registerType(iri, DataPropertyIRITokenType.get());
                    }
                } catch (RuntimeException e) {
                    tokenizer.raiseError();
                }

            }

            // Carry on if we see a dot because we expect further triples
            if (peek(DOT)) {
                tokenizer.consume(DOT);
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
        if(peek(OPEN_BRACE)) {
            return Optional.of(parseGroupGraphPatternOrUnion());
        }
        else if (peek(OPTIONAL_KW)) {
            return Optional.<Pattern>of(parseOptionalGraphPattern());
        }
        else if (peek(MINUS_KW)) {
            return Optional.<Pattern>of(parseMinusGraphPattern());
        }
        else if (peek(FILTER)) {
            return Optional.<Pattern>of(parseFilterCondition());
        }
        else if (peek(BIND)) {
            return Optional.<Pattern>of(parseBind());
        }


        return Optional.empty();
    }

    private Pattern parseGroupGraphPatternOrUnion() {
        ImmutableList.Builder<Pattern> patternsBuilder = ImmutableList.builder();
        // GRAMMAR:
        // 	GroupGraphPattern ( 'UNION' GroupGraphPattern )*
        GroupPattern groupPattern = parseGroupGraphPattern(false);
        patternsBuilder.add(groupPattern);
        while(peek(UNION)) {
            UnionPattern unionPattern = parseUnionGraphPattern();
            patternsBuilder.add(unionPattern);
        }
        ImmutableList<Pattern> patterns = patternsBuilder.build();
        return new GroupPattern(patterns);
    }

    private UnionPattern parseUnionGraphPattern() {
        tokenizer.consume(UNION);
        GroupPattern groupPattern = parseGroupGraphPattern(false);
        return new UnionPattern(groupPattern);
    }

    private MinusPattern parseMinusGraphPattern() {
        tokenizer.consume(MINUS_KW);
        GroupPattern groupPattern = parseGroupGraphPattern(false);
        return new MinusPattern(groupPattern);
    }


    private OptionalPattern parseOptionalGraphPattern() {
        tokenizer.consume(OPTIONAL_KW);
        GroupPattern groupPattern = parseGroupGraphPattern(false);
        return new OptionalPattern(groupPattern);
    }

    private FilterPattern parseFilterCondition() {
        tokenizer.consume(FILTER);
        Expression expression = parseConstraint();
        return new FilterPattern(expression);
    }


    private Expression parseConstraint() {
        Expression expression = null;
        if (peek(OPEN_PAR)) {
            expression = parseBracketedExpression();
        }
        else if (peek(BuiltInCallTokenType.get())) {
            expression = parseBuiltInCall();
        }
        else {
            tokenizer.raiseError();
        }
        return expression;
    }

    private BindPattern parseBind() {
        tokenizer.consume(BIND);
        tokenizer.consume(OPEN_PAR);
        Expression expression = parseConstraint();
        tokenizer.consume(AS);
        Variable variable = parseVariable();
        tokenizer.consume(CLOSE_PAR);
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
            if (peek(OWL_SAME_AS)) {
                parseSameAs(subjectToken, builder);
            }
            else if (peek(OWL_DIFFERENT_FROM)) {
                parseDifferentFrom(subjectToken, builder);
            }
            else if (peek(RDF_TYPE)) {
                parseIndividualType(subjectToken, builder);
            }
            else if (peek(ObjectPropertyIRITokenType.get()) || peekTypedVariable(PrimitiveType.OBJECT_PROPERTY) != null) {
                parseObjectPropertyAssertion(subjectToken, builder);
            }
            else if (peek(DataPropertyIRITokenType.get())|| peekTypedVariable(PrimitiveType.DATA_PROPERTY) != null) {
                parseDataPropertyAssertions(subjectToken, builder);
            }
            else if (peek(AnnotationPropertyIRITokenType.get()) || peekTypedVariable(PrimitiveType.ANNOTATION_PROPERTY) != null) {
                parseAnnotationAssertions(subjectToken, builder);
            }
            else if (peek(VariableTokenType.get())) {
                tokenizer.getVariableManager().registerVariable(new UntypedVariable(tokenizer.peek().getImage()), PrimitiveType.ANNOTATION_PROPERTY);
                parseAnnotationAssertions(subjectToken, builder);
            }
            else if (peek(VariableTokenType.get())) {
                SPARQLToken propertyToken = tokenizer.consume();
                // Object Property, Data Property or Annotation Property?
                if (peek(UntypedIRITokenType.get())) {
                    HasAnnotationSubject subject = getAtomicIndividualFromToken(subjectToken);
                    AtomicAnnotationProperty property = getAnnotationPropertyFromToken(propertyToken);
                    tokenizer.getVariableManager().registerVariable(new UntypedVariable(propertyToken.getImage()), PrimitiveType.ANNOTATION_PROPERTY);
                    for (AnnotationValue value : parseAnnotationValueObjectList()) {
                        builder.add(new AnnotationAssertion(property, subject.toAnnotationSubject(), value));
                    }
                }
                else if (peek(IndividualIRITokenType.get()) || peekTypedVariable(PrimitiveType.NAMED_INDIVIDUAL) != null) {
                    AtomicIndividual subject = getAtomicIndividualFromToken(subjectToken);
                    AtomicObjectProperty property = getObjectPropertyFromToken(propertyToken);
                    tokenizer.getVariableManager().registerVariable(new UntypedVariable(propertyToken.getImage()), PrimitiveType.OBJECT_PROPERTY);
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
            if (peek(SEMI_COLON)) {
                tokenizer.consume();
            }
            else {
                break;
            }
        }
    }

    /**
     * Parses a node that is an annotation property IRI or an annotation property variable. Possible predicates are
     * annotation property IRI, annotation property variable, rdfs:subPropertyOf, rdfs:domain and rdfs:range
     * @param subjectToken The node
     */
    private void parseAnnotationPropertyNode(SPARQLToken subjectToken, TriplesBlockPattern.Builder builder) {
        while (true) {
            if (peek(AnnotationPropertyIRITokenType.get()) || peekTypedVariable(PrimitiveType.ANNOTATION_PROPERTY) != null) {
                parseAnnotationAssertions(subjectToken, builder);
            }
            else if (peek(VariableTokenType.get())) {
                tokenizer.getVariableManager().registerVariable(new UntypedVariable(tokenizer.peek().getImage()), PrimitiveType.ANNOTATION_PROPERTY);
                parseAnnotationAssertions(subjectToken, builder);
            }
            else if (peek(RDFS_SUB_PROPERTY_OF)) {
//                parseAnnotationSubPropertyOf(subjectToken, builder);
                tokenizer.raiseError();
            }
            else if (peek(RDFS_DOMAIN)) {
                tokenizer.raiseError();
            }
            else if (peek(RDFS_RANGE)) {
                tokenizer.raiseError();
            }
            else {
                tokenizer.raiseError();
            }
            if (peek(SEMI_COLON)) {
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
        List<ClassExpression> types = parseClassNodeObjectList();
        for (ClassExpression type : types) {
            builder.add(new ClassAssertion(type, individual));
        }
    }

    private void parseDataPropertyNodePropertyList(SPARQLToken subjectToken, TriplesBlockPattern.Builder builder) {
        while (true) {

            AtomicDataProperty subject = getDataPropertyFromToken(subjectToken);
            if (peek(RDFS_SUB_PROPERTY_OF)) {
                tokenizer.consume();
                List<AtomicDataProperty> properties = parseDataPropertyNodeObjectList();
                for (AtomicDataProperty property : properties) {
                    builder.add(new SubDataPropertyOf(subject, property));
                }
            }
            else if (peek(OWL_EQUIVALENT_PROPERTY)) {
                tokenizer.consume();
                List<AtomicDataProperty> properties = parseDataPropertyNodeObjectList();
                for (AtomicDataProperty property : properties) {
                    builder.add(new EquivalentDataProperties(subject, property));
                }
            }
            else if (peek(OWL_DISJOINT_WITH)) {
                tokenizer.consume();
                List<AtomicDataProperty> properties = parseDataPropertyNodeObjectList();
                for (AtomicDataProperty property : properties) {
                    builder.add(new DisjointDataProperties(subject, property));
                }
            }
            else if (peek(RDFS_DOMAIN)) {
                tokenizer.consume();
                List<ClassExpression> clses = parseClassNodeObjectList();
                for (ClassExpression cls : clses) {
                    builder.add(new DataPropertyDomain(subject, cls));
                }
            }
            else if (peek(RDFS_RANGE)) {
                tokenizer.consume();
                List<AtomicDatatype> dataTypes = parseDataTypeNodeObjectList();
                for (AtomicDatatype datatype : dataTypes) {
                    builder.add(new DataPropertyRange(subject, datatype));
                }
            }
            else if (peek(RDF_TYPE)) {
                tokenizer.consume();
                if (peek(OWL_FUNCTIONAL_PROPERTY)) {
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
            if (peek(SEMI_COLON)) {
                tokenizer.consume();
            }
            else {
                break;
            }
        }
    }

    private void parseObjectPropertyNodePropertyList(SPARQLToken subjectToken, TriplesBlockPattern.Builder builder) {
        while (true) {
            if (peek(RDFS_SUB_PROPERTY_OF)) {
                tokenizer.consume();
                List<AtomicObjectProperty> objects = parseObjectPropertyNodeObjectList();
                AtomicObjectProperty subject = getObjectPropertyFromToken(subjectToken);
                for (AtomicObjectProperty object : objects) {
                    builder.add(new SubObjectPropertyOf(subject, object));
                }
            }
            else if (peek(OWL_EQUIVALENT_PROPERTY)) {
                tokenizer.consume();
                List<AtomicObjectProperty> objects = parseObjectPropertyNodeObjectList();
                AtomicObjectProperty subject = getObjectPropertyFromToken(subjectToken);
                for (AtomicObjectProperty object : objects) {
                    builder.add(new EquivalentObjectProperties(subject, object));
                }
            }
            else if (peek(OWL_DISJOINT_WITH)) {
                tokenizer.consume();

                List<AtomicObjectProperty> objects = parseObjectPropertyNodeObjectList();
                AtomicObjectProperty subject = getObjectPropertyFromToken(subjectToken);
                for (AtomicObjectProperty object : objects) {
                    builder.add(new DisjointObjectProperties(subject, object));
                }
            }
            else if (peek(OWL_INVERSE_OF)) {
                tokenizer.consume();
                List<AtomicObjectProperty> objects = parseObjectPropertyNodeObjectList();
                AtomicObjectProperty subject = getObjectPropertyFromToken(subjectToken);
                for (AtomicObjectProperty object : objects) {
                    builder.add(new InverseObjectProperties(subject, object));
                }
            }
            else if (peek(RDFS_DOMAIN)) {
                tokenizer.consume();
                List<ClassExpression> clses = parseClassNodeObjectList();
                AtomicObjectProperty subject = getObjectPropertyFromToken(subjectToken);
                for (ClassExpression cls : clses) {
                    builder.add(new ObjectPropertyDomain(subject, cls));
                }
            }
            else if (peek(RDFS_RANGE)) {
                tokenizer.consume();
                List<ClassExpression> clses = parseClassNodeObjectList();
                AtomicObjectProperty subject = getObjectPropertyFromToken(subjectToken);
                for (ClassExpression cls : clses) {
                    builder.add(new ObjectPropertyRange(subject, cls));
                }
            }
            else if (peek(RDF_TYPE)) {
                tokenizer.consume(RDF_TYPE);
                parseObjectPropertyTypeObjectList(subjectToken, builder);
            }
            else if (peek(AnnotationPropertyIRITokenType.get()) || peekTypedVariable(PrimitiveType.ANNOTATION_PROPERTY) != null) {
                parseAnnotationAssertions(subjectToken, builder);
            }
            else if (peek(VariableTokenType.get())) {
                tokenizer.getVariableManager().registerVariable(new UntypedVariable(tokenizer.peek().getImage()), PrimitiveType.ANNOTATION_PROPERTY);
                parseAnnotationAssertions(subjectToken, builder);
            }
            else {
                tokenizer.raiseError();
            }
            if (peek(SEMI_COLON)) {
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
            if (peek(OWL_FUNCTIONAL_PROPERTY)) {
                tokenizer.consume();
                builder.add(new FunctionalObjectProperty(objectProperty));
            }
            else if (peek(OWL_INVERSE_FUNCTIONAL_PROPERTY)) {
                tokenizer.consume();
                builder.add(new InverseFunctionalObjectProperty(objectProperty));
            }
            else if (peek(OWL_SYMMETRIC_PROPERTY)) {
                tokenizer.consume();
                builder.add(new SymmetricObjectProperty(objectProperty));
            }
            else if (peek(OWL_TRANSITIVE_PROPERTY)) {
                tokenizer.consume();
                builder.add(new TransitiveObjectProperty(objectProperty));
            }
            else if (peek(OWL_REFLEXIVE_PROPERTY)) {
                tokenizer.consume();
                builder.add(new ReflexiveObjectProperty(objectProperty));
            }
            else if (peek(OWL_IRREFLEXIVE_PROPERTY)) {
                tokenizer.consume();
                builder.add(new IrreflexiveObjectProperty(objectProperty));
            }
            else if (peek(OWL_ASYMMETRIC_PROPERTY)) {
                tokenizer.consume();
                builder.add(new AsymmetricObjectProperty(objectProperty));
            }
            else {
                tokenizer.raiseError();
            }
            if (peek(COMMA)) {
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
            if (peek(RDFS_SUBCLASS_OF)) {
                parseSubClassOf(subjectToken, builder);
            }
            else if (peek(OWL_EQUIVALENT_CLASS)) {
                parseEquivalentClasses(subjectToken, builder);
            }
            else if (peek(OWL_DISJOINT_WITH)) {
                parseDisjointWith(subjectToken, builder);
            }
            else if (peek(AnnotationPropertyIRITokenType.get()) || peekTypedVariable(PrimitiveType.ANNOTATION_PROPERTY) != null) {
                parseAnnotationAssertions(subjectToken, builder);
            }
            else if (peek(VariableTokenType.get())) {
                tokenizer.getVariableManager().registerVariable(new UntypedVariable(tokenizer.peek().getImage()), PrimitiveType.ANNOTATION_PROPERTY);
                parseAnnotationAssertions(subjectToken, builder);
            }
            else {
                tokenizer.raiseError();
            }
            if (peek(SEMI_COLON)) {
                tokenizer.consume();
            }
            else {
                break;
            }
        }
    }

    private void parseDisjointWith(SPARQLToken subjectToken, TriplesBlockPattern.Builder builder) {
        tokenizer.consume(OWL_DISJOINT_WITH);
        AtomicClass subject = getAtomicClassFromToken(subjectToken);
        List<ClassExpression> clses = parseClassNodeObjectList();
        for (ClassExpression object : clses) {
            builder.add(new DisjointClasses(subject, object));
        }
    }

    private void parseEquivalentClasses(SPARQLToken subjectToken, TriplesBlockPattern.Builder builder) {
        tokenizer.consume(OWL_EQUIVALENT_CLASS);
        AtomicClass subject = getAtomicClassFromToken(subjectToken);
        List<ClassExpression> clses = parseClassNodeObjectList();
        for (ClassExpression object : clses) {
            builder.add(new EquivalentClasses(subject, object));
        }
    }

    private void parseSubClassOf(SPARQLToken subjectToken, TriplesBlockPattern.Builder builder) {
        tokenizer.consume(RDFS_SUBCLASS_OF);
        AtomicClass subject = getAtomicClassFromToken(subjectToken);
        List<ClassExpression> clses = parseClassNodeObjectList();
        for (ClassExpression object : clses) {
            builder.add(new SubClassOf(subject, object));
        }
    }

    private AtomicClass getAtomicClassFromToken(SPARQLToken token) {
        if (token.hasTokenType(VariableTokenType.get())) {
            return new ClassVariable(token.getImage());
        }
        else {
            IRI clsIRI = getIRIFromToken(token);
            return new NamedClass(clsIRI);
        }
    }

    private UntypedVariable parseVariable() {
        SPARQLToken token = tokenizer.consume(
                VariableTokenType.get()
        );
        return new UntypedVariable(token.getImage());
    }

    private void parseUndeclaredVariablePropertyList(SPARQLToken varNameToken, TriplesBlockPattern.Builder builder) {
        while (true) {
            if (peek(RDF_TYPE)) {
                parseUndeclaredVariableTypeTriples(varNameToken, builder);
            }
            else if (peek(RDFS_SUBCLASS_OF)) {
                tokenizer.getVariableManager().registerVariable(new UntypedVariable(varNameToken.getImage()), PrimitiveType.CLASS);
                parseSubClassOf(varNameToken, builder);
            }
            else if (peek(OWL_EQUIVALENT_CLASS)) {
                tokenizer.getVariableManager().registerVariable(new UntypedVariable(varNameToken.getImage()), PrimitiveType.CLASS);
                parseEquivalentClasses(varNameToken, builder);
            }
            else if (peek(OWL_DISJOINT_WITH)) {
                tokenizer.getVariableManager().registerVariable(new UntypedVariable(varNameToken.getImage()), PrimitiveType.CLASS);
                parseDisjointWith(varNameToken, builder);
            }
            else if (peek(RDFS_SUB_PROPERTY_OF)) {
                parseUndeclaredVariableSubPropertyOf(varNameToken, builder);
            }
            else if (peek(OWL_EQUIVALENT_PROPERTY)) {
                parseUndeclaredVariableEquivalentProperty(varNameToken, builder);
            }
            // CAN'T DISAMBIGUATE!
//            else if (peek(RDFS_DOMAIN)) {
//                tokenizer.consume();
//            }
            else if (peek(RDFS_RANGE)) {
                parseUndeclaredVariableRange(varNameToken, builder);
            }
            else if (peek(OWL_PROPERTY_DISJOINT_WITH)) {
                parseUndeclaredVariablePropertyDisjointWith();
            }
            else if (peek(OWL_SAME_AS)) {
                tokenizer.getVariableManager().registerVariable(new UntypedVariable(varNameToken.getImage()), PrimitiveType.NAMED_INDIVIDUAL);
                parseSameAs(varNameToken, builder);
            }
            else if (peek(OWL_DIFFERENT_FROM)) {
                tokenizer.getVariableManager().registerVariable(new UntypedVariable(varNameToken.getImage()), PrimitiveType.NAMED_INDIVIDUAL);
                parseDifferentFrom(varNameToken, builder);
            }
            else if (peekTypedVariable(PrimitiveType.OBJECT_PROPERTY) != null) {
                tokenizer.getVariableManager().registerVariable(new UntypedVariable(varNameToken.getImage()), PrimitiveType.NAMED_INDIVIDUAL);
                parseObjectPropertyAssertion(varNameToken, builder);
            }
            else if (peek(ObjectPropertyIRITokenType.get())) {
                tokenizer.getVariableManager().registerVariable(new UntypedVariable(varNameToken.getImage()), PrimitiveType.NAMED_INDIVIDUAL);
                parseObjectPropertyAssertion(varNameToken, builder);
            }
            else if (peekTypedVariable(PrimitiveType.DATA_PROPERTY) != null) {
                tokenizer.getVariableManager().registerVariable(new UntypedVariable(varNameToken.getImage()), PrimitiveType.NAMED_INDIVIDUAL);
                parseDataPropertyAssertions(varNameToken, builder);
            }
            else if (peekTypedVariable(PrimitiveType.ANNOTATION_PROPERTY) != null) {
                parseAnnotationAssertions(varNameToken, builder);
            }
            else if (peek(DataPropertyIRITokenType.get())) {
                tokenizer.getVariableManager().registerVariable(new UntypedVariable(varNameToken.getImage()), PrimitiveType.NAMED_INDIVIDUAL);
                parseDataPropertyAssertions(varNameToken, builder);
            }
            else if (peek(AnnotationPropertyIRITokenType.get())) {
//                tokenizer.registerVariable(varNameToken.getImage(), EntityType.NAMED_INDIVIDUAL);
                parseAnnotationAssertions(varNameToken, builder);
            }
            else {
                tokenizer.raiseError();
            }
            if (!tokenizer.getVariableManager().getVariableType(new UntypedVariable(varNameToken.getImage())).isPresent()) {
                if (peek(SEMI_COLON)) {
                    tokenizer.consume();
                    // Has become typed
                    Optional<PrimitiveType> type = tokenizer.getVariableManager().getVariableType(new UntypedVariable(varNameToken.getImage()));
                    if (type.equals(Optional.of(PrimitiveType.CLASS))) {
                        parseClassNodePropertyList(varNameToken, builder);
                    }
                    else if (type.equals(Optional.of(PrimitiveType.OBJECT_PROPERTY))) {
                        parseObjectPropertyNodePropertyList(varNameToken, builder);
                    }
                    else if (type.equals(Optional.of(PrimitiveType.DATA_PROPERTY))) {
                        parseDataPropertyNodePropertyList(varNameToken, builder);
                    }
                    else if (type.equals(Optional.of(PrimitiveType.NAMED_INDIVIDUAL))) {
                        parseIndividualNodePropertyList(varNameToken, builder);
                    }
                    break;
                }
                else {
                    break;
                }

            }
            else {
                if (peek(SEMI_COLON)) {
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
            tokenizer.getVariableManager().registerVariable(new UntypedVariable(varNameToken.getImage()), PrimitiveType.OBJECT_PROPERTY);
            AtomicObjectProperty subject = getObjectPropertyFromToken(varNameToken);
            builder.add(new SubObjectPropertyOf(subject, (AtomicObjectProperty) entity));
        }
        else if (entity instanceof AtomicDataProperty) {
            tokenizer.getVariableManager().registerVariable(new UntypedVariable(varNameToken.getImage()), PrimitiveType.DATA_PROPERTY);
            AtomicDataProperty subject = getDataPropertyFromToken(varNameToken);
            builder.add(new SubDataPropertyOf(subject, (AtomicDataProperty) entity));

        }
        else if (entity instanceof AtomicAnnotationProperty) {
            tokenizer.getVariableManager().registerVariable(new UntypedVariable(varNameToken.getImage()), PrimitiveType.ANNOTATION_PROPERTY);
            AtomicAnnotationProperty subject = getAnnotationPropertyFromToken(varNameToken);
            builder.add(new SubAnnotationPropertyOf(subject, (AtomicAnnotationProperty) entity));
        }
    }

    private void parseUndeclaredVariableEquivalentProperty(SPARQLToken varNameToken, TriplesBlockPattern.Builder builder) {
        // TODO: Multiple Objects?
        tokenizer.consume(OWL_EQUIVALENT_PROPERTY);
        AtomicProperty prop = parseTypedProperty(EntityType.OBJECT_PROPERTY, EntityType.DATA_PROPERTY);
        if (prop instanceof AtomicObjectProperty) {
            tokenizer.getVariableManager().registerVariable(new UntypedVariable(varNameToken.getImage()), PrimitiveType.OBJECT_PROPERTY);
            AtomicObjectProperty subject = getObjectPropertyFromToken(varNameToken);
            builder.add(new EquivalentObjectProperties(subject, (AtomicObjectProperty) prop));
        }
        else if (prop instanceof AtomicDataProperty) {
            tokenizer.getVariableManager().registerVariable(new UntypedVariable(varNameToken.getImage()), PrimitiveType.DATA_PROPERTY);
            AtomicDataProperty subject = getDataPropertyFromToken(varNameToken);
            builder.add(new EquivalentDataProperties(subject, (AtomicDataProperty) prop));

        }
    }

    private void parseUndeclaredVariableRange(SPARQLToken varNameToken, TriplesBlockPattern.Builder builder) {
        // TODO: Multiple Objects
        tokenizer.consume(RDFS_RANGE);
        if (peek(ClassIRITokenType.get()) || peekTypedVariable(PrimitiveType.CLASS) != null) {
            tokenizer.getVariableManager().registerVariable(new UntypedVariable(varNameToken.getImage()), PrimitiveType.OBJECT_PROPERTY);
            AtomicClass cls = getAtomicClassFromToken(tokenizer.consume());
            AtomicObjectProperty property = getObjectPropertyFromToken(varNameToken);
            builder.add(new ObjectPropertyRange(property, cls));
        }
        else if (peek(DatatypeIRITokenType.get()) || peekTypedVariable(PrimitiveType.DATATYPE) != null) {
            tokenizer.getVariableManager().registerVariable(new UntypedVariable(varNameToken.getImage()), PrimitiveType.DATA_PROPERTY);
            AtomicDatatype dt = getDatatypeFromToken(tokenizer.consume());
            AtomicDataProperty property = getDataPropertyFromToken(varNameToken);
            builder.add(new DataPropertyRange(property, dt));
        }
        else if (peek(AnnotationPropertyIRITokenType.get()) || peekTypedVariable(PrimitiveType.CLASS) != null) {
            tokenizer.getVariableManager().registerVariable(new UntypedVariable(varNameToken.getImage()), PrimitiveType.ANNOTATION_PROPERTY);
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
        if (peek(OWL_CLASS)) {
            tokenizer.getVariableManager().registerVariable(new UntypedVariable(subjectToken.getImage()), PrimitiveType.CLASS);
            tokenizer.consume(OWL_CLASS);
            AtomicClass cls = getAtomicClassFromToken(subjectToken);
            builder.add(new Declaration(cls));
        }
        else if (peek(OWL_OBJECT_PROPERTY)) {
            tokenizer.getVariableManager().registerVariable(new UntypedVariable(subjectToken.getImage()), PrimitiveType.OBJECT_PROPERTY);
            tokenizer.consume(OWL_OBJECT_PROPERTY);
            AtomicObjectProperty prop = getObjectPropertyFromToken(subjectToken);
            builder.add(new Declaration(prop));
        }
        else if (peek(OWL_DATA_PROPERTY)) {
            tokenizer.getVariableManager().registerVariable(new UntypedVariable(subjectToken.getImage()), PrimitiveType.DATA_PROPERTY);
            tokenizer.consume(OWL_DATA_PROPERTY);
            AtomicDataProperty prop = getDataPropertyFromToken(subjectToken);
            builder.add(new Declaration(prop));
        }
        else if (peek(OWL_NAMED_INDIVIDUAL)) {
            tokenizer.getVariableManager().registerVariable(new UntypedVariable(subjectToken.getImage()), PrimitiveType.NAMED_INDIVIDUAL);
            tokenizer.consume(OWL_NAMED_INDIVIDUAL);
            AtomicIndividual ind = getAtomicIndividualFromToken(subjectToken);
            builder.add(new Declaration(ind));
        }
        else if (peek(OWL_ANNOTATION_PROPERTY)) {
            tokenizer.getVariableManager().registerVariable(new UntypedVariable(subjectToken.getImage()), PrimitiveType.ANNOTATION_PROPERTY);
            tokenizer.consume(OWL_ANNOTATION_PROPERTY);
            AtomicAnnotationProperty prop = getAnnotationPropertyFromToken(subjectToken);
            builder.add(new Declaration(prop));
        }
        else if (peek(OWL_INVERSE_FUNCTIONAL_PROPERTY, OWL_TRANSITIVE_PROPERTY, OWL_SYMMETRIC_PROPERTY, OWL_REFLEXIVE_PROPERTY, OWL_IRREFLEXIVE_PROPERTY)) {
            tokenizer.getVariableManager().registerVariable(new UntypedVariable(subjectToken.getImage()), PrimitiveType.OBJECT_PROPERTY);
            parseObjectPropertyTypeObjectList(subjectToken, builder);
        }
        else if (peekTypedVariable(PrimitiveType.CLASS) != null || peek(ClassIRITokenType.get())) {
            tokenizer.getVariableManager().registerVariable(new UntypedVariable(subjectToken.getImage()), PrimitiveType.NAMED_INDIVIDUAL);
            parseSubjectTypeClass(subjectToken, builder);
        }
        else if (peek(VariableTokenType.get())) {
            tokenizer.getVariableManager().registerVariable(new UntypedVariable(subjectToken.getImage()), PrimitiveType.NAMED_INDIVIDUAL);
            SPARQLToken typeNode = tokenizer.consume();
            tokenizer.getVariableManager().registerVariable(new UntypedVariable(typeNode.getImage()), PrimitiveType.CLASS);
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
        if(isVariableOfTypeOrIndirectType(subjectToken, PrimitiveType.CLASS)) {
            subject = new IRIVariable(subjectToken.getImage());
        }
        else if(isVariableOfTypeOrIndirectType(subjectToken, PrimitiveType.DATATYPE)) {
            subject = new IRIVariable(subjectToken.getImage());
        }
        else if(isVariableOfTypeOrIndirectType(subjectToken, PrimitiveType.OBJECT_PROPERTY)) {
            subject = new IRIVariable(subjectToken.getImage());
        }
        else if(isVariableOfTypeOrIndirectType(subjectToken, PrimitiveType.DATA_PROPERTY)) {
            subject = new IRIVariable(subjectToken.getImage());
        }
        else if(isVariableOfTypeOrIndirectType(subjectToken, PrimitiveType.ANNOTATION_PROPERTY)) {
            subject = new IRIVariable(subjectToken.getImage());
        }
        else if(isVariableOfTypeOrIndirectType(subjectToken, PrimitiveType.NAMED_INDIVIDUAL)) {
            subject = new IRIVariable(subjectToken.getImage());
        }
        else if(subjectToken.hasTokenType(VariableTokenType.get())) {
            subject = new IRIVariable(subjectToken.getImage());
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
        if (possibleTypes.contains(EntityType.OBJECT_PROPERTY) && (peek(ObjectPropertyIRITokenType.get()) || peekTypedVariable(PrimitiveType.OBJECT_PROPERTY) != null)) {
            SPARQLToken token = tokenizer.consume();
            property = getObjectPropertyFromToken(token);
        }
        else if (possibleTypes.contains(EntityType.DATA_PROPERTY) && (peek(DataPropertyIRITokenType.get()) || peekTypedVariable(PrimitiveType.DATA_PROPERTY) != null)) {
            SPARQLToken token = tokenizer.consume();
            property = getDataPropertyFromToken(token);
        }
        else if (possibleTypes.contains(EntityType.ANNOTATION_PROPERTY) && (peek(AnnotationPropertyIRITokenType.get()) || peekTypedVariable(PrimitiveType.ANNOTATION_PROPERTY) != null)) {
            SPARQLToken token = tokenizer.consume();
            property = getAnnotationPropertyFromToken(token);
        }
        else {
            tokenizer.raiseError();
        }
        return property;
    }


    public void parsePrefixDeclaration() {
        tokenizer.consume(PREFIX);
        SPARQLToken prefixName;
        if (!tokenizer.getPrefixManager().containsPrefixMapping(":")) {
            prefixName = tokenizer.consume(PrefixNameTokenType.get(), SPARQLTerminalTokenType.get(COLON));
        }
        else {
            prefixName = tokenizer.consume(PrefixNameTokenType.get());
        }
        SPARQLToken prefix = tokenizer.consume(UntypedIRITokenType.get(),
                EntityIRITokenType.get(EntityType.ANNOTATION_PROPERTY),
                EntityIRITokenType.get(EntityType.DATA_PROPERTY),
                EntityIRITokenType.get(EntityType.OBJECT_PROPERTY),
                EntityIRITokenType.get(EntityType.CLASS),
                EntityIRITokenType.get(EntityType.NAMED_INDIVIDUAL),
                PrologueDeclarationIRITokenType.get());
        String prefixImage = prefix.getImage();
        String iriString = prefixImage.substring(1, prefixImage.length() - 1);
        tokenizer.registerPrefix(prefixName.getImage(), iriString);

    }

    public void parseBaseDeclaration() {
        tokenizer.consume(BASE);
        SPARQLToken token = tokenizer.consume();
        tokenizer.setBase(token.getImage());
    }

    private List<ClassExpression> parseClassNodeObjectList() {
        List<ClassExpression> result = new ArrayList<>();
        while (true) {
            ClassExpression cls = parseClassNode();
            result.add(cls);
            if (peek(COMMA)) {
                tokenizer.consume();
            }
            else {
                break;
            }
        }
        return result;
    }

    private ClassExpression parseClassNode() {
        AtomicClass result = null;
        if (peek(VariableTokenType.get())) {
            SPARQLToken token = tokenizer.consume(VariableTokenType.get());
            tokenizer.getVariableManager().registerVariable(new UntypedVariable(token.getImage()), PrimitiveType.CLASS);
            result = new ClassVariable(token.getImage());
        }
        else if (peekTypedVariable(PrimitiveType.CLASS) != null) {
            SPARQLToken token = tokenizer.consume();
            result = new ClassVariable(token.getImage());
        }
        else if (peek(ClassIRITokenType.get())) {
            SPARQLToken token = tokenizer.consume();
            result = new NamedClass(getIRIFromToken(token));
        }
        else {
            tokenizer.raiseError();
        }
        return result;
    }

    private AtomicDatatype getDatatypeFromToken(SPARQLToken token) {
        if(token.hasTokenType(VariableTokenType.get())) {
            return new DatatypeVariable(token.getImage());
        }
        else {
            IRI iri = getIRIFromToken(token);
            return  Datatype.get(iri);
        }
    }
    
    private AtomicObjectProperty getObjectPropertyFromToken(SPARQLToken token) {
        if(token.hasTokenType(VariableTokenType.get())) {
            return new ObjectPropertyVariable(token.getImage());
        }
        else {
            IRI iri = getIRIFromToken(token);
            return new ObjectProperty(iri);
        }
    }

    private AtomicDataProperty getDataPropertyFromToken(SPARQLToken token) {
        if(token.hasTokenType(VariableTokenType.get())) {
            return new DataPropertyVariable(token.getImage());
        }
        else {
            IRI iri = getIRIFromToken(token);
            return new DataProperty(iri);
        }
    }

    private AtomicAnnotationProperty getAnnotationPropertyFromToken(SPARQLToken token) {
        if(token.hasTokenType(VariableTokenType.get())) {
            return new AnnotationPropertyVariable(token.getImage());
        }
        else {
            IRI iri = getIRIFromToken(token);
            return new AnnotationProperty(iri);
        }
    }

    private AtomicIndividual getAtomicIndividualFromToken(SPARQLToken token) {
        if(token.hasTokenType(VariableTokenType.get())) {
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
        else if (token.hasTokenType(VariableTokenType.get())) {
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
            if (peek(COMMA)) {
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
        if (peek(VariableTokenType.get())) {
            SPARQLToken token = tokenizer.consume(VariableTokenType.get());
            tokenizer.getVariableManager().registerVariable(new UntypedVariable(token.getImage()), PrimitiveType.NAMED_INDIVIDUAL);
            result = new IndividualVariable(token.getImage());
        }
        else if (peekTypedVariable(PrimitiveType.NAMED_INDIVIDUAL) != null) {
            SPARQLToken token = tokenizer.consume();
            result = new IndividualVariable(token.getImage());
        }
        else if (peek(IndividualIRITokenType.get())) {
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
            if (peek(COMMA)) {
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
        if (peek(UntypedIRITokenType.get())) {
            SPARQLToken token = tokenizer.consume();
            result = new AtomicIRI(getIRIFromToken(token));
        }
        else if (peekEntityIRI() != null) {
            SPARQLToken token = tokenizer.consume();
            result = new AtomicIRI(getIRIFromToken(token));
        }
        else if (peek(VariableTokenType.get())) {
            SPARQLToken token = tokenizer.consume();
            tokenizer.getVariableManager().registerVariable(new UntypedVariable(token.getImage()));
            result = new AnnotationValueVariable(token.getImage());
        }
        else if (peek(StringTokenType.get())) {
            result = parseLiteralNode();
        }
        else if (peek(IntegerTokenType.get())) {
            result = parseLiteralNode();
        }
        else if (peek(DecimalTokenType.get())) {
            result = parseLiteralNode();
        }
        else if (peek(DoubleTokenType.get())) {
            result = parseLiteralNode();
        }
        else if (peek(BooleanTokenType.get())) {
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
            if (peek(COMMA)) {
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
        if (peek(VariableTokenType.get())) {
            SPARQLToken token = tokenizer.consume(VariableTokenType.get());
            tokenizer.getVariableManager().registerVariable(new UntypedVariable(token.getImage()), PrimitiveType.OBJECT_PROPERTY);
            result = new ObjectPropertyVariable(token.getImage());
        }
        else if (peekTypedVariable(PrimitiveType.OBJECT_PROPERTY) != null) {
            SPARQLToken token = tokenizer.consume();
            result = new ObjectPropertyVariable(token.getImage());
        }
        else if (peek(ObjectPropertyIRITokenType.get())) {
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
            if (peek(COMMA)) {
                tokenizer.consume();
            }
            else {
                break;
            }
        }
        return result;
    }

    private AtomicDataProperty parseDataPropertyNode() {
        if (peek(VariableTokenType.get())) {
            SPARQLToken token = tokenizer.consume(VariableTokenType.get());
            tokenizer.getVariableManager().registerVariable(new UntypedVariable(token.getImage()), PrimitiveType.DATA_PROPERTY);
            return new DataPropertyVariable(token.getImage());
        }
        else if (peekTypedVariable(PrimitiveType.DATA_PROPERTY) != null) {
            SPARQLToken token = tokenizer.consume();
            return new DataPropertyVariable(token.getImage());
        }
        else if (peek(DataPropertyIRITokenType.get())) {
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
            if (peek(COMMA)) {
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
        if (peek(StringTokenType.get())) {
            SPARQLToken lexicalToken = tokenizer.consume();
            String lexicalValue = getStringFromLexicalToken(lexicalToken);
            if (peek(DOUBLE_CARET)) {
                tokenizer.consume();
                SPARQLToken token = tokenizer.consume(DatatypeIRITokenType.get());
                result = new Literal( Datatype.get(getIRIFromToken(token)), lexicalValue, "");
            }
            else {
                result = Literal.createString(lexicalValue);
            }
        }
        else if (peek(IntegerTokenType.get())) {
            SPARQLToken token = tokenizer.consume();
            result = Literal.createInteger(Integer.parseInt(token.getImage()));
        }
        else if (peek(DecimalTokenType.get())) {
            SPARQLToken token = tokenizer.consume();
            result = Literal.createDecimal(BigDecimal.valueOf(Double.parseDouble(token.getImage())));
        }
        else if (peek(DoubleTokenType.get())) {
            SPARQLToken token = tokenizer.consume();
            result = Literal.createDouble(Double.parseDouble(token.getImage()));
        }
        else if (peek(BooleanTokenType.get())) {
            SPARQLToken token = tokenizer.consume();
            result = Literal.createBoolean(Boolean.parseBoolean(token.getImage()));
        }
        else if (peek(VariableTokenType.get())) {
            SPARQLToken token = tokenizer.consume();
            tokenizer.getVariableManager().registerVariable(new UntypedVariable(token.getImage()), PrimitiveType.LITERAL);
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
            if (peek(COMMA)) {
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
        if (peek(VariableTokenType.get())) {
            SPARQLToken token = tokenizer.consume(VariableTokenType.get());
            tokenizer.getVariableManager().registerVariable(new UntypedVariable(token.getImage()), PrimitiveType.DATATYPE);
            result = new DatatypeVariable(token.getImage());
        }
        else if (peekTypedVariable(PrimitiveType.DATATYPE) != null) {
            SPARQLToken token = tokenizer.consume();
            result = new DatatypeVariable(token.getImage());
        }
        else if (peek(DatatypeIRITokenType.get())) {
            SPARQLToken token = tokenizer.consume();
            result =  Datatype.get(getIRIFromToken(token));
        }
        else {
            tokenizer.raiseError();
        }
        return result;
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public Expression parseBracketedExpression() {
        tokenizer.consume(OPEN_PAR);
        Expression expression = parseExpression();
        tokenizer.consume(CLOSE_PAR);
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
            if (peek(OR)) {
                tokenizer.consume(OR);
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
            if (peek(AND)) {
                tokenizer.consume(AND);
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
        if (peek(EQUAL)) {
            tokenizer.consume(EQUAL);
            Expression right = parseNumericExpression();
            return new RelationExpression(left, right, Relation.EQUAL);
        }
        else if (peek(NOT_EQUAL)) {
            tokenizer.consume(NOT_EQUAL);
            Expression right = parseNumericExpression();
            return new RelationExpression(left, right, Relation.NOT_EQUAL);
        }
        else if (peek(LESS_THAN)) {
            tokenizer.consume(LESS_THAN);
            Expression right = parseNumericExpression();
            return new RelationExpression(left, right, Relation.LESS_THAN);
        }
        else if (peek(LESS_THAN_OR_EQUAL)) {
            tokenizer.consume(LESS_THAN_OR_EQUAL);
            Expression right = parseNumericExpression();
            return new RelationExpression(left, right, Relation.LESS_THAN_OR_EQUAL);
        }
        else if (peek(GREATER_THAN)) {
            tokenizer.consume(GREATER_THAN);
            Expression right = parseNumericExpression();
            return new RelationExpression(left, right, Relation.GREATER_THAN);
        }
        else if (peek(GREATER_THAN_OR_EQUAL)) {
            tokenizer.consume(GREATER_THAN_OR_EQUAL);
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
            if (peek(PLUS)) {
                tokenizer.consume(PLUS);
                Expression rightExpr = parseMultiplicativeExpression();
                leftExpr = new PlusExpression(leftExpr, rightExpr);
            }
            else if (peek(MINUS)) {
                tokenizer.consume(MINUS);
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
            if (peek(ASTERISK)) {
                tokenizer.consume(ASTERISK);
                Expression rightExpr = parseUnaryExpression();
                leftExpr = new MultiplyExpression(leftExpr, rightExpr);
            }
            else if (peek(DIVIDE)) {
                tokenizer.consume(DIVIDE);
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
        if (peek(NOT)) {
            tokenizer.consume(NOT);
            return new NotExpression(parsePrimaryExpression());
        }
        else if (peek(PLUS)) {
            tokenizer.consume(PLUS);
            return parsePrimaryExpression();
        }
        else if (peek(MINUS)) {
            tokenizer.consume(MINUS);
            return new UnaryMinusExpression(parsePrimaryExpression());
        }
        else {
            return parsePrimaryExpression();
        }
    }

    public Expression parsePrimaryExpression() {
//        BrackettedExpression | BuiltInCall | iriOrFunction | RDFLiteral | NumericLiteral | BooleanLiteral | Var
        Expression expression = null;
        if (peek(OPEN_PAR)) {
            expression = parseBracketedExpression();
        }
        else if (peek(BuiltInCallTokenType.get())) {
            expression = parseBuiltInCall();
        }
        else if (peek(ClassIRITokenType.get())) {
            expression = new NamedClass(getIRIFromToken(tokenizer.consume(ClassIRITokenType.get())));
        }
        else if (peek(DatatypeIRITokenType.get())) {
            expression =  Datatype.get(getIRIFromToken(tokenizer.consume(DatatypeIRITokenType.get())));
        }
        else if (peek(ObjectPropertyIRITokenType.get())) {
            expression = new ObjectProperty(getIRIFromToken(tokenizer.consume(ObjectPropertyIRITokenType.get())));
        }
        else if (peek(DataPropertyIRITokenType.get())) {
            expression = new DataProperty(getIRIFromToken(tokenizer.consume(DataPropertyIRITokenType.get())));
        }
        else if (peek(AnnotationPropertyIRITokenType.get())) {
            expression = new AnnotationProperty(getIRIFromToken(tokenizer.consume(AnnotationPropertyIRITokenType.get())));
        }
        else if (peek(IndividualIRITokenType.get())) {
            expression = new NamedIndividual(getIRIFromToken(tokenizer.consume(IndividualIRITokenType.get())));
        }
        else if (peek(UntypedIRITokenType.get())) {
            expression = new AtomicIRI(getIRIFromToken(tokenizer.consume(UntypedIRITokenType.get())));
        }
        else if (peek(IntegerTokenType.get(), DoubleTokenType.get(), DecimalTokenType.get(), StringTokenType.get(), BooleanTokenType.get())) {
            expression = parseLiteralNode();
        }
        else if (peekTypedVariable(PrimitiveType.CLASS) != null) {
            SPARQLToken token = tokenizer.consume(VariableTokenType.get());
            expression = new ClassVariable(token.getImage());
        }
        else if (peekTypedVariable(PrimitiveType.DATATYPE) != null) {
            SPARQLToken token = tokenizer.consume(VariableTokenType.get());
            expression = new DatatypeVariable(token.getImage());
        }
        else if (peekTypedVariable(PrimitiveType.OBJECT_PROPERTY) != null) {
            SPARQLToken token = tokenizer.consume(VariableTokenType.get());
            expression = new ObjectPropertyVariable(token.getImage());
        }
        else if (peekTypedVariable(PrimitiveType.DATA_PROPERTY) != null) {
            SPARQLToken token = tokenizer.consume(VariableTokenType.get());
            expression = new DataPropertyVariable(token.getImage());
        }
        else if (peekTypedVariable(PrimitiveType.ANNOTATION_PROPERTY) != null) {
            SPARQLToken token = tokenizer.consume(VariableTokenType.get());
            expression = new AnnotationPropertyVariable(token.getImage());
        }
        else if (peekTypedVariable(PrimitiveType.NAMED_INDIVIDUAL) != null) {
            SPARQLToken token = tokenizer.consume(VariableTokenType.get());
            expression = new IndividualVariable(token.getImage());
        }
        else if (peekTypedVariable(PrimitiveType.LITERAL) != null) {
            SPARQLToken token = tokenizer.consume(VariableTokenType.get());
            expression = new LiteralVariable(token.getImage());
        }
        else if (peek(VariableTokenType.get())) {
            SPARQLToken token = tokenizer.consume(VariableTokenType.get());
            expression = new UntypedVariable(token.getImage());
        }
        else if (peek(StringTokenType.get())) {
            return parseLiteralNode();
        }
        else {
            tokenizer.raiseError();
        }
        return expression;

    }

    public BuiltInCallExpression parseBuiltInCall() {
        SPARQLToken token = tokenizer.consume(BuiltInCallTokenType.get());
        tokenizer.consume(OPEN_PAR);
        String callName = token.getImage().toUpperCase();
        BuiltInCall builtInCall = BuiltInCall.valueOf(callName);
        if(builtInCall.isAggregate()) {
            // DISTINCT?
            SPARQLToken peek = tokenizer.peek(DISTINCT);
            if(peek != null) {
                tokenizer.consume(DISTINCT);
            }
        }

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

        if(hasEmptyArgList && peek(CLOSE_PAR)) {
            tokenizer.consume(CLOSE_PAR);
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
                        if (peek(COMMA)) {
                            tokenizer.consume(COMMA);
                        }
                        else {
                            break;
                        }
                    }
                    else {
                        tokenizer.consume(COMMA);
                    }
                }
                else {
                    if (peek(COMMA)) {
                        tokenizer.consume(COMMA);
                    }
                    else {
                        break;
                    }
                }
            }
            if (builtInCall.isAggregate()) {
                BuiltInAggregateCallEvaluator aggregateCallEvaluator = (BuiltInAggregateCallEvaluator) builtInCall.getEvaluator();
                if(!aggregateCallEvaluator.getScalars().isEmpty()) {
                    Set<String> parsedScalars = new HashSet<>();
                    if(tokenizer.peek(SEMI_COLON) != null) {
                        tokenizer.consume(SEMI_COLON);
                        SPARQLToken nextToken = tokenizer.peek(ScalarKeyTokenType.get());
                        if(nextToken != null && !parsedScalars.contains(nextToken.getImage())) {
                            SPARQLToken scalarNameToken = tokenizer.consume(ScalarKeyTokenType.get());
                            parsedScalars.add(scalarNameToken.getImage());
                            tokenizer.consume(EQUAL);
                            SPARQLToken scalarValueToken = tokenizer.consume(StringTokenType.get());
                        }

                    }
                }
            }
            tokenizer.consume(CLOSE_PAR);
        }



        return new BuiltInCallExpression(builtInCall, ImmutableList.copyOf(args));
    }


}
