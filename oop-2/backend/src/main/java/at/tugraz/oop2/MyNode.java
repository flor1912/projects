package at.tugraz.oop2;

import lombok.Data;
import org.locationtech.jts.geom.Coordinate;

import java.util.HashMap;

@Data
public class MyNode {
    private String id;
    private Double lat;
    private Double lon;
    private Coordinate coordinate;
    private HashMap<String, String> tags;

    public MyNode(String id, Double lat, Double lon, Coordinate coordinate, HashMap<String, String> tags) {
        this.id = id;
        this.lat = lat;
        this.lon = lon;
        this.coordinate = coordinate;
        this.tags = tags;
    }
}
