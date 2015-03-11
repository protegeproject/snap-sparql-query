package org.semanticweb.owlapi.sparql.builtin.eval;

import org.semanticweb.owlapi.sparql.api.EvaluationResult;
import org.semanticweb.owlapi.sparql.api.Expression;
import org.semanticweb.owlapi.sparql.api.SolutionMapping;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 10/03/15
 */
public class REGEX_Evaluator implements BuiltInCallEvaluator {

    @Override
    public EvaluationResult evaluate(List<Expression> args, SolutionMapping sm) {
        if(args.size() != 2 && args.size() != 3) {
            return EvaluationResult.getError();
        }
        Expression string = args.get(0);
        EvaluationResult matchResult = string.evaluateAsStringLiteral(sm);
        if(matchResult.isError()) {
            return matchResult;
        }
        Expression pattern = args.get(1);
        EvaluationResult patternResult = pattern.evaluateAsSimpleLiteral(sm);
        if(patternResult.isError()) {
            return patternResult;
        }
        Pattern patternPattern = Pattern.compile(patternResult.asSimpleLiteral());
        return EvaluationResult.getBoolean(patternPattern.matcher(matchResult.asLiteral().getLexicalForm()).matches());
    }
}
