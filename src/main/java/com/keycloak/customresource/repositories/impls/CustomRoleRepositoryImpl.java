package com.keycloak.customresource.repositories.impls;

import com.keycloak.customresource.repositories.CustomRoleRepository;
import jakarta.persistence.EntityManager;
import org.keycloak.connections.jpa.JpaConnectionProvider;
import org.keycloak.models.KeycloakSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class CustomRoleRepositoryImpl implements CustomRoleRepository {

    private final Logger log = LoggerFactory.getLogger(CustomRoleRepositoryImpl.class);

    private final KeycloakSession session;

    private final EntityManager em;

    public CustomRoleRepositoryImpl(KeycloakSession session) {
        this.session = session;
        this.em = session.getProvider(JpaConnectionProvider.class).getEntityManager();
    }

    @Override
    public List<String> findRolesNamesByUserId(String userId) {
        final List<String> roleNames = em.createNativeQuery("select kc.name from keycloak_role as kc inner join user_role_mapping as urm on kc.id = urm.role_id where urm.user_id = :userId", String.class)
                .setParameter("userId", userId)
                .getResultList();
        log.info("for user: " + userId + " roles: " + roleNames);

        return roleNames;
    }
}
