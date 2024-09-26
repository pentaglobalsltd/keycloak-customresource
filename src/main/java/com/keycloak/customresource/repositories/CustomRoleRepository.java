package com.keycloak.customresource.repositories;

import org.keycloak.models.RealmModel;

import java.util.List;

public interface CustomRoleRepository {
    List<String> findRolesNamesByUserId(String userId);
}
