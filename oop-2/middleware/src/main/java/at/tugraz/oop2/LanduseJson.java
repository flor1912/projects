package at.tugraz.oop2;
import lombok.Data;
import java.util.List;

@Data
public class LanduseJson {
    private String type;
    private Double share;
    private Double area;

    public LanduseJson(Landuse landuse) {
        this.area = landuse.getArea();
        this.share = landuse.getShare();
        this.type = landuse.getType();
    }
}



