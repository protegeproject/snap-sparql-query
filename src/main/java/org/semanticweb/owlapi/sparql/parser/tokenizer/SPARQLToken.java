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

import java.util.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 16/03/2012
 */
public class SPARQLToken {

    private String image;
  
    private TokenPosition tokenPosition;
    
    private Set<TokenType> types;


    public SPARQLToken(String image, TokenPosition tokenPosition, Collection<TokenType> types) {
        this.image = image;
        this.tokenPosition = tokenPosition;
        this.types = new HashSet<TokenType>(types);
    }

    public String getImage() {
        return image;
    }

    public TokenPosition getTokenPosition() {
        return tokenPosition;
    }

    public Collection<TokenType> getTokenTypes() {
        return Collections.unmodifiableSet(types);
    }
    
    public boolean hasTokenType(TokenType type) {
        return types.contains(type);
    }
    
    public boolean hasTokenType(TokenType ... tokenTypes) {
        if(tokenTypes.length == 0) {
            return true;
        }
        for(TokenType type : tokenTypes) {
            if(types.contains(type)) {
                return true;
            }
        }
        return false;
    }

    public boolean isPolyTyped() {
        return types.size() > 1;
    }

    /**
     * Returns a string representation of the object. In general, the
     * <code>toString</code> method returns a string that
     * "textually represents" this object. The result should
     * be a concise but informative representation that is easy for a
     * person to read.
     * It is recommended that all subclasses override this method.
     * <p>
     * The <code>toString</code> method for class <code>Object</code>
     * returns a string consisting of the name of the class of which the
     * object is an instance, the at-sign character `<code>@</code>', and
     * the unsigned hexadecimal representation of the hash code of the
     * object. In other words, this method returns a string equal to the
     * value of:
     * <blockquote>
     * <pre>
     * getClass().getName() + '@' + Integer.toHexString(hashCode())
     * </pre></blockquote>
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SPARQLToken(");
        sb.append("Types(");
        for(TokenType type : types) {
            sb.append(type);
        }
        sb.append(")");
        sb.append("  Image(\"");
        sb.append(image);
        sb.append("\")");
        sb.append(")");
        return sb.toString();
    }
}
