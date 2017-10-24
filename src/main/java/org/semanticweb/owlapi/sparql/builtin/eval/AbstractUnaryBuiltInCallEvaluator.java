package org.semanticweb.owlapi.sparql.builtin.eval;

import org.semanticweb.owlapi.sparql.api.EvaluationResult;
import org.semanticweb.owlapi.sparql.api.Expression;
import org.semanticweb.owlapi.sparql.api.SolutionMapping;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 10/03/15
 */
public abstract class AbstractUnaryBuiltInCallEvaluator implements BuiltInCallEvaluator {

    @Nonnull
    @Override
    public final EvaluationResult evaluate(@Nonnull List<Expression> args, @Nonnull SolutionMapping sm) {
        if(args.size() != 1) {
            return EvaluationResult.getError();
        }
        return evaluate(args.get(0), sm);
    }

    protected abstract EvaluationResult evaluate(Expression arg, SolutionMapping sm);
}
