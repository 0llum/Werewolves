package com.ollum.werewolves;

import java.security.Timestamp;

public class User {

    String username, password, email, lastOnline;
    int status;

    public User (String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.status = 0;
        this.lastOnline = "";
    }

    public User (String username, String password) {
        this.username = username;
        this.password = password;
        this.email = "";
        this.status = 0;
        this.lastOnline = "";
    }

    public User (String username, int status) {
        this.username = username;
        this.password = "";
        this.email = "";
        this.status = status;
        this.lastOnline = "";
    }

    public User (String username, int status, String lastOnline) {
        this.username = username;
        this.password = "";
        this.email = "";
        this.status = status;
        this.lastOnline = lastOnline;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setLastOnline(String lastOnline) {
        this.lastOnline = lastOnline;
    }

    public String getLastOnline() {
        return lastOnline;
    }
}
