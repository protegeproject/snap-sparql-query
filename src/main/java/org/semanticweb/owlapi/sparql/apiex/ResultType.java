package org.semanticweb.owlapi.sparql.apiex;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 10/03/15
 */
public class ResultType {

    private OperandType operandType;

    public ResultType(OperandType operandType) {
        this.operandType = operandType;
    }

    public OperandType getOperandType() {
        return operandType;
    }

    public boolean isSimpleLiteral() {
        return operandType == OperandType.SIMPLE_LITERAL;
    }

    public boolean isBoolean() {
        return operandType == OperandType.BOOLEAN;
    }

    public boolean isStringLiteral() {
        return operandType == OperandType.STRING_LITERAL;
    }

    public boolean isNumeric() {
        return operandType == OperandType.NUMERIC || operandType == OperandType.XSD_DOUBLE || operandType == OperandType.XSD_STRING;
    }

    public boolean isLiteral() {
        return operandType == OperandType.LITERAL
                || operandType == OperandType.SIMPLE_LITERAL
                || operandType == OperandType.STRING_LITERAL;
    }

    public boolean isIRI() {
        return operandType == OperandType.IRI;
    }
}
