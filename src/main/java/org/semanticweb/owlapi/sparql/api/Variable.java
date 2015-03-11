package org.semanticweb.owlapi.sparql.api;

import org.semanticweb.owlapi.model.IRI;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/07/2012
 */
public abstract class Variable implements Atomic, HasIdentifier, HasVariables, HasName, AnnotationSubject {

    public abstract VariableNamePrefix getVariableNamePrefix();

    public abstract Term getBound(IRI iri);

    public abstract PrimitiveType getType();
    
    public static Variable create(String name, PrimitiveType entityType) {
        if(entityType == PrimitiveType.CLASS) {
            return new ClassVariable(name);
        }
        else if(entityType == PrimitiveType.DATATYPE) {
            return new DatatypeVariable(name);
        }
        else if(entityType == PrimitiveType.OBJECT_PROPERTY) {
            return new ObjectPropertyVariable(name);
        }
        else if(entityType == PrimitiveType.DATA_PROPERTY) {
            return new DataPropertyVariable(name);
        }
        else if(entityType == PrimitiveType.ANNOTATION_PROPERTY) {
            return new AnnotationPropertyVariable(name);
        }
        else if(entityType == PrimitiveType.NAMED_INDIVIDUAL) {
            return new IndividualVariable(name);
        }
        else if(entityType == PrimitiveType.LITERAL) {
            return new LiteralVariable(name);
        }
        throw new IllegalStateException("Unknown EntityType: " + entityType);
    }
}
