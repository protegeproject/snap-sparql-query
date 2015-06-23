package org.semanticweb.owlapi.sparql.algebra;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 11/06/15
 */
public interface AlgebraExpressionVisitor<R, E extends Exception> {

    R visit(Bgp bgp) throws E;

    R visit(Distinct distinct) throws E;

    R visit(Empty empty) throws E;

    R visit(Extend extend) throws E;

    R visit(Filter filter) throws E;

    R visit(Join join) throws E;

    R visit(LeftJoin leftJoin) throws E;

    R visit(Minus minus) throws E;

    R visit(OrderBy orderBy) throws E;

    R visit(Project project) throws E;

    R visit(ToList toList) throws E;

    R visit(Union union) throws E;

    R visit(Aggregation aggregation) throws E;

    R visit(AggregateJoin aggregateJoin) throws E;

    R visit(Group group) throws E;
}

