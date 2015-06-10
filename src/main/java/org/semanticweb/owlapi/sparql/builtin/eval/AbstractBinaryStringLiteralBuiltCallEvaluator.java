package org.semanticweb.owlapi.sparql.builtin.eval;

import org.semanticweb.owlapi.sparql.api.EvaluationResult;
import org.semanticweb.owlapi.sparql.api.Expression;
import org.semanticweb.owlapi.sparql.api.Literal;
import org.semanticweb.owlapi.sparql.api.SolutionMapping;
import org.semanticweb.owlapi.sparql.builtin.LiteralCompatibilityChecker;

import java.util.List;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 10/06/15
 */
public abstract class AbstractBinaryStringLiteralBuiltCallEvaluator implements BuiltInCallEvaluator {

    private LiteralCompatibilityChecker compatibilityChecker = new LiteralCompatibilityChecker();

    @Override
    public EvaluationResult evaluate(List<Expression> args, SolutionMapping sm) {
        if(args.size() != 2) {
            return EvaluationResult.getError();
        }
        EvaluationResult evalLeft = args.get(0).evaluateAsLiteral(sm);
        if(evalLeft.isError()) {
            return EvaluationResult.getError();
        }
        EvaluationResult evalRight = args.get(0).evaluateAsLiteral(sm);
        if(evalRight.isError()) {
            return EvaluationResult.getError();
        }
        Literal left = evalLeft.asLiteral();
        Literal right = evalRight.asLiteral();
        if(!compatibilityChecker.isCompatibleWith(left, right)) {
            return EvaluationResult.getError();
        }
        return evaluate(left, right, sm);
    }

    protected abstract EvaluationResult evaluate(Literal left, Literal right, SolutionMapping sm);
}
