package telehealth.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Doctor extends User {

    @JsonCreator
    public Doctor(@JsonProperty("name") String name, @JsonProperty("email") String email, @JsonProperty("password") String password) {
        super(name, email, "Doctor", password);
    }

    public Doctor() {
        // Default constructor for Jackson
        super();
        this.setRole("Doctor");
    }

    // Additional fields and methods for doctors
}