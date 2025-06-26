package at.tugraz.oop2;

import lombok.Data;

import java.util.HashMap;

@Data
public class Members {
    public String type;
    public Way ref;
    public String role;

    public Members(String type, Way ref, String role) {
        this.type = type;
        this.ref = ref;
        this.role = role;
    }
}
