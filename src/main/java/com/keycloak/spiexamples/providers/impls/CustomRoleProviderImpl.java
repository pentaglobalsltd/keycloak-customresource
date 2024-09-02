package com.keycloak.spiexamples.providers.impls;

import com.keycloak.spiexamples.model.RolesModel;
import com.keycloak.spiexamples.providers.CustomRoleProvider;
import com.keycloak.spiexamples.repositories.CustomRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.RoleModel;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class CustomRoleProviderImpl implements CustomRoleProvider {

	private final KeycloakSession session;

	private final CustomRoleRepository roleRepository;

	@Override
	public RolesModel getRolesNamesByUserId(String userId) {
		final var rolesNamesByUserId = roleRepository.findRolesNamesByUserId(userId);
		RolesModel rolesModel = new RolesModel(rolesNamesByUserId);
		return rolesModel;
	}

}
