package com.sen3006.coursewise.client.models;

import com.sen3006.coursewise.client.API;

import java.util.Observable;

public class User extends Observable {
        private int user_id;
        private String name;
        private String surname;
        private String email;

        // Constructor
        public User(int id, String name, String surname, String email) {
            this.user_id = id;
            this.name = name;
            this.surname = surname;
            this.email = email;

            // Register this classroom as an observable to the API
            this.addObserver(API.getInstance());
        }


        // Getter and Setter for id
        public int getId() {
            return user_id;
        }
        private void setId(int id) {
            this.user_id = id;
        }

        // Getter and Setter for name
        public String getName() {
            return name;
        }
        public void setName(String name) {
            String oldName = this.name;
            this.name = name;

            if (oldName != null && !oldName.equals(name)) {
                // Notify observers about the change
                setChanged();
                notifyObservers();
            }
        }

        // Getter and Setter for surname
        public String getSurname() {
            return surname;
        }
        public void setSurname(String surname) {
            String oldSurname = this.surname;
            this.surname = surname;

            if (oldSurname != null && !oldSurname.equals(surname)) {
                // Notify observers about the change
                setChanged();
                notifyObservers();
            }
        }

        // Getter and Setter for email
        public String getEmail() {
            return email;
        }
        public void setEmail(String email) {
            String oldEmail = this.email;
            this.email = email;

            if (oldEmail != null && !oldEmail.equals(email)) {
                // Notify observers about the change
                setChanged();
                notifyObservers();
            }
        }
    }

