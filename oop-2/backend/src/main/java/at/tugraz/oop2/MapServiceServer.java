package at.tugraz.oop2;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.locationtech.jts.geom.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.logging.Logger;

import at.tugraz.oop2.BackendServer;


public class MapServiceServer {
    private static final Logger logger = Logger.getLogger(MapServiceServer.class.getName());
    public static LinkedList<Event> events;
    public static Map<String, MyNode> nodes_list;
    public static Map<String, Way> ways_list;
    public static List<Relation> relations;

    public static void main(String[] args) throws InterruptedException {

        logger.info("Starting backend...");

        var port = System.getenv().getOrDefault("JMAP_BACKEND_PORT", "8020");
        int portBackend;
        try{
            portBackend = Integer.parseInt(port);
            if((portBackend <=0) || (portBackend >= 65535)){
                portBackend = 8020;
            }
        } catch (NumberFormatException e) {
            portBackend = 8020;
        }

        var osmFile = System.getenv().getOrDefault("JMAP_BACKEND_OSMFILE", "data/styria_reduced.osm");
        if(!new File(osmFile).exists())
        {
            logger.info("No file found");
            return;
        }

        MapLogger.backendStartup(portBackend, osmFile);

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        GeometryFactory geometryFactory = new GeometryFactory();
        Map<String, MyNode> nodes = new HashMap<>();
        Map<String, Way> ways = new HashMap<>();
        List<Relation> relations_list = new ArrayList<>();
        LinkedList<Event> event_list = new LinkedList<>();
        HashMap<String, MyNode> needed_nodes = new HashMap<>();
        HashMap<String, Way> needed_ways = new HashMap<>();
        int crashes = 0;

        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(osmFile);
            document.getDocumentElement().normalize();

            NodeList nodeList = document.getElementsByTagName("node");

            int nodes_length = nodeList.getLength();
            for (int i = 0; i < nodes_length; i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String id = element.getAttribute("id") ;
                    double lat = Double.parseDouble(element.getAttribute("lat")) ;
                    double lon = Double.parseDouble(element.getAttribute("lon"));
                    Coordinate temp = new Coordinate(lon, lat);

                    HashMap<String, String> node_tags = new HashMap<>();
                    NodeList tags = element.getElementsByTagName("tag");
                    int length = tags.getLength();
                    for(int m = 0; m < length; m++){
                        Element tagElement = (Element) tags.item(m);
                        String key = tagElement.getAttribute("k");
                        String value = tagElement.getAttribute("v");
                        node_tags.put(key, value);
                    }
                    MyNode new_node = new MyNode(id, lat, lon, temp, node_tags);
                    nodes.put(id, new_node);
                    event_list.add(Event.newBuilder().setEvent(new_node.toString()).build());
                }
            }
            //------------------Way-----------------------------------
            NodeList wayList = document.getElementsByTagName("way");
            int ways_length = wayList.getLength();
            for (int i = 0; i < ways_length; i++) {
                Node wayNode = wayList.item(i);
                if (wayNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element wayElement = (Element) wayNode;
                    String wayId = wayElement.getAttribute("id");

                    NodeList ndList = wayElement.getElementsByTagName("nd");
                    List<Coordinate> coordinates = new ArrayList<>();
                    List<Long> refs = new ArrayList<>();
                    int nd_length = ndList.getLength();
                    for (int j = 0; j < nd_length; j++) {
                        Element ndElement = (Element) ndList.item(j);
                        String ref = ndElement.getAttribute("ref");
                        MyNode refernceNode = nodes.get(ref);
                        if (refernceNode != null) {
                            coordinates.add(refernceNode.getCoordinate());
                            refs.add(Long.parseLong(refernceNode.getId()));
                            needed_nodes.put(ref, refernceNode);
                        }
                    }
                    Geometry geometry;
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

                    NodeList tagList = wayElement.getElementsByTagName("tag");
                    int tag_length = tagList.getLength();
                    HashMap<String, String > temp = new HashMap<>();
                    for (int k = 0; k < tag_length; k++) {
                        Element tagElement = (Element) tagList.item(k);
                        String key = tagElement.getAttribute("k");
                        String value = tagElement.getAttribute("v");
                        temp.put(key, value);
                    }
                    Way new_way = new Way(wayId, refs, temp, geometry);
                    ways.put(wayId,new_way);
                    event_list.add(Event.newBuilder().setEvent(new_way.toString()).build());
                }
            }
            //---------------------Relation----------------------------------------------\
            NodeList relationList = document.getElementsByTagName("relation");
            int relations_length = relationList.getLength();
            for(int i= 0; i< relations_length; i++){
                Node relationNote = relationList.item(i);
                if(relationNote.getNodeType() == Node.ELEMENT_NODE){
                    Element relationElement = (Element) relationNote;
                    String relation_id = relationElement.getAttribute("id");

                    NodeList memberNode = relationElement.getElementsByTagName("member");
                    int members_length = memberNode.getLength();
                    List<Members> temp = new ArrayList<>();
                    for(int f = 0; f < members_length; f++){
                        Element memElement = (Element) memberNode.item(f);
                        String type = memElement.getAttribute("type");
                        String ref = memElement.getAttribute("ref");
                        Way refway = ways.get(ref);
                        if (refway != null && !needed_ways.containsKey(refway)) {
                            needed_ways.put(ref, refway);
                        }
                        String role = memElement.getAttribute("role");
                        Members member = new Members(type, refway, role);
                        temp.add(member);
                    }

                    NodeList tagNode = relationElement.getElementsByTagName("tag");
                    int tags_length = tagNode.getLength();
                    HashMap<String, String> temp_tags = new HashMap<>();
                    for(int m = 0; m < tags_length; m++){
                        Element tagsElement = (Element) tagNode.item(m);
                        String t_key = tagsElement.getAttribute("k");
                        String v_key = tagsElement.getAttribute("v");
                        temp_tags.put(t_key, v_key);
                    }

                    Relation new_relation = new Relation(relation_id, temp, null, temp_tags);
                    relations_list.add(new_relation);
                    List <Polygon> polys = new ArrayList<>();
                    try{
                        polys.addAll(createOuterPolygons(new_relation.getMembers()));
                    }
                    catch (Exception e){
                        crashes++;
                    }
                    try{
                        polys.addAll(createInnerPolygons(new_relation.getMembers()));
                    } catch (Exception e) {
                        crashes++;
                    }
                    MultiPolygon multiPolygon = geometryFactory.createMultiPolygon(polys.toArray(new Polygon[0]));
                    GeometryCollection geometryCollection = new GeometryCollection(new MultiPolygon[]{multiPolygon}, geometryFactory);
                    new_relation.setGeometry(geometryCollection);
                    event_list.add(Event.newBuilder().setEvent(new_relation.toString()).build());
                }
            }
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
        events = event_list;
        nodes_list = nodes;
        ways_list = ways;
        relations = relations_list;

