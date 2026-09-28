package org.pac4j.sparkjava;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.pac4j.core.client.Client;
import org.pac4j.core.client.IndirectClient;
import org.pac4j.core.client.direct.AnonymousClient;
import org.pac4j.core.config.Config;
import org.pac4j.core.exception.http.FoundAction;
import spark.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests {@link SecurityFilter} in a running Spark server.
 */
class SecurityFilterTest {

    private static final String IDP_URL = "https://idp.example.org/login";

    private static final String PROTECTED_BODY = "protected content";

    private final AtomicInteger routeCalls = new AtomicInteger();

    private final HttpClient httpClient = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.NEVER).build();

    private Service http;

    @BeforeEach
    void setUp() {
        routeCalls.set(0);
    }

    @AfterEach
    void tearDown() {
        if (http != null) {
            http.stop();
            http.awaitStop();
        }
    }

    @Test
    void redirectionToIdentityProviderHaltsGet() throws Exception {
        start(new RedirectingClient());

        final HttpResponse<String> response = send(HttpRequest.newBuilder(uri()).GET());

        assertEquals(302, response.statusCode());
        assertEquals(IDP_URL, response.headers().firstValue("Location").orElse(null));
        assertEquals(0, routeCalls.get());
    }

    @Test
    void redirectionToIdentityProviderHaltsPost() throws Exception {
        start(new RedirectingClient());

        final HttpResponse<String> response = send(HttpRequest.newBuilder(uri()).POST(HttpRequest.BodyPublishers.noBody()));

        assertEquals(302, response.statusCode());
        assertEquals(IDP_URL, response.headers().firstValue("Location").orElse(null));
        assertEquals(0, routeCalls.get());
    }

    @Test
    void grantedAccessContinues() throws Exception {
        start(new AnonymousClient());

        final HttpResponse<String> response = send(HttpRequest.newBuilder(uri()).GET());

        assertEquals(200, response.statusCode());
        assertEquals(PROTECTED_BODY, response.body());
        assertEquals(1, routeCalls.get());
    }

    private void start(final Client client) {
        final Config config = new Config("/callback", client);
        http = Service.ignite().port(0);
        http.before("/protected", new SecurityFilter(config, client.getName()));
        http.get("/protected", (req, res) -> protectedRoute());
        http.post("/protected", (req, res) -> protectedRoute());
        http.awaitInitialization();
    }

    private String protectedRoute() {
        routeCalls.incrementAndGet();
        return PROTECTED_BODY;
    }

    private URI uri() {
        return URI.create("http://localhost:" + http.port() + "/protected");
    }

    private HttpResponse<String> send(final HttpRequest.Builder builder) throws Exception {
        return httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofString());
    }

    /**
     * Indirect client which always redirects to the identity provider.
     */
    private static final class RedirectingClient extends IndirectClient {
        @Override
        protected void internalInit(final boolean forceReinit) {
            setRedirectionActionBuilderIfUndefined(ctx -> Optional.of(new FoundAction(IDP_URL)));
            setCredentialsExtractorIfUndefined(ctx -> Optional.empty());
            setAuthenticatorIfUndefined((ctx, credentials) -> Optional.of(credentials));
        }
    }
}
