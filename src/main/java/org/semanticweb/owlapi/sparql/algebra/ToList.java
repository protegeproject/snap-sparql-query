package org.semanticweb.owlapi.sparql.algebra;

import org.semanticweb.owlapi.reasoner.OWLReasoner;

import java.io.PrintWriter;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 09/06/15
 */
public class ToList extends AlgebraExpression {

    private GraphPatternAlgebraExpression algebraExpression;

    public ToList(GraphPatternAlgebraExpression algebraExpression) {
        this.algebraExpression = algebraExpression;
    }

    @Override
    public SolutionSequence evaluate(OWLReasoner reasoner) {
        return algebraExpression.evaluate(reasoner);
    }

    @Override
    protected void prettyPrint(PrintWriter writer, int level, String indentation) {
        writer.print(indentation);
        writer.println("(ToList ");
        algebraExpression.prettyPrint(writer, level + 1);
        writer.print(indentation);
        writer.println(")");
    }
}
