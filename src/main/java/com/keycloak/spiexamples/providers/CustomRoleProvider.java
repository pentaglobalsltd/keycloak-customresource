package com.keycloak.spiexamples.providers;

import com.keycloak.spiexamples.model.RolesModel;

public interface CustomRoleProvider {
    RolesModel getRolesNamesByUserId(String userId);
}
