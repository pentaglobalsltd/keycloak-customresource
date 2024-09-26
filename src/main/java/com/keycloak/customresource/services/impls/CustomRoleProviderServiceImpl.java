package com.keycloak.customresource.services.impls;

import com.keycloak.customresource.model.RolesModel;
import com.keycloak.customresource.services.CustomRoleProviderService;
import com.keycloak.customresource.repositories.CustomRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.models.KeycloakSession;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class CustomRoleProviderServiceImpl implements CustomRoleProviderService {

	private final CustomRoleRepository roleRepository;

	@Override
	public RolesModel findRolesNamesByUserId(String userId) {
		final var rolesNamesByUserId = roleRepository.findRolesNamesByUserId(userId);
		return new RolesModel(rolesNamesByUserId);
	}

}
