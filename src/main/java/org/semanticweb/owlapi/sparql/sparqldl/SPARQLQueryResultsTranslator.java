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

import de.derivo.sparqldlapi.QueryBinding;
import de.derivo.sparqldlapi.QueryResult;
import de.derivo.sparqldlapi.impl.LiteralTranslator;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.sparql.api.*;
import org.semanticweb.owlapi.sparql.syntax.SelectQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 28/03/2012
 */
public class SPARQLQueryResultsTranslator {

    private final QueryResult result;
    
//    private final QueryResult minusResult;
//
//    private final QueryResult optionalResult;

    private final OWLDataFactory dataFactory;
    
    private final SelectQuery query;
    
    private Map<String, Variable> nameVariableMap = new HashMap<String, Variable>();

    private SolutionMappingTranslator solutionMappingTranslator;

    public SPARQLQueryResultsTranslator(SelectQuery query, QueryResult result, OWLDataFactory dataFactory) {
        this.query = query;
        this.result = result;
//        this.optionalResult = optionalResult;
        this.dataFactory = dataFactory;
//        this.minusResult = minusResult;
        for(Variable v : query.getGroupPattern().getVariables()) {
            nameVariableMap.put(v.getName(), v);
        }
//        for(Variable v : query.getOptionalPatternVariables()) {
//            nameVariableMap.put(v.getName(), v);
//        }
//        for(Variable v : query.getMinusGraphPatternVariables()) {
//            nameVariableMap.put(v.getName(), v);
//        }
        for(Variable variable : query.getSelectClauseVariables()) {
            nameVariableMap.put(variable.getName(), variable);
        }
//        for(SelectAs selectAs : query.getSelectAs()) {
//            Variable variable = selectAs.getVariable();
//            nameVariableMap.put(variable.getName(), variable);
//        }
        LiteralTranslator translator = new LiteralTranslator(dataFactory);
        solutionMappingTranslator = new SolutionMappingTranslator();
    }
    
    public SPARQLQueryResult translate() {
//        List<SolutionMapping> solutionSequence = translateResult(result);
//
////        if (!query.getMinusGraphPatternVariables().isEmpty()) {
////            List<SolutionMapping> minusSolutionSequence = translateResult(minusResult);
////            Set<Variable> minusVariables = new HashSet<>();
////            minusVariables.addAll(query.getGraphPatternVariables());
////            minusVariables.retainAll(query.getMinusGraphPatternVariables());
////            Minus minus = new Minus(solutionSequence, minusSolutionSequence, minusVariables);
////            solutionSequence = minus.getMinus();
////        }
//
////        if(!query.getOptionalPatterns().isEmpty()) {
////            List<SolutionMapping> optionalSolutionSequence = translateResult(optionalResult);
////            Set<Variable> joinVariables = new HashSet<>();
////            joinVariables.addAll(query.getGraphPatternVariables());
////            joinVariables.retainAll(query.getOptionalPatternVariables());
////            Join join = new Join(solutionSequence, optionalSolutionSequence, joinVariables);
////            solutionSequence = join.getLeftJoin();
////        }
//        SolutionModifier solutionModifier = query.getSolutionModifier();
//        com.google.common.base.Optional<OrderClause> orderClause = solutionModifier.getOrderClause();
//        if (orderClause.isPresent()) {
//            Collections.sort(solutionSequence, new OrderByComparator(orderClause.get().getOrderConditions()));
//        }
//        return new SPARQLQueryResult(query, solutionSequence);
        throw new RuntimeException();
    }

    private List<SolutionMapping> translateResult(QueryResult result) {
        List<SolutionMapping> solutionSequence = new ArrayList<>();
        for(int i = 0; i < result.size(); i++) {
            QueryBinding binding = result.get(i);
            SolutionMapping sm = solutionMappingTranslator.translate(binding, nameVariableMap);
            solutionSequence.add(sm);
        }
        return extendAndFilterSolutionSequence(solutionSequence);
    }

    private List<SolutionMapping> extendAndFilterSolutionSequence(List<SolutionMapping> solutionSequence) {
//        SPARQLGraphPattern graphPattern = query.getGraphPatterns().get(0);
//        if(query.getSelectAs().isEmpty() && graphPattern.getFilters().isEmpty() && graphPattern.getBinds().isEmpty()) {
//            return solutionSequence;
//        }
//        List<SolutionMapping> filteredSolutionMapping = new ArrayList<>();
//        for (SolutionMapping solutionMapping : solutionSequence) {
//            // Extend
//            for(SelectAs selectAs : query.getSelectAs()) {
//                EvaluationResult eval = selectAs.getExpression().evaluate(solutionMapping);
//                if (!eval.isError()) {
//                    solutionMapping.bind(selectAs.getVariable(), eval.getResult());
//                }
//            }
//            List<Bind> binds = graphPattern.getBinds();
//            for(Bind bind : binds) {
//                bind.evaluate(solutionMapping);
//            }
//
//            List<Expression> filterConditions = graphPattern.getFilters();
//            // Filter
//            if(filterConditions.isEmpty()) {
//                filteredSolutionMapping.add(solutionMapping);
//            }
//            else {
//                if(matches(filterConditions, solutionMapping)) {
//                    filteredSolutionMapping.add(solutionMapping);
//                }
//            }
//        }
//        return filteredSolutionMapping;
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
