package org.semanticweb.owlapi.sparql.builtin.eval;

import org.semanticweb.owlapi.sparql.api.EvaluationResult;
import org.semanticweb.owlapi.sparql.api.Expression;
import org.semanticweb.owlapi.sparql.api.SolutionMapping;
import org.semanticweb.owlapi.sparql.algebra.AlgebraEvaluationContext;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 10/03/15
 */
public class LANGMATCHES_Evaluator implements BuiltInCallEvaluator {

    @Nonnull
    @Override
    public EvaluationResult evaluate(@Nonnull List<Expression> args, @Nonnull SolutionMapping sm, AlgebraEvaluationContext evaluationContext) {
        if(args.size() != 2) {
            return EvaluationResult.getError();
        }
        EvaluationResult result0 = args.get(0).evaluate(sm, evaluationContext).asStringLiteralOrElseError();
        if(result0.isError()) {
            return result0;
        }
        EvaluationResult result1 = args.get(1).evaluate(sm, evaluationContext).asStringLiteralOrElseError();
        if(result1.isError()) {
            return result1;
        }
        String lang = result0.asSimpleLiteral();
        if(lang.isEmpty()) {
            return EvaluationResult.getFalse();
        }
        String pattern = result1.asSimpleLiteral();
        boolean matches = !Locale.filterTags(getRangeFromPattern(pattern), Collections.singleton(lang)).isEmpty();
        return EvaluationResult.getBoolean(matches);
    }


    public static List<Locale.LanguageRange> getRangeFromPattern(@Nonnull String pattern) {
        try {
            return Locale.LanguageRange.parse(pattern);
        } catch (IllegalArgumentException e) {
            return Collections.emptyList();
        }
    }
}
