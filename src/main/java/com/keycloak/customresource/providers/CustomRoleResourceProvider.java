package com.keycloak.customresource.providers;

import com.keycloak.customresource.auth.Authenticator;
import com.keycloak.customresource.model.RolesModel;
import com.keycloak.customresource.services.CustomRoleProviderService;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.services.resource.RealmResourceProvider;

import java.util.Map;

@Slf4j
public class CustomRoleResourceProvider implements RealmResourceProvider {

    private final RealmModel realm;
    private final CustomRoleProviderService customRoleProvider;
    private final KeycloakSession keycloakSession;

    public CustomRoleResourceProvider(RealmModel realm,
                                      CustomRoleProviderService customRoleProvider,
                                      KeycloakSession keycloakSession) {
        this.realm = realm;
        this.customRoleProvider = customRoleProvider;
        this.keycloakSession = keycloakSession;
    }

	@Override
	public void close() {
	}

	@Override
	public Object getResource() {
		return this;
	}

    @GET
    @Path("/users/{userId}/roles/names")
    @Produces(MediaType.APPLICATION_JSON)
    public RolesModel getRolesNameByUserIdWithoutCustomQuery(@PathParam("userId") String userId) {
        log.info("Getting roles name for user Id: " + userId);
        Authenticator.INSTANCE.authenticate(keycloakSession);
        final var rolesNamesByUserId = customRoleProvider.findRolesNamesByUserId(userId);
        return rolesNamesByUserId;
    }
}
