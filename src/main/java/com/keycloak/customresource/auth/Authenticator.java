package com.keycloak.customresource.auth;

import org.keycloak.models.KeycloakSession;
import org.keycloak.services.managers.AppAuthManager;
import org.keycloak.services.managers.AuthenticationManager.AuthResult;

import jakarta.ws.rs.NotAuthorizedException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum Authenticator {
	INSTANCE;
	
	Authenticator() {}
	
	public AuthResult authenticate(KeycloakSession session) {
		AuthResult authResult = new AppAuthManager.BearerTokenAuthenticator(session).authenticate();
		log.info("authorization result: " + authResult);
		if(authResult == null || authResult.getToken().getIssuedFor() == null) {
			log.info("throwing not authorization error");
			throw new NotAuthorizedException("Not authorized for this resource");
		}
		
		return authResult;
	}
}
