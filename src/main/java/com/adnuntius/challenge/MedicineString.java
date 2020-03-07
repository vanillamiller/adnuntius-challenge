package com.adnuntius.challenge;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

public class MedicineString {
    private String id;
    private String bottleSize;
    private int dosageCount;

    MedicineString(String rawString) throws MedicineStringFormatException, NumberFormatException {
        String[] parts = rawString.split("_");
        this.parseRawString(parts);
    }

    private void parseRawString(String [] parts) throws MedicineStringFormatException, NumberFormatException{
        if (parts.length != 3) {
            throw new MedicineStringFormatException("not enough sections of your medical string");
        }

        if (parts[0].length() < 20) {
            this.id = parts[0];
        } else {
            throw new MedicineStringFormatException("id does not match specifications");
        }

        Boolean correctSize = false;
        String potentialSize = parts[1].toUpperCase();
        for(Sizes s : Sizes.values()){
            if (s.name().equals(potentialSize)) {
                correctSize = true;
                break;
            }
        }

        if (correctSize) {
            this.bottleSize = potentialSize;
        } else {
            throw new MedicineStringFormatException("bottle size is not valid");
        }
        if (parts[2].length() != 4){
            throw new MedicineStringFormatException("dosage is in the wrong format");
        }
        this.dosageCount = Integer.parseInt(parts[2]);
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

    public JsonElement serialize(){
        Gson gson = new Gson();
        return gson.toJsonTree(this);
    }
}