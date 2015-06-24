package org.semanticweb.owlapi.sparql.algebra;

import org.semanticweb.owlapi.sparql.api.*;

import java.io.PrintWriter;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 23/06/15
 */
public class AlgebraPrettyPrinter {


    public void prettyPrint(AlgebraExpression<?> expression, PrintWriter printWriter) {
        PrettyPrintingVisitor visitor = new PrettyPrintingVisitor(printWriter);
        expression.accept(visitor);
        printWriter.flush();
    }

    private static final class PrettyPrintingVisitor implements AlgebraExpressionVisitor<Void, RuntimeException> {

        private int indent = 0;

        private PrintWriter printWriter;

        public PrettyPrintingVisitor(PrintWriter printWriter) {
            this.printWriter = printWriter;
        }

        public void increaseIndent() {
            indent++;
        }

        public void decreaseIndent() {
            indent--;
        }


        public void println(Object line) {
            // Indent
            for(int i = 0; i < indent; i++) {
                printWriter.print("    ");
            }
            if(line instanceof AlgebraExpression) {
                try {
                    ((AlgebraExpression) line).accept(this);
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
            else {
                printWriter.print(line);
                printWriter.println();
            }
        }

        public void printSExpressionStart(String name) {
            printWriter.print("(");
            printWriter.print(name);
            increaseIndent();
            printWriter.println();
        }

        public void printSExpressionEnd() {
            decreaseIndent();
            println(")");
        }


        @Override
        public Void visit(Bgp bgp) {
            printSExpressionStart("Bgp");
            for(Axiom ax : bgp.getAxioms()) {
                println(ax);
            }
            printSExpressionEnd();
            return null;
        }

        @Override
        public Void visit(Distinct distinct) {
            printSExpressionStart("Distinct");
            println(distinct.getExpression());
            printSExpressionEnd();
            return null;
        }

        @Override
        public Void visit(Empty empty) {
            println("(Empty)");
            return null;
        }

        @Override
        public Void visit(Extend extend) {
            printSExpressionStart("Extend");
            println(extend.getAlgebraExpression());
            println(extend.getVariable());
            println(extend.getExpression());
            printSExpressionEnd();
            return null;
        }

        @Override
        public Void visit(Filter filter) {
            printSExpressionStart("Filter");
            println(filter.getPattern());
            for(Expression expression : filter.getExpressions()) {
                println(expression);
            }
            printSExpressionEnd();
            return null;
        }

        @Override
        public Void visit(Join join) {
            printSExpressionStart("Join");
            println(join.getLeft());
            println(join.getRight());
            printSExpressionEnd();
            return null;
        }

        @Override
        public Void visit(LeftJoin leftJoin) {
            printSExpressionStart("LeftJoin");
            println(leftJoin.getLeft());
            println(leftJoin.getRight());
            printSExpressionEnd();
            return null;
        }

        @Override
        public Void visit(Minus minus) {
            printSExpressionStart("Minus");
            println(minus.getLeft());
            println(minus.getRight());
            printSExpressionEnd();
            return null;
        }

        @Override
        public Void visit(OrderBy orderBy) {
            printSExpressionStart("OrderBy");
            println(orderBy.getAlgebraExpression());
            for(OrderCondition cond : orderBy.getComparator().getOrderConditions()) {
                println(cond);
            }
            printSExpressionEnd();
            return null;
        }

        @Override
        public Void visit(Project project) {
            printSExpressionStart("Project");
            println(project.getAlgebra());
            for(Variable variable : project.getProjectVariables()) {
                println(variable);
            }
            printSExpressionEnd();

            return null;
        }

        @Override
        public Void visit(ToList toList) {
            printSExpressionStart("ToList");
            println(toList.getAlgebraExpression());
            printSExpressionEnd();
            return null;
        }

        @Override
        public Void visit(Union union) {
            printSExpressionStart("Union");
            println(union.getLeft());
            println(union.getRight());
            printSExpressionEnd();
            return null;
        }

        @Override
        public Void visit(Aggregation aggregation) {
            printSExpressionStart("Aggregation");
            println(aggregation.getExpression());
            println(aggregation.getVariable());
            println(aggregation.getAlgebraExpression());
            printSExpressionEnd();
            return null;
        }

        @Override
        public Void visit(AggregateJoin aggregateJoin) {
            printSExpressionStart("AggregateJoin");
            for(Aggregation aggregation : aggregateJoin.getAggregations()) {
                println(aggregation);
            }
            printSExpressionEnd();
            return null;
        }

        @Override
        public Void visit(Group group) {
            printSExpressionStart("Group");
            for(GroupCondition cond : group.getExpressionList()) {
                println(cond.asExpression());
            }
            println(group.getPattern());
            printSExpressionEnd();
            return null;
        }
    }
}
