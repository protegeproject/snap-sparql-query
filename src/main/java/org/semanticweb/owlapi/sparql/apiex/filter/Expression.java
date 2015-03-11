package org.semanticweb.owlapi.sparql.apiex.filter;

import org.semanticweb.owlapi.sparql.apiex.tmp.EBV;
import org.semanticweb.owlapi.sparql.apiex.tmp.SolutionMapping;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/07/2012
 */
public abstract class Expression extends Constraint {

    public EBV evaluate(SolutionMapping solutionMapping) {
        // TODO: ABSTRACT
        return EBV.FALSE;
    }
}
