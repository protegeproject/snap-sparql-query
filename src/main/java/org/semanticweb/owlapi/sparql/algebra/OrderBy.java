package org.semanticweb.owlapi.sparql.algebra;

import com.google.common.collect.ImmutableList;
import org.semanticweb.owlapi.sparql.api.SolutionMapping;
import org.semanticweb.owlapi.sparql.sparqldl.OrderByComparator;

import java.util.ArrayList;
import java.util.List;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 09/06/15
 */
public class OrderBy extends AlgebraExpression<SolutionSequence> {

    private AlgebraExpression<SolutionSequence> algebraExpression;

    private OrderByComparator comparator;

    public OrderBy(AlgebraExpression<SolutionSequence> algebraExpression, OrderByComparator comparator) {
        this.algebraExpression = algebraExpression;
        this.comparator = comparator;
    }

    public AlgebraExpression<SolutionSequence> getAlgebraExpression() {
        return algebraExpression;
    }

    public OrderByComparator getComparator() {
        return comparator;
    }

    @Override
    public SolutionSequence evaluate(AlgebraEvaluationContext context) {
        SolutionSequence sequence = algebraExpression.evaluate(context);
        List<SolutionMapping> sortedList = new ArrayList<>(sequence.getSolutionMappings());
        sortedList.sort(comparator);
        return new SolutionSequence(sequence.getVariableList(), ImmutableList.copyOf(sortedList));
    }

    @Override
    public <R, E extends Throwable> R accept(AlgebraExpressionVisitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }
}
