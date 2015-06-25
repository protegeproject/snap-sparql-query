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
        else if(o1 instanceof AtomicIRI) {
            if(o2 instanceof AnonymousIndividual) {
                return 1;
            }
            else if(o2 instanceof AtomicIRI) {
                return ((AtomicIRI) o1).getIRI().compareTo(((AtomicIRI) o2).getIRI());
            }
            else {
                return -1;
            }
            
        }
        else if(o1 instanceof Literal) {
            if(o2 instanceof AnonymousIndividual) {
                return -1;
            }
            else if(o2 instanceof AtomicIRI) {
                return -1;
            }
            else if(o2 instanceof Literal) {
                int diff;
                Literal l1 = (Literal) o1;
                Literal l2 = (Literal) o2;
                try {
                    if(l1.isDatatypeNumeric() && l2.isDatatypeNumeric()) {
                        double d1 = Double.parseDouble(l1.getLexicalForm());
                        double d2 = Double.parseDouble(l2.getLexicalForm());
                        if(d1 < d2) {
                            return -1;
                        }
                        else if(d1 > d2) {
                            return 1;
                        }
                    }
                } catch (NumberFormatException e) {
                    // Ignore
                }

                diff = l1.getLexicalForm().compareTo(l2.getLexicalForm());
                if(diff != 0) {
                    return diff;
                }
                else {
                    return l1.getLang().compareTo(l2.getLang());
                }
            }
            else {
                return 0;
            }
        }
        return 0;
    }
}
