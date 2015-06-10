package org.semanticweb.owlapi.sparql.builtin.eval;

import org.semanticweb.owlapi.sparql.api.EvaluationResult;
import org.semanticweb.owlapi.sparql.api.Literal;
import org.semanticweb.owlapi.sparql.api.SolutionMapping;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 10/06/15
 */
public class STRBEFORE_Evaluator extends AbstractBinaryStringLiteralBuiltCallEvaluator {

    @Override
    protected EvaluationResult evaluate(Literal left, Literal right, SolutionMapping sm) {
        if(!left.getLang().equals(right.getLang())) {
            if(!left.getLang().isEmpty() && !right.getLang().isEmpty()) {
                return EvaluationResult.getError();
            }
        }
        int index = left.getLexicalForm().indexOf(right.getLexicalForm());
        if(index == -1) {
            return EvaluationResult.getSimpleLiteral("");
        }
        else {
            String before = left.getLexicalForm().substring(0, index);
            return EvaluationResult.getResult(new Literal(left.getDatatype(), before, left.getLang()));
        }
    }
}
