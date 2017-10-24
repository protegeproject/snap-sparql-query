package org.semanticweb.owlapi.sparql.algebra;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.semanticweb.owlapi.sparql.api.Axiom;
import org.semanticweb.owlapi.sparql.api.Variable;
import org.semanticweb.owlapi.sparql.sparqldl.EvaluationContext;
import org.semanticweb.owlapi.sparql.sparqldl.Joiner;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 08/06/15
 */
public class Join extends GraphPatternAlgebraExpression<SolutionSequence> {

    private final GraphPatternAlgebraExpression<SolutionSequence> left;

    private final GraphPatternAlgebraExpression<SolutionSequence> right;

    public Join(GraphPatternAlgebraExpression<SolutionSequence> left, GraphPatternAlgebraExpression<SolutionSequence> right) {
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
    public GraphPatternAlgebraExpression<SolutionSequence> getSimplified() {
        GraphPatternAlgebraExpression<SolutionSequence> leftSimp = left.getSimplified();
        GraphPatternAlgebraExpression<SolutionSequence> rightSimp = right.getSimplified();
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
    public SolutionSequence evaluate(AlgebraEvaluationContext context, EvaluationContext evaluationContext) {
        SolutionSequence leftSeq = left.evaluate(context, evaluationContext);
        SolutionSequence rightSeq = right.evaluate(context, evaluationContext);
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
        return toStringHelper("JOIN")
                .addValue(left)
                .addValue(right)
                .toString();
    }

    @Override
    public <R, E extends Throwable> R accept(AlgebraExpressionVisitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }
}
