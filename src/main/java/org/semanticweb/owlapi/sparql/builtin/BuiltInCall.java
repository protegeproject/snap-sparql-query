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

import static org.semanticweb.owlapi.sparql.builtin.BuiltInCallType.AGGREGATE;
import static org.semanticweb.owlapi.sparql.builtin.BuiltInCallType.SIMPLE;
import static org.semanticweb.owlapi.sparql.builtin.SparqlType.*;

/**
 * Author: Matthew Horridge<br> Stanford University<br> Bio-Medical Informatics Research Group<br> Date: 01/04/2012
 */
public enum BuiltInCall {

    // @formatter:off

    STR(
            SIMPLE,
            new STR_Evaluator(),
            SimpleLiteral(),
            argList(LITERAL),
            argList(SparqlType.IRI)),

    LANG(
            SIMPLE,
            new LANG_Evaluator(),
            SimpleLiteral(),
            argList(LITERAL)),

    LANGMATCHES(
            SIMPLE,
            new LANGMATCHES_Evaluator(),
            Boolean(),
            namedArg(SIMPLE_LITERAL, "language-tag"), namedArg(SIMPLE_LITERAL, "language-range")),

    DATATYPE(
            SIMPLE,
            notImplemented(),
            IRI(),
            argList(LITERAL)),

    BOUND(
            SIMPLE,
            new BOUND_Evaluator(),
            Boolean(),
            argList(VARIABLE)),

    IRI(
            SIMPLE,
            new IRI_Evaluator(),
            IRI(), argList(SIMPLE_LITERAL),
            argList(XSD_STRING),
            argList(SparqlType.IRI)),


    URI(
            SIMPLE,
            notImplemented(), IRI(),
            argList(SIMPLE_LITERAL),
            argList(XSD_STRING),
            argList(SparqlType.IRI)),

    BNODE(
            SIMPLE,
            notImplemented(),
            new ReturnType(SparqlType.BLANK_NODE),
            argList(),
            argList(SIMPLE_LITERAL),
            argList(XSD_STRING)),

    RAND(
            SIMPLE,
            new RAND_Evaluator(),
            Double(),
            argList()),

    ABS(
            SIMPLE,
            new ABS_Evaluator(),
            Numeric(),
            argList(NUMERIC)),

    CEIL(
            SIMPLE,
            notImplemented(),
            Numeric(),
            argList(NUMERIC)),

    FLOOR(
            SIMPLE,
            notImplemented(),
            Numeric(),
            argList(NUMERIC)),

    ROUND(
            SIMPLE,
            new ROUND_Evaluator(),
            Numeric(),
            argList(NUMERIC)),

    CONCAT(
            SIMPLE,
            new CONCAT_Evaluator(),
            StringLiteral(),
            argList(STRING_LITERAL, VarArg.VARIABLE)),

    SUBSTR(
            SIMPLE,
            new SUBSTR_Evaluator(),
            StringLiteral(),
            argList(new Arg(STRING_LITERAL), namedArg(XSD_INTEGER, "startingLoc")),
            argList(new Arg(STRING_LITERAL), namedArg(XSD_INTEGER, "startingLoc"), namedArg(XSD_INTEGER, "length"))),

    STRLEN(
            SIMPLE,
            new STRLEN_Evaluator(),
            Numeric(),
            argList(STRING_LITERAL)),

    REPLACE(
            SIMPLE,
            new REPLACE_Evaluator(),
            StringLiteral(),
            argList(namedArg(STRING_LITERAL, "arg"), namedArg(SIMPLE_LITERAL, "pattern"), namedArg(SIMPLE_LITERAL, "replacement")),
            argList(namedArg(STRING_LITERAL, "arg"), namedArg(SIMPLE_LITERAL, "pattern"), namedArg(SIMPLE_LITERAL, "replacement"), namedArg(SIMPLE_LITERAL, "flags"))),