        BackendServer server = new BackendServer(portBackend);
        MapLogger.backendLoadFinished(nodes.size() - needed_nodes.size() , ways.size() - needed_ways.size(),
                relations_list.size());

        server.getServer().awaitTermination();
    }


    // creates the outer polygons as the name already says,
    // input: members of a relation
    public static List<Polygon> createOuterPolygons(List<Members> members) {
        List<Polygon> pols = new ArrayList<>();
        GeometryFactory geometryFactory = new GeometryFactory();
        ArrayList<Coordinate> cords = new ArrayList<>();
        ArrayList<List<Coordinate>> cords_list = new ArrayList<>();

        // Filter outers
        List<Way> ways = new ArrayList<>();
        for(Members mem : members) {
            if(mem.getRole().equals("outer")) {
                ways.add(mem.getRef());
            }
        }

        // get all ways from the references
        int ways_size = ways.size();
        for(int i = 0; i < ways_size; i++) {

            // add all coordinates of a way to a 'Stream' of coordinated
            cords.addAll(Arrays.asList(ways.get(i).getGeometry().getCoordinates()));

            // add the way to a List of "Ways" where the way is only his coordinates, so the same as the one above but as 2D List
            cords_list.add(Arrays.asList(ways.get(i).getGeometry().getCoordinates()));

            // The HashSets are gonna be used to check if a coordinate set of one or two ways has the same coordinates at some point
            Set<Coordinate> set = new HashSet<>(cords);
//            System.out.println(set.size() + "   before function     " + cords.size());

            // the set only has unique coordinates, so if the way for example is closed the set is one smaller than the total coordinates
            if(set.size() < cords.size()) {
//                System.out.println(cords_list);

                // defined a custom class for easier results
                PolygonResponse temp = PolyCords(cords_list);
//                System.out.println("All cords: " + temp.getCoordinates());
//                System.out.println("Cords left in list: " + temp.getCoordinate_list());

                // if the building attempt was successfull, create the polygon, for now, I'm too tired to bother creating the multipoligon now
                if(temp.isSuccess()) {
                    pols.add(geometryFactory.createPolygon((Coordinate[]) temp.getCoordinates().toArray(new Coordinate[]{})));
                    cords.removeAll(temp.getCoordinates());
                }

                // get the List of Ways from the building function, if the building was successfull, the "used" ways are removed from the list
                cords_list = new ArrayList<>(temp.getCoordinate_list());
//                System.out.println(temp.getCoordinates().size() + "   after removing     " + cords.size());

            }
        }

        return pols;
    }


    // same as outer, can't be bothered right now to combine the two so I just copied the same thing
    public static List<Polygon> createInnerPolygons(List<Members> members) {
        List<Polygon> pols = new ArrayList<>();
        GeometryFactory geometryFactory = new GeometryFactory();
        ArrayList<Coordinate> cords = new ArrayList<>();
        ArrayList<List<Coordinate>> cords_list = new ArrayList<>();

        List<Way> ways = new ArrayList<>();
        for(Members mem : members) {
            if(mem.getRole().equals("inner")) {
                ways.add(mem.getRef());
            }
        }
        int ways_size = ways.size();
        for(int i = 0; i < ways_size; i++) {
            cords.addAll(Arrays.asList(ways.get(i).getGeometry().getCoordinates()));
            cords_list.add(Arrays.asList(ways.get(i).getGeometry().getCoordinates()));
            Set<Coordinate> set = new HashSet<>(cords);
//            System.out.println(set.size() + "   before function     " + cords.size());
            if(set.size() < cords.size()) {
//                System.out.println(cords_list);
                PolygonResponse temp = PolyCords(cords_list);
//                System.out.println("All cords: " + temp.getCoordinates());
//                System.out.println("Cords left in list: " + temp.getCoordinate_list());
                if(temp.isSuccess()) {
                    try {
                        pols.add(geometryFactory.createPolygon((Coordinate[]) temp.getCoordinates().toArray(new Coordinate[]{})));
                        cords.removeAll(temp.getCoordinates());
                    } catch (Exception e) {
                       // System.out.println("Error trying to create Polygon");
                    }
                }
                cords_list = new ArrayList<>(temp.getCoordinate_list());
//                System.out.println(temp.getCoordinates().size() + "   after removing     " + cords.size());

            }
        }
//        System.out.println(pols);
//        System.out.println(pols.size());
        return pols;
    }

    // this is where the magic happens
    // The overall principle is that it throws all the coordinates into a pseudo stream of coordinates, in this case a list
    // and then checks each iteration if we have a closed circle, this is checked by the HashSets
    // If we have fewer unique coordinates than expected, then we could have a polygon, so it then tries to form one.
    // If a polygon is successfully built, those coordinates get removed from the stream, and this continues until all
    // coordinates have been processed

    public static PolygonResponse PolyCords(ArrayList<List<Coordinate>> input_cords){
        int input_size = input_cords.size();
        // If we need to "reset" to the incoming coordinates, since we delete from the input
        ArrayList<List<Coordinate>> input_cords_old = new ArrayList<>(input_cords);

        // remembers which ways were chosen
        ArrayList<List<Coordinate>> used_cords = new ArrayList<>();

        // 'Stream'/List of coords which get returned
        ArrayList<Coordinate> cords_output = new ArrayList<>();

        // explained further down
        ArrayList<Coordinate> cords1 = new ArrayList<>();
        ArrayList<Coordinate> cords2 = new ArrayList<>();

        // how many ways the potential polygon consists of, gets increased every "Successful" iteration
        int polygon_size = 0;
//        System.out.println("input size = " + input_size);
        boolean last_two_flag = false; // flag for using the last two available ways

        // if the input is only one Way, we only need to check if it is a closed way
        if(input_size == 1){
            cords_output.addAll(input_cords.get(0));
            Set<Coordinate> set = new HashSet<>(cords_output);

            // again, check if unique coordinates
            if(set.size() < cords_output.size()) {
//                System.out.println("input size = 1");
                input_cords.remove(0);
                PolygonResponse response = new PolygonResponse(input_cords, cords_output, true);
                return response;
            }
            else{
                PolygonResponse response = new PolygonResponse(input_cords, cords_output, false);
                return response;
            }
        }


        // the idea behind this groundbreaking algorithm is that it compares to coordinate lists at a time and checks
        // if the lists have a common coordinate, if yes, then we add it to the potential polygon list
        // We begin searching from top to bottom because at the create Inner/Outer polygon we only come to this function
        // if we detect a possible Polygon, and as conclusion we can draw, that the newly added way caused this.
        // So there is little reason to start from the beginning, since this could potentially be a way which is not
        // in the desired polygon.

        int list_index_compared_to = input_size;

        // We go top to bottom, until all coordinated have been searched
        for(int current_compare  = input_size; current_compare > 0;){
            if(current_compare == 1) {
                break;
            }

            // the coordinate which gets compared to every other coordinate until either a connection is found or no more
            // ways are available
            cords1.addAll(input_cords.get(list_index_compared_to - 1));

            // the coordinate which gets chosen each cycle
            cords2.addAll(input_cords.get(current_compare - 2));
//            System.out.println(cords1.size() + "  inside function   " +  cords2.size());

            // if it is different we know the ways are connected
            if(checkIfDifferent(cords1, cords2)) {
//                System.out.println("Were different");

                // if only two ways are left then they both are part of the polygon
                if(current_compare == 2) {
                    cords_output.addAll(input_cords.get(1));
                    cords_output.addAll(input_cords.get(0));
                    used_cords.add(input_cords.get(1));
                    used_cords.add(input_cords.get(0));

//                    System.out.println("Break because i = 2");
                    polygon_size = polygon_size + 2;
                    last_two_flag = true;
                    break;
                }

                // this stuff just add to the according lists and adjusts the indexes accordingly
                cords_output.addAll(input_cords.get(list_index_compared_to - 1));
                used_cords.add(input_cords.get(list_index_compared_to - 1));
                input_cords.remove(list_index_compared_to - 1);
                List<Coordinate> new_end = new ArrayList<>(input_cords.remove(current_compare - 2));
                input_cords.add(new_end);
                polygon_size++;
                input_size--;
                current_compare = input_size;
                list_index_compared_to--;
                continue;
            }

            // reset each 'stream' so no bad things can happen
            cords1.clear();
            cords2.clear();

            // go one step down
            current_compare--;
        }

        // Here we check if the "built" polygon is actually closed
        // the idea: If we have for example 3 ways they must share exactly 3 points, so we check again for uniqueness of
        // the coordinates and if we have the same amount of points "missing" from the total size of the polygon (amount of ways)
        // we know that it is a correct polygon
        Set<Coordinate> set3 = new HashSet<>(cords_output);
        if(cords_output.size() - set3.size() == polygon_size){
            if(last_two_flag) {
                input_cords.remove(0);
                input_cords.remove(0);
            }

            // call the sorting function
            // unsuccessful attempts at creating polygons with sorting: 8
            //                                          without       : 358
            cords_output = sortCoordinates(used_cords);

            PolygonResponse response = new PolygonResponse(input_cords, cords_output, true);
//            System.out.println(cords_output);
//            System.out.println("Polygon built");
            return response;
        }
        else{
            PolygonResponse response = new PolygonResponse(input_cords_old, new ArrayList<>(), false);
//            System.out.println(input_cords_old);
            return response;
        }
    }

    // checks if two coordinate lists share a same coordinate point
    public static boolean checkIfDifferent(List<Coordinate> input1, List<Coordinate> input2){
        List<Coordinate> old_input1 = input1;
        input1.removeAll(input2);
        if(input1.equals(old_input1)){
            return true;
        }
        return false;
    }

    // as the name says it sorts the coordinates
    // at the beginning it adds the first element and then checks for the last coordinate of this element which
    // other coordinate list should follow, and if needed the list gets reversed
    // the input list gets iteratively smaller until it becomes empty
    public static ArrayList<Coordinate> sortCoordinates (ArrayList<List<Coordinate>> input) {
        ArrayList<List<Coordinate>> sorted = new ArrayList<>();
        ArrayList<Coordinate> response = new ArrayList<>();
        sorted.add(input.get(0));
        response.addAll(sorted.get(0));

        // until all elements are sorted
        while(!input.isEmpty()) {
                List<Coordinate> list = input.get(0);
                Coordinate last_element = list.get(list.size() - 1);

                // remove already sorted element from the cycle before
                input.remove(0);
                for(List<Coordinate> next_list : input){

                    // check for all lists if the contain the chosen point
                    if(next_list.contains(last_element)) {

                        // checks if the point is at the end and if it is then we reverse it
                        int index = next_list.indexOf(last_element);
                        if(index != 0) {
                            Collections.reverse(next_list);
                        }

                        // those just add the result to the corresponding list and put the chosen list at the first index of the input
                        // per cycle : 2 lists removed, 1 added
                        sorted.add(next_list);
                        response.addAll(next_list);
                        input.remove(next_list);
                        input.add(0, next_list);
                        break;
                    }
                }
            }
        return response;
    }
}
