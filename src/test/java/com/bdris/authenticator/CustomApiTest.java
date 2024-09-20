package com.bdris.authenticator;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertTrue;

import com.keycloak.customresource.CustomRoleResourceProviderFactory;
import dasniko.testcontainers.keycloak.KeycloakContainer;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Map;

@Slf4j
@Testcontainers
public class CustomApiTest {
    //Define the base uri of the custom api
    private final String REALM_URI = "/realms/test";
    private final String USER_ID = "3f18f02a-1edd-4dfe-ba7c-0ee19b0a5ba9";

    //Define the keycloak container
    @Container
    KeycloakContainer KEYCLOAK_CONTAINER = new KeycloakContainer()
            .withRealmImportFile("/test-realm.json")
            .withProviderClassesFrom("target/classes")
            .waitingFor(Wait.forHttp("/").forStatusCode(200));

    @Before
    public void startKeycloak() {
        if(!KEYCLOAK_CONTAINER.isRunning()) {
            KEYCLOAK_CONTAINER.start();
        }
    }

    private String getAccessToken() {
        final String authServerUrl = getAuthServerUrl();

        final String accessToken = given().contentType(ContentType.URLENC)
                .formParams(Map.of("username", "naz", "password", "123", "grant_type", "password", "client_id",
                        "test-cli", "client_secret", "**********"))
                .post(authServerUrl + REALM_URI + "/protocol/openid-connect/token")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .path("access_token");
        return accessToken;
    }

    private String getAuthServerUrl() {
        log.info("container logs: " + KEYCLOAK_CONTAINER.getLogs());
        assertTrue(KEYCLOAK_CONTAINER.isRunning());

        final String authServerUrl = KEYCLOAK_CONTAINER.getAuthServerUrl();
        log.info("Auth server url: " + authServerUrl);
        return authServerUrl;
    }

    @Test
    public void testAccessTokenGeneration() {
        String accessToken = getAccessToken();
        log.info("Access token: " + accessToken);
    }

    @Test
    public void testCustomRoleMappingByUserIdWithDirectRoute() {

        String authServerUrl = getAuthServerUrl();

        log.info("auth url: " + authServerUrl);
        String customRoleMappingUrl = authServerUrl + REALM_URI + "/" + CustomRoleResourceProviderFactory.ROLE_RESOURCE_URI
                + "/users/" + USER_ID + "/roles/names-without-custom-query";

        log.info("full url: " + customRoleMappingUrl);

        final String userRoleMappings = given()
                .header("Authorization", "Bearer " + getAccessToken())
                .get(customRoleMappingUrl)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .asPrettyString();

        log.info("result: " + userRoleMappings);
    }

    @Test
    public void testCustomRoleMappingByUserIdWithoutDirectRoute() {
        final String userRoleMappings = givenSpec()
                .auth()
                .oauth2(getAccessToken())
                .when()
                .get("/roles/names-without-custom-query")
                .then()
                .statusCode(200)
                .extract()
                .asPrettyString();
        log.info("result: " + userRoleMappings);
    }

    private RequestSpecification givenSpec() {
        String customRoleMappingUrl = REALM_URI + "/" + CustomRoleResourceProviderFactory.ROLE_RESOURCE_URI
                + "/users/" + USER_ID;

        log.info("full url: " + customRoleMappingUrl);
        return given().baseUri(getAuthServerUrl()).basePath(customRoleMappingUrl);
    }
}
