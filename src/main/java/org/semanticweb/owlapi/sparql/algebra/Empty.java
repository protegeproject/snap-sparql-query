package org.semanticweb.owlapi.sparql.algebra;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.sparql.api.Variable;
import org.semanticweb.owlapi.sparql.sparqldl.BgpEvaluator;

import java.io.PrintWriter;
import java.util.Collections;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 09/06/15
 */
public class Empty extends GraphPatternAlgebraExpression<SolutionSequence> {

    private static final Empty instance = new Empty();

    private Empty() {
    }

    public static Empty get() {
        return instance;
    }

    @Override
    public void collectVisibleVariables(ImmutableSet.Builder<Variable> variableBuilder) {

    }

    @Override
    public SolutionSequence evaluate(AlgebraEvaluationContext context) {
        return SolutionSequence.getEmptySolutionSequence();
    }

    @Override
    public String toString() {
        return Objects.toStringHelper("Empty")
                .toString();
    }

    @Override
    public GraphPatternAlgebraExpression getSimplified() {
        return this;
    }

    @Override
    protected void prettyPrint(PrintWriter writer, int level, String indentation) {
        writer.print(indentation);
        writer.println("(Empty)");
    }


    @Override
    public <R, E extends Exception> R accept(AlgebraExpressionVisitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }
}
