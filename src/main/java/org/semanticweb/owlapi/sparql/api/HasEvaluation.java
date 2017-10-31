package org.semanticweb.owlapi.sparql.api;

import org.semanticweb.owlapi.sparql.algebra.AlgebraEvaluationContext;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 27/07/2012
 */
public interface HasEvaluation {

    EvaluationResult evaluate(SolutionMapping sm, AlgebraEvaluationContext evaluationContext);



    // TODO: VAR

    /**
     * Evaluates this expression expecting an effective boolean value (EBV).
     *
     * 17.2.2 Effective Boolean Value (EBV)
     *
     * Effective boolean value is used to calculate the arguments to the logical functions logical-and, logical-or, and
     * fn:not, as well as evaluate the result of a FILTER expression.
     *
     * The XQuery Effective Boolean Value rules rely on the definition of XPath's fn:boolean. The following rules
     * reflect the rules for fn:boolean applied to the argument types present in SPARQL queries:
     *
     * The EBV of any literal whose type is xsd:boolean or numeric is false if the lexical form is not valid for that
     * datatype (e.g. "abc"^^xsd:integer). If the argument is a typed literal with a datatype of xsd:boolean, and it has
     * a valid lexical form, the EBV is the value of that argument. If the argument is a plain literal or a typed
     * literal with a datatype of xsd:string, the EBV is false if the operand value has zero length; otherwise the EBV
     * is true. If the argument is a numeric type or a typed literal with a datatype derived from a numeric type, and it
     * has a valid lexical form, the EBV is false if the operand value is NaN or is numerically equal to zero; otherwise
     * the EBV is true. All other arguments, including unbound arguments, produce a type error. An EBV of true is
     * represented as a typed literal with a datatype of xsd:boolean and a lexical value of "true"; an EBV of false is
     * represented as a typed literal with a datatype of xsd:boolean and a lexical value of "false".
     */
    EvaluationResult evaluateAsEffectiveBooleanValue(SolutionMapping sm, AlgebraEvaluationContext evaluationContext);

    EvaluationResult evaluateAsLiteral(SolutionMapping sm, AlgebraEvaluationContext evaluationContext);

    EvaluationResult evaluateAsSimpleLiteral(SolutionMapping sm, AlgebraEvaluationContext evaluationContext);

    EvaluationResult evaluateAsStringLiteral(SolutionMapping sm, AlgebraEvaluationContext evaluationContext);
}
