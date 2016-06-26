package me.buumi;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UnitService {

    private List<Unit> units;

    @PostConstruct
    public void init() {
        File file = new File("defs.txt");

        try {
            FileUtils.copyInputStreamToFile(getClass().getResourceAsStream("/static/defs.txt"), file);
        } catch (IOException e) {
            System.out.println("Could not copy defs.txt to file. Aborting program..");
            System.exit(-1);
        }

        Parser parser = new Parser();

        this.units = parser.parseUnits(file);
    }

    public List<Unit> getUnits() {
        return units;
    }

    public List<String> getUnitNames() {
        List<String> list = new ArrayList();

        for (Unit unit : units) {
            list.add(unit.getName());
        }

        return list;
    }

    public HashMap<String, String[]> getInterestingValues() {
        HashMap<String, String[]> map = new HashMap<>();

        map.put("Weapons", Weapon.INTERESTING_VALUES);
        map.put("Units", Unit.INTERESTING_VALUES);

        return map;
    }
}
