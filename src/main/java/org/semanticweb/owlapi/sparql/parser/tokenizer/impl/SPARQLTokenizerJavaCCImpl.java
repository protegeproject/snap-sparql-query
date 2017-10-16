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

package org.semanticweb.owlapi.sparql.parser.tokenizer.impl;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.model.parameters.Imports;
import org.semanticweb.owlapi.sparql.api.SPARQLPrefixManager;
import org.semanticweb.owlapi.sparql.parser.tokenizer.*;
import org.semanticweb.owlapi.vocab.OWL2Datatype;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.*;

import static org.semanticweb.owlapi.sparql.parser.tokenizer.impl.JavaCCSPARQLTokenizerConstants.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 16/03/2012
 */
public class SPARQLTokenizerJavaCCImpl implements SPARQLTokenizer {

    private JavaCCSPARQLTokenizerTokenManager tm;

    private SPARQLToken currentToken;
    
    private Token currentJavaCCToken;

    private Set<TokenType> peekedTypes = new HashSet<TokenType>();


    private OWLOntology rootOntology;

    private SPARQLPrefixManager prefixManager = SPARQLPrefixManager.createWithDefaultPrefixes();
    
    private VariableManager variableManager = new VariableManager();
    
    private String base = "";
    
    private static Map<IRI, OWLRDFVocabulary> vocabularyMap = new HashMap<IRI, OWLRDFVocabulary>();
    
    private int currentPos = 0;
    
    private String input;
    
    static {
        for(OWLRDFVocabulary v : OWLRDFVocabulary.values()) {
            vocabularyMap.put(v.getIRI(), v);
        }
    }
    
