package org.semanticweb.owlapi.sparql.algebra;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.sparql.api.EvaluationResult;
import org.semanticweb.owlapi.sparql.api.Expression;
import org.semanticweb.owlapi.sparql.api.SolutionMapping;
import org.semanticweb.owlapi.sparql.api.Variable;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 08/06/15
 */
public class Filter extends GraphPatternAlgebraExpression {

    private final ImmutableList<Expression> expressions;

    private final GraphPatternAlgebraExpression algebraExpression;

    public Filter(ImmutableList<Expression> expressions, GraphPatternAlgebraExpression algebraExpression) {
        this.expressions = expressions;
        this.algebraExpression = algebraExpression;
    }

    public ImmutableList<Expression> getExpressions() {
        return expressions;
    }

    public GraphPatternAlgebraExpression getPattern() {
        return algebraExpression;
    }

    @Override
    public void collectVisibleVariables(ImmutableSet.Builder<Variable> variableBuilder) {

    }

    @Override
    public SolutionSequence evaluate(OWLReasoner reasoner) {
        SolutionSequence sequence = algebraExpression.evaluate(reasoner);
        List<SolutionMapping> filteredList = new ArrayList<>();
        for(SolutionMapping sm : sequence.getSolutionMappings()) {
            for(Expression expression : expressions) {
                EvaluationResult evaluate = expression.evaluateAsEffectiveBooleanValue(sm);
                if(evaluate.isTrue()) {
                    filteredList.add(sm);
                }
            }
        }
        return new SolutionSequence(sequence.getVariableList(), ImmutableList.copyOf(filteredList));
    }

    @Override
    public String toString() {
        return Objects.toStringHelper("FILTER")
                .addValue(expressions)
                .addValue(algebraExpression)
                .toString();
    }

    @Override
    public GraphPatternAlgebraExpression getSimplified() {
        return new Filter(expressions, algebraExpression.getSimplified());
    }

    @Override
    protected void prettyPrint(PrintWriter writer, int level, String indentation) {
        writer.print(indentation);
        writer.println("(Filter ");
        writer.print(indentation + "    ");
        for(Expression expression : expressions) {
            writer.print(expression);
            writer.print(" ");
        }
        writer.println();
        algebraExpression.prettyPrint(writer, level + 1);
        writer.print(indentation);
        writer.print(")");
    }
}
