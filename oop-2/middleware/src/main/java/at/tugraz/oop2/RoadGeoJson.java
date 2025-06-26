package at.tugraz.oop2;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;
import lombok.Data;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.geojson.GeoJsonWriter;
import org.locationtech.jts.geom.GeometryFactory;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Data
public class RoadGeoJson {
    private Long id;
    private String name;
    @JsonRawValue
    private String geom; // GeoJSON object for geometry
    private Map<String, String> tags;
    private String type;
    private List<Long> child_ids;

    public RoadGeoJson(Long id, String name, String geometry, Map<String, String> tags, List<Long> childIds) {
        this.id = id;
        this.name = name;
        this.geom = geometry;
        this.tags = tags;
        this.type = tags.getOrDefault("highway", "unknown");
        this.child_ids = childIds;
    }
}


