package com.sen3006.coursewise.client.models;

import com.sen3006.coursewise.client.API;

public class CurrentUser extends User {
    private static CurrentUser currentUser; // Singleton instance
    // private int role; to be implemented later

    // Private constructor (for a singleton pattern)
    private CurrentUser(User user) {
        super(user.getId(), user.getName(), user.getSurname(), user.getEmail());
    }

    // Singleton pattern implementation
    public static CurrentUser createInstance(User user) {
        return currentUser = new CurrentUser(user);
    }

    public static CurrentUser getInstance() {
        return currentUser;
    }


    public static boolean validateEmailFormat(String email) {
        String emailRegex = "[a-z0-9!#$%&'+/=?^`{|}~-]+(?:.[a-z0-9!#$%&'+/=?^`{|}~-]+)@(?:[a-z0-9](?:[a-z0-9-][a-z0-9])?.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])+(?:.[a-z0-9!#$%&'+/=?^`{|}~-]+)(?:.[a-z0-9!#$%&'+/=?^`{|}~-]+)";

        if (email == null || !email.matches(emailRegex)) {
            System.out.println("Invalid email format");
            return false;
        } else {
            // Email is valid
            System.out.println("Valid email format");
            return true;
        }
    }

    public static boolean login(String password, String email) {
        API api = API.getInstance();
        password = String.valueOf(password.hashCode()); // Hashing the password for security comparison from API;

        String[] credentials = api.getCredentials(email);
        if (credentials[0].isBlank()) {
            System.out.println("User not found");
            return false;
        }

        // Assuming credentials[0] is the user ID, casting it to int
        int id = Integer.parseInt(credentials[0]);
        // Assuming credentials[1] is the password
        String passwordFromAPI = credentials[1];

        if (!validateEmailFormat(email)) {
            return false;
        }

        if (!password.contentEquals(passwordFromAPI)) {
            System.out.println("Invalid password");
            return false;
        }
        else {
            User instance = api.getUser(id);
            CurrentUser.createInstance(instance);
            System.out.println("logged in as the user with id: " + id);
            System.out.println("Login successful, welcome " + currentUser.getName() + " " + currentUser.getSurname());
            return true;
        }
    }

    public void logout() {
        // Logic to log out the user
        currentUser = null; // Clear the instance on logout
    }
}
