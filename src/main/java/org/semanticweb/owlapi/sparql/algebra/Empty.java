package org.semanticweb.owlapi.sparql.algebra;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableSet;
import org.semanticweb.owlapi.sparql.api.Variable;

import java.io.PrintWriter;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 09/06/15
 */
public class Empty extends GraphPatternAlgebraExpression<SolutionSequence> {

    private static final Empty instance = new Empty();

    private Empty() {
    }

    public static Empty get() {
        return instance;
    }

    @Override
    public void collectVisibleVariables(ImmutableSet.Builder<Variable> variableBuilder) {

    }

    @Override
    public SolutionSequence evaluate(AlgebraEvaluationContext context) {
        return SolutionSequence.getEmptySolutionSequence();
    }

    @Override
    public String toString() {
        return toStringHelper("Empty")
                .toString();
    }

    @Override
    public Empty getSimplified() {
        return this;
    }

    @Override
    public <R, E extends Throwable> R accept(AlgebraExpressionVisitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }
}
