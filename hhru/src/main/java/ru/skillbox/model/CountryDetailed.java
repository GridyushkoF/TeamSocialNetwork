package ru.skillbox.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
//@Entity
public class CountryDetailed {
    private int id;
    private int parent_id;
    private String name;
    private List<Area> areas;

    @Getter
    @Setter
    public static class Area {
        private int id;
        private int parent_id;
        private String name;
        private List<Area> areas;
    }
}
