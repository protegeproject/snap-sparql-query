package org.semanticweb.owlapi.sparql.apiex.function.types;

import org.semanticweb.owlapi.model.OWLDataFactory;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/07/2012
 */
public class TypeFactory {

    private static OWLDataFactory dataFactory =  new OWLDataFactoryImpl(false, false);

    public TypeFactory() {
    }

    public static IntegerLiteral getIntegerLiteral(int value) {
        return new IntegerLiteral(dataFactory.getOWLLiteral(value));
    }
    
    public static StringLiteral getStringLiteral(String lexicalValue) {
        return new StringLiteral(dataFactory.getOWLLiteral(lexicalValue));
    }
    
    public static StringLiteral getStringLiteral(String lexicalValue, String lang) {
        return new StringLiteral(dataFactory.getOWLLiteral(lexicalValue, lang));
    }
}
