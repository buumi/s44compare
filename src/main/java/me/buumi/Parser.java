package me.buumi;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class Parser {

    public List<Unit> parseUnits(File file) {
        List<Unit> units = new ArrayList<>();

        try {
            List<String> lines = FileUtils.readLines(file, "UTF-8");

            List<String> linesForNextUnit = new ArrayList<>();

            String currentUnitName = getUnitName(lines.get(0));

            for (String line : lines) {
                if (getUnitName(line) == null) {
                    continue;
                }
                else if (getUnitName(line).equals(currentUnitName)) {
                    linesForNextUnit.add(line);
                }
                else {
                    units.add(parseUnit(linesForNextUnit));

                    currentUnitName = getUnitName(line);
                    linesForNextUnit = new ArrayList<>();
                    linesForNextUnit.add(line);
                }
            }
        } catch (IOException e) {
            System.err.println("Could not open file: " + file);
            System.exit(-1);
        }

        return units;
    }

    private String getUnitName(String line) {
        if (line.contains("Unit:")) {
            line = line.substring(5);
            line = line.split(">")[0];
            return line;
        }
        return null;
    }

    private Unit parseUnit(List<String> unitLines) {
        List<String> weaponLines = new ArrayList<>();
        List<String> singleWeaponLines = new ArrayList<>();

        List<Weapon> weapons = new ArrayList<>();

        Unit unit = new Unit(getUnitName(unitLines.get(0)));

        for (String line : unitLines) {
            if (line.contains("wDefs")) {
                weaponLines.add(line);
            }
            for (String interestingValue : Unit.INTERESTING_VALUES) {
                if (line.contains(">" + interestingValue + ":") && !line.contains("wDefs")) {
                    unit.addValue(interestingValue, getValueOfLine(line));
                    break;
                }
            }
        }

        int currentId = 1;
        for (String line : weaponLines) {
            if (getWeaponId(line) == currentId) {
                singleWeaponLines.add(line);
            }
            else {
                currentId = getWeaponId(line);

                Weapon weapon = parseWeapon(singleWeaponLines);
                weapons.add(weapon);

                singleWeaponLines = new ArrayList<>();
            }
        }
        if (!singleWeaponLines.isEmpty()) {
            Weapon weapon = parseWeapon(singleWeaponLines);
            weapons.add(weapon);
        }

        if (weapons.size() > 0)
            unit.setWeapons(weapons);

        return unit;
    }

    private String getValueOfLine(String line) {
        String[] split = line.split(":");

        String value = split[split.length-1].trim();
        value = value.replaceAll(",", ".");

        try {
            DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
            decimalFormatSymbols.setDecimalSeparator('.');
            DecimalFormat decimalFormat = new DecimalFormat();
            decimalFormat.setDecimalFormatSymbols(decimalFormatSymbols);
            decimalFormat.setGroupingUsed(false);
            decimalFormat.setMaximumFractionDigits(3);
            value = decimalFormat.format(Double.parseDouble(value));
        } catch (NumberFormatException e) {
            System.out.println("Error reformatting. Expected double, got: " +line);
        }
        return value;
    }

    private int getWeaponId(String line) {
        String split[] = line.split(">");
        int index = 0;

        for (int i = 0 ; i < split.length; i++) {
            if (split[i].equals("wDefs")) {
                index = i+1;
            }
        }

        return Integer.parseInt(split[index]);
    }

    private Weapon parseWeapon(List<String> weaponLines) {
        Weapon weapon = new Weapon();

        for (String line : weaponLines) {
            if (line.contains(">name:")) {
                weapon.setName(getValueOfLine(line));
            }
            else if (line.contains(">damages>")) {
                String splittedLine[] = line.split(">");
                String interestingOnes = splittedLine[splittedLine.length-1];

                int index;
                try {
                    index = Integer.parseInt(interestingOnes.split(":")[0]);
                } catch (NumberFormatException e) {
                    System.out.println("Skipping non numeric damage value..");
                    continue;
                }
                String value = getValueOfLine(interestingOnes);

                weapon.getDamages().put(index, value);
            }
            else {
                for (String importantValue : Weapon.INTERESTING_VALUES) {
                    if (line.contains(">" + importantValue + ":")) {
                        weapon.addValue(importantValue, getValueOfLine(line));
                        break;
                    }
                }
            }
        }

        return weapon;
    }


}
