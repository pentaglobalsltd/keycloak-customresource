# Extending Keycloak with Custom REST API for User Roles

This guide provides a detailed walkthrough of extending Keycloak to add a custom REST API that fetches user roles in a simplified and optimized manner. We will implement a Keycloak Service Provider Interface (SPI) to create a new endpoint that returns only the role names assigned to a user, improving performance with native database queries.

## 1. Keycloak SPI Implementation

Keycloak provides a powerful extension mechanism called Service Provider Interface (SPI), allowing you to extend or modify its core functionality by creating custom providers. To achieve our goal of exposing a new REST API, we need to follow specific rules:

### Rules for Implementing SPI

1. **Extend the Desired Provider**:
    - To create a new REST API in Keycloak, we extend the `RealmResourceProvider`. This class is responsible for exposing custom endpoints within a specific realm.
    - In our implementation, we created a class `CustomRoleResourceProvider` to define our custom API endpoint.

2. **Create a Factory Class**:
    - A factory class is needed to instantiate our custom provider. This factory must implement `RealmResourceProviderFactory`.
    - Our factory class, `CustomRoleResourceProviderFactory`, provides the logic to create an instance of our `CustomRoleResourceProvider` and registers it with Keycloak.

3. **Register the Provider**:
    - Finally, we need to inform Keycloak about our new provider. This is done by adding the provider’s fully qualified class name to a specific file:
        - File path: `META-INF/services/org.keycloak.services.resource.RealmResourceProviderFactory`
        - Content: `com.keycloak.customresource.CustomRoleResourceProviderFactory`
    - This file tells Keycloak to deploy our custom provider when the server starts.

### Custom API Implementation

We implemented a new API endpoint under the path `/users/{userId}/roles/names`, which fetches and returns only the role names assigned to the specified user.

```java
public class CustomRoleResourceProvider implements RealmResourceProvider {
    @GET
    @Path("/users/{userId}/roles/names")
    @Produces(MediaType.APPLICATION_JSON)
    public RolesModel getRolesNameByUserId(@PathParam("userId") String userId) {
        log.info("Getting roles name for user Id: " + userId);
        final var rolesNamesByUserId = customRoleProvider.getRolesNamesByUserId(userId);
        return rolesNamesByUserId;
    }
    }
}
```