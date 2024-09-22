package com.keycloak.customresource.repositories.impls;

import com.keycloak.customresource.repositories.CustomRoleRepository;
import jakarta.persistence.EntityManager;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.keycloak.connections.jpa.JpaConnectionProvider;
import org.keycloak.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


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

    @Override
    public List<String> findRolesNamesByUserIdWithOutCustomQuery(String userId) {
        RealmModel realm = session.getContext().getRealm();
        UserModel user = session.users().getUserById(realm, userId);


        if (user == null) {
            throw new WebApplicationException("User not found", Response.Status.NOT_FOUND);
        }

        // Combine both realm roles and client roles assigned to the user
        Stream<RoleModel> assignedRoles = user.getRealmRoleMappingsStream();

        // Extract role names and collect them into a list
        List<String> roleNames = assignedRoles
                .map(RoleModel::getName)
                .collect(Collectors.toList());

        log.info("roles: " + roleNames);

        return roleNames;
    }
}
