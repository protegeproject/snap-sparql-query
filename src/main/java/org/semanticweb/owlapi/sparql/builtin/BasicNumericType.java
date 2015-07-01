package org.semanticweb.owlapi.sparql.builtin;

import org.semanticweb.owlapi.sparql.api.Datatype;
import org.semanticweb.owlapi.sparql.api.Literal;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 30/06/15
 */
public class BasicNumericType {


    public static Datatype getBasicNumericType(Datatype datatype) {
        if(datatype.isXSDIntegerDerived()) {
            return Datatype.getXSDInteger();
        }
        else if(datatype.isXSDDecimal()) {
            return Datatype.getXSDDecimal();
        }
        else if(datatype.isXSDFloat()) {
            return Datatype.getXSDFloat();
        }
        else {
            return Datatype.getXSDDouble();
        }
    }

    public static Datatype getMostSpecificBasicNumericType(Datatype left, Datatype right) {
        if(!left.isNumeric()) {
            throw new IllegalArgumentException("Left Datatype is not numeric");
        }
        if(!right.isNumeric()) {
            throw new IllegalArgumentException("Right Datatype is not numeric");
        }
        if(left.isXSDIntegerDerived()) {
            if(right.isXSDIntegerDerived()) {
                return Datatype.getXSDInteger();
            }
            else if(right.isXSDDecimal()) {
                return Datatype.getXSDDecimal();
            }
            else if(right.isXSDFloat()) {
                return Datatype.getXSDFloat();
            }
            else {
                return Datatype.getXSDDouble();
            }
        }
        else if(left.isXSDDecimalDerived()) {
            if(right.isXSDDecimalDerived()) {
                return Datatype.getXSDDecimal();
            }
            else if(right.isXSDFloat()) {
                return Datatype.getXSDFloat();
            }
            else {
                return Datatype.getXSDDouble();
            }
        }
        else if(left.isXSDFloat()) {
            if(right.isXSDFloat()) {
                return Datatype.getXSDFloat();
            }
            else {
                return Datatype.getXSDDouble();
            }
        }
        else {
            return Datatype.getXSDDouble();
        }
    }

    public static Literal getLiteralOfBasicNumericType(double value, Datatype type) {
        if(type.isXSDIntegerDerived()) {
            return Literal.createInteger((int) value);
        }
        else if(type.isXSDDecimal()) {
            return Literal.createDecimal(value);
        }
        else if(type.isXSDFloat()) {
            return Literal.createFloat((float) value);
        }
        else if(type.isXSDDouble()) {
            return Literal.createDouble(value);
        }
        else {
            throw new IllegalArgumentException("Not a basic numeric type: " + type);
        }
    }
}
