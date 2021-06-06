package org.meicode.appfilm.Models;

public class User {
    private long id;
    private String name;
    private String email;

    public User(long id, String username, String email) {
        this.id = id;
        this.name = username;
        this.email = email;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String username) {
        this.name = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
