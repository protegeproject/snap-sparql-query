package org.semanticweb.owlapi.sparql.api;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 27/07/2012
 */
public interface Term extends Expression, Visitable {

    boolean isSameRDFTermAs(Term term);
}
