package com.sen3006.coursewise.models;

import com.sen3006.coursewise.API;

import java.util.Scanner;

public class CurrentUser extends User {
    private static CurrentUser currentUser; // Singleton instance
    private API api = API.getInstance();
    // private int role; to be implemented later

    // Private constructor (for a singleton pattern)
    private CurrentUser(User user) {
        super(user.getId(), user.getName(), user.getSurname(), user.getEmail());
    }

    // Singleton pattern implementation
    public static CurrentUser getInstance(User user) {
        if (currentUser == null) {
            currentUser = new CurrentUser(user);
        }
        return currentUser;
    }

//    public int getRole() {
//        return role;
//    }
//
//    public void setRole(int role) {
//         this.role = role;
//    }

    public void validateEmailFormat(String email) {
        String emailRegex = "[a-z0-9!#$%&'+/=?^_`{|}~-]+(?:.[a-z0-9!#$%&'+/=?^_`{|}~-]+)@(?:[a-z0-9](?:[a-z0-9-][a-z0-9])?.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?";

        if (email == null || !email.matches(emailRegex)) {
            throw new IllegalArgumentException("Invalid email format: " + email);
        }
    }

    public void login(String password) {
        // Read email from user input
        String email = null; // to be replaced with actual input
        String[] credentials = api.getCredentials(email);
        // Assuming credentials[0] is the user ID, casting it to int
        int id = Integer.parseInt(credentials[0]);
        // Assuming credentials[1] is the password
        String passwordFromAPI = credentials[1];

        try{
            validateEmailFormat(email);
            if (!password.equals(passwordFromAPI)) {
                throw new IllegalArgumentException("Invalid password");
            }
            else CurrentUser.getInstance(new User(id, currentUser.getName(), currentUser.getSurname(), email));
        }catch (IllegalArgumentException e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void logout() {
        // Logic to log out the user
        currentUser = null; // Clear the instance on logout
    }
}
