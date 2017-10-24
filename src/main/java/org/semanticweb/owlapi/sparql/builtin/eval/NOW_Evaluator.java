package org.semanticweb.owlapi.sparql.builtin.eval;

import org.semanticweb.owlapi.sparql.api.*;
import org.semanticweb.owlapi.sparql.builtin.DateTime;
import org.semanticweb.owlapi.sparql.algebra.AlgebraEvaluationContext;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 05/06/15
 */
public class NOW_Evaluator implements BuiltInCallEvaluator {

    @Nonnull
    @Override
    public EvaluationResult evaluate(@Nonnull List<Expression> args, @Nonnull SolutionMapping sm, AlgebraEvaluationContext evaluationContext) {
        // NOW() must return the same value in a given query execution
        DateTime ts = new DateTime(evaluationContext.getEvaluationTime());
        return EvaluationResult.getResult(new Literal(Datatype.getXSDDateTime(), ts.getFormattedDateTime(), ""));
    }
}
