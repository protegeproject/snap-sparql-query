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

package org.semanticweb.owlapi.sparql.api;


import org.semanticweb.owlapi.model.PrefixManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 28/03/2012
 */
public class SPARQLQuery {

    private SPARQLQueryType queryType;
    
    private List<Variable> selectVariables;

    private List<Variable> allVariables;

    private List<SelectAs> selectAsList = new ArrayList<SelectAs>();
    
    private List<SPARQLGraphPattern> graphPatterns = new ArrayList<SPARQLGraphPattern>();

    private List<SPARQLGraphPattern> minusPatterns = new ArrayList<SPARQLGraphPattern>();
    
    private SolutionModifier solutionModifier;

    private PrefixManager pm;

    public SPARQLQuery(PrefixManager pm,
                       SPARQLQueryType queryType,
                       List<Variable> selectVariables,
                       List<Variable> graphPatternVariables,
                       List<SelectAs> selectAsList,
                       List<SPARQLGraphPattern> graphPatterns,
                       List<SPARQLGraphPattern> minusPatterns,
                       SolutionModifier solutionModifier) {
        this.pm = pm;
        this.queryType = queryType;
        this.selectVariables = selectVariables;
        this.allVariables = graphPatternVariables;
        this.selectAsList.addAll(selectAsList);
        this.graphPatterns = graphPatterns;
        this.solutionModifier = solutionModifier;
        this.minusPatterns.addAll(minusPatterns);
    }

    public Set<Variable> getGraphPatternVariables() {
        Set<Variable> result = new HashSet<Variable>();
        for(SPARQLGraphPattern pattern : graphPatterns) {
            result.addAll(pattern.getTriplePatternVariablesVariables());
        }
        return result;
    }

    public PrefixManager getPrefixManager() {
        return pm;
    }

    public SPARQLQueryType getQueryType() {
        return queryType;
    }

    public List<Variable> getSelectVariables() {
        return new ArrayList<Variable>(selectVariables);
    }

    public List<Variable> getAllVariables() {
        return allVariables;
    }

    public List<Variable> getSelectAsVariables() {
        List<Variable> selectAsVariables = new ArrayList<Variable>();
        for(SelectAs selectAs : selectAsList) {
            selectAsVariables.add(selectAs.getVariable());
        }
        return selectAsVariables;
    }

    public List<SelectAs> getSelectAs() {
        return selectAsList;
    }

    public List<SPARQLGraphPattern> getGraphPatterns() {
        return graphPatterns;
    }

    public List<SPARQLGraphPattern> getMinusPatterns() {
        return minusPatterns;
    }
    
    public void addMinus(SPARQLGraphPattern graphPattern) {
        minusPatterns.add(graphPattern);
    }
    

    public SolutionModifier getSolutionModifier() {
        return solutionModifier;
    }
}
