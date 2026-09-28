package org.pac4j.sparkjava;

import org.junit.jupiter.api.Test;
import org.pac4j.core.exception.TechnicalException;
import spark.Request;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests {@link SparkWebContext}.
 */
class SparkWebContextTest {

    @Test
    void nullRequestIsRejected() {
        final TechnicalException e = assertThrows(TechnicalException.class, () -> new SparkWebContext(null, null));
        assertEquals("request cannot be null", e.getMessage());
    }

    @Test
    void nullResponseIsRejected() {
        final Request request = new Request() { };
        final TechnicalException e = assertThrows(TechnicalException.class, () -> new SparkWebContext(request, null));
        assertEquals("response cannot be null", e.getMessage());
    }
}
