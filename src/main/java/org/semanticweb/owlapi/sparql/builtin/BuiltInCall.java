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

import org.semanticweb.owlapi.sparql.builtin.eval.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.semanticweb.owlapi.sparql.builtin.SparqlType.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 01/04/2012
 */
public enum BuiltInCall {

    // @formatter:off

    STR(
            new STR_Evaluator(),
            SimpleLiteral(), operandList(LITERAL),
            operandList(SparqlType.IRI)),

    LANG(
            new LANG_Evaluator(),
            SimpleLiteral(),
            operandList(LITERAL)),

    LANGMATCHES(
            new LANGMATCHES_Evaluator(),
            Boolean(),
            new Operand(SIMPLE_LITERAL, "language-tag"), new Operand(SIMPLE_LITERAL, "language-range")),

    DATATYPE(
            new NullBuiltInCallEvaluator(),
            IRI(),
            operandList(LITERAL)),

    BOUND(
            new NullBuiltInCallEvaluator(),
            Boolean(),
            operandList(VARIABLE)),

    IRI(
            new NullBuiltInCallEvaluator(), IRI(), operandList(SIMPLE_LITERAL),
            operandList(XSD_STRING),
            operandList(SparqlType.IRI)),


    URI(
            new NullBuiltInCallEvaluator(), IRI(),
            operandList(SIMPLE_LITERAL),
            operandList(XSD_STRING),
            operandList(SparqlType.IRI)),

    BNODE(
            new NullBuiltInCallEvaluator(),
            new ResultType(SparqlType.BLANK_NODE), operandList(),
            operandList(SIMPLE_LITERAL),
            operandList(XSD_STRING)),

    RAND(
            new NullBuiltInCallEvaluator(),
            Double(),
            operandList()),

    ABS(
            new NullBuiltInCallEvaluator(),
            Numeric(),
            operandList(NUMERIC)),

    CEIL(
            new NullBuiltInCallEvaluator(),
            Numeric(),
            operandList(NUMERIC)),

    FLOOR(
            new NullBuiltInCallEvaluator(),
            Numeric(),
            operandList(NUMERIC)),

    ROUND(
            new NullBuiltInCallEvaluator(),
            Numeric(),
            operandList(NUMERIC)),

    CONCAT(
            new CONCAT_Evaluator(),
            StringLiteral(),
            operandList(STRING_LITERAL, VarArg.VARIABLE)),

    SUBSTR(
            new NullBuiltInCallEvaluator(),
            StringLiteral(),
            operandList(new Operand(STRING_LITERAL), new Operand(XSD_INTEGER, "startingLoc")),
            operandList(new Operand(STRING_LITERAL), new Operand(XSD_INTEGER, "startingLoc"), new Operand(XSD_INTEGER, "length"))),

    STRLEN(
            new STRLEN_Evaluator(),
            Numeric(),
            operandList(STRING_LITERAL)),

    REPLACE(
            new NullBuiltInCallEvaluator(),
            StringLiteral(),
            operandList(new Operand(STRING_LITERAL, "arg"), new Operand(SIMPLE_LITERAL, "pattern"), new Operand(SIMPLE_LITERAL, "replacement")),
            operandList(new Operand(STRING_LITERAL, "arg"), new Operand(SIMPLE_LITERAL, "pattern"), new Operand(SIMPLE_LITERAL, "replacement"), new Operand(SIMPLE_LITERAL, "flags"))),

    UCASE(
            new UCASE_Evaluator(),
            StringLiteral(),
            operandList(SIMPLE_LITERAL)),

    LCASE(
            new NullBuiltInCallEvaluator(),
            StringLiteral(),
            operandList(SIMPLE_LITERAL)),

    ENCODE_FOR_URI(
            new NullBuiltInCallEvaluator(),
            SimpleLiteral(),
            operandList(STRING_LITERAL)),

    CONTAINS(
            new CONTAINS_Evaluator(),
            Boolean(),
            operandList(STRING_LITERAL, STRING_LITERAL)),

    STRSTARTS(
            new STRSTARTS_Evaluator(),
            Boolean(),
            operandList(STRING_LITERAL, STRING_LITERAL)),

    STRENDS(
            new NullBuiltInCallEvaluator(),
            Boolean(),
            operandList(STRING_LITERAL, STRING_LITERAL)),

    STRBEFORE(
            new NullBuiltInCallEvaluator(),
            Literal(),
            operandList(STRING_LITERAL, STRING_LITERAL)),

    STRAFTER(
            new NullBuiltInCallEvaluator(),
            Literal(),
            operandList(STRING_LITERAL, STRING_LITERAL)),

    YEAR(
            new NullBuiltInCallEvaluator(),
            Integer(),
            operandList(XSD_DATE_TIME)),

    MONTH(
            new NullBuiltInCallEvaluator(),
            Integer(),
            operandList(XSD_DATE_TIME)),

    DAY(
            new NullBuiltInCallEvaluator(),
            Integer(),
            operandList(XSD_DATE_TIME)),

    HOURS(
            new NullBuiltInCallEvaluator(),
            Integer(),
            operandList(XSD_DATE_TIME)),

    MINUTES(
            new NullBuiltInCallEvaluator(),
            Integer(),
            operandList(XSD_DATE_TIME)),

