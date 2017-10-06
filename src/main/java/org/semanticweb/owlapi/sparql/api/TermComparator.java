package org.semanticweb.owlapi.sparql.api;

import java.util.Comparator;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 01/08/2012
 */
public class TermComparator implements Comparator<RDFTerm> {

    private static final int BEFORE = -1;

    private static final int AFTER = 1;

    private static final int SAME = 0;

    public int compare(RDFTerm o1, RDFTerm o2) {
//        Blank nodes
//        IRIs
//        RDF literals

        // Anonymous individuals
        if(o1 instanceof AnonymousIndividual) {
            if(o2 instanceof AnonymousIndividual) {
                return ((AnonymousIndividual) o1).getIdentifier().compareTo(((AnonymousIndividual) o2).getIdentifier());
            }
            else {
                // Blank nodes come first
                return BEFORE;
            }
        }
        // IRIs
        else if(o1 instanceof AtomicIRI) {
            if(o2 instanceof AnonymousIndividual) {
                return AFTER;
            }
            else if(o2 instanceof AtomicIRI) {
                return ((AtomicIRI) o1).getIRI().compareTo(((AtomicIRI) o2).getIRI());
            }
            else {
                return BEFORE;
            }
            
        }
        else if(o1 instanceof Literal) {
            if(o2 instanceof AnonymousIndividual) {
                return AFTER;
            }
            else if(o2 instanceof AtomicIRI) {
                return AFTER;
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
                            return BEFORE;
                        }
                        else if(d1 > d2) {
                            return AFTER;
                        }
                    }
                } catch (NumberFormatException e) {
                    // Ignore
                }

                diff = l1.getLexicalForm().compareTo(l2.getLexicalForm());
                if(diff != SAME) {
                    return diff;
                }
                else {
                    return l1.getLang().compareTo(l2.getLang());
                }
            }
            else {
                return SAME;
            }
        }
        return SAME;
    }
}
