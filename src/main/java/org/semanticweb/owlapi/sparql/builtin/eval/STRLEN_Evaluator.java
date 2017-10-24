package org.semanticweb.owlapi.sparql.builtin.eval;

import org.semanticweb.owlapi.sparql.api.EvaluationResult;
import org.semanticweb.owlapi.sparql.api.Expression;
import org.semanticweb.owlapi.sparql.api.SolutionMapping;
import org.semanticweb.owlapi.sparql.algebra.EvaluationContext;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 10/03/15
 */
public class STRLEN_Evaluator implements BuiltInCallEvaluator {

    @Nonnull
    @Override
    public EvaluationResult evaluate(@Nonnull List<Expression> args, @Nonnull SolutionMapping sm, EvaluationContext evaluationContext) {
        EvaluationResult eval = args.get(0).evaluateAsStringLiteral(sm, evaluationContext);
        if(eval.isError()) {
            return eval;
        }
        else {
            return EvaluationResult.getInteger(eval.asLiteral().getLexicalForm().length());
        }
    }
}
