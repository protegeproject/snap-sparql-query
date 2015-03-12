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

import com.google.common.collect.ImmutableList;
import org.semanticweb.owlapi.sparql.builtin.eval.*;

import java.util.ArrayList;
import java.util.List;

import static org.semanticweb.owlapi.sparql.builtin.SparqlType.*;

/**
 * Author: Matthew Horridge<br> Stanford University<br> Bio-Medical Informatics Research Group<br> Date: 01/04/2012
 */
public enum BuiltInCall {

    // @formatter:off

    STR(
            new STR_Evaluator(),
            SimpleLiteral(),
            argList(LITERAL),
            argList(SparqlType.IRI)),

    LANG(
            new LANG_Evaluator(),
            SimpleLiteral(),
            argList(LITERAL)),

    LANGMATCHES(
            new LANGMATCHES_Evaluator(),
            Boolean(),
            namedArg(SIMPLE_LITERAL, "language-tag"), namedArg(SIMPLE_LITERAL, "language-range")),

    DATATYPE(
            notImplemented(),
            IRI(),
            argList(LITERAL)),

    BOUND(
            notImplemented(),
            Boolean(),
            argList(VARIABLE)),

    IRI(
            notImplemented(), IRI(), argList(SIMPLE_LITERAL),
            argList(XSD_STRING),
            argList(SparqlType.IRI)),


    URI(
            notImplemented(), IRI(),
            argList(SIMPLE_LITERAL),
            argList(XSD_STRING),
            argList(SparqlType.IRI)),

    BNODE(
            notImplemented(),
            new ReturnType(SparqlType.BLANK_NODE),
            argList(),
            argList(SIMPLE_LITERAL),
            argList(XSD_STRING)),

    RAND(
            notImplemented(),
            Double(),
            argList()),

    ABS(
            notImplemented(),
            Numeric(),
            argList(NUMERIC)),

    CEIL(
            notImplemented(),
            Numeric(),
            argList(NUMERIC)),

    FLOOR(
            notImplemented(),
            Numeric(),
            argList(NUMERIC)),

    ROUND(
            notImplemented(),
            Numeric(),
            argList(NUMERIC)),

    CONCAT(
            new CONCAT_Evaluator(),
            StringLiteral(),
            argList(STRING_LITERAL, VarArg.VARIABLE)),

    SUBSTR(
            notImplemented(),
            StringLiteral(),
            argList(new Arg(STRING_LITERAL), namedArg(XSD_INTEGER, "startingLoc")),
            argList(new Arg(STRING_LITERAL), namedArg(XSD_INTEGER, "startingLoc"), namedArg(XSD_INTEGER, "length"))),

    STRLEN(
            new STRLEN_Evaluator(),
            Numeric(),
            argList(STRING_LITERAL)),

    REPLACE(
            notImplemented(),
            StringLiteral(),
            argList(namedArg(STRING_LITERAL, "arg"), namedArg(SIMPLE_LITERAL, "pattern"), namedArg(SIMPLE_LITERAL, "replacement")),
            argList(namedArg(STRING_LITERAL, "arg"), namedArg(SIMPLE_LITERAL, "pattern"), namedArg(SIMPLE_LITERAL, "replacement"), namedArg(SIMPLE_LITERAL, "flags"))),

    UCASE(
            new UCASE_Evaluator(),
            StringLiteral(),
            argList(SIMPLE_LITERAL)),

    LCASE(
            new LCASE_Evaluator(),
            StringLiteral(),
            argList(SIMPLE_LITERAL)),

    ENCODE_FOR_URI(
            notImplemented(),
            SimpleLiteral(),
            argList(STRING_LITERAL)),

    CONTAINS(
            new CONTAINS_Evaluator(),
            Boolean(),
            argList(STRING_LITERAL, STRING_LITERAL)),

    STRSTARTS(
            new STRSTARTS_Evaluator(),
            Boolean(),
            argList(STRING_LITERAL, STRING_LITERAL)),

    STRENDS(
            notImplemented(),
            Boolean(),
            argList(STRING_LITERAL, STRING_LITERAL)),

    STRBEFORE(
            notImplemented(),
            Literal(),
            argList(STRING_LITERAL, STRING_LITERAL)),

    STRAFTER(
            notImplemented(),
            Literal(),
            argList(STRING_LITERAL, STRING_LITERAL)),

    YEAR(
            notImplemented(),
            Integer(),
            argList(XSD_DATE_TIME)),

    MONTH(
            notImplemented(),
            Integer(),
            argList(XSD_DATE_TIME)),

    DAY(
            notImplemented(),
            Integer(),
            argList(XSD_DATE_TIME)),

    HOURS(
            notImplemented(),
            Integer(),
            argList(XSD_DATE_TIME)),

    MINUTES(
            notImplemented(),
            Integer(),
            argList(XSD_DATE_TIME)),

    SECONDS(
            notImplemented(),
            Integer(),
            argList(XSD_DATE_TIME)),

