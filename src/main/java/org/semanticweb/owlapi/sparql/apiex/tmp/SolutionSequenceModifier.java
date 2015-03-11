package org.semanticweb.owlapi.sparql.apiex.tmp;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/07/2012
 */
public enum SolutionSequenceModifier {

    ORDER_BY,

    PROJECTION,

    DISTINCT,

    REDUCED,

    OFFSET,

    LIMIT

//    Order By modifier: put the solutions in order
//    Projection modifier: choose certain variables
//    Distinct modifier: ensure solutions in the sequence are unique
//    Reduced modifier: permit any non-distinct solutions to be eliminated
//    Offset modifier: control where the solutions start from in the overall sequence of solutions
//    Limit modifier: restrict the number of solutions

}
