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

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.sparql.api.AtomicIRI;
import org.semanticweb.owlapi.sparql.api.HasPrefixedName;
import org.semanticweb.owlapi.sparql.api.Literal;
import org.semanticweb.owlapi.sparql.api.SPARQLPrefixManager;
import org.semanticweb.owlapi.util.DefaultPrefixManager;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 29/03/2012
 */
public class SPARQLResultsTableCellRenderer extends DefaultTableCellRenderer implements TableCellRenderer {

    private SPARQLPrefixManager pm = SPARQLPrefixManager.createWithDefaultPrefixes();

    public void setPrefixManager(SPARQLPrefixManager pm) {
        this.pm = pm;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if(value instanceof HasPrefixedName) {
            label.setText(((HasPrefixedName) value).getPrefixedName(pm));
        }
        else if(value instanceof AtomicIRI) {
            AtomicIRI entity = (AtomicIRI) value;
            String prefixIRI = pm.getPrefixedNameOrIri(entity.getIRI());
            label.setText(prefixIRI);
        }
        else if(value instanceof IRI) {
            String prefixIRI = pm.getPrefixedNameOrIri((IRI) value);
            label.setText(prefixIRI);
        }
        else if(value instanceof Literal) {
            Literal literal = (Literal) value;
            if(literal.isDatatypeNumeric()) {
                label.setText(literal.getLexicalForm());
            }
            else {
                if(literal.isRDFPlainLiteral()) {
                    if (!literal.getLang().isEmpty()) {
                        label.setText(String.format("\"%s\"@%s", literal.getLexicalForm(), literal.getLang()));
                    }
                    else {
                        label.setText(String.format("\"%s\"", literal.getLexicalForm()));
                    }
                }
                else {
                    String prefixIRI = pm.getPrefixedNameOrIri(literal.getDatatype().getIRI());
                    label.setText(String.format("\"%s\"^^%s", literal.getLexicalForm(), prefixIRI));
                }
            }
        }
        else if(value != null) {
            label.setText(value.toString());
        }
        return label;
    

    }
}
