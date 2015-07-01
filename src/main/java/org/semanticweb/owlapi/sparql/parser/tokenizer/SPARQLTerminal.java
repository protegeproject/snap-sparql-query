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

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 13/03/2012
 */
public enum SPARQLTerminal {

    OPEN_SQUARE_BRACKET("["),

    CLOSE_SQUARE_BRACKET("]"),
    
    
    OPEN_BRACE("{"),
    
    CLOSE_BRACE("}"),


    OPEN_PAR("("),

    CLOSE_PAR(")"),

    
    DOT("."),

    COMMA(","),

    COLON(":"),

    SEMI_COLON(";"),


    ASTERISK("*"),

    DOUBLE_CARET("^^"),


    AS ("AS"),

    BASE("BASE"),

    PREFIX("PREFIX"),

    SELECT("SELECT"),

    CONSTRUCT("CONSTRUCT"),

    UNION("UNION"),

    FROM("FROM"),
    
    WHERE("WHERE"),

    REDUCED("REDUCED"),

    DISTINCT("DISTINCT"),

    NAMED("NAMED"),


    ORDER("ORDER"),

    GROUP("GROUP"),

    BY("BY"),

    ASC("ASC"),

    DESC("DESC"),

    FILTER("FILTER"),

    HAVING("HAVING"),

    BIND("BIND"),

    OR("||"),

    AND("&&"),

    NOT("!"),

    EQUAL("="),

    NOT_EQUAL("!="),

    LESS_THAN("<"),

    LESS_THAN_OR_EQUAL("<="),

    GREATER_THAN(">"),

    GREATER_THAN_OR_EQUAL(">="),

    PLUS("+"),

    MINUS("-"),

    DIVIDE("/"),

    MINUS_KW("MINUS"),

    OPTIONAL_KW("OPTIONAL");






    
    private String image;

    private SPARQLTerminal(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }
}
