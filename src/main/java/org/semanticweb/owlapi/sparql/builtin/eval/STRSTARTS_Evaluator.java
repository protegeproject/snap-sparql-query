package org.semanticweb.owlapi.sparql.builtin.eval;

import org.semanticweb.owlapi.sparql.api.EvaluationResult;
import org.semanticweb.owlapi.sparql.api.Expression;
import org.semanticweb.owlapi.sparql.api.Literal;
import org.semanticweb.owlapi.sparql.api.SolutionMapping;

import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 10/03/15
 */
public class STRSTARTS_Evaluator extends AbstractBinaryStringLiteralBuiltCallEvaluator {

    @Override
    protected EvaluationResult evaluateCompatibleLiterals(Literal left, Literal right, SolutionMapping sm) {
        boolean b = left.getLexicalForm().startsWith(right.getLexicalForm());
        return EvaluationResult.getBoolean(b);
    }
}
