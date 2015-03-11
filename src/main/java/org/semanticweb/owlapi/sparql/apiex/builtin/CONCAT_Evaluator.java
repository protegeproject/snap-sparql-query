package org.semanticweb.owlapi.sparql.apiex.builtin;

import org.semanticweb.owlapi.sparql.api.*;
import org.semanticweb.owlapi.vocab.XSDVocabulary;

import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11/03/15
 */
public class CONCAT_Evaluator implements BuiltInCallEvaluator {

    /**
     * The lexical form of the returned literal is obtained by concatenating the lexical forms of its inputs.
     * If all input literals are typed literals of type xsd:string, then the returned literal is also of type
     * xsd:string, if all input literals are plain literals with identical language tag, then the returned literal
     * is a plain literal with the same language tag, in all other cases, the returned literal is a simple literal.
     */
    @Override
    public EvaluationResult evaluate(List<Expression> args, SolutionMapping sm) {
        StringBuilder concatenatedLexicalForm = new StringBuilder();
        boolean xsdString = true;
        String prevLangTag = "";
        String resultLang = "";
        for(Expression arg : args) {
            EvaluationResult argEval = arg.evaluateAsStringLiteral(sm);
            if(argEval.isError()) {
                return EvaluationResult.getError();
            }
            Literal argLiteral = argEval.asLiteral();
            concatenatedLexicalForm.append(argLiteral.getLexicalForm());
            if(!argLiteral.getDatatype().isXSDString()) {
                xsdString = false;
            }
            if(prevLangTag.isEmpty()) {
                resultLang = prevLangTag = argLiteral.getLang();
            }
            else if(!prevLangTag.equals(argLiteral.getLang())) {
                resultLang = "";
            }
        }
        Datatype resultDatatype;
        if(xsdString) {
            resultDatatype = Datatype.getXSDString();
        }
        else {
            resultDatatype = Datatype.getRDFPlainLiteral();
        }
        Literal result = new Literal(resultDatatype, concatenatedLexicalForm.toString(), resultLang);
        return EvaluationResult.getResult(result);
    }
}
