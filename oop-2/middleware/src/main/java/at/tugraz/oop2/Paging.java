package at.tugraz.oop2;

import lombok.Data;


@Data
public class Paging {
    private Integer take;
    private Integer total;
    private Integer skip;

    public Paging(String take, Integer total, String skip) {
        this.take = Integer.valueOf(take);
        this.total = total;
        this.skip = Integer.valueOf(skip);
    }
}
