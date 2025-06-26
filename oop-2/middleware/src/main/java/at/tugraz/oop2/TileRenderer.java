package at.tugraz.oop2;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class TileRenderer {

    // Layer properties: color and stroke width
    private static final Map<String, Pair<Color, Integer>> LAYER_PROPERTIES = new HashMap<>();

    static {
        LAYER_PROPERTIES.put("motorway", new Pair<>(new Color(255, 0, 0), 3));
        LAYER_PROPERTIES.put("trunk", new Pair<>(new Color(255, 140, 0), 2));
        LAYER_PROPERTIES.put("primary", new Pair<>(new Color(255, 165, 0), 2));
        LAYER_PROPERTIES.put("secondary", new Pair<>(new Color(255, 255, 0), 2));
        LAYER_PROPERTIES.put("road", new Pair<>(new Color(128, 128, 128), 2));
        LAYER_PROPERTIES.put("forest", new Pair<>(new Color(173, 209, 158), 1));
        LAYER_PROPERTIES.put("residential", new Pair<>(new Color(223, 233, 233), 1));
        LAYER_PROPERTIES.put("vineyard", new Pair<>(new Color(172, 224, 161), 1));
        LAYER_PROPERTIES.put("grass", new Pair<>(new Color(205, 235, 176), 1));
        LAYER_PROPERTIES.put("railway", new Pair<>(new Color(235, 219, 233), 1));
        LAYER_PROPERTIES.put("water", new Pair<>(new Color(0, 128, 255), 1));
    }

    @GetMapping("/tile/{z}/{x}/{y}")
    public ResponseEntity<byte[]> renderTile(
            @PathVariable int z,
            @PathVariable int x,
            @PathVariable int y,
            @RequestParam(name = "layers", defaultValue = "motorway") String layers
    ) {
        try {
            // Calculate tile boundaries
            double[] bounds = calculateTileBounds(z, x, y);

            // Render tile as BufferedImage
            BufferedImage image = renderTileImage(bounds, layers);

            // Convert image to byte array
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);

            // Return the image as a response
            return ResponseEntity.ok()
                    .header("Content-Type", "image/png")
                    .body(baos.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    /**
     * Calculates the geographic boundaries of the tile based on z/x/y.
     */
    private double[] calculateTileBounds(int z, int x, int y) {
        double n = Math.pow(2, z);
        double lon1 = x / n * 360.0 - 180.0;
        double lat1 = Math.toDegrees(Math.atan(Math.sinh(Math.PI * (1 - 2 * y / n))));
        double lon2 = (x + 1) / n * 360.0 - 180.0;
        double lat2 = Math.toDegrees(Math.atan(Math.sinh(Math.PI * (1 - 2 * (y + 1) / n))));
        return new double[]{lon1, lat1, lon2, lat2};
    }

    /**
     * Renders the tile image for the given bounds and layers.
     */
    private BufferedImage renderTileImage(double[] bounds, String layers) {
        int tileSize = 512;
        BufferedImage image = new BufferedImage(tileSize, tileSize, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();

        // Set anti-aliasing and rendering hints for better quality
        g2d.setRenderingHints(Map.of(
                RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON,
                RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON,
                RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY
        ));

        // Set clipping boundaries to ensure drawing stays within the tile
        g2d.setClip(0, 0, tileSize, tileSize);

        // Draw the background
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, tileSize, tileSize);

        Map<String, String> styles = new HashMap<>();

        // Draw features based on layers
        String[] layerList = layers.split(",");
        for (String layer : layerList) {
            drawLayer(g2d, bounds, layer.trim(), styles);
        }

        g2d.dispose();
        return image;
    }

    /**
     * Draws a specific layer on the map tile.
     */
    private void drawLayer(Graphics2D g2d, double[] bounds, String layer, Map<String, String> styles) {
        String colorKey = layer + ".color";
        String widthKey = layer + ".width";

        // Get color and width from styles or fallback to default
        Color color = styles.containsKey(colorKey)
                ? parseColor(styles.get(colorKey))
                : LAYER_PROPERTIES.get(layer).getFirst();
        int width = styles.containsKey(widthKey)
                ? Integer.parseInt(styles.get(widthKey))
                : LAYER_PROPERTIES.get(layer).getSecond();

        g2d.setColor(color);
        g2d.setStroke(new BasicStroke(width, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND));
    }

    private Color parseColor(String rgb) {
        String[] parts = rgb.split(",");
        return new Color(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
    }


    /**
     * Utility class to store layer properties (color and width).
     */
    private static class Pair<T, U> {
        private final T first;
        private final U second;

        public Pair(T first, U second) {
            this.first = first;
            this.second = second;
        }

        public T getFirst() {
            return first;
        }

        public U getSecond() {
            return second;
        }
    }
}
