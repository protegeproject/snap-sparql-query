package org.semanticweb.owlapi.sparql.builtin.eval;

import org.semanticweb.owlapi.sparql.api.EvaluationResult;
import org.semanticweb.owlapi.sparql.api.Literal;
import org.semanticweb.owlapi.sparql.api.SolutionMapping;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 10/06/15
 */
public class STRAFTER_Evaluator extends AbstractBinaryStringLiteralBuiltCallEvaluator {

    @Override
    protected EvaluationResult evaluateCompatibleLiterals(Literal left, Literal right, SolutionMapping sm) {
        int index = left.getLexicalForm().indexOf(right.getLexicalForm());
        if(index == -1) {
            return EvaluationResult.getSimpleLiteral("");
        }
        else {
            String after = left.getLexicalForm().substring(index + right.getLexicalForm().length());
            return EvaluationResult.getResult(new Literal(left.getDatatype(), after, left.getLang()));
        }
    }
}
