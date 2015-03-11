package org.semanticweb.owlapi.sparql.apiex.tmp;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/07/2012
 */
public enum QueryForm {

//    SPARQL has four query forms. These query forms use the solutions from pattern matching to form result sets or RDF graphs. The query forms are:
//
//    SELECT
//    Returns all, or a subset of, the variables bound in a query pattern match.
//    CONSTRUCT
//    Returns an RDF graph constructed by substituting variables in a set of triple templates.
//    ASK
//    Returns a boolean indicating whether a query pattern matches or not.
//    DESCRIBE
//    Returns an RDF graph that describes the resources found.

    SELECT,

    CONSTRUCT,

    ASK,

    DESCRIBE
}
