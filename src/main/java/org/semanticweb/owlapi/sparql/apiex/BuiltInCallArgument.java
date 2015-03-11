package org.semanticweb.owlapi.sparql.apiex;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 11/07/2012
 */
public class BuiltInCallArgument {

    private BuiltInCall builtInCall;

    private OperandList operandList;

    private Object operand;
    
    private int typeIndex;

    public BuiltInCallArgument(BuiltInCall builtInCall, OperandList operandList, int typeIndex, Object operand) {
        this.builtInCall = builtInCall;
        this.operandList = operandList;
        this.typeIndex = typeIndex;
        this.operand = operand;
    }
}
