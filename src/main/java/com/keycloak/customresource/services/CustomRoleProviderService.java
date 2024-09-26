package com.keycloak.customresource.services;

import com.keycloak.customresource.model.RolesModel;

import java.util.List;

public interface CustomRoleProviderService {
    public RolesModel findRolesNamesByUserId(String userId);
}
