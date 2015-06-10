package org.semanticweb.owlapi.sparql.algebra;

import com.google.common.collect.ImmutableList;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.sparql.api.OrderCondition;
import org.semanticweb.owlapi.sparql.api.SolutionMapping;
import org.semanticweb.owlapi.sparql.sparqldl.OrderByComparator;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 09/06/15
 */
public class OrderBy extends AlgebraExpression {

    private AlgebraExpression algebraExpression;

    private OrderByComparator comparator;

    public OrderBy(AlgebraExpression algebraExpression, OrderByComparator comparator) {
        this.algebraExpression = algebraExpression;
        this.comparator = comparator;
    }

    @Override
    public SolutionSequence evaluate(OWLReasoner reasoner) {
        SolutionSequence sequence = algebraExpression.evaluate(reasoner);
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
}
