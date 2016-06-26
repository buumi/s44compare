package me.buumi;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.HashMap;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Unit {

    public static int sId = 1;

    public static final String[] INTERESTING_VALUES = {"health", "cost", "speed", "turnRate", "maxammo", "armor_front", "armor_side", "armor_rear", "armor_top"};

    private int id;
    private String name;
    private String humanName;
    private HashMap attributes;

    private List<Weapon> weapons;

    public Unit(String name) {
        this.id = Unit.sId++;
        this.name = name;
        this.attributes = new HashMap();
    }

    public void addValue(String key, String value) {
        attributes.put(key, value);
    }

    public String getName() {
        return name;
    }

    public HashMap getAttributes() {
        return attributes;
    }

    public List<Weapon> getWeapons() {
        return weapons;
    }

    public int getId() {
        return id;
    }

    public String getHumanName() {
        return humanName;
    }

    public void setHumanName(String humanName) {
        this.humanName = humanName;
    }

    public void setWeapons(List<Weapon> weapons) {
        this.weapons = weapons;
    }
}