    UCASE(
            SIMPLE,
            new UCASE_Evaluator(),
            StringLiteral(),
            argList(SIMPLE_LITERAL)),

    LCASE(
            SIMPLE,
            new LCASE_Evaluator(),
            StringLiteral(),
            argList(SIMPLE_LITERAL)),

    ENCODE_FOR_URI(
            SIMPLE,
            notImplemented(),
            SimpleLiteral(),
            argList(STRING_LITERAL)),

    CONTAINS(
            SIMPLE,
            new CONTAINS_Evaluator(),
            Boolean(),
            argList(STRING_LITERAL, STRING_LITERAL)),

    STRSTARTS(
            SIMPLE,
            new STRSTARTS_Evaluator(),
            Boolean(),
            argList(STRING_LITERAL, STRING_LITERAL)),

    STRENDS(
            SIMPLE,
            new STRENDS_Evaluator(),
            Boolean(),
            argList(STRING_LITERAL, STRING_LITERAL)),

    STRBEFORE(
            SIMPLE,
            new STRBEFORE_Evaluator(),
            Literal(),
            argList(STRING_LITERAL, STRING_LITERAL)),

    STRAFTER(
            SIMPLE,
            new STRAFTER_Evaluator(),
            Literal(),
            argList(STRING_LITERAL, STRING_LITERAL)),

    YEAR(
            SIMPLE,
            new YEAR_Evaluator(),
            Integer(),
            argList(XSD_DATE_TIME)),

    MONTH(
            SIMPLE,
            new MONTH_Evaluator(),
            Integer(),
            argList(XSD_DATE_TIME)),

    DAY(
            SIMPLE,
            new DAY_Evaluator(),
            Integer(),
            argList(XSD_DATE_TIME)),

    HOURS(
            SIMPLE,
            new HOURS_Evaluator(),
            Integer(),
            argList(XSD_DATE_TIME)),

    MINUTES(
            SIMPLE,
            new MINUTES_Evaluator(),
            Integer(),
            argList(XSD_DATE_TIME)),

    SECONDS(
            SIMPLE,
            new SECONDS_Evaluator(),
            Integer(),
            argList(XSD_DATE_TIME)),

    TIMEZONE(
            SIMPLE,
            notImplemented(),
            new ReturnType(XSD_DATE_TIME),
            argList(XSD_DATE_TIME)),

    TZ(
            SIMPLE,
            notImplemented(),
            SimpleLiteral(),
            argList(XSD_DATE_TIME)),

    NOW(
            SIMPLE,
            new NOW_Evaluator(),
            new ReturnType(XSD_DATE_TIME),
            argList()),

    MD5(
            SIMPLE,
            notImplemented(),
            SimpleLiteral(),
            argList(STRING_LITERAL),
            argList(XSD_STRING)),

    SHA1(
            SIMPLE,
            notImplemented(),
            SimpleLiteral(),
            argList(STRING_LITERAL),
            argList(XSD_STRING)),

    SHA256(
            SIMPLE,
            notImplemented(),
            SimpleLiteral(),
            argList(STRING_LITERAL),
            argList(XSD_STRING)),

    SHA384(
            SIMPLE,
            notImplemented(),
            SimpleLiteral(),
            argList(STRING_LITERAL),
            argList(XSD_STRING)),

    SHA512(
            SIMPLE,
            notImplemented(),
            SimpleLiteral(),
            argList(STRING_LITERAL),
            argList(XSD_STRING)),

    COALESCE(
            SIMPLE,
            notImplemented(),
            SimpleLiteral(),
            argList(EXPRESSION, VarArg.VARIABLE)),

    IF(
            SIMPLE,
            new IF_Evaluator(),
            Boolean(),
            argList(EXPRESSION, EXPRESSION, EXPRESSION)),

    STRLANG(
            SIMPLE,
            new STRLANG_Evaluator(),
            Literal(),
            argList(SIMPLE_LITERAL, SIMPLE_LITERAL)),

