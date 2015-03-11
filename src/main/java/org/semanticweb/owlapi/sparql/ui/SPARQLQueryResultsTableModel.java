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

package org.semanticweb.owlapi.sparql.ui;

import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.sparql.api.SolutionMapping;
import org.semanticweb.owlapi.sparql.api.Variable;
import org.semanticweb.owlapi.sparql.api.SPARQLQueryResult;

import javax.swing.table.AbstractTableModel;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 28/03/2012
 */
public class SPARQLQueryResultsTableModel extends AbstractTableModel {

    private SPARQLQueryResult result;

    private int size;

    private Map<Integer, Variable> indexVariableMap = new HashMap<Integer, Variable>();

    private PrefixManager pm;

    public SPARQLQueryResult getResult() {
        return result;
    }

    public void setPm(PrefixManager pm) {
        this.pm = pm;
    }

    public void setResult(SPARQLQueryResult result) {
        this.result = result;
        this.size = result.getQuery().getSelectVariables().size() + result.getQuery().getSelectAsVariables().size();
        indexVariableMap.clear();
        int index = 0;
        for(Variable var : result.getQuery().getSelectVariables()) {
            indexVariableMap.put(index, var);
            index++;
        }
        for(Variable var : result.getQuery().getSelectAsVariables()) {
            indexVariableMap.put(index, var);
            index++;
        }
        fireTableStructureChanged();
    }

    public int getRowCount() {
        return result == null ? 0 : result.getSize();
    }

    public int getColumnCount() {
        return size;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        SolutionMapping binding = result.getBindingAt(rowIndex);
        Variable variable = indexVariableMap.get(columnIndex);
        if (variable != null) {
            return binding.getTermForVariable(variable);
        }
        else {
            return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        Variable var = indexVariableMap.get(column);
        if(var != null) {
            return var.getVariableNamePrefix().getPrefix() + var.getName();
        }
        else {
            return "";
        }
    }
}
