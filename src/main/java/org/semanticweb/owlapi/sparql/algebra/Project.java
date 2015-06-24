package org.semanticweb.owlapi.sparql.algebra;

import com.google.common.collect.ImmutableList;
import org.semanticweb.owlapi.sparql.api.SolutionMapping;
import org.semanticweb.owlapi.sparql.api.Variable;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 09/06/15
 */
public class Project extends AlgebraExpression<SolutionSequence> {

    private AlgebraExpression<SolutionSequence> algebra;

    private List<Variable> projectVariables;

    public Project(AlgebraExpression<SolutionSequence> algebra, List<Variable> projectVariables) {
        this.algebra = algebra;
        this.projectVariables = projectVariables;
    }

    @Override
    public SolutionSequence evaluate(AlgebraEvaluationContext context) {
        SolutionSequence sequence = algebra.evaluate(context);
        if(sequence.getVariableList().equals(projectVariables)) {
            return sequence;
        }
        else {
            List<SolutionMapping> projectedSequence = new ArrayList<>();
            for(SolutionMapping sm : sequence.getSolutionMappings()) {
                SolutionMapping projectedSolutionMapping = sm.projectForVariables(projectVariables);
                projectedSequence.add(projectedSolutionMapping);
            }
            return new SolutionSequence(projectVariables, ImmutableList.copyOf(projectedSequence));
        }
    }

    public AlgebraExpression getAlgebra() {
        return algebra;
    }

    public List<Variable> getProjectVariables() {
        return projectVariables;
    }

    @Override
    protected void prettyPrint(PrintWriter writer, int level, String indentation) {
        writer.print(indentation);
        writer.println("(Project ");
        algebra.prettyPrint(writer, level + 1);
        writer.print(indentation + "    ");
        for(Variable variable : projectVariables) {
            writer.print(variable.getVariableNamePrefix().getPrefix());
            writer.print(variable.getName());
            writer.print(" ");
        }
        writer.println();
        writer.print(indentation);
        writer.println(")");
    }


    @Override
    public <R, E extends Exception> R accept(AlgebraExpressionVisitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }
}
