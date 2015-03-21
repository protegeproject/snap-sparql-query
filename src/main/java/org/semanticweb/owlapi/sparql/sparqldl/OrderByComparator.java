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

import com.google.common.base.Optional;
import org.semanticweb.owlapi.sparql.api.SolutionMapping;
import org.semanticweb.owlapi.sparql.api.Term;
import org.semanticweb.owlapi.sparql.api.TermComparator;
import org.semanticweb.owlapi.sparql.api.OrderByModifier;
import org.semanticweb.owlapi.sparql.api.OrderCondition;
import org.semanticweb.owlapi.sparql.api.SolutionModifier;

import java.util.Comparator;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 01/04/2012
 */
public class OrderByComparator implements Comparator<SolutionMapping> {

    private SolutionModifier solutionModifier;

    private final TermComparator termComparator;

    public OrderByComparator(SolutionModifier solutionModifier) {
        this.solutionModifier = solutionModifier;
        termComparator = new TermComparator();
    }

    public int compare(SolutionMapping o1, SolutionMapping o2) {
        for(OrderCondition orderCondition : solutionModifier.getOrderConditions()) {
            Optional<Term> binding1 = o1.getTermForVariableName(orderCondition.getVariable());
            Optional<Term> binding2 = o2.getTermForVariableName(orderCondition.getVariable());
            if(binding1.isPresent()) {
                if(binding2.isPresent()) {
                    int diff = termComparator.compare(binding1.get(), binding2.get());
                    if(diff != 0) {
                        return orderCondition.getOrderByModifier() == OrderByModifier.ASC ? diff : -diff;
                    }
                }
                else {
                    return -1;
                }
            }
            else {
                if(binding2.isPresent()) {
                    return 1;
                }
                else {
                    return 0;
                }
            }

//            int diff = 0;
//            if(binding1 instanceof OWLLiteral && binding2 instanceof OWLLiteral) {
//                OWLLiteral lit1 = ((OWLLiteral) binding1);
//                OWLLiteral lit2 = ((OWLLiteral) binding2);
//                Pattern pattern = OWL2Datatype.XSD_DOUBLE.getPattern();
//                if(pattern.matcher(lit1.getLiteral()).matches() && pattern.matcher(lit2.getLiteral()).matches()) {
//                    Double d1 = Double.parseDouble(lit1.getLiteral());
//                    Double d2 = Double.parseDouble(lit2.getLiteral());
//                    diff = d1.compareTo(d2);
//                }
//                else {
//                    diff = binding1.compareTo(binding2);
//                }
//            }
//            else {
//                diff = binding1.compareTo(binding2);
//            }
//            if(diff != 0) {
//                return orderCondition.getOrderByModifier() == OrderByModifier.ASC ? diff : -diff;
//            }
        }
        return 0;
    }
}
