package org.semanticweb.owlapi.sparql.builtin.eval;

import com.google.common.base.Objects;
import org.semanticweb.owlapi.sparql.api.EvaluationResult;
import org.semanticweb.owlapi.sparql.api.Expression;
import org.semanticweb.owlapi.sparql.api.SolutionMapping;
import org.semanticweb.owlapi.sparql.algebra.AlgebraEvaluationContext;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 20/03/15
 */
public class STRENDS_Evaluator implements BuiltInCallEvaluator {

    @Nonnull
    @Override
    public EvaluationResult evaluate(@Nonnull List<Expression> args, @Nonnull SolutionMapping sm, AlgebraEvaluationContext evaluationContext) {
        if(args.size() != 2) {
            return EvaluationResult.getError();
        }
        EvaluationResult arg0 = args.get(0).evaluate(sm, evaluationContext).asStringLiteralOrElseError();
        if(arg0.isError()) {
            return arg0;
        }
        EvaluationResult arg1 = args.get(1).evaluate(sm, evaluationContext).asStringLiteralOrElseError();
        if(arg1.isError()) {
            return EvaluationResult.getError();
        }
        return EvaluationResult.getBoolean(arg0.asLiteral().getLexicalForm().endsWith(arg1.asLiteral().getLexicalForm()));
    }


    @Override
    public String toString() {
        return Objects.toStringHelper("STRENDS_Evaluator")
                .toString();
    }
}