    SECONDS(
            new NullBuiltInCallEvaluator(),
            Integer(),
            operandList(XSD_DATE_TIME)),

    TIMEZONE(
            new NullBuiltInCallEvaluator(),
            new ResultType(XSD_DATE_TIME),
            operandList(XSD_DATE_TIME)),

    TZ(
            new NullBuiltInCallEvaluator(),
            SimpleLiteral(),
            operandList(XSD_DATE_TIME)),

    NOW(
            new NullBuiltInCallEvaluator(),
            new ResultType(XSD_DATE_TIME),
            operandList()),

    MD5(
            new NullBuiltInCallEvaluator(),
            SimpleLiteral(),
            operandList(STRING_LITERAL),
            operandList(XSD_STRING)),

    SHA1(
            new NullBuiltInCallEvaluator(),
            SimpleLiteral(),
            operandList(STRING_LITERAL),
            operandList(XSD_STRING)),

    SHA256(
            new NullBuiltInCallEvaluator(),
            SimpleLiteral(),
            operandList(STRING_LITERAL),
            operandList(XSD_STRING)),

    SHA384(
            new NullBuiltInCallEvaluator(),
            SimpleLiteral(),
            operandList(STRING_LITERAL),
            operandList(XSD_STRING)),

    SHA512(
            new NullBuiltInCallEvaluator(),
            SimpleLiteral(),
            operandList(STRING_LITERAL),
            operandList(XSD_STRING)),

    COALESCE(
            new NullBuiltInCallEvaluator(),
            SimpleLiteral(),
            operandList(EXPRESSION, VarArg.VARIABLE)),

    IF(
            new NullBuiltInCallEvaluator(),
            Boolean(),
            operandList(EXPRESSION, EXPRESSION, EXPRESSION)),

    STRLANG(
            new NullBuiltInCallEvaluator(),
            Literal(),
            operandList(SIMPLE_LITERAL, SIMPLE_LITERAL)),

    STRDT(
            new NullBuiltInCallEvaluator(),
            Literal(),
            operandList(SIMPLE_LITERAL, SparqlType.IRI)),

    SAMETERM(
            new SAMETERM_Evaluator(),
            Boolean(),
            operandList(RDF_TERM, RDF_TERM)),

    ISIRI(
            new NullBuiltInCallEvaluator(),
            Boolean(),
            operandList(RDF_TERM)),

    ISURI(
            new NullBuiltInCallEvaluator(),
            Boolean(),
            operandList(RDF_TERM)),

    ISBLANK(
            new NullBuiltInCallEvaluator(),
            Boolean(),
            operandList(RDF_TERM)),

    ISLITERAL(
            new NullBuiltInCallEvaluator(),
            Boolean(),
            operandList(RDF_TERM)),

    ISNUMERIC(
            new NullBuiltInCallEvaluator(),
            Boolean(),
            operandList(RDF_TERM)),

    REGEX(
            new REGEX_Evaluator(),
            Boolean(),
            operandList(new Operand(SIMPLE_LITERAL, "text"), new Operand(SIMPLE_LITERAL, "pattern")),
            operandList(new Operand(SIMPLE_LITERAL, "text"), new Operand(SIMPLE_LITERAL, "pattern"), new Operand(SIMPLE_LITERAL, "flags")));

    // @formatter:on

    private static OperandList operandList() {
        return new OperandList();
    }

    private static OperandList operandList(SparqlType... operands) {
        return new OperandList(operands);
    }

    private static OperandList operandList(Operand ... operands) {
        return new OperandList(operands);
    }

    private static OperandList operandList(SparqlType sparqlType, VarArg varArg) {
        return new OperandList(sparqlType, varArg);
    }

    private static ResultType Integer() {
        return new ResultType(XSD_INTEGER);
    }

    private static ResultType Literal() {
        return new ResultType(LITERAL);
    }

    private static ResultType IRI() {
        return new ResultType(SparqlType.IRI);
    }

    private static ResultType SimpleLiteral() {
        return new ResultType(SIMPLE_LITERAL);
    }

    private static ResultType StringLiteral() {
        return new ResultType(STRING_LITERAL);
    }

    private static ResultType Boolean() {
        return new ResultType(BOOLEAN);
    }

    private static ResultType Numeric() {
        return new ResultType(NUMERIC);
    }

    private static ResultType Double() {
        return new ResultType(XSD_DOUBLE);
    }

//    ExistsFunc,
//
//    NotExistsFunc;

    

    private final List<OperandList> operandLists = new ArrayList<OperandList>();

    private final ResultType resultType;

    private final BuiltInCallEvaluator evaluator;

    private BuiltInCall(BuiltInCallEvaluator evaluator, ResultType resultType, OperandList ... operandLists) {
        this.evaluator = evaluator;
        this.resultType = resultType;
        this.operandLists.addAll(Arrays.asList(operandLists));
    }

    private BuiltInCall(BuiltInCallEvaluator evaluator, ResultType resultType, Operand ... operands) {
        this.evaluator = evaluator;
        this.resultType = resultType;
        operandLists.add(new OperandList(operands));
    }

    public ResultType getResultType() {
        return resultType;
    }

    public BuiltInCallEvaluator getEvaluator() {
        return evaluator;
    }

    public List<OperandList> getOperandLists() {
        return new ArrayList<OperandList>(operandLists);
    }
}
