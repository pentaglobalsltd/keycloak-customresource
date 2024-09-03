package com.keycloak.customresource.services;

import com.keycloak.customresource.model.RolesModel;

public interface CustomRoleProviderService {
    RolesModel getRolesNamesByUserId(String userId);
}
