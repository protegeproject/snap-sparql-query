package org.semanticweb.owlapi.sparql.apiex.expr;

import org.semanticweb.owlapi.sparql.apiex.BuiltInCall;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 24/07/2012
 */
public class BuiltInCallExpression implements Expression {

    private BuiltInCall builtInCall;
    
    private List<Expression> args = new ArrayList<Expression>();

    public BuiltInCallExpression(BuiltInCall builtInCall, List<Expression> args) {
        this.builtInCall = builtInCall;
        this.args = args;
    }

    public BuiltInCall getBuiltInCall() {
        return builtInCall;
    }

    public List<Expression> getArgs() {
        return args;
    }

    public boolean isBoolean() {
        return false;
    }

    public boolean isNumeric() {
        return false;
    }
}
