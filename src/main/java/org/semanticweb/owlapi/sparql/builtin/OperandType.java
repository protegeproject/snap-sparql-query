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

package org.semanticweb.owlapi.sparql.builtin;

import org.semanticweb.owlapi.sparql.api.PrimitiveType;
import org.semanticweb.owlapi.sparql.parser.tokenizer.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/04/2012
 */
public enum OperandType {

    BOOLEAN(),



    BLANK_NODE(),

    DATATYPE(IntegerTokenType.get(), DecimalTokenType.get(), DoubleTokenType.get(), StringTokenType.get(), BooleanTokenType.get(), UndeclaredVariableTokenType.get(), DeclaredVariableTokenType.get(PrimitiveType.CLASS), DeclaredVariableTokenType.get(PrimitiveType.OBJECT_PROPERTY), DeclaredVariableTokenType.get(PrimitiveType.DATA_PROPERTY), DeclaredVariableTokenType.get(PrimitiveType.NAMED_INDIVIDUAL), DeclaredVariableTokenType.get(PrimitiveType.DATATYPE), DeclaredVariableTokenType.get(PrimitiveType.ANNOTATION_PROPERTY)),

    XSD_STRING(StringTokenType.get(), UndeclaredVariableTokenType.get(), DeclaredVariableTokenType.get(PrimitiveType.CLASS), DeclaredVariableTokenType.get(PrimitiveType.OBJECT_PROPERTY), DeclaredVariableTokenType.get(PrimitiveType.DATA_PROPERTY), DeclaredVariableTokenType.get(PrimitiveType.NAMED_INDIVIDUAL), DeclaredVariableTokenType.get(PrimitiveType.DATATYPE), DeclaredVariableTokenType.get(PrimitiveType.ANNOTATION_PROPERTY)),

    XSD_INTEGER(IntegerTokenType.get(), UndeclaredVariableTokenType.get(), DeclaredVariableTokenType.get(PrimitiveType.CLASS), DeclaredVariableTokenType.get(PrimitiveType.OBJECT_PROPERTY), DeclaredVariableTokenType.get(PrimitiveType.DATA_PROPERTY), DeclaredVariableTokenType.get(PrimitiveType.NAMED_INDIVIDUAL), DeclaredVariableTokenType.get(PrimitiveType.DATATYPE), DeclaredVariableTokenType.get(PrimitiveType.ANNOTATION_PROPERTY)),

    XSD_DOUBLE(),

    XSD_DATE_TIME(),

    NUMERIC(IntegerTokenType.get(), DecimalTokenType.get(), DoubleTokenType.get(), UndeclaredVariableTokenType.get(), DeclaredVariableTokenType.get(PrimitiveType.CLASS), DeclaredVariableTokenType.get(PrimitiveType.OBJECT_PROPERTY), DeclaredVariableTokenType.get(PrimitiveType.DATA_PROPERTY), DeclaredVariableTokenType.get(PrimitiveType.NAMED_INDIVIDUAL), DeclaredVariableTokenType.get(PrimitiveType.DATATYPE), DeclaredVariableTokenType.get(PrimitiveType.ANNOTATION_PROPERTY)),


    IRI(UndeclaredVariableTokenType.get(), DeclaredVariableTokenType.get(PrimitiveType.CLASS), DeclaredVariableTokenType.get(PrimitiveType.OBJECT_PROPERTY), DeclaredVariableTokenType.get(PrimitiveType.DATA_PROPERTY), DeclaredVariableTokenType.get(PrimitiveType.NAMED_INDIVIDUAL), DeclaredVariableTokenType.get(PrimitiveType.DATATYPE), DeclaredVariableTokenType.get(PrimitiveType.ANNOTATION_PROPERTY)),

    /**
     * Any type of literal (any datatype or language tag)
     */
    LITERAL(StringTokenType.get(), UndeclaredVariableTokenType.get(), DeclaredVariableTokenType.get(PrimitiveType.CLASS), DeclaredVariableTokenType.get(PrimitiveType.OBJECT_PROPERTY), DeclaredVariableTokenType.get(PrimitiveType.DATA_PROPERTY), DeclaredVariableTokenType.get(PrimitiveType.NAMED_INDIVIDUAL), DeclaredVariableTokenType.get(PrimitiveType.DATATYPE), DeclaredVariableTokenType.get(PrimitiveType.ANNOTATION_PROPERTY)),

    /**
     * Any literal that has a type of rdf:PlainLiteral or xsd:string
     */
    STRING_LITERAL(StringTokenType.get(), UndeclaredVariableTokenType.get(), DeclaredVariableTokenType.get(PrimitiveType.CLASS), DeclaredVariableTokenType.get(PrimitiveType.OBJECT_PROPERTY), DeclaredVariableTokenType.get(PrimitiveType.DATA_PROPERTY), DeclaredVariableTokenType.get(PrimitiveType.NAMED_INDIVIDUAL), DeclaredVariableTokenType.get(PrimitiveType.DATATYPE), DeclaredVariableTokenType.get(PrimitiveType.ANNOTATION_PROPERTY)),

    /**
     * A literal that has a type of rdf:PlainLiteral and an EMPTY language tag
     */
    SIMPLE_LITERAL(StringTokenType.get(), UndeclaredVariableTokenType.get(), DeclaredVariableTokenType.get(PrimitiveType.CLASS), DeclaredVariableTokenType.get(PrimitiveType.OBJECT_PROPERTY), DeclaredVariableTokenType.get(PrimitiveType.DATA_PROPERTY), DeclaredVariableTokenType.get(PrimitiveType.NAMED_INDIVIDUAL), DeclaredVariableTokenType.get(PrimitiveType.DATATYPE), DeclaredVariableTokenType.get(PrimitiveType.ANNOTATION_PROPERTY)),


    RDF_TERM(DeclaredVariableTokenType.get(PrimitiveType.CLASS), DeclaredVariableTokenType.get(PrimitiveType.OBJECT_PROPERTY), DeclaredVariableTokenType.get(PrimitiveType.DATA_PROPERTY), DeclaredVariableTokenType.get(PrimitiveType.ANNOTATION_PROPERTY), DeclaredVariableTokenType.get(PrimitiveType.NAMED_INDIVIDUAL), DeclaredVariableTokenType.get(PrimitiveType.DATATYPE), UndeclaredVariableTokenType.get(), ClassIRITokenType.get(), ObjectPropertyIRITokenType.get(), DataPropertyIRITokenType.get(), AnnotationPropertyIRITokenType.get(), IndividualIRITokenType.get(), DatatypeIRITokenType.get()),

    VARIABLE(DeclaredVariableTokenType.get(PrimitiveType.CLASS), DeclaredVariableTokenType.get(PrimitiveType.OBJECT_PROPERTY), DeclaredVariableTokenType.get(PrimitiveType.DATA_PROPERTY), DeclaredVariableTokenType.get(PrimitiveType.ANNOTATION_PROPERTY), DeclaredVariableTokenType.get(PrimitiveType.NAMED_INDIVIDUAL), DeclaredVariableTokenType.get(PrimitiveType.DATATYPE), UndeclaredVariableTokenType.get()),

    EXPRESSION;

    private List<TokenType> tokenTypeList = new ArrayList<TokenType>();

    private OperandType(TokenType... tokenTypes) {
        tokenTypeList.addAll(Arrays.asList(tokenTypes));
    }

    public List<TokenType> getTokenTypeList() {
        return tokenTypeList;
    }
}
