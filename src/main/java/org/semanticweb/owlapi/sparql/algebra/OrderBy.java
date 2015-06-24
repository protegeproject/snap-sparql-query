package org.semanticweb.owlapi.sparql.algebra;

import com.google.common.collect.ImmutableList;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.sparql.api.OrderCondition;
import org.semanticweb.owlapi.sparql.api.SolutionMapping;
import org.semanticweb.owlapi.sparql.sparqldl.BgpEvaluator;
import org.semanticweb.owlapi.sparql.sparqldl.OrderByComparator;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public SolutionSequence evaluate(AlgebraEvaluationContext context) {
        SolutionSequence sequence = algebraExpression.evaluate(context);
        List<SolutionMapping> sortedList = new ArrayList<>(sequence.getSolutionMappings());
        sortedList.sort(comparator);
        return new SolutionSequence(sequence.getVariableList(), ImmutableList.copyOf(sortedList));
    }

    @Override
    protected void prettyPrint(PrintWriter writer, int level, String indentation) {
        writer.print(indentation);
        writer.println("(OrderBy ");
        algebraExpression.prettyPrint(writer, level + 1);
        for(OrderCondition orderCondition : comparator.getOrderConditions()) {
            writer.print(indentation);
            writer.print("    ");
            writer.println(orderCondition);
        }
        writer.print(indentation);
        writer.println(")");
    }


    @Override
    public <R, E extends Exception> R accept(AlgebraExpressionVisitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }
}
