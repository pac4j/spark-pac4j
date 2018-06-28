package org.pac4j.sparkjava;

/**
 * The marker singleton object as a signal that the access is granted.
 */
final class SecurityGrantedAccess {
    
    /**
     * The only instance.
     */
    static final SecurityGrantedAccess INSTANCE = new SecurityGrantedAccess();
    
    /**
     * Enforce singleton
     */
    private SecurityGrantedAccess() {}
    
}
