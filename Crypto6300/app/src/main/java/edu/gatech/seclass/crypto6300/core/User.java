package edu.gatech.seclass.crypto6300.core;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class User {

    private String username;
    private String emailAddress;

    @SerializedName("type")
    private String typeName;

    public User() {
    }

    public User(String username, String emailAddress) {
        this.typeName = getClass().getName();
        this.username = username;
        this.emailAddress = emailAddress;
    }

    public String getUsername() {
        return username;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return username.equals(user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}
