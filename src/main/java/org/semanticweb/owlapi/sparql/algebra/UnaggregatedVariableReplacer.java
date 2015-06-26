package org.semanticweb.owlapi.sparql.algebra;

import com.google.common.collect.ImmutableList;
import org.semanticweb.owlapi.sparql.api.*;
import org.semanticweb.owlapi.sparql.builtin.BuiltInCall;

import java.util.ArrayList;
import java.util.List;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 23/06/15
 */
public class UnaggregatedVariableReplacer {


    public Expression replaceUnaggregatedVariables(Expression expression) {
        return expression.accept(new ReplacementVisitor(), new ReplacementContext());
    }

    private static final class ReplacementVisitor implements ExpressionVisitor<Expression, RuntimeException, ReplacementContext> {

        private static BuiltInCallExpression replaceVariableWithSampleVariable(Variable variable) {
            return new BuiltInCallExpression(BuiltInCall.SAMPLE, ImmutableList.of(variable));
        }

        @Override
        public Expression visit(NamedClass atomicClass, ReplacementContext context) throws RuntimeException {
            return atomicClass;
        }

        @Override
        public Expression visit(ClassVariable atomicClass, ReplacementContext context) throws RuntimeException {
            return replaceVariableWithSampleVariable(atomicClass);
        }

        @Override
        public Expression visit(ObjectProperty objectProperty, ReplacementContext context) throws RuntimeException {
            return objectProperty;
        }

        @Override
        public Expression visit(ObjectPropertyVariable variable, ReplacementContext context) throws RuntimeException {
            return replaceVariableWithSampleVariable(variable);
        }

        @Override
        public Expression visit(DataProperty dataProperty, ReplacementContext context) throws RuntimeException {
            return dataProperty;
        }

        @Override
        public Expression visit(DataPropertyVariable variable, ReplacementContext context) throws RuntimeException {
            return replaceVariableWithSampleVariable(variable);
        }

        @Override
        public Expression visit(AnnotationValueVariable variable, ReplacementContext context) throws RuntimeException {
            return replaceVariableWithSampleVariable(variable);
        }

        @Override
        public Expression visit(AnnotationProperty annotationProperty, ReplacementContext context) throws RuntimeException {
            return annotationProperty;
        }

        @Override
        public Expression visit(AnnotationPropertyVariable variable, ReplacementContext context) throws RuntimeException {
            return replaceVariableWithSampleVariable(variable);
        }

        @Override
        public Expression visit(IRIVariable variable, ReplacementContext context) throws RuntimeException {
            return replaceVariableWithSampleVariable(variable);
        }

        @Override
        public Expression visit(NamedIndividual individual, ReplacementContext context) throws RuntimeException {
            return individual;
        }

        @Override
        public Expression visit(IndividualVariable variable, ReplacementContext context) throws RuntimeException {
            return replaceVariableWithSampleVariable(variable);
        }

        @Override
        public Expression visit(Datatype datatype, ReplacementContext context) throws RuntimeException {
            return datatype;
        }

        @Override
        public Expression visit(DatatypeVariable variable, ReplacementContext context) throws RuntimeException {
            return replaceVariableWithSampleVariable(variable);
        }

        @Override
        public Expression visit(AnonymousIndividual individual, ReplacementContext context) throws RuntimeException {
            return individual;
        }

        @Override
        public Expression visit(UntypedVariable variable, ReplacementContext context) throws RuntimeException {
            return replaceVariableWithSampleVariable(variable);
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
            return replaceVariableWithSampleVariable(variable);
        }

        @Override
        public Expression visit(BuiltInCallExpression builtInCallExpression, ReplacementContext context) throws RuntimeException {
            // Things nested inside an aggregate do not need replacing
            if(builtInCallExpression.getBuiltInCall().isAggregate()) {
                return builtInCallExpression;
            }
            else {
                List<Expression> replacementArgs = new ArrayList<>();
                for(Expression arg : builtInCallExpression.getArgs()) {
                    Expression replacementArg = arg.accept(this, context);
                    replacementArgs.add(replacementArg);
                }
                return new BuiltInCallExpression(builtInCallExpression.getBuiltInCall(), ImmutableList.copyOf(replacementArgs));
            }
        }

        @Override
        public Expression visit(RelationExpression relationExpression, ReplacementContext context) throws RuntimeException {
            Expression leftReplacement = relationExpression.getLeft().accept(this, context);
            Expression rightReplacement = relationExpression.getRight().accept(this, context);
            return new RelationExpression(leftReplacement, rightReplacement, relationExpression.getRelation());
        }

        @Override
        public Expression visit(AndExpression andExpression, ReplacementContext context) throws RuntimeException {
            Expression leftReplacement = andExpression.getLeft().accept(this, context);
            Expression rightReplacement = andExpression.getRight().accept(this, context);
            return new AndExpression(leftReplacement, rightReplacement);
        }

        @Override
        public Expression visit(OrExpression orExpression, ReplacementContext context) throws RuntimeException {
            Expression leftReplacement = orExpression.getLeft().accept(this, context);
            Expression rightReplacement = orExpression.getRight().accept(this, context);
            return new OrExpression(leftReplacement, rightReplacement);
        }

        @Override
        public Expression visit(NotExpression notExpression, ReplacementContext context) throws RuntimeException {
            Expression replacementExpression = notExpression.getExpression().accept(this, context);
            return new NotExpression(replacementExpression);
        }

        @Override
        public Expression visit(PlusExpression plusExpression, ReplacementContext context) throws RuntimeException {
            Expression leftReplacement = plusExpression.getLeft().accept(this, context);
            Expression rightReplacement = plusExpression.getRight().accept(this, context);
            return new PlusExpression(leftReplacement, rightReplacement);
        }

        @Override
        public Expression visit(MinusExpression minusExpression, ReplacementContext context) throws RuntimeException {
            Expression leftReplacement = minusExpression.getLeft().accept(this, context);
            Expression rightReplacement = minusExpression.getRight().accept(this, context);
            return new MinusExpression(leftReplacement, rightReplacement);
        }

        @Override
        public Expression visit(DivideExpression divideExpression, ReplacementContext context) throws RuntimeException {
            Expression leftReplacement = divideExpression.getLeft().accept(this, context);
            Expression rightReplacement = divideExpression.getRight().accept(this, context);
            return new DivideExpression(leftReplacement, rightReplacement);
        }

        @Override
        public Expression visit(MultiplyExpression multiplyExpression, ReplacementContext context) throws RuntimeException {
            Expression leftReplacement = multiplyExpression.getLeft().accept(this, context);
            Expression rightReplacement = multiplyExpression.getRight().accept(this, context);
            return new MultiplyExpression(leftReplacement, rightReplacement);
        }

        @Override
        public Expression visit(UnaryMinusExpression unaryMinusExpression, ReplacementContext context) throws RuntimeException {
            Expression replacementExpression = unaryMinusExpression.getExpression().accept(this, context);
            return new UnaryMinusExpression(replacementExpression);
        }
    }



    private static final class ReplacementContext {}
}
