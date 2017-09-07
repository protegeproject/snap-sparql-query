package org.semanticweb.owlapi.sparql.syntax;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import org.semanticweb.owlapi.sparql.algebra.*;
import org.semanticweb.owlapi.sparql.api.Axiom;
import org.semanticweb.owlapi.sparql.api.Expression;
import org.semanticweb.owlapi.sparql.api.Variable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 08/06/15
 */
public class GroupPattern extends Pattern {

    private final ImmutableList<Pattern> patterns;

    public GroupPattern(ImmutableList<Pattern> patterns) {
        this.patterns = checkNotNull(patterns);
    }

    public ImmutableList<Pattern> getPatterns() {
        return patterns;
    }

    public Set<Variable> getVariables() {
        Set<Variable> variables = new HashSet<>();
        for(TriplesBlockPattern triplesBlockPattern : getTriplesBlockPatterns()) {
            variables.addAll(triplesBlockPattern.getVariables());
        }
        return variables;
    }

    public List<TriplesBlockPattern> getTriplesBlockPatterns() {
        ImmutableList.Builder<TriplesBlockPattern> triplesBlockPatternBuilder = ImmutableList.builder();
        for(Pattern pattern : patterns) {
            if(pattern instanceof TriplesBlockPattern) {
                triplesBlockPatternBuilder.add((TriplesBlockPattern) pattern);
            }
        }
        return triplesBlockPatternBuilder.build();
    }

    public List<FilterPattern> getFilterPatterns() {
        List<FilterPattern> result = new ArrayList<>();
        for(Pattern pattern : patterns) {
            if(pattern instanceof FilterPattern) {
                result.add((FilterPattern) pattern);
            }
        }
        return result;
    }

    private List<Pattern> getNonFilterPatterns() {
        List<Pattern> result = new ArrayList<>();
        for(Pattern pattern : patterns) {
            if(!(pattern instanceof FilterPattern)) {
                result.add(pattern);
            }
        }
        return patterns;
    }

    private List<Pattern> getCollapsedNonFilterPatterns() {
        ImmutableList.Builder<Pattern> collapsedPatterns = ImmutableList.builder();
        Optional<TriplesBlockPattern> lastTriplesBlockPattern = Optional.absent();
        for(Pattern pattern : getNonFilterPatterns()) {
            if(pattern instanceof TriplesBlockPattern) {
                if(lastTriplesBlockPattern.isPresent()) {
                    ImmutableList.Builder<Axiom> mergedAxioms = ImmutableList.builder();
                    mergedAxioms.addAll(lastTriplesBlockPattern.get().getAxioms());
                    mergedAxioms.addAll(((TriplesBlockPattern) pattern).getAxioms());
                    lastTriplesBlockPattern = Optional.of(new TriplesBlockPattern(mergedAxioms.build()));
                }
                else {
                    lastTriplesBlockPattern = Optional.of((TriplesBlockPattern) pattern);
                }
            }
            else {
                if(lastTriplesBlockPattern.isPresent()) {
                    collapsedPatterns.add(lastTriplesBlockPattern.get());
                    lastTriplesBlockPattern = Optional.absent();
                }
                collapsedPatterns.add(pattern);
            }
        }
        if(lastTriplesBlockPattern.isPresent()) {
            collapsedPatterns.add(lastTriplesBlockPattern.get());
        }
        return collapsedPatterns.build();
    }

    @Override
    public Set<Variable> getInScopeVariables() {
        Set<Variable> result = new HashSet<>();
        for(Pattern pattern : patterns) {
            result.addAll(pattern.getInScopeVariables());
        }
        return result;
    }

    @Override
    public Optional<FilterPattern> asFilterPattern() {
        return Optional.absent();
    }

    @Override
    public Optional<MinusPattern> asMinusPattern() {
        return Optional.absent();
    }

    @Override
    public Optional<BindPattern> asBindPattern() {
        return Optional.absent();
    }

    @Override
    public Optional<OptionalPattern> asOptionalPattern() {
        return Optional.absent();
    }