    STRDT(
            SIMPLE,
            new STRDT_Evaluator(),
            Literal(),
            argList(SIMPLE_LITERAL, SparqlType.IRI)),

    SAMETERM(
            SIMPLE,
            new SAMETERM_Evaluator(),
            Boolean(),
            argList(RDF_TERM, RDF_TERM)),

    ISIRI(
            SIMPLE,
            notImplemented(),
            Boolean(),
            argList(RDF_TERM)),

    ISURI(
            SIMPLE,
            notImplemented(),
            Boolean(),
            argList(RDF_TERM)),

    ISBLANK(
            SIMPLE,
            notImplemented(),
            Boolean(),
            argList(RDF_TERM)),

    ISLITERAL(
            SIMPLE,
            notImplemented(),
            Boolean(),
            argList(RDF_TERM)),

    ISNUMERIC(
            SIMPLE,
            notImplemented(),
            Boolean(),
            argList(RDF_TERM)),

    REGEX(
            SIMPLE,
            new REGEX_Evaluator(),
            Boolean(),
            argList(namedArg(SIMPLE_LITERAL, "text"), namedArg(SIMPLE_LITERAL, "pattern")),
            argList(namedArg(SIMPLE_LITERAL, "text"), namedArg(SIMPLE_LITERAL, "pattern"), namedArg(SIMPLE_LITERAL, "flags"))),


    UUID(
            SIMPLE,
            new UUID_Evaluator(),
            IRI(),
            argList()
    ),

    STRUUID(
            SIMPLE,
            new STRUUID_Evaluator(),
            SimpleLiteral(),
            argList()
    ),

    SUM(
            AGGREGATE,
            new SUM_Evaluator(),
            Numeric(),
            argList(NUMERIC)
    ),

    COUNT(
            AGGREGATE,
            new COUNT_Evaluator(),
            Numeric(),
            argList(NUMERIC)
    ),

    MAX(
            AGGREGATE,
            new MAX_Evaluator(),
            Numeric(),
            argList(NUMERIC)
    ),

    MIN(
            AGGREGATE,
            new MIN_Evaluator(),
            Numeric(),
            argList(NUMERIC)
    ),

    AVG(
            AGGREGATE,
            new AVG_Evaluator(),
            Numeric(),
            argList(NUMERIC)
    ),

    SAMPLE(
            AGGREGATE,
            new SAMPLE_Evaluator(),
            Term(),
            argList(RDF_TERM)
    ),

    GROUP_CONCAT(
            AGGREGATE,
            new GROUP_CONCAT_Evaluator(),
            SimpleLiteral(),
            argList(SIMPLE_LITERAL)
    ),

    SPLITCAMEL(
            SIMPLE,
            new SPLITCAMEL_Evaluator(),
            Literal(),
            argList(LITERAL)
    );

    // @formatter:on

    private final BuiltInCallType type;

    private final ImmutableList<ArgList> argLists;

    private final ReturnType returnType;

    private final BuiltInCallEvaluator evaluator;

    BuiltInCall(BuiltInCallType type,
                BuiltInCallEvaluator evaluator,
                ReturnType returnType,
                ArgList... argLists) {
        this.type = type;
        this.evaluator = evaluator;
        this.returnType = returnType;
        ImmutableList.Builder<ArgList> builder = ImmutableList.builder();
        for (ArgList argList : argLists) {
            builder.add(argList);
        }
        this.argLists = builder.build();
    }

    BuiltInCall(BuiltInCallType type,
                BuiltInCallEvaluator evaluator,
                ReturnType returnType,
                Arg... args) {
        this.type = type;
        this.evaluator = evaluator;
        this.returnType = returnType;
        argLists = ImmutableList.of(new ArgList(args));
    }

    public boolean isSupported() {
        return !(evaluator instanceof NullBuiltInCallEvaluator);
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

    private static ReturnType Term() {
        return new ReturnType(RDF_TERM);
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

    public boolean isAggregate() {
        return type == AGGREGATE;
    }
}