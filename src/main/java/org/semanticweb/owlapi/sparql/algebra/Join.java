package org.semanticweb.owlapi.sparql.algebra;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.sparql.api.Axiom;
import org.semanticweb.owlapi.sparql.api.Variable;
import org.semanticweb.owlapi.sparql.sparqldl.Joiner;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 08/06/15
 */
public class Join extends GraphPatternAlgebraExpression {

    private final GraphPatternAlgebraExpression left;

    private final GraphPatternAlgebraExpression right;

    public Join(GraphPatternAlgebraExpression left, GraphPatternAlgebraExpression right) {
        this.left = left;
        this.right = right;
    }

    public GraphPatternAlgebraExpression getLeft() {
        return left;
    }

    public GraphPatternAlgebraExpression getRight() {
        return right;
    }

    @Override
    public GraphPatternAlgebraExpression getSimplified() {
        GraphPatternAlgebraExpression leftSimp = left.getSimplified();
        GraphPatternAlgebraExpression rightSimp = right.getSimplified();
        if(leftSimp instanceof Empty) {
            return rightSimp;
        }
        if(rightSimp instanceof Empty) {
            return leftSimp;
        }
        // Optimisation - we can merge adjacent BGPs
        if(leftSimp instanceof Bgp && rightSimp instanceof Bgp) {
            ImmutableList<Axiom> axioms = ImmutableList.<Axiom>builder()
                    .addAll(((Bgp) leftSimp).getAxioms())
                    .addAll(((Bgp) rightSimp).getAxioms()).build();
            return new Bgp(axioms);
        }
        return new Join(leftSimp, rightSimp);
    }

    @Override
    public void collectVisibleVariables(ImmutableSet.Builder<Variable> variableBuilder) {
        left.collectVisibleVariables(variableBuilder);
        right.collectVisibleVariables(variableBuilder);
    }

    @Override
    public SolutionSequence evaluate(OWLReasoner reasoner) {
        SolutionSequence leftSeq = left.evaluate(reasoner);
        SolutionSequence rightSeq = right.evaluate(reasoner);
        Joiner joiner = new Joiner(leftSeq.getSolutionMappings(), rightSeq.getSolutionMappings(), left.getSharedVariables(right));
        List<Variable> unionVariables = new ArrayList<>();
        unionVariables.addAll(leftSeq.getVariableList());
        for(Variable variable : rightSeq.getVariableList()) {
            unionVariables.add(variable);
        }
        return new SolutionSequence(unionVariables, ImmutableList.copyOf(joiner.getJoin()));
    }

    @Override
    public String toString() {
        return Objects.toStringHelper("JOIN")
                .addValue(left)
                .addValue(right)
                .toString();
    }

    @Override
    protected void prettyPrint(PrintWriter writer, int level, String indentation) {
        writer.print(indentation);
        writer.println("(Join ");
        left.prettyPrint(writer, level + 1);
        right.prettyPrint(writer, level + 1);
        writer.print(indentation);
        writer.println(")");
    }
}
