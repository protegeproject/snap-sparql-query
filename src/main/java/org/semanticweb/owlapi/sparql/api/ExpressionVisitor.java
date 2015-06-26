package org.semanticweb.owlapi.sparql.api;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 23/06/15
 */
public interface ExpressionVisitor<R, E extends Throwable, C> {

    R visit(NamedClass atomicClass, C context) throws E;

    R visit(ClassVariable atomicClass, C context) throws E;

    R visit(ObjectProperty objectProperty, C context) throws E;

    R visit(ObjectPropertyVariable variable, C context) throws E;

    R visit(DataProperty dataProperty, C context) throws E;

    R visit(DataPropertyVariable variable, C context) throws E;

    R visit(AnnotationProperty annotationProperty, C context) throws E;

    R visit(AnnotationPropertyVariable variable, C context) throws E;

    R visit(IRIVariable variable, C context) throws E;

    R visit(NamedIndividual individual, C context) throws E;

    R visit(IndividualVariable variable, C context) throws E;

    R visit(Datatype datatype, C context) throws E;

    R visit(DatatypeVariable variable, C context) throws E;

    R visit(AnonymousIndividual individual, C context) throws E;

    R visit(UntypedVariable variable, C context) throws E;

    R visit(AtomicIRI atomicIRI, C context) throws E;

    R visit(AnnotationValueVariable variable, C context) throws E;

    R visit(Literal literal, C context) throws E;

    R visit(LiteralVariable variable, C context) throws E;

    R visit(BuiltInCallExpression builtInCallExpression, C context) throws E;

    R visit(RelationExpression relationExpression, C context) throws E;

    R visit(AndExpression andExpression, C context) throws E;

    R visit(OrExpression orExpression, C context) throws E;

    R visit(NotExpression notExpression, C context) throws E;

    R visit(PlusExpression plusExpression, C context) throws E;

    R visit(MinusExpression minusExpression, C context) throws E;

    R visit(DivideExpression divideExpression, C context) throws E;

    R visit(MultiplyExpression multiplyExpression, C context) throws E;

    R visit(UnaryMinusExpression unaryMinusExpression, C context) throws E;


}