    @Override
    public Optional<UnionPattern> asUnionPattern() {
        return Optional.absent();
    }



//    /**
//     * Places filters at the end and collapses adjacent triple patterns
//     * @return A fresh group pattern that is a simplified form of this pattern.
//     */
//    @Override
//    public GroupPattern getSimplified() {
//        // Remove filters to be placed at the end of the block
//        List<FilterPattern> filterPatterns = new ArrayList<>();
//
//        // Simplified contained patterns
//        List<Pattern> simplifiedPatterns = new ArrayList<>();
//
//        for(Pattern pattern : patterns) {
//            Optional<FilterPattern> filterPattern = pattern.asFilterPattern();
//            if(filterPattern.isPresent()) {
//                filterPatterns.add(filterPattern.get());
//            }
//            else {
//                simplifiedPatterns.add(pattern.getSimplified());
//            }
//        }
//
//        // Collapse adjacent triple blocks
//        ImmutableList.Builder<Pattern> collapsedPatterns = ImmutableList.builder();
//        Optional<TriplesBlockPattern> lastTriplesBlockPattern = Optional.absent();
//
//        for(Pattern simplifiedPattern : simplifiedPatterns) {
//            if(simplifiedPattern instanceof TriplesBlockPattern) {
//                if(lastTriplesBlockPattern.isPresent()) {
//                    ImmutableList.Builder<Axiom> mergedAxioms = ImmutableList.builder();
//                    mergedAxioms.addAll(lastTriplesBlockPattern.get().getAxioms());
//                    mergedAxioms.addAll(((TriplesBlockPattern) simplifiedPattern).getAxioms());
//                    lastTriplesBlockPattern = Optional.of(new TriplesBlockPattern(mergedAxioms.build()));
//                }
//                else {
//                    lastTriplesBlockPattern = Optional.of((TriplesBlockPattern) simplifiedPattern);
//                }
//            }
//            else {
//                if(lastTriplesBlockPattern.isPresent()) {
//                    collapsedPatterns.add(lastTriplesBlockPattern.get());
//                    lastTriplesBlockPattern = Optional.absent();
//                }
//                collapsedPatterns.add(simplifiedPattern);
//            }
//        }
//        if(lastTriplesBlockPattern.isPresent()) {
//            collapsedPatterns.add(lastTriplesBlockPattern.get());
//        }
//        // Filters go at the end
//        collapsedPatterns.addAll(filterPatterns);
//        return new GroupPattern(collapsedPatterns.build());
//    }

    @Override
    public GraphPatternAlgebraExpression translate() {
        GraphPatternAlgebraExpression G = Empty.get();
        List<Pattern> nonFilterPatterns = getCollapsedNonFilterPatterns();
        for(Pattern pattern : nonFilterPatterns) {
            // OPTIONAL
            Optional<OptionalPattern> optionalPattern = pattern.asOptionalPattern();
            if(optionalPattern.isPresent()) {
                GraphPatternAlgebraExpression A = optionalPattern.get().translate();
                if(A instanceof Filter) {
                    GraphPatternAlgebraExpression A2 = ((Filter) A).getPattern();
                    G = new LeftJoin(G, A2, ((Filter) A).getExpressions());
                }
                else {
                    G = new LeftJoin(G, A, ImmutableList.<Expression>of());
                }
            }
            else {
                // MINUS
                Optional<MinusPattern> minusPattern = pattern.asMinusPattern();
                if(minusPattern.isPresent()) {
                    G = new Minus(G, minusPattern.get().translate());
                }
                else {
                    // BIND
                    Optional<BindPattern> bindPattern = pattern.asBindPattern();
                    if(bindPattern.isPresent()) {
                        G = new Extend(G, bindPattern.get().getVariable(), bindPattern.get().getExpression());
                    }
                    else {
                        Optional<UnionPattern> unionPattern = pattern.asUnionPattern();
                        if(unionPattern.isPresent()) {
                            G = new Union(G, unionPattern.get().translate());
                        }
                        else {
                            // OTHER
                            G = new Join(G, pattern.translate());
                        }
                    }
                }
            }
        }

        List<FilterPattern> filterPatterns = getFilterPatterns();
        if(!filterPatterns.isEmpty()) {
            ImmutableList.Builder<Expression> expressions = ImmutableList.builder();
            for (FilterPattern filterPattern : filterPatterns) {
                expressions.add(filterPattern.getExpression());
            }
            G = new Filter(expressions.build(), G);
        }
        if(G instanceof Join) {
            Join join = (Join) G;
            G = join.getSimplified();
        }
        return G;
    }
}
