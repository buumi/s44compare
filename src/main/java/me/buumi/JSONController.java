package me.buumi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController
public class JSONController {

    @Autowired
    private UnitService unitService;

    @RequestMapping("/units")
    public List<Unit> testi() {
        return unitService.getUnits();
    }

    @RequestMapping("/unitnames")
    public List<String> getUnitNames() {
        return unitService.getUnitNames();
    }

    @RequestMapping("/interestingvalues")
    public HashMap<String, String[]> getInterestingValues() {
        return unitService.getInterestingValues();
    }
}
