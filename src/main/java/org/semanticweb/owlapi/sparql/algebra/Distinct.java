package org.semanticweb.owlapi.sparql.algebra;

import com.google.common.collect.ImmutableList;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.sparql.api.SolutionMapping;

import java.io.PrintWriter;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 09/06/15
 */
public class Distinct extends AlgebraExpression {

    private AlgebraExpression expression;

    public Distinct(AlgebraExpression expression) {
        this.expression = expression;
    }

    @Override
    public SolutionSequence evaluate(OWLReasoner reasoner) {
        SolutionSequence sequence = expression.evaluate(reasoner);
        Set<SolutionMapping> distinctSolutions = new LinkedHashSet<>(sequence.getSolutionMappings());
        return new SolutionSequence(sequence.getVariableList(), ImmutableList.copyOf(distinctSolutions));
    }

    @Override
    protected void prettyPrint(PrintWriter writer, int level, String indentation) {
        writer.print(indentation);
        writer.println("(Distinct ");
        expression.prettyPrint(writer, level + 1);
        writer.print(indentation);
        writer.println(")");
    }
}
