package com.keycloak.spiexamples.resources;

import com.keycloak.spiexamples.auth.Authenticator;
import com.keycloak.spiexamples.model.RolesModel;
import com.keycloak.spiexamples.providers.CustomRoleProvider;
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
public class CustomRoleResource implements RealmResourceProvider {

    private final RealmModel realm;
    private final CustomRoleProvider customRoleProvider;
    private final KeycloakSession keycloakSession;

    public CustomRoleResource(RealmModel realm,
                              CustomRoleProvider customRoleProvider,
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
    public RolesModel getRolesNameByUserId(@PathParam("userId") String userId) {
        log.info("Getting roles name for user Id: " + userId);
        Authenticator.INSTANCE.authenticate(keycloakSession);
        final var rolesNamesByUserId = customRoleProvider.getRolesNamesByUserId(userId);
        return rolesNamesByUserId;
    }
    
    @GET
    @Path("hello")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, String> hello() {
//    	System.out.println("in hellooooooooooooooooooooooo: " + keycloakSession.getContext().getRealm().getName());
        return Map.of("hello",
        		"test"
        		);
    }
}
