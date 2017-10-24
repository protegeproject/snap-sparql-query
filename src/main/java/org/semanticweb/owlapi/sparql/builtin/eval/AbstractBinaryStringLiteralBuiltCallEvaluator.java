package org.semanticweb.owlapi.sparql.builtin.eval;

import org.semanticweb.owlapi.sparql.api.EvaluationResult;
import org.semanticweb.owlapi.sparql.api.Expression;
import org.semanticweb.owlapi.sparql.api.Literal;
import org.semanticweb.owlapi.sparql.api.SolutionMapping;
import org.semanticweb.owlapi.sparql.builtin.LiteralCompatibilityChecker;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 10/06/15
 */
public abstract class AbstractBinaryStringLiteralBuiltCallEvaluator implements BuiltInCallEvaluator {

    private LiteralCompatibilityChecker compatibilityChecker = new LiteralCompatibilityChecker();

    @Nonnull
    @Override
    public EvaluationResult evaluate(@Nonnull List<Expression> args, @Nonnull SolutionMapping sm) {
        if(args.size() != 2) {
            return EvaluationResult.getError();
        }
        EvaluationResult evalLeft = args.get(0).evaluateAsLiteral(sm);
        if(evalLeft.isError()) {
            return EvaluationResult.getError();
        }
        EvaluationResult evalRight = args.get(1).evaluateAsLiteral(sm);
        if(evalRight.isError()) {
            return EvaluationResult.getError();
        }
        Literal left = evalLeft.asLiteral();
        Literal right = evalRight.asLiteral();
        return evaluate(left, right, sm);
    }

    final protected EvaluationResult evaluate(Literal left, Literal right, SolutionMapping sm) {
        if(!compatibilityChecker.isCompatibleWith(left, right)) {
            return EvaluationResult.getError();
        }
        return evaluateCompatibleLiterals(left, right, sm);
    }

    protected abstract EvaluationResult evaluateCompatibleLiterals(Literal left, Literal right, SolutionMapping sm);
}
