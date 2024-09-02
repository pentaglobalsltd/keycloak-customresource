package com.keycloak.spiexamples;

import com.keycloak.spiexamples.listeners.RoleAssignmentListener;

import org.jboss.logging.Logger;
import org.keycloak.Config;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventListenerProviderFactory;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;

public class RoleAssignmentListenerFactory implements EventListenerProviderFactory {
    private static final Logger log = Logger.getLogger(RoleAssignmentListener.class);
    
    @Override
    public EventListenerProvider create(KeycloakSession session) {
        log.info("initializing the event lister factor for role assignment");
        return new RoleAssignmentListener(session);
    }

    @Override
    public void init(Config.Scope config) {

    }

    @Override
    public void postInit(KeycloakSessionFactory factory) {

    }

    @Override
    public void close() {}

    @Override
    public String getId() {
        return "role-assignment-listener";
    }
}