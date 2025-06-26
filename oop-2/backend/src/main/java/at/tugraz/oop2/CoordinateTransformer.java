package at.tugraz.oop2;

import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.util.AffineTransformation;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.locationtech.jts.geom.util.AffineTransformation;
import org.locationtech.jts.geom.util.AffineTransformation;

public class CoordinateTransformer {

    private static MathTransform transform;

    static {
        try {
            // Initialize CRS only once
            CoordinateReferenceSystem sourceCRS = CRS.decode("EPSG:4326", true);
            CoordinateReferenceSystem targetCRS = CRS.decode("EPSG:31256");
            transform = CRS.findMathTransform(sourceCRS, targetCRS, true);

        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize coordinate transformation", e);
        }
    }

    public static Geometry transformGeometry(Geometry geometry) {
        try {
            return org.geotools.geometry.jts.JTS.transform(geometry, transform);
        } catch (Exception e) {
            throw new RuntimeException("Failed to transform geometry", e);
        }
    }
}
