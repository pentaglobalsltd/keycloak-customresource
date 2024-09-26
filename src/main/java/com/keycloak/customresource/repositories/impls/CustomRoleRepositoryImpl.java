package com.keycloak.customresource.repositories.impls;

import com.keycloak.customresource.repositories.CustomRoleRepository;
import jakarta.persistence.EntityManager;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.keycloak.connections.jpa.JpaConnectionProvider;
import org.keycloak.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
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
        RealmModel realm = session.getContext().getRealm();
        UserModel user = session.users().getUserById(realm, userId);

        if (user == null) {
            throw new WebApplicationException("User not found: " + userId, Response.Status.NOT_FOUND);
        }

        // Create a set to avoid duplicates
        Set<String> roleNames = new HashSet<>();

        // Add direct roles
        addRoles(roleNames, user.getRealmRoleMappingsStream());

        // Add roles from groups
        user.getGroupsStream().forEach(group -> {
            addRoles(roleNames, group.getRoleMappingsStream());
        });

        log.info("Groups for user {}: {}", userId, user.getGroupsStream().map(GroupModel::getName).collect(Collectors.toList()));

        log.info("Roles for user {}: {}", userId, roleNames);

        return new ArrayList<>(roleNames);
    }

    // Recursive method to add roles
    private void addRoles(Set<String> roleNames, Stream<RoleModel> rolesStream) {
        rolesStream.forEach(role -> {
            roleNames.add(role.getName());
            if (role.isComposite()) {
                addRoles(roleNames, role.getCompositesStream());
            }
        });
    }
}
