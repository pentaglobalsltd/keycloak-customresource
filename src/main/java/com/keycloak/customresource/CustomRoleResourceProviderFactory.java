package com.keycloak.customresource;

import com.keycloak.customresource.services.CustomRoleProviderService;
import com.keycloak.customresource.services.impls.CustomRoleProviderServiceImpl;
import com.keycloak.customresource.repositories.CustomRoleRepository;
import com.keycloak.customresource.repositories.impls.CustomRoleRepositoryImpl;
import com.keycloak.customresource.providers.CustomRoleResourceProvider;
import org.keycloak.Config.Scope;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.services.resource.RealmResourceProvider;
import org.keycloak.services.resource.RealmResourceProviderFactory;

public class CustomRoleResourceProviderFactory implements RealmResourceProviderFactory {

    public final static String ROLE_RESOURCE_URI = "role-resource";

    @Override
    public String getId() {
        return ROLE_RESOURCE_URI;
    }

    @Override
    public RealmResourceProvider create(KeycloakSession session) {
        return new CustomRoleResourceProvider(session.getContext().getRealm(),
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

	public CustomRoleProviderService customRoleProvider(KeycloakSession session) {
		return new CustomRoleProviderServiceImpl(customRoleRepository(session));
	}

	public CustomRoleRepository customRoleRepository(KeycloakSession session) {
		return new CustomRoleRepositoryImpl(session);
	}
}
