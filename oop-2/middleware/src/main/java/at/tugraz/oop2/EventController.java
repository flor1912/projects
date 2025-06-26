package at.tugraz.oop2;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.geojson.GeoJsonReader;
import org.locationtech.jts.io.geojson.GeoJsonWriter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;


@RestController
public class EventController {
    @GetMapping("/roads")
    public ResponseEntity<EventRepository> entries(@RequestParam(name = "road", required = false, defaultValue = "Not Specified") String amenity_name,
                                  @RequestParam(name = "bbox.tl.x", required = true) Double tl_x,
                                  @RequestParam(name = "bbox.tl.y", required = true) Double tl_y,
                                  @RequestParam(name = "bbox.br.x", required = true) Double br_x,
                                  @RequestParam(name = "bbox.br.y", required = true) Double br_y,
                                  @RequestParam(name = "take", required = false, defaultValue = "50") String take,
                                  @RequestParam(name = "skip", required = false, defaultValue = "0") String skip)
            throws NotFoundException, InvalidRequestException, JsonProcessingException {
        MapLogger.backendLogRoadsRequest();
        if(tl_x == null || tl_y == null || br_x == null && br_y == null)
        {
            throw new InvalidRequestException();
        }
        if (tl_x < -180 || tl_x > 180 || tl_y < -90 || tl_y > 90) {
            throw new InvalidRequestException();
        }
        if (br_x < -180 || br_x > 180 || br_y < -90 || br_y > 90) {
            throw new InvalidRequestException();
        }
        if (br_x < tl_x || br_y > tl_y) {
            throw new InvalidRequestException();
        }
        Events all_roads;
        String result;
        List<String> results = new ArrayList<>();
        all_roads = gRPCClient.getRoads(amenity_name, tl_x, tl_y, br_x, br_y);
        for(Event road : all_roads.getEventsList())
        {
            Road response = new Road(road);
            GeoJsonWriter writer = new GeoJsonWriter(6);
            RoadGeoJson geo_road = new RoadGeoJson(response.getId(), response.getName(), writer.write(response.getGeom()), response.getTags(), response.getChild_ids());
            ObjectMapper objectMapper = new ObjectMapper();
            result = objectMapper.writeValueAsString(geo_road);
            results.add(result);
        }
        results = sortList(results);
        Paging paging = new Paging(take, results.size(), skip);
        EventRepository rep = new EventRepository(results, paging, Integer.parseInt(take), Integer.parseInt(skip));

        return ResponseEntity.ok(rep);
    }

    @GetMapping("/amenities")
    public ResponseEntity<EventRepository> entries(@RequestParam(name = "amenity", required = false, defaultValue = "Not Specified") String amenity_name,
                                     @RequestParam(name = "bbox.tl.x", required = false) Double tl_x,
                                     @RequestParam(name = "bbox.tl.y", required = false) Double tl_y,
                                     @RequestParam(name = "bbox.br.x", required = false) Double br_x,
                                     @RequestParam(name = "bbox.br.y", required = false) Double br_y,
                                     @RequestParam(name = "point.x", required = false) Double point_x,
                                     @RequestParam(name = "point.y", required = false) Double point_y,
                                     @RequestParam(name = "point.d", required = false) Double point_d,
                                     @RequestParam(name = "take", required = false, defaultValue = "50") String take,
                                     @RequestParam(name = "skip", required = false, defaultValue = "0") String skip)
            throws NotFoundException, InvalidRequestException, JsonProcessingException {
        MapLogger.backendLogAmenitiesRequest();
        if(tl_x == null && tl_y == null && br_x == null && br_y == null && point_x == null && point_y == null && point_d == null)
        {
            throw new InvalidRequestException();
        }
        if((tl_x == null && tl_y != null) || (tl_x != null && tl_y == null))
        {
            throw new InvalidRequestException();
        }
        if(tl_x != null && tl_y != null)
        {
            if(tl_x < -180 || tl_x > 180 || tl_y < -90 || tl_y > 90)
            {
                throw new InvalidRequestException();
            }
            if((br_x == null && br_y != null) || (br_x != null && br_y == null))
            {
                throw new InvalidRequestException();
            }
            if(br_x != null && br_y != null)
            {
                if(br_x < -180 || br_x > 180 || br_y < -90 || br_y > 90)
                {
                    throw new InvalidRequestException();
                }
                if(br_x < tl_x || br_y > tl_y)
                {
                    throw new InvalidRequestException();
                }
            }

        }
        if(point_x != null || point_y != null || point_d != null)
        {
            if(point_x != null && point_y != null && point_d != null)
            {
                if(point_d < 0 || point_x < -180 || point_x > 180|| point_y < -90 || point_y > 90)
                {
                    throw new InvalidRequestException();
                }
            }
            else
            {
                throw new InvalidRequestException();
            }
        }

        if(point_x == null) {
            point_x = 0.0;
            point_y = 0.0;
            point_d = 0.0;
        }
        if(br_x == null || br_y == null || tl_x == null || tl_y == null) {
            br_x = 0.0;
            br_y = 0.0;
            tl_x = 0.0;
            tl_y = 0.0;
        }

        Events all_amenities;
        String result;
        List<String> results = new ArrayList<>();
        all_amenities = gRPCClient.getAmenities(amenity_name, tl_x, tl_y, br_x, br_y, point_x, point_y, point_d);
        for(Event amenity : all_amenities.getEventsList())
        {
            try{
                Amenity response = new Amenity(amenity);
                GeoJsonWriter writer = new GeoJsonWriter(6);
                AmenityGeoJson geo_road = new AmenityGeoJson(response.getId(), response.getName(), writer.write(response.getGeom()), response.getTags());
                ObjectMapper objectMapper = new ObjectMapper();
                result = objectMapper.writeValueAsString(geo_road);
                results.add(result);
            } catch (Exception e) {
                System.out.println("Generic Error");
            }
        }
        results = sortList(results);
        Paging paging = new Paging(take, results.size(), skip);
        EventRepository rep = new EventRepository(results, paging, Integer.parseInt(take), Integer.parseInt(skip));

        return ResponseEntity.ok(rep);
    }


