package com.sen3006.coursewise.models;

import com.sen3006.coursewise.API;

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

//    public int getRole() {
//        return role;
//    }
//
//    public void setRole(int role) {
//         this.role = role;
//    }

    //        users[0] = new UserPassword(2200900, "Salim Mert", "Uçar", "salim.ucar@bahcesehir.edu.tr", String.valueOf("123".hashCode()));
//        users[1] = new UserPassword(2200780, "Azizcan", "Tam", "azizcan.tam@bahcesehir.edu.tr", String.valueOf("223".hashCode()));
//        users[2] = new UserPassword(2200870, "Murat Kerem", "Serter", "murat.serter@bahcesehir.edu.tr", String.valueOf("323".hashCode()));

    public static boolean validateEmailFormat(String email) {
        String emailRegex = "[a-z0-9!#$%&'+/=?^`{|}~-]+(?:.[a-z0-9!#$%&'+/=?^`{|}~-]+)@(?:[a-z0-9](?:[a-z0-9-][a-z0-9])?.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])+(?:.[a-z0-9!#$%&'+/=?^`{|}~-]+)(?:.[a-z0-9!#$%&'+/=?^`{|}~-]+)";

        if (email == null || !email.matches(emailRegex)) {
            //throw new IllegalArgumentException("Invalid email format");
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
            //throw new IllegalArgumentException("User not found");
            System.out.println("User not found");
            return false;
        }

        // Assuming credentials[0] is the user ID, casting it to int
        int id = Integer.parseInt(credentials[0]);
        // Assuming credentials[1] is the password
        String passwordFromAPI = credentials[1];

        if (!validateEmailFormat(email)) {
            //throw new IllegalArgumentException("Invalid email format");
            return false;
        }

        if (!password.contentEquals(passwordFromAPI)) {
            //throw new IllegalArgumentException("Invalid password");
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
