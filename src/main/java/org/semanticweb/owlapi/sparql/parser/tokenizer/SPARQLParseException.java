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

package org.semanticweb.owlapi.sparql.parser.tokenizer;

import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.sparql.api.Variable;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

import java.util.HashSet;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 27/03/2012
 */
public class SPARQLParseException extends RuntimeException {

    private Set<SPARQLTerminal> expectedTerminals = new HashSet<SPARQLTerminal>();
    
    private Set<EntityType> expectedEntityTypes = new HashSet<EntityType>();
    
    private Set<OWLRDFVocabulary> expectedVocabulary = new HashSet<OWLRDFVocabulary>();

    private SPARQLToken token;
    
    private Set<TokenType> expectedTokenTypes = new HashSet<TokenType>();
    
    private Set<Variable> parsedVariables = new HashSet<Variable>();

    public SPARQLParseException(String message, Set<Variable> parsedVariables,  Set<TokenType> expectedTokenTypes, SPARQLToken error, Set<SPARQLTerminal> expectedTerminals, Set<EntityType> expectedEntityTypes, Set<OWLRDFVocabulary> expectedVocabulary) {
        super(message);
        this.parsedVariables.addAll(parsedVariables);
        this.token = error;
        this.expectedTokenTypes.addAll(expectedTokenTypes);
        this.expectedTerminals = expectedTerminals;
        this.expectedEntityTypes = expectedEntityTypes;
        this.expectedVocabulary = expectedVocabulary;
    }

    public Set<Variable> getParsedVariables() {
        return parsedVariables;
    }

    public Set<TokenType> getExpectedTokenTypes() {
        return expectedTokenTypes;
    }

    public SPARQLToken getToken() {
        return token;
    }

    public Set<SPARQLTerminal> getExpectedTerminals() {
        return expectedTerminals;
    }

    public Set<EntityType> getExpectedEntityTypes() {
        return expectedEntityTypes;
    }

    public Set<OWLRDFVocabulary> getExpectedVocabulary() {
        return expectedVocabulary;
    }
}
