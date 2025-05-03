package com.sen3006.coursewise.client.json;

import com.sen3006.coursewise.client.models.User;

public class UserPassword extends User {
    String password;

    public UserPassword(int id, String name, String surname, String email, String password) {
        super(id, name, surname, email);
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}
