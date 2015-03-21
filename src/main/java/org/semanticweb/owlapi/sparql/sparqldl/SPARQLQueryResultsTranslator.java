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

    private final LiteralTranslator translator;

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
        translator = new LiteralTranslator(dataFactory);
    }
    
    public SPARQLQueryResult translate() {
        List<SolutionMapping> solutionMappings = translateResult(result);
        List<SolutionMapping> minusSolutionMappings = translateResult(minusResult);
        for(Iterator<SolutionMapping> it = solutionMappings.iterator(); it.hasNext();) {
            SolutionMapping solutionMapping = it.next();
            for(SolutionMapping minusSolutionMapping : minusSolutionMappings) {
                if(solutionMapping.containsAll(minusSolutionMapping)) {
                    it.remove();
                    break;
                }
            }
        }
        Collections.sort(solutionMappings, new OrderByComparator(query.getSolutionModifier()));
        return new SPARQLQueryResult(query, solutionMappings);
    }

    private List<SolutionMapping> translateResult(QueryResult result) {
        List<SolutionMapping> resultBindings = new ArrayList<>();
        for(int i = 0; i < result.size(); i++) {
            QueryBinding binding = result.get(i);
            Set<QueryArgument> boundArgs = binding.getBoundArgs();
            Map<Variable, Term> bindings = new HashMap<>(boundArgs.size());
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
                        OWLLiteral literal = translator.toOWLLiteral(value);
                        term = new Literal(Datatype.get(literal.getDatatype().getIRI()), literal.getLiteral(), literal.getLang());
                        break;
                }
                if(term != null) {
                    bindings.put(var, term);
                }

            }
            SolutionMapping currentSolution = new SolutionMapping(bindings);
            for(SelectAs selectAs : query.getSelectAs()) {
                EvaluationResult eval = selectAs.getExpression().evaluate(currentSolution);
                bindings.put(selectAs.getVariable(), eval.getResult());
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
        return resultBindings;
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
