package org.semanticweb.owlapi.sparql.builtin.eval;

import org.semanticweb.owlapi.sparql.api.EvaluationResult;
import org.semanticweb.owlapi.sparql.api.Literal;
import org.semanticweb.owlapi.sparql.api.SolutionMapping;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 10/03/15
 */
public class CONTAINS_Evaluator extends AbstractBinaryStringLiteralBuiltCallEvaluator {

    @Override
    protected EvaluationResult evaluateCompatibleLiterals(Literal left, Literal right, SolutionMapping sm) {
        boolean contains = left.getLexicalForm().contains(right.getLexicalForm());
        return EvaluationResult.getBoolean(contains);
    }
}
