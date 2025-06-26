package at.tugraz.oop2;

import com.google.protobuf.Empty;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import at.tugraz.oop2.EventServiceGrpc;
import lombok.Data;
import org.geotools.geometry.GeometryBuilder;
import org.geotools.geometry.jts.JTS;
import org.locationtech.jts.geom.*;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.geotools.referencing.CRS;
import org.opengis.referencing.operation.TransformException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.sql.Array;
import java.util.*;
import java.util.logging.Logger;
import java.util.HashMap;
import java.util.Map;

@Data
public class BackendServer {
    private static final Logger logger = Logger.getLogger(BackendServer.class.getName());
    public Server server;

    public BackendServer(int port)
    {
        this.server = ServerBuilder.forPort(port)
                .addService(new EventServiceImpl())
                .build();
        try
        {
            server.start();
            logger.info(String.format("Server has started on port %d", port));

        }
        catch (Exception e)
        {
            throw new InternalError();
        }
    }
}


class EventServiceImpl extends EventServiceGrpc.EventServiceImplBase {
    private long id = 1;
    public LinkedList<Event> events;
    public static Map<String, MyNode> nodes_list;
    public static Map<String, Way> ways_list;
    public static List<Relation> relations;



    public EventServiceImpl ()  {
        events = MapServiceServer.events;
        nodes_list = MapServiceServer.nodes_list;
        ways_list = MapServiceServer.ways_list;
        relations = MapServiceServer.relations;
    }

    @Override
    public void eventById(EventById request, StreamObserver<Event> responseObserver)
    {

        for (Event event : events) {
            int idStart = event.getEvent().indexOf("id=") + 3; // Add 3 to skip "id="
            int idEnd = event.getEvent().indexOf(",", idStart);
            String id = event.getEvent().substring(idStart, idEnd);
            if (Long.parseLong(id) == request.getId()) {
                responseObserver.onNext(event);
                responseObserver.onCompleted();
                return;
            }
        }
        responseObserver.onNext(null);
        responseObserver.onCompleted();
    }
    @Override
    public void eventCreation(Event event, StreamObserver<Event> responseObserver)
    {
        events.add(event);
    }