    public SPARQLTokenizerJavaCCImpl(OWLOntology rootOntology, Reader reader) {
        this.rootOntology = rootOntology;
        char [] buffer = new char [2040];
        StringBuilder sb = new StringBuilder();
        int read = 0;
        try {
            while((read = reader.read(buffer)) > 0) {
                sb.append(buffer, 0, read);
            }
            input = sb.toString();
            tm = new JavaCCSPARQLTokenizerTokenManager(new JavaCharStream(new StringReader(input)));
            advanceToNextToken();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public SPARQLPrefixManager getPrefixManager() {
        return prefixManager;
    }

    public VariableManager getVariableManager() {
        return variableManager;
    }


//    public void registerVariable(String variableName, PrimitiveType variableType) {
//        variableManager.putType(new UntypedVariable(variableName), variableType);
//    }

    public void setBase(String base) {
        this.base = base;
    }

    public void registerPrefix(String prefixName, String prefix) {
        prefixManager.setPrefix(prefixName, prefix);
    }

    @Override
    public SPARQLToken nextToken() {
        SPARQLToken token = currentToken;
        advanceToNextToken();
        return token;
    }

    public SPARQLToken advanceToNextToken() {
        Token javaCCToken = tm.getNextToken();
        currentJavaCCToken = javaCCToken;
        currentToken = translate(javaCCToken);
        return currentToken;
    }

    public SPARQLToken getCurrentToken() {
        return currentToken;
    }

    @Nullable
    public SPARQLToken peek(TokenType... tokenTypes) {
        peekedTypes.addAll(Arrays.asList(tokenTypes));
        if(currentToken.hasTokenType(tokenTypes)) {
            return currentToken;
        }
        else {
            return null;
        }
    }

    @Nullable
    public SPARQLToken peek(SPARQLTerminal terminal) {
        return peek(SPARQLTerminalTokenType.get(terminal));
    }

    public SPARQLToken peek(OWLRDFVocabulary vocabulary) {
        return peek(OWLRDFVocabularyTokenType.get(vocabulary));
    }

    public SPARQLToken peek(OWLRDFVocabulary vocabulary, OWLRDFVocabulary... vocabularies) {
        List<OWLRDFVocabulary> allVocabularies = new ArrayList<OWLRDFVocabulary>();
        allVocabularies.add(vocabulary);
        allVocabularies.addAll(Arrays.asList(vocabularies));
        SPARQLToken token = null;
        for(OWLRDFVocabulary voc : allVocabularies) {
            OWLRDFVocabularyTokenType type = OWLRDFVocabularyTokenType.get(voc);
            peekedTypes.add(type);
            if(currentToken.hasTokenType(type)) {
                token = currentToken;
            }
        }
        return token;
    }

    public SPARQLToken consume(TokenType... tokenTypes) {
        if(!currentToken.hasTokenType(tokenTypes)) {
            raiseErrorInternal(tokenTypes);
        }
        peekedTypes.clear();
        SPARQLToken token = currentToken;
        advanceToNextToken();
        while (currentToken.hasTokenType(CommentTokenType.get())) {
            advanceToNextToken();
        }
        return token;
    }

    public SPARQLToken consume(OWLRDFVocabulary vocabulary) {
        return consume(OWLRDFVocabularyTokenType.get(vocabulary));
    }

    public SPARQLToken consume(SPARQLTerminal terminal) {
        return consume(SPARQLTerminalTokenType.get(terminal));
    }

    public void raiseError() {
        raiseErrorInternal();
    }

    private void raiseErrorInternal(TokenType ... consume) {
        Set<TokenType> allExpectedTokens = new HashSet<TokenType>();
        allExpectedTokens.addAll(Arrays.asList(consume));
        allExpectedTokens.addAll(peekedTypes);
        StringBuilder msg = new StringBuilder();
        msg.append("Encountered ");
        msg.append(currentToken.getImage());
        msg.append(" (" + currentToken.getTokenTypes() + ")");
        msg.append(" at ");
        msg.append(currentToken.getTokenPosition());
        msg.append(". Expected one of ");
        for(TokenType tokenType : allExpectedTokens) {
            msg.append("\n\t");
            msg.append(tokenType);
        }
        final Set<EntityType> expectedEntityTypes = new HashSet<EntityType>();
        final Set<OWLRDFVocabulary> expectedVocabulary = new HashSet<OWLRDFVocabulary>();
        final Set<SPARQLTerminal> expectedTerminals = new HashSet<SPARQLTerminal>();
        for(TokenType tokenType : allExpectedTokens) {
            tokenType.accept(new TokenTypeVisitor<Object, RuntimeException>() {
                public Object visit(ClassIRITokenType tokenType) throws RuntimeException {
                    expectedEntityTypes.add(EntityType.CLASS);
                    return null;
                }

                public Object visit(ObjectPropertyIRITokenType tokenType) throws RuntimeException {
                    expectedEntityTypes.add(EntityType.OBJECT_PROPERTY);
                    return null;
                }

                public Object visit(DataPropertyIRITokenType tokenType) throws RuntimeException {
                    expectedEntityTypes.add(EntityType.DATA_PROPERTY);
                    return null;
                }

                public Object visit(AnnotationPropertyIRITokenType tokenType) throws RuntimeException {
                    expectedEntityTypes.add(EntityType.ANNOTATION_PROPERTY);
                    return null;
                }

                public Object visit(IndividualIRITokenType tokenType) throws RuntimeException {
                    expectedEntityTypes.add(EntityType.NAMED_INDIVIDUAL);
                    return null;
                }

                public Object visit(DatatypeIRITokenType tokenType) throws RuntimeException {
                    expectedEntityTypes.add(EntityType.DATATYPE);
                    return null;
                }

                public Object visit(UntypedIRITokenType tokenType) throws RuntimeException {
                    return null;
                }

                public Object visit(VariableTokenType tokenType) throws RuntimeException {
                    return null;
                }

                public Object visit(OWLRDFVocabularyTokenType tokenType) throws RuntimeException {
                    expectedVocabulary.add(tokenType.getVocabulary());
                    return null;
                }

                public Object visit(SPARQLTerminalTokenType tokenType) throws RuntimeException {
                    expectedTerminals.add(tokenType.getTerminal());
                    return null;
                }

                public Object visit(PrefixNameTokenType tokenType) throws RuntimeException {
                    return null;
                }

                public Object visit(StringTokenType tokenType) throws RuntimeException {
                    return null;
                }

                public Object visit(BooleanTokenType tokenType) throws RuntimeException {
                    return null;
                }

                public Object visit(IntegerTokenType tokenType) throws RuntimeException {
                    return null;
                }

                public Object visit(DecimalTokenType tokenType) throws RuntimeException {
                    return null;
                }

                public Object visit(DoubleTokenType tokenType) throws RuntimeException {
                    return null;
                }

                public Object visit(ErrorTokenType tokenType) throws RuntimeException {
                    return null;
                }

                public Object visit(EOFTokenType tokenType) throws RuntimeException {
                    return null;
                }

                public Object visit(PrologueDeclarationIRITokenType tokenType) throws RuntimeException {
                    return null;
                }

                public Object visit(BuiltInCallTokenType tokenType) throws RuntimeException {
                    return null;
                }

                @Override
                public Object visit(CommentTokenType tokenType) throws RuntimeException {
                    return null;
                }

                @Override
                public Object visit(ScalarKeyTokenType tokenType) throws RuntimeException {
                    return null;
                }
            });
        }
        throw new SPARQLParseException(msg.toString(), variableManager.getVariables(), allExpectedTokens, currentToken, expectedTerminals, expectedEntityTypes, expectedVocabulary);
    }

    public boolean hasMoreTokens() {
        return currentJavaCCToken.kind != EOF;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////

    private SPARQLToken translate(Token token) {
        Collection<TokenType> tokenTypes = translate(token.image, token.kind);
        TokenPosition tokenPosition = translatePosition(token);
        return new SPARQLToken(token.image, tokenPosition, tokenTypes);
    }
    
    private Collection<TokenType> wrap(SPARQLTerminal terminal) {
        return wrap(SPARQLTerminalTokenType.get(terminal));
    }
    
    
    private Collection<TokenType> wrap(TokenType tokenType) {
        return Collections.<TokenType>singleton(tokenType);
    }
    


    private Collection<TokenType> translate(String image, int kind) {
        switch (kind) {
            case A:
                return wrap(OWLRDFVocabularyTokenType.get(OWLRDFVocabulary.RDF_TYPE));
            case AND:
                return wrap(SPARQLTerminal.AND);
            case ANON:
                // TODO
                break;
            case AS:
                return wrap(SPARQLTerminal.AS);
            case ASC:
                return wrap(SPARQLTerminal.ASC);
            case ASTERISK:
                return wrap(SPARQLTerminal.ASTERISK);
            case BASE:
                return wrap(SPARQLTerminal.BASE);
            case BLANK_NODE_LABEL:
                // TODO
                break;
            case BIND:
                return wrap(SPARQLTerminal.BIND);
            case BY:
                return wrap(SPARQLTerminal.BY);
            case CLOSE_BRACE:
                return wrap(SPARQLTerminal.CLOSE_BRACE);
            case CLOSE_SQUARE_BRACKET:
                return wrap(SPARQLTerminal.CLOSE_SQUARE_BRACKET);
            case COMMA:
                return wrap(SPARQLTerminal.COMMA);
            case DECIMAL:
                return wrap(DecimalTokenType.get());
            case DECIMAL_NEGATIVE:
                return wrap(DecimalTokenType.get());
            case DECIMAL_POSITIVE:
                return wrap(DecimalTokenType.get());
            case DESC:
                return wrap(SPARQLTerminal.DESC);
            case DISTINCT:
                return wrap(SPARQLTerminal.DISTINCT);
            case DIVIDE:
                return wrap(SPARQLTerminal.DIVIDE);
            case DOT:
                return wrap(SPARQLTerminal.DOT);
            case DOUBLE:
                return wrap(DoubleTokenType.get());
            case DOUBLE_CARET:
                return wrap(SPARQLTerminal.DOUBLE_CARET);
            case DOUBLE_NEGATIVE:
                return wrap(DoubleTokenType.get());
            case DOUBLE_POSITIVE:
                return wrap(DoubleTokenType.get());
            case ECHAR:
                // Sub-token
                break;
            case EOF:
                return wrap(EOFTokenType.get());
            case EQUAL:
                return wrap(SPARQLTerminal.EQUAL);
            case EXPONENT:
                // Sub-token
                break;
            case FALSE:
                return wrap(BooleanTokenType.get());
            case FILTER:
                return wrap(SPARQLTerminal.FILTER);
            case HAVING:
                return wrap(SPARQLTerminal.HAVING);
            case FROM:
                return wrap(SPARQLTerminal.FROM);
            case GREATER_THAN:
                return wrap(SPARQLTerminal.GREATER_THAN);
            case GREATER_THAN_OR_EQUAL:
                return wrap(SPARQLTerminal.GREATER_THAN_OR_EQUAL);
            case GROUP:
                return wrap(SPARQLTerminal.GROUP);
            case HEX:
                // Sub-token
                break;
            case INTEGER:
                return wrap(IntegerTokenType.get());
            case INTEGER_NEGATIVE:
                return wrap(IntegerTokenType.get());
            case INTEGER_POSITIVE:
                return wrap(IntegerTokenType.get());
            case IRI_REF:
                return getIRITypes(image, kind);
            case LANGTAG:
                break;
            case LESS_THAN:
                return wrap(SPARQLTerminal.LESS_THAN);
            case LESS_THAN_OR_EQUAL:
                return wrap(SPARQLTerminal.LESS_THAN_OR_EQUAL);
            case MINUS:
                return wrap(SPARQLTerminal.MINUS);
            case MINUS_KW:
                return wrap(SPARQLTerminal.MINUS_KW);
            case NAMED:
                return wrap(SPARQLTerminal.NAMED);
//            case NIL:
//                break;
            case NOT:
                return wrap(SPARQLTerminal.NOT);
            case NOT_EQUAL:
                return wrap(SPARQLTerminal.NOT_EQUAL);
            case OPEN_BRACE:
                return wrap(SPARQLTerminal.OPEN_BRACE);
            case OPEN_PAR:
                return wrap(SPARQLTerminal.OPEN_PAR);
            case CLOSE_PAR:
                return wrap(SPARQLTerminal.CLOSE_PAR);
            case OPEN_SQUARE_BRACKET:
                return wrap(SPARQLTerminal.OPEN_SQUARE_BRACKET);
            case OR:
                return wrap(SPARQLTerminal.OR);
            case ORDER:
                return wrap(SPARQLTerminal.ORDER);
            case PERCENT:
                break;
            case PLUS:
                return wrap(SPARQLTerminal.PLUS);
            case PLX:
                // Sub-token
                break;
            case PN_CHARS:
                // Sub-token
                break;
            case PN_CHARS_BASE:
                // Sub-token
                break;
            case PN_CHARS_U:
                // Sub-token
                break;
            case PN_LOCAL:
                // Sub-token
                break;
            case PN_LOCAL_ESC:
                // Sub-token
                break;
            case PN_PREFIX:
                // Sub-token
                break;
            case PNAME_LN:
                Collection<TokenType> pnameLNTypes = getIRITypes(image, kind);
                pnameLNTypes.add(PrefixNameTokenType.get());
                return pnameLNTypes;
            case PNAME_NS:
                Collection<TokenType> pnameNSTypes = new HashSet<TokenType>();
                pnameNSTypes.add(PrefixNameTokenType.get());
                if(prefixManager.containsPrefixMapping(image)) {
                    pnameNSTypes.addAll(getIRITypes(image, kind));
                }
                return pnameNSTypes;
            case PREFIX:
                return wrap(SPARQLTerminal.PREFIX);
            case REDUCED:
                break;
            case SELECT:
                return wrap(SPARQLTerminal.SELECT);
            case CONSTRUCT:
                return wrap(SPARQLTerminal.CONSTRUCT);
            case SCALAR_KEY:
                return wrap(ScalarKeyTokenType.get());
            case SEMI_COLON:
                return wrap(SPARQLTerminal.SEMI_COLON);
            case STRING_LITERAL1:
                return wrap(StringTokenType.get());
            case STRING_LITERAL2:
                return wrap(StringTokenType.get());
            case STRING_LITERAL_LONG1:
                return wrap(StringTokenType.get());
            case STRING_LITERAL_LONG2:
                return wrap(StringTokenType.get());
            case TRUE:
                return wrap(BooleanTokenType.get());
            case UNION:
                return wrap(SPARQLTerminal.UNION);
            case VAR1:
                return wrap(VariableTokenType.get());
            case VAR2:
                return wrap(VariableTokenType.get());
            case VARNAME:
                // Sub-token
                break;
            case WHERE:
                return wrap(SPARQLTerminal.WHERE);
            case OPTIONAL_KW:
                return wrap(SPARQLTerminal.OPTIONAL_KW);
            case WS:
                // Sub-token
                break;
            case BUILT_IN_CALL:
                return wrap(BuiltInCallTokenType.get());
            case COMMENT:
                return wrap(CommentTokenType.get());
            default:
                return wrap(ErrorTokenType.get());
        }
        return wrap(ErrorTokenType.get());
//        throw new RuntimeException("Unexpected token kind: " + kind + " (" + image + ")");
    }
    

    private int lastEndPos = 0;
    
    private TokenPosition translatePosition(Token token) {
        int index = input.indexOf(token.image, lastEndPos);
        TokenPosition pos = new TokenPosition(index, index + token.image.length(), token.beginLine, token.beginColumn);
        lastEndPos = index + token.image.length();
        return pos;
    }
    
//    private Collection<TokenType> getVariableTypes(UntypedVariable variable, int kind) {
//        Optional<PrimitiveType> type = variableManager.getVariableType(variable);
//        if(type.isPresent()) {
//            return Collections.<TokenType>singleton(DeclaredVariableTokenType.get(type.get()));
//        }
//        else {
//            return Collections.<TokenType>singleton(VariableTokenType.get());
//        }
//    }

    private Multimap<IRI, EntityIRITokenType> additionalTypes = HashMultimap.create();

    @Override
    public void registerType(IRI iri, EntityIRITokenType type) {
        System.out.println("Registering additional type: "+ type + " for " + iri);
        additionalTypes.put(iri, type);
    }

    private Collection<TokenType> getIRITypes(String image, int kind) {
        IRI iri = getIRI(image, kind);
        Collection<TokenType> types = new HashSet<TokenType>();
        for(EntityType<?> type : EntityType.values()) {
            OWLEntity entity = getOWLDataFactory().getOWLEntity(type, iri);
            if(rootOntology.containsEntityInSignature(entity, Imports.INCLUDED)) {
                types.add(EntityIRITokenType.get(type));
            }
        }
        types.addAll(additionalTypes.get(iri));
        if(iri.equals(OWLRDFVocabulary.OWL_THING.getIRI()) || iri.equals(OWLRDFVocabulary.OWL_NOTHING.getIRI())) {
            types.add(ClassIRITokenType.get());
        }
        if(iri.equals(OWLRDFVocabulary.OWL_TOP_OBJECT_PROPERTY.getIRI()) || iri.equals(OWLRDFVocabulary.OWL_BOTTOM_OBJECT_PROPERTY.getIRI())) {
            types.add(ObjectPropertyIRITokenType.get());
        }

        if(iri.equals(OWLRDFVocabulary.OWL_TOP_DATA_PROPERTY.getIRI()) || iri.equals(OWLRDFVocabulary.OWL_BOTTOM_DATA_PROPERTY.getIRI())) {
            types.add(ObjectPropertyIRITokenType.get());
        }

        if(OWLRDFVocabulary.BUILT_IN_ANNOTATION_PROPERTY_IRIS.contains(iri)) {
            types.add(AnnotationPropertyIRITokenType.get());
        }
        if(OWL2Datatype.isBuiltIn(iri)) {
            types.add(DatatypeIRITokenType.get());
        }
        OWLRDFVocabulary v = vocabularyMap.get(iri);
        if(v != null) {
            types.add(OWLRDFVocabularyTokenType.get(v));
        }
        if(types.isEmpty()) {
            types.add(UntypedIRITokenType.get());
        }
        return types;
    }

    private OWLDataFactory getOWLDataFactory() {
        return rootOntology.getOWLOntologyManager().getOWLDataFactory();
    }

    private IRI getIRI(String image, int kind) {
        switch (kind) {
            case IRI_REF:
                return IRI.create(image.substring(1, image.length() - 1));
            case PNAME_LN:
            case PNAME_NS:
                return getIRIFromPrefixName(image);
            default:
                throw new RuntimeException("Not an IRI kind: " + kind);
        }
    }

    private IRI getIRIFromPrefixName(String image) {
        int endOfPrefix = image.indexOf(":") + 1;
        String prefix = image.substring(0, endOfPrefix);
        if (!prefixManager.containsPrefixMapping(prefix)) {
            return IRI.create(image);
        }
        return prefixManager.getIRI(image);
    }

    public void throwParseError() {
    }
}
