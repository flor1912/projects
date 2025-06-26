package at.tugraz.oop2;

import lombok.Data;
import org.locationtech.jts.geom.Coordinate;

import java.util.ArrayList;
import java.util.List;

@Data
public class PolygonResponse {
        private ArrayList<List<Coordinate>> coordinate_list;
        private ArrayList<Coordinate> coordinates;
        private boolean success;

        PolygonResponse(ArrayList<List<Coordinate>> coordinate_list, ArrayList<Coordinate> coordinates, boolean success) {
            this.coordinate_list = coordinate_list;
            this.coordinates = coordinates;
            this.success = success;
        }
}
