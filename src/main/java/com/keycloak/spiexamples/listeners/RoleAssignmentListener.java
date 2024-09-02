package com.keycloak.spiexamples.listeners;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.jboss.logging.Logger;
import org.keycloak.events.Event;
import org.keycloak.events.admin.AdminEvent;
import org.keycloak.events.admin.OperationType;
import org.keycloak.events.admin.ResourceType;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.models.KeycloakSession;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class RoleAssignmentListener implements EventListenerProvider {
    private static final Logger log = Logger.getLogger(RoleAssignmentListener.class);
    private final static String QUEUE_NAME = "q.role.mapper";
    private final KeycloakSession session;

    public RoleAssignmentListener(KeycloakSession session) {
        this.session = session;
    }

    @Override
    public void onEvent(Event event) {
    }

    @Override
    public void onEvent(AdminEvent adminEvent, boolean includeRepresentation) {

        log.info("## NEW %s EVENT " + adminEvent.getOperationType());
        log.info("## NEW %s resource " + adminEvent.getResourceTypeAsString());

        log.info("## representation %s resource " + adminEvent.getRepresentation());
        log.info("## representation %s resource " + adminEvent.getId());
        log.info("## path %s resource " + adminEvent.getResourcePath());

        final var authDetails = adminEvent.getAuthDetails();
        log.info("Auth clientid " + authDetails.getClientId());
        log.info("Auth ipaddress " + authDetails.getIpAddress());
        log.info("Auth realmid " + authDetails.getRealmId());
        log.info("Auth userid " + authDetails.getUserId());

        log.info("-----------------------end of admin event------------------------------------");
//        adminEvent.getDetails().forEach((key, value) -> log.info(key + ": " + value));

        if (
                (adminEvent.getOperationType().equals(OperationType.CREATE) ||
                        adminEvent.getOperationType().equals(OperationType.DELETE)) &&
                        (adminEvent.getResourceType().equals(ResourceType.REALM_ROLE_MAPPING) ||
                                adminEvent.getResourceType().equals(ResourceType.CLIENT_ROLE_MAPPING))
        ) {
            publishToRabbitMQ(adminEvent);
        }

           
    }

    private void publishToRabbitMQ(AdminEvent adminEvent) {

        final var authDetails = adminEvent.getAuthDetails();
        log.info("Creating a connectionnnnnnnnn");
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("guest");
        factory.setPassword("guest");

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            log.info("publishing userid to the queue");
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);
            String message = authDetails.getUserId();
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes(StandardCharsets.UTF_8));
            System.out.println(" [x] Sent '" + message + "'");
        } catch (IOException e) {
            log.error("Cant publish for IO exception", e);
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            log.error("Cant publish for TimeoutException", e);
            throw new RuntimeException(e);
        }

    }

    @Override
    public void close() {
    }
}
