package org.semanticweb.owlapi.sparql.algebra;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import jpaul.Constraints.Var;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.sparql.api.Expression;
import org.semanticweb.owlapi.sparql.api.SolutionMapping;
import org.semanticweb.owlapi.sparql.api.Variable;
import org.semanticweb.owlapi.sparql.sparqldl.BgpEvaluator;
import org.semanticweb.owlapi.sparql.sparqldl.Joiner;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 08/06/15
 */
public class LeftJoin extends GraphPatternAlgebraExpression<SolutionSequence> {

    private final GraphPatternAlgebraExpression<SolutionSequence> left;

    private final GraphPatternAlgebraExpression<SolutionSequence> right;

    private final ImmutableList<Expression> expression;

    public LeftJoin(GraphPatternAlgebraExpression<SolutionSequence> left,
                    GraphPatternAlgebraExpression<SolutionSequence> right,
                    ImmutableList<Expression> expression) {
        this.left = left;
        this.right = right;
        this.expression = expression;
    }

    public GraphPatternAlgebraExpression getLeft() {
        return left;
    }

    @Override
    public GraphPatternAlgebraExpression<SolutionSequence> getSimplified() {
        return new LeftJoin(left.getSimplified(), right.getSimplified(), expression);
    }

    @Override
    public void collectVisibleVariables(ImmutableSet.Builder<Variable> variableBuilder) {
        left.collectVisibleVariables(variableBuilder);
        right.collectVisibleVariables(variableBuilder);
    }

    @Override
    public SolutionSequence evaluate(AlgebraEvaluationContext context) {
        SolutionSequence leftSeq = left.evaluate(context);
        SolutionSequence rightSeq = right.evaluate(context);
        List<Variable> unionVariables = new ArrayList<>();
        unionVariables.addAll(leftSeq.getVariableList());
        for(Variable variable : rightSeq.getVariableList()) {
            unionVariables.add(variable);
        }
        Set<Variable> sharedVariables = left.getSharedVariables(right);
        Joiner joiner = new Joiner(leftSeq.getSolutionMappings(), rightSeq.getSolutionMappings(), sharedVariables);
        List<SolutionMapping> leftJoin = joiner.getLeftJoin();
        if(!expression.isEmpty()) {
            for(Iterator<SolutionMapping> it = leftJoin.iterator(); it.hasNext(); ) {
                SolutionMapping next = it.next();
                for(Expression exp : expression) {
                    if(exp.evaluateAsEffectiveBooleanValue(next).isFalse()) {
                        it.remove();
                        break;
                    }
                }
            }
        }
        return new SolutionSequence(unionVariables, ImmutableList.copyOf(leftJoin));
    }

    @Override
    public String toString() {
        return Objects.toStringHelper("LeftJoin")
                .addValue(left)
                .addValue(right)
                .addValue(expression)
                .toString();
    }

    @Override
    protected void prettyPrint(PrintWriter writer, int level, String indentation) {
        writer.print(indentation);
        writer.println("(LeftJoin ");
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
