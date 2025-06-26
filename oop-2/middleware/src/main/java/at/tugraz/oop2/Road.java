package at.tugraz.oop2;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.io.geojson.GeoJsonWriter;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Data
public class Road {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("geom")
    private Geometry geom; // GeoJSON object for geometry

    @JsonProperty("tags")
    private Map<String, String> tags;

    @JsonProperty("type")
    private String type;

    @JsonProperty("child_ids")
    private List<Long> child_ids;

    // Static method to get the road map
    @Getter
    private static final Map<Long, Road> roadMap = new HashMap<>();

    // Constructor
    public Road(Long id, String name, Geometry geometry, Map<String, String> tags, List<Long> childIds) {
        this.id = id;
        this.name = name;
        this.geom = geometry;
        this.tags = tags;
        this.type = tags.getOrDefault("highway", "unknown");
        this.child_ids = childIds;
    }

    public Road() {

    }

    // Convert Geometry to GeoJSON
    private String convertToGeoJSON(Geometry geometry) {
        GeoJsonWriter writer = new GeoJsonWriter();
        return writer.write(geometry);
    }

    public Road (Event ev) {
        if(ev.getEvent().isEmpty()) {
            throw new NotFoundException();
        }
        if(ev.getEvent().contains("highway"))
        {
            // convert to UTF-8
            String string = ev.getEvent();
            String event;
            byte[] b = string.getBytes(StandardCharsets.UTF_8); // taken from https://stackoverflow.com/questions/49536891/string-encoding-utf-8-java
            event = new String(b, StandardCharsets.UTF_8);      // I nearly had a stroke trying to convert the ü from Süd Autobahn

            int idStart = event.indexOf("id=") + 3;         // Add 3 to skip "id="
            int idEnd = event.indexOf(",", idStart);
            int idNext = 0;
            int count = 0;
            String new_line;
            Long id = Long.parseLong(event.substring(idStart, idEnd));

            idStart = event.indexOf("tags=") + 6;           // start after the {
            idEnd = event.indexOf("}", idStart);       // until the end -> }
            String tags = event.substring(idStart, idEnd);
            Map<String, String> tags_hashmap = new HashMap<>();
            String key, value;
            // checks if tags exist
            if(tags.length() > 3) {
                while(true) {
                    idStart = tags.indexOf("=");            // scan until '=' to find something of form key=value
                    idEnd = tags.indexOf(",", idStart);
                    idNext = tags.indexOf("=", idStart + 1);

                    // this entire block checks if there are more than one ',' in between two keys
                    if(idNext != -1) {
                        new_line = tags.substring(0, idNext);
                        count = new_line.length() - new_line.replace(",", "").length();
                        if(count > 1) {
                            idEnd = new_line.lastIndexOf(",");
                        }
                    }
                    if(idNext == -1 && tags.length() - tags.replace(",", "").length() != -1)
                    {
                        idEnd = tags.indexOf("}");
                    }

                    // last element
                    if(idEnd == -1) {
                        idEnd = tags.length();
                        key = tags.substring(0, idStart);
                        value = tags.substring(idStart + 1, idEnd);
                        tags_hashmap.put(key, value);
                        break;
                    }
                    // adding the keys-value pairs for each tag
                    key = tags.substring(0, idStart);
                    value = tags.substring(idStart + 1, idEnd);
                    tags = tags.substring(idEnd + 2); // start again after ", "
                    tags_hashmap.put(key, value);
                }
            }


            // same thing for refs, slightly different
            String refs_string = "";
            idStart = event.indexOf("refs=") + 6;
            idEnd = event.indexOf("]", idStart);
            if(idStart != 5) {   // the 5 follows from idStart being at least 6 in length and .indexOf returning -1 for not found
                refs_string = event.substring(idStart, idEnd);
            }
            List<Long> childIds = new ArrayList<>();
            if(refs_string.length() > 3) {                         // checks if refs exist
                while(true) {
                    idEnd = refs_string.indexOf(",");
                    if(idEnd == -1) {
                        idEnd = refs_string.length();
                        childIds.add(Long.parseLong(refs_string.substring(0, idEnd)));
                        break;
                    }
                    childIds.add(Long.parseLong(refs_string.substring(0, idEnd)));
                    refs_string = refs_string.substring(idEnd + 2);
                }
            }

            GeometryFactory geometryFactory = new GeometryFactory();
            idStart = event.indexOf("geometry=") + 10;
            Geometry geometry = null;

            int lat_flag, long_flag, end1, end2 = 0;
            String lati, longi;
            lat_flag = event.indexOf("lat=");
            long_flag = event.indexOf("lon=");
            if(lat_flag != -1 && long_flag != -1) {
                end1 = event.indexOf(",", lat_flag);
                end2 = event.indexOf(",", long_flag);
                lati = event.substring(lat_flag + 4, end1);
                longi = event.substring(long_flag + 4, end2);
                Coordinate cord = new Coordinate(Double.parseDouble(longi), Double.parseDouble(lati));
                List<Coordinate> coordinates = new ArrayList<>();
                coordinates.add(cord);

                geometry = geometryFactory.createPoint(coordinates.get(0));
            }

            if(idStart != 9 && !event.contains("MULTIPOLYGON")) {
                idStart = event.indexOf("(", idStart) + 1;
                if(string.contains("POLYGON")) {
                    idStart = idStart + 1;
                }
                idEnd = event.indexOf(")", idStart);
                int idMiddle = 0;
                Coordinate cord;
                double latitude, longitude;
                List<Coordinate> coordinates = new ArrayList<>();
                String geom_string = event.substring(idStart, idEnd);
                if(geom_string.length() > 3) {                         // checks if refs exist
                    while(true) {
                        idEnd = geom_string.indexOf(",");
                        idMiddle = geom_string.indexOf(" ");
                        if(idEnd == -1) {
                            idEnd = geom_string.length();
                            idMiddle = geom_string.indexOf(" ");
                            latitude = Double.parseDouble(geom_string.substring(0, idMiddle));
                            longitude = Double.parseDouble(geom_string.substring(idMiddle + 1, idEnd));
                            cord = new Coordinate(latitude, longitude);
                            coordinates.add(cord);
                            break;
                        }
                        latitude = Double.parseDouble(geom_string.substring(0, idMiddle));
                        longitude = Double.parseDouble(geom_string.substring(idMiddle + 1, idEnd));
                        cord = new Coordinate(latitude, longitude);
                        coordinates.add(cord);
                        geom_string = geom_string.substring(idEnd + 2);
                    }
                }
                if((coordinates.size() >= 2) && coordinates.get(0).equals(coordinates.get(coordinates.size() - 1))){
                    geometry = geometryFactory.createPolygon(coordinates.toArray(new Coordinate[0]));
                }
                else if(coordinates.size() >= 2){
                    // LineString
                    geometry = geometryFactory.createLineString(coordinates.toArray(new Coordinate[0]));
                }
                else{
                    geometry = geometryFactory.createPoint(coordinates.get(0));
                }
            }

            if(event.contains("MULTIPOLYGON")){
                idStart = event.indexOf("MULTIPOLYGON") + 16;
                idEnd = event.indexOf(")", idStart);
                String geom_string = event.substring(idStart, idEnd);
                int total_end = event.indexOf("))))", idStart);

                String geom_string_total = event.substring(idStart, total_end + 4);

                List<Polygon> polygons = new ArrayList<>();

                while(true) {
                    Coordinate cord;
                    double latitude, longitude;
                    int idMiddle = 0;
                    List<Coordinate> coordinates = new ArrayList<>();

                    if(geom_string.length() > 3) {    // checks if refs exist
                        while(true) {
                            idEnd = geom_string.indexOf(",");
                            idMiddle = geom_string.indexOf(" ");
                            if(idEnd == -1) {
                                idEnd = geom_string.length();
                                idMiddle = geom_string.indexOf(" ");
                                latitude = Double.parseDouble(geom_string.substring(0, idMiddle));
                                longitude = Double.parseDouble(geom_string.substring(idMiddle + 1, idEnd));
                                cord = new Coordinate(latitude, longitude);
                                coordinates.add(cord);
                                break;
                            }
                            latitude = Double.parseDouble(geom_string.substring(0, idMiddle));
                            longitude = Double.parseDouble(geom_string.substring(idMiddle + 1, idEnd));
                            cord = new Coordinate(latitude, longitude);
                            coordinates.add(cord);
                            geom_string = geom_string.substring(idEnd + 2);
                        }
                    }
                    Polygon geom = geometryFactory.createPolygon(coordinates.toArray(new Coordinate[0]));
                    polygons.add(geom);
                    idEnd = geom_string_total.indexOf("))");
                    geom_string_total = geom_string_total.substring(idEnd + 2);
                    idStart = geom_string_total.indexOf("((") + 2;
                    idEnd = geom_string_total.indexOf("))");
                    int end_of_geom = geom_string_total.indexOf("))))");
                    if(end_of_geom == -1){
                        int tagsStart = event.indexOf("tags=", total_end) + 6;
                        int tagsEnd = event.indexOf("}", total_end);
                        String tagsString = event.substring(tagsStart, tagsEnd);
                        tags_hashmap = parseTags(tagsString);
                        MultiPolygon multiPolygon = geometryFactory.createMultiPolygon(polygons.toArray(new Polygon[0]));
                        GeometryCollection geometryCollection = new GeometryCollection(new MultiPolygon[]{multiPolygon}, geometryFactory);
                        geometry = geometryCollection;
                        break;
                    }
                    geom_string = geom_string_total.substring(idStart, idEnd);
                }

                refs_string = "";
                idStart = event.indexOf("Members") + 8;
                idEnd = event.indexOf("GEOMETRYCOLLECTION", idStart);
                refs_string = event.substring(idStart, idEnd);
                childIds.clear();
                if(refs_string.length() > 3) {                         // checks if refs exist
                    while(true) {
                        idEnd = refs_string.indexOf("Members(") + 9;
                        idStart = refs_string.indexOf("id=") + 3;
                        int End2 = refs_string.indexOf(",", idStart);
                        if(idEnd == 8) {
                            childIds.add(Long.parseLong(refs_string.substring(idStart, End2)));
                            break;
                        }
                        childIds.add(Long.parseLong(refs_string.substring(idStart, End2)));
                        refs_string = refs_string.substring(idEnd);
                    }
                }
            }

            this.id = id;
            if(geometry != null) {
                this.geom = geometry;
            }
            this.tags = tags_hashmap;
            this.child_ids = childIds;
            this.name = tags_hashmap.get("name");
            if(tags_hashmap.get("name") == null) {
                this.name = "";
            }
            this.type = tags_hashmap.get("highway");

            // Check for "highway" tag and add to map if it's a road
            if (this.tags.containsKey("highway")) {
                roadMap.put(this.id, this);
            }
        }
        else {
            throw new NotFoundException();
        }
    }

    private Map<String, String> parseTags(String tagsString) {
        Map<String, String> tags = new HashMap<>();
        while (!tagsString.isEmpty()) {
            int keyEnd = tagsString.indexOf("=");
            int valueEnd = tagsString.indexOf(",", keyEnd);
            if (valueEnd == -1) valueEnd = tagsString.length();

            String key = tagsString.substring(0, keyEnd).trim();
            String value = tagsString.substring(keyEnd + 1, valueEnd).trim();

            tags.put(key, value);
            tagsString = valueEnd == tagsString.length() ? "" : tagsString.substring(valueEnd + 2); // Skip ", "
        }
        return tags;
    }
}