    TIMEZONE(
            notImplemented(),
            new ReturnType(XSD_DATE_TIME),
            argList(XSD_DATE_TIME)),

    TZ(
            notImplemented(),
            SimpleLiteral(),
            argList(XSD_DATE_TIME)),

    NOW(
            notImplemented(),
            new ReturnType(XSD_DATE_TIME),
            argList()),

    MD5(
            notImplemented(),
            SimpleLiteral(),
            argList(STRING_LITERAL),
            argList(XSD_STRING)),

    SHA1(
            notImplemented(),
            SimpleLiteral(),
            argList(STRING_LITERAL),
            argList(XSD_STRING)),

    SHA256(
            notImplemented(),
            SimpleLiteral(),
            argList(STRING_LITERAL),
            argList(XSD_STRING)),

    SHA384(
            notImplemented(),
            SimpleLiteral(),
            argList(STRING_LITERAL),
            argList(XSD_STRING)),

    SHA512(
            notImplemented(),
            SimpleLiteral(),
            argList(STRING_LITERAL),
            argList(XSD_STRING)),

    COALESCE(
            notImplemented(),
            SimpleLiteral(),
            argList(EXPRESSION, VarArg.VARIABLE)),

    IF(
            notImplemented(),
            Boolean(),
            argList(EXPRESSION, EXPRESSION, EXPRESSION)),

    STRLANG(
            notImplemented(),
            Literal(),
            argList(SIMPLE_LITERAL, SIMPLE_LITERAL)),

    STRDT(
            notImplemented(),
            Literal(),
            argList(SIMPLE_LITERAL, SparqlType.IRI)),

    SAMETERM(
            new SAMETERM_Evaluator(),
            Boolean(),
            argList(RDF_TERM, RDF_TERM)),

    ISIRI(
            notImplemented(),
            Boolean(),
            argList(RDF_TERM)),

    ISURI(
            notImplemented(),
            Boolean(),
            argList(RDF_TERM)),

    ISBLANK(
            notImplemented(),
            Boolean(),
            argList(RDF_TERM)),

    ISLITERAL(
            notImplemented(),
            Boolean(),
            argList(RDF_TERM)),

    ISNUMERIC(
            notImplemented(),
            Boolean(),
            argList(RDF_TERM)),

    REGEX(
            new REGEX_Evaluator(),
            Boolean(),
            argList(namedArg(SIMPLE_LITERAL, "text"), namedArg(SIMPLE_LITERAL, "pattern")),
            argList(namedArg(SIMPLE_LITERAL, "text"), namedArg(SIMPLE_LITERAL, "pattern"), namedArg(SIMPLE_LITERAL, "flags")));

    // @formatter:on

    private final ImmutableList<ArgList> argLists;

    private final ReturnType returnType;

    private final BuiltInCallEvaluator evaluator;

    private BuiltInCall(BuiltInCallEvaluator evaluator, ReturnType returnType, ArgList... argLists) {
        this.evaluator = evaluator;
        this.returnType = returnType;
        ImmutableList.Builder<ArgList> builder = ImmutableList.builder();
        for(ArgList argList : argLists) {
            builder.add(argList);
        }
        this.argLists = builder.build();
    }

    private BuiltInCall(BuiltInCallEvaluator evaluator, ReturnType returnType, Arg... args) {
        this.evaluator = evaluator;
        this.returnType = returnType;
        argLists = ImmutableList.of(new ArgList(args));
    }

    private static ArgList argList() {
        return new ArgList();
    }

    private static ArgList argList(SparqlType... args) {
        return new ArgList(args);
    }

    private static ArgList argList(Arg... args) {
        return new ArgList(args);
    }

    private static ArgList argList(SparqlType sparqlType, VarArg varArg) {
        return new ArgList(sparqlType, varArg);
    }

    private static Arg namedArg(SparqlType sparqlType, String name) {
        return new Arg(sparqlType, name);
    }

    private static ReturnType Integer() {
        return new ReturnType(XSD_INTEGER);
    }

    private static ReturnType Literal() {
        return new ReturnType(LITERAL);
    }

    private static ReturnType IRI() {
        return new ReturnType(SparqlType.IRI);
    }

//    ExistsFunc,
//
//    NotExistsFunc;

    private static ReturnType SimpleLiteral() {
        return new ReturnType(SIMPLE_LITERAL);
    }

    private static ReturnType StringLiteral() {
        return new ReturnType(STRING_LITERAL);
    }

    private static ReturnType Boolean() {
        return new ReturnType(BOOLEAN);
    }

    private static ReturnType Numeric() {
        return new ReturnType(NUMERIC);
    }

    private static ReturnType Double() {
        return new ReturnType(XSD_DOUBLE);
    }

    public ReturnType getReturnType() {
        return returnType;
    }

    public BuiltInCallEvaluator getEvaluator() {
        return evaluator;
    }

    public List<ArgList> getArgLists() {
        return new ArrayList<>(argLists);
    }

    private static NullBuiltInCallEvaluator notImplemented() {
        return new NullBuiltInCallEvaluator();
    }
}