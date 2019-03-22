package models;

import java.util.Locale;

public class User {
    private String id;
    private String email;
    private String name;
    private String contact;

    public User() {

    }

    public User(String email, String name, String contact) {
        this.email = email;
        this.name = name;
        this.contact = contact;

    }

    public User(String id, String email, String name, String contact) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.contact = contact;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "%s\n%s\n%s",
                this.name,
                this.email,
                this.contact);
    }
}
