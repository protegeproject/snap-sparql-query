package org.semanticweb.owlapi.sparql.builtin.eval;

import com.google.common.io.BaseEncoding;
import org.semanticweb.owlapi.sparql.api.EvaluationResult;
import org.semanticweb.owlapi.sparql.api.Expression;
import org.semanticweb.owlapi.sparql.api.Literal;
import org.semanticweb.owlapi.sparql.api.SolutionMapping;
import org.semanticweb.owlapi.sparql.algebra.AlgebraEvaluationContext;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 16 Oct 2017
 */
public class MD5_Evaluator extends AbstractUnaryLiteralBuiltInCallEvaluator {

    private static final String MD5 = "MD5";

    private static final Charset UTF_8 = Charset.forName("UTF-8");

    private static final BaseEncoding HEX_ENCODER = BaseEncoding.base16().lowerCase();


    @Override
    protected EvaluationResult evaluate(Literal literal, Expression arg, SolutionMapping sm, AlgebraEvaluationContext evaluationContext) {
        try {
            EvaluationResult evaluationResult = literal.evaluateAsSimpleLiteral(sm, evaluationContext);
            if(evaluationResult.isError()) {
                return evaluationResult;
            }
            String simpleLiteral = evaluationResult.asSimpleLiteral();
            MessageDigest messageDigest = MessageDigest.getInstance(MD5);
            messageDigest.update(simpleLiteral.getBytes(UTF_8));
            byte [] digest = messageDigest.digest();
            String hexEncoding = HEX_ENCODER.encode(digest);
            return EvaluationResult.getSimpleLiteral(hexEncoding);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
