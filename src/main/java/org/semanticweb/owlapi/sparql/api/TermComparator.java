package org.semanticweb.owlapi.sparql.api;

import java.util.Comparator;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 01/08/2012
 */
public class TermComparator implements Comparator<RDFTerm> {

    public int compare(RDFTerm o1, RDFTerm o2) {
        // Anonymous individuals
        if(o1 instanceof AnonymousIndividual) {
            if(o2 instanceof AnonymousIndividual) {
                return ((AnonymousIndividual) o1).getIdentifier().compareTo(((AnonymousIndividual) o2).getIdentifier());
            }
            else {
                return -1;
            }
        }
        // IRIs
        else if(o1 instanceof Entity) {
            if(o2 instanceof AnonymousIndividual) {
                return 1;
            }
            else if(o2 instanceof Entity) {
                return ((Entity) o1).getIRI().compareTo(((Entity) o2).getIRI());
            }
            else {
                return -1;
            }
            
        }
        else if(o1 instanceof Literal) {
            if(o2 instanceof AnonymousIndividual) {
                return -1;
            }
            else if(o2 instanceof Entity) {
                return -1;
            }
            else if(o2 instanceof Literal) {
                int diff = ((Literal) o1).getLexicalForm().compareTo(((Literal) o2).getLexicalForm());
                if(diff != 0) {
                    return diff;
                }
                else {
                    return ((Literal) o1).getLang().compareTo(((Literal) o2).getLang());
                }
            }
            else {
                return 0;
            }
        }
        return 0;
    }
}
