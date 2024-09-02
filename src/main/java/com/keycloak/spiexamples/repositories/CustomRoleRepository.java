package com.keycloak.spiexamples.repositories;

import java.util.List;

public interface CustomRoleRepository {

    List<String> findRolesNamesByUserId(String userId);
}
