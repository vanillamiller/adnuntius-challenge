package com.adnuntius.challenge;

import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

/**
 *  creates the medication from the combined raw String
 */
class MedicationFactory {
    /**
     * 
     * @param rawString the ; delimeted string (format 1)
     * @return List of fresh medications
     * @throws MedicationFormatException
     * @throws NumberFormatException
     */
    static ArrayList<Medication> create(String rawString) throws MedicationFormatException, NumberFormatException{
        ArrayList<Medication> msList = new ArrayList<Medication>();
        String [] parts = rawString.split(";");
        for(String s : parts) {
            msList.add(new Medication(s));
        }
        return msList;
    }   
    /**
     * 
     * @param rawMedicationJsonArray json array pulled from the format 2 post request
     * @return a list of fresh and tasty medications
     * @throws MedicationFormatException
     * @throws NumberFormatException
     */
    static ArrayList<Medication> create(JsonArray rawMedicationJsonArray) throws MedicationFormatException, NumberFormatException{
        ArrayList<Medication> msList = new ArrayList<Medication>();
        for (JsonElement rms : rawMedicationJsonArray) {
            msList.add(new Medication(rms.getAsString()));
        }
        return msList;
    }   
}