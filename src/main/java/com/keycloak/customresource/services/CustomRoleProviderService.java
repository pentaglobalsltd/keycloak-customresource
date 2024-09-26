package com.keycloak.customresource.services;

import com.keycloak.customresource.model.RolesModel;

import java.util.List;

public interface CustomRoleProviderService {
    RolesModel findRolesNamesByUserId(String userId);
}
