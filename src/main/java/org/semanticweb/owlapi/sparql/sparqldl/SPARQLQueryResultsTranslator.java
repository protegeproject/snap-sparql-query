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
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.sparql.api.*;
import org.semanticweb.owlapi.sparql.apiex.*;

import java.util.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 28/03/2012
 */
public class SPARQLQueryResultsTranslator {

    private QueryResult result;
    
    private QueryResult minusResult;
    
    private OWLDataFactory dataFactory;
    
    private SPARQLQuery query;
    
    private Map<String, Variable> nameVariableMap = new HashMap<String, Variable>();
    private final LiteralPatternTranslator translator;

    public SPARQLQueryResultsTranslator(SPARQLQuery query, QueryResult result, OWLDataFactory dataFactory) {
        this.query = query;
        this.result = result;
        this.dataFactory = dataFactory;
        for(Variable v : query.getGraphPatternVariables()) {
            nameVariableMap.put(v.getName(), v);
        }
        for(SelectAs selectAs : query.getSelectAs()) {
            Variable variable = selectAs.getVariable();
            nameVariableMap.put(variable.getName(), variable);
        }
        translator = new LiteralPatternTranslator();
    }
    
    public SPARQLQueryResult translate() {
        List<SolutionMapping> resultBindings = new ArrayList<SolutionMapping>();
        for(int i = 0; i < result.size(); i++) {
            QueryBinding binding = result.get(i);
            Set<QueryArgument> boundArgs = binding.getBoundArgs();
            Map<Variable, Term> bindings = new HashMap<Variable, Term>(boundArgs.size());
            for(QueryArgument arg : boundArgs) {
                Variable var = nameVariableMap.get(arg.getValue());
                QueryArgument value = binding.get(arg);
                Term term = null;
                switch (value.getType()) {
                    case VAR:
                        break;
                    case URI:
                        IRI iri = IRI.create(value.getValue());
                        term = var.getBound(iri);
                        break;
                    case BNODE:
                        break;
                    case LITERAL:
                        term = translator.translateLiteralPattern(value.getValue());
                        break;
                }
                if(term != null) {
                    bindings.put(var, term);
                }

            }
            SolutionMapping currentSolution = new SolutionMapping(bindings);
            for(SelectAs selectAs : query.getSelectAs()) {
//                EvaluationResult result = selectAs.getExpression().evaluate(currentSolution);
//                bindings.put(selectAs.getVariable(), result.getResult());
            }
            List<Bind> binds = query.getGraphPatterns().get(0).getBinds();
            for(Bind bind : binds) {
                bind.evaluate(currentSolution);
            }
            List<Expression> filterConditions = query.getGraphPatterns().get(0).getFilters();
            SolutionMapping solutionMapping = new SolutionMapping(currentSolution.asMap());
            if(filterConditions.isEmpty()) {
                resultBindings.add(solutionMapping);
            }
            else {
                if(matches(filterConditions, solutionMapping)) {
                    resultBindings.add(solutionMapping);
                }
            }
            
        }
        Collections.sort(resultBindings, new OrderByComparator(query.getSolutionModifier()));
        return new SPARQLQueryResult(query, resultBindings);
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
