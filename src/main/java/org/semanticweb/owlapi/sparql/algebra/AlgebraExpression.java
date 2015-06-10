package org.semanticweb.owlapi.sparql.algebra;

import org.semanticweb.owlapi.reasoner.OWLReasoner;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 09/06/15
 */
public abstract class AlgebraExpression {

    public abstract SolutionSequence evaluate(OWLReasoner reasoner);

    public String toPrettyPrintedString() {
        StringWriter out = new StringWriter();
        PrintWriter printWriter = new PrintWriter(out);
        prettyPrint(printWriter, 0);
        return out.toString();
    }

    protected final void prettyPrint(PrintWriter printWriter, int level) {
        StringBuilder indentation = new StringBuilder();
        for(int i = 0; i < level; i++) {
            indentation.append("    ");
        }
        prettyPrint(printWriter, level, indentation.toString());

    }

    protected abstract void prettyPrint(PrintWriter writer, int level, String indentation);
}
