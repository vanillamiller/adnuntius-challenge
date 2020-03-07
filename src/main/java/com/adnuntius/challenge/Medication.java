package com.adnuntius.challenge;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

/**
 * Class that instantiates an object representing and enforcing format
 * constratints upon medication
 */
public class Medication {
    private String id;
    private String bottleSize;
    private int dosageCount;

    /**
     * 
     * @param rawString a raw medication id string
     * @throws MedicationFormatException thrown if not correct string format
     * @throws NumberFormatException thrown if the dose is not a number
     */
    Medication(String rawString) throws MedicationFormatException, NumberFormatException {
        String[] parts = rawString.split("_");
        this.parseRawString(parts);
    }

    /**
     * 
     * @param parts the 3 parts of the split medication string
     * @throws MedicationFormatException thrown if incorrect string format
     * @throws NumberFormatExceptionthrown if the dose is not a number
     */
    private void parseRawString(String[] parts) throws MedicationFormatException, NumberFormatException {
        if (parts.length != 3) {
            throw new MedicationFormatException("not enough sections of your medical string");
        }

        if (parts[0].length() <= 20) {
            this.id = parts[0];
        } else {
            throw new MedicationFormatException("id does not match specifications");
        }

        Boolean correctSize = false;
        String potentialSize = parts[1].toUpperCase();
        for (Sizes s : Sizes.values()) {
            if (s.name().equals(potentialSize)) {
                correctSize = true;
                break;
            }
        }

        if (correctSize) {
            this.bottleSize = potentialSize;
        } else {
            throw new MedicationFormatException("bottle size is not valid");
        }
        if (parts[2].length() != 4) {
            throw new MedicationFormatException("dosage is in the wrong format");
        }
        if (Integer.parseInt(parts[2]) > 0) {
            this.dosageCount = Integer.parseInt(parts[2]);
        } else {
            throw new MedicationFormatException("cannot have negative dose");
        }

    }

    public String getId() {
        return id;
    }

    public String getBottleSize() {
        return bottleSize;
    }

    public int getdosageCount() {
        return dosageCount;
    }

    /**
     * 
     * @return A Json representation of this medication
     */
    public JsonElement serialize() {
        Gson gson = new Gson();
        return gson.toJsonTree(this);
    }
}