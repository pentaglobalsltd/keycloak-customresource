package com.keycloak.spiexamples;

import com.keycloak.spiexamples.providers.CustomRoleProvider;
import com.keycloak.spiexamples.providers.impls.CustomRoleProviderImpl;
import com.keycloak.spiexamples.repositories.CustomRoleRepository;
import com.keycloak.spiexamples.repositories.impls.CustomRoleRepositoryImpl;
import com.keycloak.spiexamples.resources.CustomRoleResource;
import org.keycloak.Config.Scope;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.services.resource.RealmResourceProvider;
import org.keycloak.services.resource.RealmResourceProviderFactory;

public class CustomRoleResourceProviderFactory implements RealmResourceProviderFactory {

    public final static String COMPOSITE_ROLE_RESOURCE = "role-resource";

    @Override
    public String getId() {
        return COMPOSITE_ROLE_RESOURCE;
    }

    @Override
    public RealmResourceProvider create(KeycloakSession session) {
        return new CustomRoleResource(session.getContext().getRealm(),
                customRoleProvider(session), session);
    }

    @Override
    public void init(Scope config) {

    }

    @Override
    public void postInit(KeycloakSessionFactory factory) {

    }

    @Override
    public void close() {

    }

	public CustomRoleProvider customRoleProvider(KeycloakSession session) {
		return new CustomRoleProviderImpl(session, customRoleRepository(session));
	}

	public CustomRoleRepository customRoleRepository(KeycloakSession session) {
		return new CustomRoleRepositoryImpl(session);
	}
}
