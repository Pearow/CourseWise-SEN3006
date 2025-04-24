package com.sen3006.coursewise.models;

    public class User {
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
        }


        // Getter and Setter for id
        public int getId() {
            return user_id;
        }
        public void setId(int id) {
            this.user_id = id;
        }

        // Getter and Setter for name
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }

        // Getter and Setter for surname
        public String getSurname() {
            return surname;
        }
        public void setSurname(String surname) {
            this.surname = surname;
        }

        // Getter and Setter for email
        public String getEmail() {
            return email;
        }
        public void setEmail(String email) {
            this.email = email;
        }
    }

