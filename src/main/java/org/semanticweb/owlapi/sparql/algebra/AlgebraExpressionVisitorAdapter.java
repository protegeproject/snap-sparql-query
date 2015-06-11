package org.semanticweb.owlapi.sparql.algebra;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 11/06/15
 */
public abstract class AlgebraExpressionVisitorAdapter<R, E extends Exception> implements AlgebraExpressionVisitor<R, E> {

    private final R defaultReturnValue;

    public AlgebraExpressionVisitorAdapter(R defaultReturnValue) {
        this.defaultReturnValue = defaultReturnValue;
    }

    protected  R doDefault() throws E {
        return defaultReturnValue;
    }

    @Override
    public R visit(Bgp bgp) throws E {
        return doDefault();
    }

    @Override
    public R visit(Distinct distinct) throws E {
        return doDefault();
    }

    @Override
    public R visit(Empty empty) throws E {
        return doDefault();
    }

    @Override
    public R visit(Extend extend) throws E {
        return doDefault();
    }

    @Override
    public R visit(Filter filter) throws E {
        return doDefault();
    }

    @Override
    public R visit(Join join) throws E {
        return doDefault();
    }

    @Override
    public R visit(LeftJoin leftJoin) throws E {
        return doDefault();
    }

    @Override
    public R visit(Minus minus) throws E {
        return doDefault();
    }

    @Override
    public R visit(OrderBy orderBy) throws E {
        return doDefault();
    }

    @Override
    public R visit(Project project) throws E {
        return doDefault();
    }

    @Override
    public R visit(ToList toList) throws E {
        return doDefault();
    }

    @Override
    public R visit(Union union) throws E {
        return doDefault();
    }
}
