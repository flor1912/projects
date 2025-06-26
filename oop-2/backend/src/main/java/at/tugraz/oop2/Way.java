package at.tugraz.oop2;

import lombok.Data;
import org.locationtech.jts.geom.Geometry;

import java.util.HashMap;
import java.util.List;

@Data
public class Way {
    private String id;
    private List<Long> refs;
    public Geometry geometry;
    private HashMap<String, String> tags;

    public Way(String id, List<Long> refs, HashMap<String, String> tags, Geometry geometry) {
        this.id = id;
        this.refs = refs;
        this.tags = tags;
        this.geometry = geometry;
    }
}