    @Override
    public void getRoads(RoadRequest request, StreamObserver<Events> responseObserver)
    {
        List<Event> valids = new ArrayList<>();
        String name = request.getRoad();
        String current_name;
        double bbox_tl_x = request.getBboxTlX();
        double bbox_tl_y = request.getBboxTlY();
        double bbox_br_x = request.getBboxBrX();
        double bbox_br_y = request.getBboxBrY();

        Coordinate[] coordinates_list = new Coordinate[] {
                new Coordinate(bbox_tl_x, bbox_tl_y),
                new Coordinate(bbox_tl_x, bbox_br_y),
                new Coordinate(bbox_br_x, bbox_br_y),
                new Coordinate(bbox_br_x, bbox_tl_y),
                new Coordinate(bbox_tl_x, bbox_tl_y)
        };
        GeometryFactory geom = new GeometryFactory();
        Polygon bounding_box = geom.createPolygon(coordinates_list);
        System.out.println(bounding_box);

        for (MyNode node : nodes_list.values()) {
            if(node.getTags().containsKey("highway")) {
                current_name = node.getTags().get("highway");
                if(!Objects.equals(current_name, name) && !Objects.equals(name, "Not Specified")) {
                    continue;
                }
                Point point = geom.createPoint(new Coordinate(node.getLon(), node.getLat()));
                if(bounding_box.intersects(point)) {
                    valids.add(Event.newBuilder().setEvent(node.toString()).build());
                }
            }
        }

        for (Way way : ways_list.values()) {
            if(way.getTags().containsKey("highway")) {
                current_name = way.getTags().get("highway");
                if(!Objects.equals(current_name, name) && !Objects.equals(name, "Not Specified")) {
                    continue;
                }
                if (bounding_box.intersects(way.getGeometry())) {
                    valids.add(Event.newBuilder().setEvent(way.toString()).build());
                }
            }
        }

        for (Relation relation : relations) {
            if(relation.getTags().containsKey("highway")) {
                current_name = relation.getTags().get("highway");
                if(!Objects.equals(current_name, name) && !Objects.equals(name, "Not Specified")) {
                    continue;
                }
                if(bounding_box.intersects(relation.getGeometry())) {
                    valids.add(Event.newBuilder().setEvent(relation.toString()).build());
                }
            }
        }

        Events response = Events.newBuilder().addAllEvents(valids).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getAmenities(AmenitiesRequest request, StreamObserver<Events> responseObserver)
    {
        List<Event> valids = new ArrayList<>();
        String name = request.getAmenity();
        String current_name;
        double bbox_tl_x = request.getBboxTlX();
        double bbox_tl_y = request.getBboxTlY();
        double bbox_br_x = request.getBboxBrX();
        double bbox_br_y = request.getBboxBrY();
        double point_x = request.getPointX();
        double point_y = request.getPointY();
        double point_d = request.getPointD();
        boolean box_method = true;
        Coordinate center = new Coordinate (point_x, point_y);
        GeometryFactory geometryFactory = new GeometryFactory();
        Geometry target_point = geometryFactory.createPoint(center);

        Coordinate[] coordinates_list = new Coordinate[] {
                new Coordinate(bbox_tl_x, bbox_tl_y),
                new Coordinate(bbox_tl_x, bbox_br_y),
                new Coordinate(bbox_br_x, bbox_br_y),
                new Coordinate(bbox_br_x, bbox_tl_y),
                new Coordinate(bbox_tl_x, bbox_tl_y)
        };
        GeometryFactory geom = new GeometryFactory();
        Polygon bounding_box = geom.createPolygon(coordinates_list);

        if(bbox_tl_x == 0.0) {
            box_method = false;
        }

        MathTransform transform = null;
        try{
            CoordinateReferenceSystem sourceCRS = CRS.decode("EPSG:4326", true);
            CoordinateReferenceSystem targetCRS = CRS.decode("EPSG:31256");
            transform = CRS.findMathTransform(sourceCRS, targetCRS, true);
            target_point = JTS.transform(target_point, transform);
            }
        catch(Exception e){
            System.out.println("Couldn't transform");
        }

        for (MyNode node : nodes_list.values()) {
            if(node.getTags().containsKey("amenity")) {
                current_name = node.getTags().get("amenity");
                if(!Objects.equals(current_name, name) && !Objects.equals(name, "Not Specified")) {
                    continue;
                }
                if(box_method) {
                    Point point = geom.createPoint(new Coordinate(node.getLon(), node.getLat()));
                    if(bounding_box.intersects(point)) {
                        valids.add(Event.newBuilder().setEvent(node.toString()).build());
                    }
                }
                else{
                    Geometry geo = null;
                    try {
                        geo = JTS.transform(geometryFactory.createPoint(node.getCoordinate()), transform);
                    } catch (TransformException e) {
                        throw new RuntimeException(e);
                    }
                    if (geo.isWithinDistance(target_point, point_d)) {
                        valids.add(Event.newBuilder().setEvent(node.toString()).build());
                    }
                }
            }
        }

        for (Way way : ways_list.values()) {
            if (way.getTags().containsKey("amenity")) {
                current_name = way.getTags().get("amenity");
                if (!Objects.equals(current_name, name) && !Objects.equals(name, "Not Specified")) {
                    continue;
                }
                if (box_method) {
                   if(bounding_box.intersects(way.geometry)) {
                       valids.add(Event.newBuilder().setEvent(way.toString()).build());
                   }
                }
                else {
                    Geometry geo = way.getGeometry();
                    try {
                        geo = JTS.transform(geo, transform);
                    } catch (TransformException e) {
                        throw new RuntimeException(e);
                    }
                    if (geo.isWithinDistance(target_point, point_d)) {
                        valids.add(Event.newBuilder().setEvent(way.toString()).build());
                    }
                }
            }
        }

        for (Relation relation : relations) {
            if(relation.getTags().containsKey("amenity")) {
                current_name = relation.getTags().get("amenity");
                if(!Objects.equals(current_name, name) && !Objects.equals(name, "Not Specified")) {
                    continue;
                }
                if(box_method) {
                    if(bounding_box.intersects(relation.getGeometry())) {
                        valids.add(Event.newBuilder().setEvent(relation.toString()).build());
                    }
                }
                else {
                    Geometry geo = relation.getGeometry();
                    try {
                        geo = JTS.transform(geo, transform);
                    } catch (TransformException e) {
                        throw new RuntimeException(e);
                    }
                    if (geo.isWithinDistance(target_point, point_d)) {
                        valids.add(Event.newBuilder().setEvent(relation.toString()).build());
                    }
                }
            }
        }
        Events response = Events.newBuilder().addAllEvents(valids).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getLanduse(LanduseRequest request, StreamObserver<LanduseResponse> responseObserver) {
        double bbox_tl_x = request.getBboxTlX();
        double bbox_tl_y = request.getBboxTlY();
        double bbox_br_x = request.getBboxBrX();
        double bbox_br_y = request.getBboxBrY();

        MathTransform transform = null;
        try {
            CoordinateReferenceSystem sourceCRS = CRS.decode("EPSG:4326", true);
            CoordinateReferenceSystem targetCRS = CRS.decode("EPSG:31256");
            transform = CRS.findMathTransform(sourceCRS, targetCRS, true);
        } catch (Exception e) {
            System.out.println("Couldn't transform");
        }

        Coordinate top_left = new Coordinate(bbox_tl_x, bbox_tl_y);
        Coordinate bottom_left = new Coordinate(bbox_tl_x, bbox_br_y);
        Coordinate bottom_right = new Coordinate(bbox_br_x, bbox_br_y);
        Coordinate top_right = new Coordinate(bbox_br_x, bbox_tl_y);

        Coordinate[] coordinates_list = new Coordinate[]{
                top_left,
                bottom_left,
                bottom_right,
                top_right,
                top_left
        };

        try {
            top_left = JTS.transform(top_left, null, transform);
            bottom_left = JTS.transform(bottom_left, null, transform);
            bottom_right = JTS.transform(bottom_right, null, transform);
            top_right = JTS.transform(top_right, null, transform);
        } catch (TransformException e) {
            throw new RuntimeException(e);
        }

        HashMap<String, Double> valids = new HashMap<>();

        Coordinate[] coordinates_list_transformed = new Coordinate[]{
                top_left,
                bottom_left,
                bottom_right,
                top_right,
                top_left
        };

        GeometryFactory geom = new GeometryFactory();
        Polygon bounding_box = geom.createPolygon(coordinates_list);
        Polygon bounding_box_transformed = geom.createPolygon(coordinates_list_transformed);
        List<Landuse> landuses = new ArrayList<>();
        double total_area = bounding_box_transformed.getArea();

        for (Way way : ways_list.values()) {
            if(way.getTags().containsKey("landuse")) {
                String current_type = way.getTags().get("landuse");
                if(bounding_box.intersects(way.getGeometry())) {
                    Geometry rel_geom = null;
                    try{
                        rel_geom = JTS.transform(way.getGeometry(), transform);
                    } catch (TransformException e) {
                        throw new RuntimeException(e);
                    }
                    Geometry intersection = bounding_box_transformed.intersection(rel_geom);
                    double area = intersection.getArea();
                    valids.put(current_type, valids.getOrDefault(current_type, 0.0) + area);
                }
            }
        }

        for (Relation relation : relations) {
            if(relation.getTags().containsKey("landuse")) {
                String current_type = relation.getTags().get("landuse");
                if(bounding_box.intersects(relation.getGeometry())) {
                    Geometry rel_geom = null;
                    try{
                        rel_geom = JTS.transform(relation.getGeometry(), transform);
                    } catch (TransformException e) {
                        throw new RuntimeException(e);
                    }
                    Geometry intersection = bounding_box_transformed.intersection(rel_geom);
                    double area = intersection.getArea();
                    if(area != total_area) {
                        valids.put(current_type, valids.getOrDefault(current_type, 0.0) + area);
                    }
                }
            }
        }
        for (Map.Entry<String, Double> entry : valids.entrySet()) {
            String key = entry.getKey();
            Double value = entry.getValue();
            landuses.add(Landuse.newBuilder().setType(key).setArea(value).setShare(value/total_area).build());
        }

        System.out.println(landuses);
        LanduseResponse response = LanduseResponse
                .newBuilder()
                .setArea(total_area)
                .addAllUsages(landuses)
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
        System.out.println(response);
    }
}