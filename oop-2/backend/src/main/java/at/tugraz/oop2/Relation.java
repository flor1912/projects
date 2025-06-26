package at.tugraz.oop2;

import lombok.Data;
import org.locationtech.jts.geom.Geometry;

import java.util.HashMap;
import java.util.List;

@Data
public class Relation {
    private String id;
    private List<Members> members;
    public Geometry geometry;
    private HashMap<String, String> tags;

    public Relation(String id, List<Members> members, Geometry geometry, HashMap<String, String> tags) {
        this.id = id;
        this.members = members;
        this.geometry = geometry;
        this.tags = tags;
    }

}
