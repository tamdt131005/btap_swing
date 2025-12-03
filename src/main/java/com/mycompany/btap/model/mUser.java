package com.mycompany.btap.model;
public class mUser {

    private final int id;
    private final String username;
    private final String email;
    private final String passwordHash;
    private final String salt;

    public mUser(int id, String username, String email, String passwordHash, String salt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.salt = salt;
    }

    // convenience constructor for cases where password is not included
    public mUser(int id, String username, String email) {
        this(id, username, email, null, null);
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getSalt() {
        return salt;
    }
    
    @Override
    public String toString() {
        return "mUser{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