    @GetMapping("/roads/{id}")
    public ResponseEntity<String> eventById(@PathVariable Long id) throws NotFoundException, InvalidRequestException,
            JsonProcessingException {
    MapLogger.backendLogRoadRequest((int) Long.parseLong(id.toString()));
    if (id == 0) {
        throw new InvalidRequestException();
    }
    Event road = gRPCClient.getEventbyIdReqeust(id);
    Road response = new Road(road);
    GeoJsonWriter writer = new GeoJsonWriter(6);
    RoadGeoJson geo_road = new RoadGeoJson(response.getId(), response.getName(), writer.write(response.getGeom()), response.getTags(), response.getChild_ids());
    ObjectMapper objectMapper = new ObjectMapper();
    String result = objectMapper.writeValueAsString(geo_road);
    return ResponseEntity.ok(result);
    }

    @GetMapping("/amenities/{id}")
    public ResponseEntity<String> amenityById(@PathVariable Long id) throws NotFoundException, InvalidRequestException,
            JsonProcessingException {
        MapLogger.backendLogAmenityRequest((int) Long.parseLong(id.toString()));
        if (id == 0) {
            throw new InvalidRequestException();
        }
        Event amenity;
        amenity = gRPCClient.getEventbyIdReqeust(id);
        Amenity response = new Amenity(amenity);
        GeoJsonWriter writer = new GeoJsonWriter(6);

        AmenityGeoJson geo_road = new AmenityGeoJson(response.getId(), response.getName(), writer.write(response.getGeom()), response.getTags());
        ObjectMapper objectMapper = new ObjectMapper();
        String result = objectMapper.writeValueAsString(geo_road);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/usage")
    public ResponseEntity<String> entries(@RequestParam(name = "bbox.tl.x", required = true) Double tl_x,
                                                   @RequestParam(name = "bbox.tl.y", required = true) Double tl_y,
                                                   @RequestParam(name = "bbox.br.x", required = true) Double br_x,
                                                   @RequestParam(name = "bbox.br.y", required = true) Double br_y)
            throws NotFoundException, InvalidRequestException, JsonProcessingException {
        if(tl_x == null || tl_y == null || br_x == null && br_y == null)
        {
            throw new InvalidRequestException();
        }
        if (tl_x < -180 || tl_x > 180 || tl_y < -90 || tl_y > 90) {
            throw new InvalidRequestException();
        }
        if (br_x < -180 || br_x > 180 || br_y < -90 || br_y > 90) {
            throw new InvalidRequestException();
        }
        if (br_x < tl_x || br_y > tl_y) {
            throw new InvalidRequestException();
        }
        LanduseResponse landuse;
        List<LanduseJson> landuse_json = new ArrayList<>();
        landuse = gRPCClient.getLanduse(tl_x, tl_y, br_x, br_y);
        ObjectMapper objectMapper = new ObjectMapper();
        for(Landuse land : landuse.getUsagesList())
        {
            landuse_json.add(new LanduseJson(land));
        }
        landuse_json.sort(Comparator.comparingDouble(LanduseJson::getArea));
        LanduseRepository response = new LanduseRepository(landuse.getArea(), landuse_json);
        return ResponseEntity.ok(objectMapper.writeValueAsString(response));
    }

    public List<String> sortList (List<String> list){
        Map<Long, String> map = new HashMap<>();
        List<String> sorted = new ArrayList<>();

        for(String string : list){
            int idStart = string.indexOf("id=") + 3;         // Add 3 to skip "id="
            int idEnd = string.indexOf(",", idStart);
            String id = (string.substring(idStart + 4, idEnd));
            try {
                map.put(Long.parseLong(id), string);
            }
            catch (NumberFormatException e) {
                System.out.println(string);
            }
        }
        TreeMap<Long, String> tree_map = new TreeMap<>(map);
        for(Long key : tree_map.keySet()){
            String value = tree_map.get(key);
            sorted.add(value);
        }
        return List.copyOf(sorted);
    }
}

