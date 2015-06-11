package org.semanticweb.owlapi.sparql.algebra;

import com.google.common.collect.ImmutableList;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.sparql.api.EvaluationResult;
import org.semanticweb.owlapi.sparql.api.SolutionMapping;
import org.semanticweb.owlapi.sparql.api.Term;
import org.semanticweb.owlapi.sparql.api.Variable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 11/06/15
 */
public class ExtendEvaluator {

    private AlgebraExpressionEvaluatorVisitor evaluator;

    public ExtendEvaluator(AlgebraExpressionEvaluatorVisitor evaluator) {
        this.evaluator = evaluator;
    }

    public SolutionSequence evaluate(Extend extend) {
        SolutionSequence sequence = extend.getAlgebraExpression().accept(evaluator);
        ImmutableList.Builder<SolutionMapping> extendedSequence = ImmutableList.builder();
        for(SolutionMapping sm : sequence.getSolutionMappings()) {
            EvaluationResult result = extend.getExpression().evaluate(sm);
            if(!result.isError()) {
                Map<Variable, Term> variableTermMap = sm.asMap();
                variableTermMap.put(extend.getVariable(), result.getResult());
                SolutionMapping extendedMapping = new SolutionMapping(variableTermMap);
                extendedSequence.add(extendedMapping);
            }
            else {
                extendedSequence.add(sm);
            }
        }
        List<Variable> extendedVariableList = new ArrayList<>(sequence.getVariableList());
        extendedVariableList.add(extend.getVariable());
        return new SolutionSequence(extendedVariableList, extendedSequence.build());
    }
}
