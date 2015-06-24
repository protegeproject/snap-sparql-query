package org.semanticweb.owlapi.sparql.algebra;

import com.google.common.collect.ImmutableList;
import org.semanticweb.owlapi.sparql.api.*;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 23/06/15
 */
public class AggregateBuiltInReplacer {

    private ReplacementContext replacementContext;

    public AggregateBuiltInReplacer(ReplacementContext replacementContext) {
        this.replacementContext = replacementContext;
    }

    public Expression replaceAggregateBuiltInWithVariable(Expression expression) {
        return expression.accept(new ReplacerVisitor(), replacementContext);
    }


    private static final class ReplacerVisitor implements ExpressionVisitor<Expression, RuntimeException, ReplacementContext> {
        @Override
        public Expression visit(NamedClass atomicClass, ReplacementContext context) throws RuntimeException {
            return atomicClass;
        }

        @Override
        public Expression visit(ClassVariable atomicClass, ReplacementContext context) throws RuntimeException {
            return atomicClass;
        }

        @Override
        public Expression visit(ObjectProperty objectProperty, ReplacementContext context) throws RuntimeException {
            return objectProperty;
        }

        @Override
        public Expression visit(ObjectPropertyVariable variable, ReplacementContext context) throws RuntimeException {
            return variable;
        }

        @Override
        public Expression visit(DataProperty dataProperty, ReplacementContext context) throws RuntimeException {
            return dataProperty;
        }

        @Override
        public Expression visit(DataPropertyVariable variable, ReplacementContext context) throws RuntimeException {
            return variable;
        }

        @Override
        public Expression visit(AnnotationProperty annotationProperty, ReplacementContext context) throws RuntimeException {
            return annotationProperty;
        }

        @Override
        public Expression visit(AnnotationPropertyVariable variable, ReplacementContext context) throws RuntimeException {
            return variable;
        }

        @Override
        public Expression visit(NamedIndividual individual, ReplacementContext context) throws RuntimeException {
            return individual;
        }

        @Override
        public Expression visit(IndividualVariable variable, ReplacementContext context) throws RuntimeException {
            return variable;
        }

        @Override
        public Expression visit(Datatype datatype, ReplacementContext context) throws RuntimeException {
            return datatype;
        }

        @Override
        public Expression visit(DatatypeVariable variable, ReplacementContext context) throws RuntimeException {
            return variable;
        }

        @Override
        public Expression visit(AnonymousIndividual individual, ReplacementContext context) throws RuntimeException {
            return individual;
        }

        @Override
        public Expression visit(UntypedVariable variable, ReplacementContext context) throws RuntimeException {
            return variable;
        }

        @Override
        public Expression visit(AtomicIRI atomicIRI, ReplacementContext context) throws RuntimeException {
            return atomicIRI;
        }

        @Override
        public Expression visit(Literal literal, ReplacementContext context) throws RuntimeException {
            return literal;
        }

        @Override
        public Expression visit(LiteralVariable variable, ReplacementContext context) throws RuntimeException {
            return variable;
        }

        @Override
        public Expression visit(BuiltInCallExpression builtInCallExpression, ReplacementContext context) throws RuntimeException {
            if(builtInCallExpression.isAggregate()) {
                return context.replaceAggregate(builtInCallExpression);
            }
            else {
                return builtInCallExpression;
            }
        }

        @Override
        public Expression visit(RelationExpression relationExpression, ReplacementContext context) throws RuntimeException {
            return relationExpression;
        }

        @Override
        public Expression visit(AndExpression andExpression, ReplacementContext context) throws RuntimeException {
            return andExpression;
        }

        @Override
        public Expression visit(OrExpression orExpression, ReplacementContext context) throws RuntimeException {
            return orExpression;
        }

        @Override
        public Expression visit(NotExpression notExpression, ReplacementContext context) throws RuntimeException {
            return notExpression;
        }

        @Override
        public Expression visit(PlusExpression plusExpression, ReplacementContext context) throws RuntimeException {
            return plusExpression;
        }

        @Override
        public Expression visit(MinusExpression minusExpression, ReplacementContext context) throws RuntimeException {
            return minusExpression;
        }

        @Override
        public Expression visit(DivideExpression divideExpression, ReplacementContext context) throws RuntimeException {
            return divideExpression;
        }

        @Override
        public Expression visit(MultiplyExpression multiplyExpression, ReplacementContext context) throws RuntimeException {
            return multiplyExpression;
        }

        @Override
        public Expression visit(UnaryMinusExpression unaryMinusExpression, ReplacementContext context) throws RuntimeException {
            return unaryMinusExpression;
        }
    }

    public static final class ReplacementContext {

        private final Group pattern;

        private final Map<Aggregation, Variable> map = new LinkedHashMap<>();

        public ReplacementContext(Group pattern) {
            this.pattern = pattern;
        }

        public Variable replaceAggregate(BuiltInCallExpression expression) {
            UntypedVariable variable = new UntypedVariable("agg_" + map.size());
            Aggregation aggregation = new Aggregation(expression, pattern, variable);
            map.put(aggregation, variable);
            return variable;
        }

        public ImmutableList<Aggregation> getAggregations() {
            return ImmutableList.copyOf(map.keySet());
        }
    }
}
