package org.semanticweb.owlapi.sparql.builtin.eval;

import org.semanticweb.owlapi.sparql.api.EvaluationResult;
import org.semanticweb.owlapi.sparql.api.Expression;
import org.semanticweb.owlapi.sparql.api.Literal;
import org.semanticweb.owlapi.sparql.api.SolutionMapping;
import org.semanticweb.owlapi.sparql.algebra.AlgebraEvaluationContext;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 07/06/15
 */
public class REPLACE_Evaluator implements BuiltInCallEvaluator {


    /**
     *
     *  string literal  REPLACE (string literal arg, simple literal pattern, simple literal replacement )
     *  string literal  REPLACE (string literal arg, simple literal pattern, simple literal replacement,  simple literal flags)
     *
     * @param args
     * @param sm
     * @param evaluationContext
     * @return
     */

    @Nonnull
    @Override
    public EvaluationResult evaluate(@Nonnull List<Expression> args, @Nonnull SolutionMapping sm, AlgebraEvaluationContext evaluationContext) {
        if(args.size() < 3 || args.size() > 4) {
            return EvaluationResult.getError();
        }
        EvaluationResult eval0 = args.get(0).evaluate(sm, evaluationContext).asLiteralOrElseError();
        if(eval0.isError()) {
            return EvaluationResult.getError();
        }
        EvaluationResult eval1 = args.get(1).evaluate(sm, evaluationContext).asStringLiteralOrElseError();
        if(eval1.isError()) {
            return EvaluationResult.getError();
        }
        EvaluationResult eval2 = args.get(2).evaluate(sm, evaluationContext).asStringLiteralOrElseError();
        if(eval2.isError()) {
            return EvaluationResult.getError();
        }
        String flags = "";
        if(args.size() == 4) {
            EvaluationResult eval4 = args.get(3).evaluate(sm, evaluationContext).asStringLiteralOrElseError();
            if(eval4.isError()) {
                return EvaluationResult.getError();
            }
            flags = eval4.asSimpleLiteral();
        }
        Literal literal = eval0.asLiteral();
        String pattern = eval1.asSimpleLiteral();
        String replacement = eval2.asSimpleLiteral();
        String formattedFlags;
        if(flags.isEmpty()) {
            formattedFlags = "";
        }
        else {
            formattedFlags = "(?" + flags + ")";
        }
        String result = literal.getLexicalForm().replaceAll(formattedFlags + pattern, replacement);
        return EvaluationResult.getResult(new Literal(literal.getDatatype(), result, literal.getLang()));
    }
}
