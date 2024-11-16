package telehealth.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class User {
    private String name;
    private String email;
    private String role;
    private String password;

    @JsonCreator
    public User(@JsonProperty("name") String name, @JsonProperty("email") String email, @JsonProperty("role") String role, @JsonProperty("password") String password) {
        this.name = name;
        this.email = email;
        this.role = role;
        this.password = password;
    }

    public User() {
        // Default constructor for Jackson
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
