package org.semanticweb.owlapi.sparql.builtin.eval;

import java.util.Optional;
import org.semanticweb.owlapi.sparql.api.EvaluationResult;
import org.semanticweb.owlapi.sparql.api.Expression;
import org.semanticweb.owlapi.sparql.api.Literal;
import org.semanticweb.owlapi.sparql.api.SolutionMapping;
import org.semanticweb.owlapi.sparql.builtin.DateTime;
import org.semanticweb.owlapi.sparql.algebra.EvaluationContext;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 06/06/15
 */
public abstract class AbstractUnaryDateTimeBuiltInCallEvaluator extends AbstractUnaryLiteralBuiltInCallEvaluator {

    @Override
    protected EvaluationResult evaluate(Literal literal, Expression arg, SolutionMapping sm, EvaluationContext evaluationContext) {
        Optional<DateTime> ts = DateTime.parseDateTime(literal.getLexicalForm());
        if(ts.isPresent()) {
            return evaluate(ts.get(), arg, sm);
        }
        else {
            return EvaluationResult.getError();
        }
    }

    protected abstract EvaluationResult evaluate(DateTime dateTime, Expression arg, SolutionMapping sm);
}
