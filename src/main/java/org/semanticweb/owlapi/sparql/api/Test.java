package org.semanticweb.owlapi.sparql.api;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/07/2012
 */
public class Test {

    public static void main(String[] args) throws Exception {
        Literal literalA = Literal.createBoolean(true);
        Literal literalB = Literal.createBoolean(false);
        Literal literalC = Literal.createInteger(3);
        Literal literalD = Literal.createDouble(4);
        Literal literalE = Literal.createDouble(3.0);
        Literal literalF = Literal.createString("X");

        SolutionMapping sm = SolutionMapping.emptyMapping();
//        System.out.println(literalA.ca(sm));
//        System.out.println(literalB.canEvaluateAsBoolean(sm));

        System.out.println("      A: " + literalA.evaluateAsEffectiveBooleanValue(sm));
        System.out.println("      B: " + literalB.evaluateAsEffectiveBooleanValue(sm));
        System.out.println("AND A B: " + new AndExpression(literalA, literalB).evaluateAsEffectiveBooleanValue(sm));
        System.out.println(" OR A B: " + new OrExpression(literalA, literalB).evaluateAsEffectiveBooleanValue(sm));
        System.out.println("  NOT A: " + new NotExpression(literalA).evaluateAsEffectiveBooleanValue(sm));

        System.out.println("!A || B: " + new OrExpression(new NotExpression(literalA), literalB).evaluateAsEffectiveBooleanValue(sm));
        System.out.println("!A || !B: " + new OrExpression(new NotExpression(literalA), new NotExpression(literalB)).evaluateAsEffectiveBooleanValue(sm));


        System.out.println(" C < D :" + new RelationExpression(literalC, literalD, Relation.LESS_THAN).evaluateAsEffectiveBooleanValue(sm));
        System.out.println(" C > D :" + new RelationExpression(literalC, literalD, Relation.GREATER_THAN).evaluateAsEffectiveBooleanValue(sm));
        System.out.println(" C < E :" + new RelationExpression(literalC, literalE, Relation.LESS_THAN).evaluateAsEffectiveBooleanValue(sm));
        System.out.println(" C <= E :" + new RelationExpression(literalC, literalE, Relation.LESS_THAN_OR_EQUAL).evaluateAsEffectiveBooleanValue(sm));
        System.out.println(" C != E :" + new RelationExpression(literalC, literalE, Relation.NOT_EQUAL).evaluateAsEffectiveBooleanValue(sm));
        System.out.println(" C = E :" + new RelationExpression(literalC, literalE, Relation.EQUAL).evaluateAsEffectiveBooleanValue(sm));
        System.out.println(" C != D :" + new RelationExpression(literalC, literalD, Relation.NOT_EQUAL).evaluateAsEffectiveBooleanValue(sm));
        System.out.println(" C = F :" + new RelationExpression(literalC, literalF, Relation.EQUAL).evaluateAsEffectiveBooleanValue(sm));
        System.out.println(" C != F :" + new RelationExpression(literalC, literalF, Relation.NOT_EQUAL).evaluateAsEffectiveBooleanValue(sm));
        System.out.println(" C < F :" + new RelationExpression(literalC, literalF, Relation.LESS_THAN).evaluateAsEffectiveBooleanValue(sm));
    }
}
