package org.pac4j.sparkjava;

import org.pac4j.core.exception.TechnicalException;

/**
 * Exception when the access is granted.
 *
 * @author Jerome Leleu
 * @since 1.3.0
 */
public class SecurityGrantedAccessException extends TechnicalException {

    public SecurityGrantedAccessException() {
        super("access granted");
    }
}
