package org.semanticweb.owlapi.sparql.api;

import org.semanticweb.owlapi.model.PrefixManager;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 02/08/2012
 */
public interface HasPrefixedName {

    String getPrefixedName(SPARQLPrefixManager pm);
}
