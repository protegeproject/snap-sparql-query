package org.semanticweb.owlapi.sparql.algebra;

import com.google.common.base.Objects;
import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import de.derivo.sparqldlapi.Query;
import de.derivo.sparqldlapi.QueryEngine;
import de.derivo.sparqldlapi.QueryResult;
import de.derivo.sparqldlapi.exceptions.QueryEngineException;
import de.derivo.sparqldlapi.impl.LiteralTranslator;
import javafx.scene.paint.Stop;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.sparql.api.Axiom;
import org.semanticweb.owlapi.sparql.api.SolutionMapping;
import org.semanticweb.owlapi.sparql.api.Variable;
import org.semanticweb.owlapi.sparql.sparqldl.BgpEvaluator;
import org.semanticweb.owlapi.sparql.sparqldl.BgpTranslator;
import org.semanticweb.owlapi.sparql.sparqldl.ResultTranslator;
import org.semanticweb.owlapi.sparql.sparqldl.SolutionMappingTranslator;

import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 08/06/15
 */
public class Bgp extends GraphPatternAlgebraExpression {

    private final ImmutableList<Axiom> axioms;

    public Bgp(ImmutableList<Axiom> axioms) {
        this.axioms = axioms;
    }

    public ImmutableList<Axiom> getAxioms() {
        return axioms;
    }

    @Override
    public GraphPatternAlgebraExpression getSimplified() {
        return this;
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
    public <R, E extends Exception> R accept(AlgebraExpressionVisitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    @Override
    public SolutionSequence evaluate(AlgebraEvaluationContext context) {
        return context.evaluateBgp(this);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper("BGP")
                .addValue(axioms)
                .toString();
    }

    @Override
    protected void prettyPrint(PrintWriter writer, int level, String indentation) {
        writer.print(indentation);
        writer.println("(Bgp ");
        for(Axiom ax : axioms) {
            writer.print(indentation + "    ");
            writer.println(ax);
        }
        writer.print(indentation);
        writer.println(")");
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
