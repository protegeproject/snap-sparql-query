package org.semanticweb.owlapi.sparql.algebra;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.semanticweb.owlapi.sparql.api.*;

import java.util.*;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 08/06/15
 */
public class Bgp extends GraphPatternAlgebraExpression<SolutionSequence> {

    private final ImmutableList<Axiom> axioms;

    public Bgp(ImmutableList<Axiom> axioms) {
        this.axioms = axioms;
    }

    public ImmutableList<Axiom> getAxioms() {
        return axioms;
    }

    @Override
    public Bgp getSimplified() {
        return new Bgp(this.removeRedundantAxiomsTemplates());
    }

    public ImmutableSet<Variable> getVariables() {
        ImmutableSet.Builder<Variable> builder = ImmutableSet.builder();
        collectVisibleVariables(builder);
        return builder.build();
    }

    @Override
    public void collectVisibleVariables(ImmutableSet.Builder<Variable> variableBuilder) {
        Set<Variable> variables = new HashSet<>();
        for(Axiom ax : axioms) {
            ax.collectVariables(variables);
        }
        variableBuilder.addAll(variables);
    }

    @Override
    public <R, E extends Throwable> R accept(AlgebraExpressionVisitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    @Override
    public SolutionSequence evaluate(AlgebraEvaluationContext context) {
        return context.evaluateBgp(this);
    }


    private ImmutableList<Axiom> removeRedundantAxiomsTemplates() {
        // Remove declarations of variables e.g.  ?x a owl:AnnotationProperty
        // where the variable appears in a strongly typed position elsewhere e.g.
        Set<Variable> nonDeclarationVars = new HashSet<>();
        List<Declaration> entityVariableDeclarations = new ArrayList<>();
        List<Axiom> nonEntityVariableDeclarationAxioms = new ArrayList<>();
        for(Axiom ax : axioms) {
            if(ax instanceof Declaration) {
                if (((Declaration) ax).isEntityVariableDeclaration()) {
                    entityVariableDeclarations.add((Declaration) ax);
                }
                else {
                    nonEntityVariableDeclarationAxioms.add(ax);
                }
            }
            else {
                nonEntityVariableDeclarationAxioms.add(ax);
                Set<Variable> variables = new HashSet<>();
                ax.collectVariables(variables);
                variables.stream()
                        .filter(Variable::isEntityVariable)
                        .forEach(nonDeclarationVars::add);
            }
        }
        Set<Axiom> filteredAxioms = new HashSet<>(nonEntityVariableDeclarationAxioms);
        for(Declaration declaration : entityVariableDeclarations) {
            if(!declaration.isEntityVariableDeclaration()) {
                filteredAxioms.add(declaration);
            }
            else {
                Optional<Variable> declVariable = declaration.getAtomic().asVariable();
                Boolean alsoNonDeclVariable = declVariable.map(nonDeclarationVars::contains).orElse(false);
                if(!alsoNonDeclVariable) {
                    filteredAxioms.add(declaration);
                }
            }
        }
        return ImmutableList.copyOf(filteredAxioms);
    }

    @Override
    public String toString() {
        return toStringHelper("BGP")
                .addValue(axioms)
                .toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Bgp)) {
            return false;
        }
        Bgp other = (Bgp) obj;
        return this.axioms.equals(other.axioms);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(axioms);
    }


}
