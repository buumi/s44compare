package me.buumi;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Weapon {
    public static final String[] INTERESTING_VALUES = {"range", "reload", "accuracy", "movingAccuracy", "armor_penetration", "armor_penetration_100m", "armor_penetration_1000m"};

    private String name;
    private Map<Integer, String> damages;
    private HashMap<String, String> weaponAttributes;

    public Weapon() {
        this.damages = new HashMap<>();
        this.weaponAttributes = new HashMap<>();
    }

    public void addValue(String name, String value) {
        this.weaponAttributes.put(name, value);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Map<Integer, String> getDamages() {
        return damages;
    }

    public HashMap<String, String> getWeaponAttributes() {
        return weaponAttributes;
    }
}
