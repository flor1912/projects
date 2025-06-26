package at.tugraz.oop2;

import java.util.List;
import lombok.Data;

@Data
public class LanduseRepository {
    private double area;
    private List<LanduseJson> usages;

    public LanduseRepository(double area, List<LanduseJson> usages) {
        this.area = area;
        this.usages = usages;
    }
}
