package org.semanticweb.owlapi.sparql.algebra;

import com.google.common.collect.ImmutableList;
import org.semanticweb.owlapi.sparql.api.SolutionMapping;
import org.semanticweb.owlapi.sparql.sparqldl.BgpEvaluator;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 11/06/15
 */
public class AlgebraExpressionEvaluatorVisitor implements AlgebraExpressionVisitor<SolutionSequence, RuntimeException> {

    private BgpEvaluator bgpEvaluator;

    @Override
    public SolutionSequence visit(Bgp bgp) {
        return bgpEvaluator.evaluate(bgp);
    }

    @Override
    public SolutionSequence visit(Distinct distinct) {
        SolutionSequence sequence = distinct.getExpression().accept(this);
        Set<SolutionMapping> distinctSolutions = new LinkedHashSet<>(sequence.getSolutionMappings());
        return new SolutionSequence(sequence.getVariableList(), ImmutableList.copyOf(distinctSolutions));
    }

    @Override
    public SolutionSequence visit(Empty empty) {
        return SolutionSequence.getEmptySolutionSequence();
    }

    @Override
    public SolutionSequence visit(Extend extend) {
        return null;
    }

    @Override
    public SolutionSequence visit(Filter filter) {
        return null;
    }

    @Override
    public SolutionSequence visit(Join join) {
        return null;
    }

    @Override
    public SolutionSequence visit(LeftJoin leftJoin) {
        return null;
    }

    @Override
    public SolutionSequence visit(Minus minus) {
        return null;
    }

    @Override
    public SolutionSequence visit(OrderBy orderBy) {
        return null;
    }

    @Override
    public SolutionSequence visit(Project project) {
        return null;
    }

    @Override
    public SolutionSequence visit(ToList toList) {
        return null;
    }

    @Override
    public SolutionSequence visit(Union union) {
        return null;
    }
}
