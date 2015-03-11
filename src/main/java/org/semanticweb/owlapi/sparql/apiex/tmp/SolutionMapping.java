package org.semanticweb.owlapi.sparql.apiex.tmp;

import org.semanticweb.owlapi.sparql.apiex.RDFTerm;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/07/2012
 */
public interface SolutionMapping {

    boolean hasBinding(String variable);

    // OWLEntity (IRI), OWLLiteral
    RDFTerm getBinding(String variable);

}
