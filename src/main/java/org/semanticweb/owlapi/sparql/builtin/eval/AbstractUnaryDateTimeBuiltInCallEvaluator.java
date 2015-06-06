package org.semanticweb.owlapi.sparql.builtin.eval;

import com.google.common.base.Optional;
import org.semanticweb.owlapi.sparql.api.EvaluationResult;
import org.semanticweb.owlapi.sparql.api.Expression;
import org.semanticweb.owlapi.sparql.api.Literal;
import org.semanticweb.owlapi.sparql.api.SolutionMapping;
import org.semanticweb.owlapi.sparql.builtin.Timestamp;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 06/06/15
 */
public abstract class AbstractUnaryDateTimeBuiltInCallEvaluator extends AbstractUnaryLiteralBuiltInCallEvaluator {

    @Override
    protected EvaluationResult evaluate(Literal literal, Expression arg, SolutionMapping sm) {
        Optional<Timestamp> ts = Timestamp.parseDateTime(literal.getLexicalForm());
        if(ts.isPresent()) {
            return evaluate(ts.get(), arg, sm);
        }
        else {
            return EvaluationResult.getError();
        }
    }

    protected abstract EvaluationResult evaluate(Timestamp timestamp, Expression arg, SolutionMapping sm);
}
