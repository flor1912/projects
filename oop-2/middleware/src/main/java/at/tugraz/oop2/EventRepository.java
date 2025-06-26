package at.tugraz.oop2;

import com.fasterxml.jackson.annotation.JsonRawValue;
import lombok.Data;

import java.util.*;
import java.util.Collections;

import lombok.Singular;

@Data
public class EventRepository {

    @JsonRawValue
    List<String> entries ;
    Paging paging = null;

    EventRepository(List<String> entries, Paging paging, int take, int skip) {
        int size = entries.size();
        int count = 0;
        List<String> keptEntries = new ArrayList<>();
        for(int index = skip; count < take; index++) {
            if(count + skip >= size) {
                break;
            }
            keptEntries.add(entries.get(index));
            count++;
        }
        this.entries = keptEntries;
        this.paging = paging;
    }
}