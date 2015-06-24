package org.semanticweb.owlapi.sparql.algebra;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 09/06/15
 */
public abstract class AlgebraExpression<E> {

    public abstract E evaluate(AlgebraEvaluationContext context);

    public abstract <R, X extends Throwable> R accept(AlgebraExpressionVisitor<R, X> visitor) throws X;
}
