package org.semanticweb.owlapi.sparql.api;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/07/2012
 */
public interface Expression extends HasEvaluation, HasVariables {

    <R, E extends Throwable, C> R accept(ExpressionVisitor<R, E, C> visitor, C context) throws E;
}
