package com.keycloak.customresource.services.impls;

import com.keycloak.customresource.model.RolesModel;
import com.keycloak.customresource.services.CustomRoleProviderService;
import com.keycloak.customresource.repositories.CustomRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.models.KeycloakSession;

@Slf4j
@RequiredArgsConstructor
public class CustomRoleProviderServiceImpl implements CustomRoleProviderService {

	private final KeycloakSession session;

	private final CustomRoleRepository roleRepository;

	@Override
	public RolesModel getRolesNamesByUserId(String userId) {
		final var rolesNamesByUserId = roleRepository.findRolesNamesByUserId(userId);
		RolesModel rolesModel = new RolesModel(rolesNamesByUserId);
		return rolesModel;
	}

}
