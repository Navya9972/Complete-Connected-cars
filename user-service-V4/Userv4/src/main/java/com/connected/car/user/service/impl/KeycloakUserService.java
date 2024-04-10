package com.connected.car.user.service.impl;

import com.connected.car.user.dto.UserDto;
import com.connected.car.user.entity.Roles;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class KeycloakUserService {


//    RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private Keycloak keycloak ;


//            = getKeyclaokBean();
//    @Autowired
//    private Keycloak keycloak = getKeyclaokBean();

    private String realm = "connected-cars";
    private String keycloakAdminServerUrl = "http://your-keycloak-server/auth";

//    public KeycloakUserService(Keycloak keycloak){
//        this.keycloak=keycloak;
//    }



//    @Bean
//    public Keycloak getKeyclaokBean(){
//        keycloak = restTemplate.getForObject("http://localhost:8088/keycloak/get/keycloak/bean",Keycloak.class);
//        return keycloak;
//    }




    public boolean addUserToKeycloakServer(UserDto userDto){
        System.out.println("I AM IN KEYCLOAK METHOD!!!");
            RealmResource realmResource = keycloak.realm(realm);
            UsersResource usersResource = realmResource.users();
            UserRepresentation keycloakUser = dtoToUserRepresentation(userDto);

            System.out.println("NEW USER EMAIL "+keycloakUser.getEmail());
//            Map<String, List<String>> clientRoles = new HashMap<>();
//            clientRoles.put(userDto.getEmail(), List.of("admin"));
//            keycloakUser.setClientRoles(clientRoles);

        Response response;
        try{
             response = usersResource.create(keycloakUser);
//             System.out.println("User Successfully created in keycloak");
//             String userId = keycloak.realm(realm).users().search(userDto.getEmail()).get(0).getId();
//             List<RoleRepresentation> newRoles = keycloak.realm(realm).roles().list()
//                     .stream()
//                     .filter(role->userDto.getRole().contains(role.getName()))
//                     .collect(Collectors.toList());

//             keycloak.realm(realm).users().get(userId).roles().realmLevel().add(newRoles);
//             System.out.println("Roles updated in keycloak");

            if(response.getStatus()==201)
                return true;
            else{
                String errorMessage = response.readEntity(String.class);
                throw new RuntimeException("Failed to create user. Keycloak API response: " + response.getStatus() + " - " + errorMessage);
            }



        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }




    public boolean updateRole(String email, List<Roles> rolesList){
        String userId = keycloak.realm(realm).users().search(email).get(0).getId();

        List<RoleRepresentation> existingRoles = keycloak.realm(realm).roles().list();
        for (RoleRepresentation existingRole : existingRoles) {
            keycloak.realm(realm).users().get(userId).roles().realmLevel().remove(Arrays.asList(existingRole));
        }


        List<RoleRepresentation> newRoles = keycloak.realm(realm).roles().list()
                .stream()
                .filter(role -> rolesList.contains(role.getName()))
                .collect(Collectors.toList());

        keycloak.realm(realm).users().get(userId).roles().realmLevel().add(newRoles);
        return true;
    }


    public void changePasswordInKeycloak(UserDto logggedInUser, String password) {
        RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersResource = realmResource.users();
        List<UserRepresentation> foundUsers = usersResource.searchByEmail(logggedInUser.getEmail(),true);
        UserRepresentation registeredUser = foundUsers.get(0);
        System.out.println("REGISTERED USER "+registeredUser.getUsername());
        System.out.println("REGISTERED USER ID "+registeredUser.getId());

        CredentialRepresentation resetCredential = new CredentialRepresentation();
        resetCredential.setType(CredentialRepresentation.PASSWORD);
        resetCredential.setValue(password);
        resetCredential.setTemporary(false);
        usersResource.get(registeredUser.getId()).resetPassword(resetCredential);

    }

//    public void updateUserInKeycloak(UserDto updatedUserDto) {
//        RealmResource realmResource = keycloak.realm(realm);
//        UsersResource usersResource = realmResource.users();
//
//        List<UserRepresentation> foundUsers = usersResource.searchByEmail(updatedUserDto.getEmail(),true);
//        UserRepresentation registeredUser = foundUsers.get(0);
//        String userId = registeredUser.getId();
//        UserResource userResource = usersResource.get(registeredUser.getId());
//        String token=user
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Content-Type", "application/json");
//        headers.set("Authorization", "Bearer " + getAccessToken());
//
//        String userData = String.format("{\"firstName\":\"%s\",\"lastName\":\"%s\",\"email\":\"%s\"}", firstName, lastName, email);
//
//        HttpEntity<String> entity = new HttpEntity<>(userData, headers);
//
//
//
//
//        String updateUserUrl = keycloakAdminServerUrl + "/admin/realms/" + realm + "/users/" + userId;
//        ResponseEntity<String> response = new RestTemplate().exchange(updateUserUrl, HttpMethod.PUT, entity, String.class);
//
//    }

    public UserRepresentation dtoToUserRepresentation(UserDto userDto){
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(userDto.getFirstName()+userDto.getLastName());
        userRepresentation.setEmail(userDto.getEmail());
        userRepresentation.setEnabled(true);
        userRepresentation.setEmailVerified(true);
        userRepresentation.setFirstName(userDto.getFirstName());
        userRepresentation.setLastName(userDto.getLastName());

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(userDto.getPassword());
        credential.setTemporary(false);

        userRepresentation.setCredentials(List.of(credential));

        return userRepresentation;
    }

}
