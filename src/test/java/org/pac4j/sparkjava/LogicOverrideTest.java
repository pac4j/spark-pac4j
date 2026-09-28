package org.pac4j.sparkjava;

import org.junit.jupiter.api.Test;
import org.pac4j.core.config.Config;
import org.pac4j.core.engine.SecurityGrantedAccessAdapter;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Tests that the logic set on the {@link SecurityFilter}, the {@link CallbackRoute} and the {@link LogoutRoute}
 * overrides the one from the config.
 */
class LogicOverrideTest {

    private final AtomicInteger calls = new AtomicInteger();

    @Test
    void securityFilterUsesItsOwnLogic() {
        final Config config = new Config();
        config.setSecurityLogic((cfg, granted, clients, authorizers, matchers, parameters) -> fail("config logic used"));
        final SecurityFilter filter = new SecurityFilter(config, "MyClient");
        filter.setSecurityLogic((cfg, granted, clients, authorizers, matchers, parameters) -> {
            assertEquals("MyClient", clients);
            return grantAccess(granted);
        });

        filter.handle(null, null);

        assertEquals(1, calls.get());
    }

    @Test
    void securityFilterUsesConfigLogicByDefault() {
        final Config config = new Config();
        config.setSecurityLogic((cfg, granted, clients, authorizers, matchers, parameters) -> grantAccess(granted));

        new SecurityFilter(config, "MyClient").handle(null, null);

        assertEquals(1, calls.get());
    }

    @Test
    void callbackRouteUsesItsOwnLogic() throws Exception {
        final Config config = new Config();
        config.setCallbackLogic((cfg, defaultUrl, renewSession, defaultClient, parameters) -> fail("config logic used"));
        final CallbackRoute route = new CallbackRoute(config, "/home");
        route.setCallbackLogic((cfg, defaultUrl, renewSession, defaultClient, parameters) -> {
            assertEquals("/home", defaultUrl);
            return calls.incrementAndGet();
        });

        route.handle(null, null);

        assertEquals(1, calls.get());
    }

    @Test
    void callbackRouteUsesConfigLogicByDefault() throws Exception {
        final Config config = new Config();
        config.setCallbackLogic((cfg, defaultUrl, renewSession, defaultClient, parameters) -> calls.incrementAndGet());

        new CallbackRoute(config).handle(null, null);

        assertEquals(1, calls.get());
    }

    @Test
    void logoutRouteUsesItsOwnLogic() throws Exception {
        final Config config = new Config();
        config.setLogoutLogic((cfg, defaultUrl, logoutUrlPattern, localLogout, destroySession, centralLogout, parameters) ->
                fail("config logic used"));
        final LogoutRoute route = new LogoutRoute(config, "/bye");
        route.setLogoutLogic((cfg, defaultUrl, logoutUrlPattern, localLogout, destroySession, centralLogout, parameters) -> {
            assertEquals("/bye", defaultUrl);
            return calls.incrementAndGet();
        });

        route.handle(null, null);

        assertEquals(1, calls.get());
    }

    @Test
    void logoutRouteUsesConfigLogicByDefault() throws Exception {
        final Config config = new Config();
        config.setLogoutLogic((cfg, defaultUrl, logoutUrlPattern, localLogout, destroySession, centralLogout, parameters) ->
                calls.incrementAndGet());

        new LogoutRoute(config).handle(null, null);

        assertEquals(1, calls.get());
    }

    private Object grantAccess(final SecurityGrantedAccessAdapter granted) {
        calls.incrementAndGet();
        try {
            return granted.adapt(null, null, List.of());
        } catch (final Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
