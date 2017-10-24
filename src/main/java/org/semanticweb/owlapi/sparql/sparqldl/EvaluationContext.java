package org.semanticweb.owlapi.sparql.sparqldl;

import javax.annotation.Nonnull;
import java.time.ZonedDateTime;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 05/06/15
 */
public class EvaluationContext {

    private ZonedDateTime evaluationTime;

    public EvaluationContext(@Nonnull ZonedDateTime zonedDateTime) {
        this.evaluationTime = checkNotNull(zonedDateTime);
    }

    @Nonnull
    public ZonedDateTime getEvaluationTime() {
        return evaluationTime;
    }
}
