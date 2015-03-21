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

package org.semanticweb.owlapi.sparql.sparqldl;

import de.derivo.sparqldlapi.QueryArgument;
import de.derivo.sparqldlapi.QueryBinding;
import de.derivo.sparqldlapi.QueryResult;
import de.derivo.sparqldlapi.impl.LiteralTranslator;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.sparql.api.*;

import java.util.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 28/03/2012
 */
public class SPARQLQueryResultsTranslator {

    private final QueryResult result;
    
    private final QueryResult minusResult;
    
    private final OWLDataFactory dataFactory;
    
    private final SPARQLQuery query;
    
    private Map<String, Variable> nameVariableMap = new HashMap<String, Variable>();

    private SolutionMappingTranslator solutionMappingTranslator;

    public SPARQLQueryResultsTranslator(SPARQLQuery query, QueryResult result, QueryResult minusResult, OWLDataFactory dataFactory) {
        this.query = query;
        this.result = result;
        this.dataFactory = dataFactory;
        this.minusResult = minusResult;
        for(Variable v : query.getGraphPatternVariables()) {
            nameVariableMap.put(v.getName(), v);
        }
        for(SelectAs selectAs : query.getSelectAs()) {
            Variable variable = selectAs.getVariable();
            nameVariableMap.put(variable.getName(), variable);
        }
        LiteralTranslator translator = new LiteralTranslator(dataFactory);
        solutionMappingTranslator = new SolutionMappingTranslator(translator);
    }
    
    public SPARQLQueryResult translate() {
        List<SolutionMapping> solutionSequence = translateResult(result);
        List<SolutionMapping> minusSolutionSequence = translateResult(minusResult);

        for(Iterator<SolutionMapping> it = solutionSequence.iterator(); it.hasNext();) {
            SolutionMapping solutionMapping = it.next();
            for(SolutionMapping minusSolutionMapping : minusSolutionSequence) {
                if(solutionMapping.containsAll(minusSolutionMapping)) {
                    it.remove();
                    break;
                }
            }
        }

        Collections.sort(solutionSequence, new OrderByComparator(query.getSolutionModifier()));
        return new SPARQLQueryResult(query, solutionSequence);
    }

    private List<SolutionMapping> translateResult(QueryResult result) {
        List<SolutionMapping> solutionSequence = new ArrayList<>();
        for(int i = 0; i < result.size(); i++) {
            QueryBinding binding = result.get(i);
            SolutionMapping currentSolution = solutionMappingTranslator.translate(binding, nameVariableMap);
            for(SelectAs selectAs : query.getSelectAs()) {
                EvaluationResult eval = selectAs.getExpression().evaluate(currentSolution);
                currentSolution.bind(selectAs.getVariable(), eval.getResult());
            }
            List<Bind> binds = query.getGraphPatterns().get(0).getBinds();
            for(Bind bind : binds) {
                bind.evaluate(currentSolution);
            }
            List<Expression> filterConditions = query.getGraphPatterns().get(0).getFilters();
            SolutionMapping solutionMapping = new SolutionMapping(currentSolution.asMap());
            if(filterConditions.isEmpty()) {
                solutionSequence.add(solutionMapping);
            }
            else {
                if(matches(filterConditions, solutionMapping)) {
                    solutionSequence.add(solutionMapping);
                }
            }

        }
        return solutionSequence;
    }

    private boolean matches(List<Expression> filterConditions, SolutionMapping solutionMapping) {
        for(Expression condition : filterConditions) {
            EvaluationResult evaluationResult = condition.evaluateAsEffectiveBooleanValue(solutionMapping);
            if(evaluationResult.isError()) {
                return false;
            }
            if(evaluationResult.isFalse()) {
                return false;
            }
        }
        return true;
    }


}
