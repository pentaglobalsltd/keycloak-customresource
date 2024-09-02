#!/bin/bash
 
sudo docker image rm -f  keycloak-with-custom-role-resource-keycloak-2
#sudo docker image rm -f  keycloak-with-rabbitmp-receiver-service
mvn clean install
sudo docker compose up
