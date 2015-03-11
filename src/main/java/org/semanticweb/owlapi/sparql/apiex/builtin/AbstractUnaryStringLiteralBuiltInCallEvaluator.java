package org.semanticweb.owlapi.sparql.apiex.builtin;

import org.semanticweb.owlapi.sparql.api.EvaluationResult;
import org.semanticweb.owlapi.sparql.api.Expression;
import org.semanticweb.owlapi.sparql.api.Literal;
import org.semanticweb.owlapi.sparql.api.SolutionMapping;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 10/03/15
 */
public abstract class AbstractUnaryStringLiteralBuiltInCallEvaluator extends AbstractUnaryBuiltInCallEvaluator {

    @Override
    protected EvaluationResult evaluate(Expression arg, SolutionMapping sm) {
        EvaluationResult literalResult = arg.evaluateAsStringLiteral(sm);
        if(literalResult.isError()) {
            return literalResult;
        }
        return evaluate(literalResult.asLiteral(), sm);
    }

    protected abstract EvaluationResult evaluate(Literal literal, SolutionMapping sm);
}
