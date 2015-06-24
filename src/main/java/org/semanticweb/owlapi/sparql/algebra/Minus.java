package org.semanticweb.owlapi.sparql.algebra;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.sparql.api.Variable;
import org.semanticweb.owlapi.sparql.sparqldl.BgpEvaluator;

import java.io.PrintWriter;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 08/06/15
 */
public class Minus extends GraphPatternAlgebraExpression<SolutionSequence> {

    private GraphPatternAlgebraExpression<SolutionSequence> left;

    private GraphPatternAlgebraExpression<SolutionSequence> right;

    public Minus(GraphPatternAlgebraExpression<SolutionSequence> left, GraphPatternAlgebraExpression<SolutionSequence> right) {
        this.left = checkNotNull(left);
        this.right = checkNotNull(right);
    }

    public GraphPatternAlgebraExpression getLeft() {
        return left;
    }

    public GraphPatternAlgebraExpression getRight() {
        return right;
    }

    @Override
    public GraphPatternAlgebraExpression<SolutionSequence> getSimplified() {
        return new Minus(left.getSimplified(), right.getSimplified());
    }

    @Override
    public void collectVisibleVariables(ImmutableSet.Builder<Variable> variableBuilder) {
        left.collectVisibleVariables(variableBuilder);
        // Right side is not visible
    }

    @Override
    public SolutionSequence evaluate(AlgebraEvaluationContext context) {
        SolutionSequence leftSeq = left.evaluate(context);
        SolutionSequence rightSeq = right.evaluate(context);
        Set<Variable> sharedVariables = left.getSharedVariables(right);
        org.semanticweb.owlapi.sparql.sparqldl.Minus minus = new org.semanticweb.owlapi.sparql.sparqldl.Minus(leftSeq.getSolutionMappings(), rightSeq.getSolutionMappings(), sharedVariables);
        return new SolutionSequence(leftSeq.getVariableList(), ImmutableList.copyOf(minus.getMinus()));
    }

    @Override
    public String toString() {
        return Objects.toStringHelper("MINUS")
                .addValue(left)
                .addValue(right)
                .toString();
    }

    @Override
    protected void prettyPrint(PrintWriter writer, int level, String indentation) {
        writer.print(indentation);
        writer.println("(Minus ");
        left.prettyPrint(writer, level + 1);
        right.prettyPrint(writer, level + 1);
        writer.print(indentation);
        writer.println(")");
    }


    @Override
    public <R, E extends Exception> R accept(AlgebraExpressionVisitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }
}
