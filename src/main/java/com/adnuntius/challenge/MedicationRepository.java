package com.adnuntius.challenge;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 *  Singleton class acts as persistent storage in local memory. As this only exists
 *  in local memory it will be lost between deployments and restarts.
 */
class MedicationRepository {
    // repository
    private ArrayList<Medication> storage = new ArrayList<Medication>();

    // private instantiator
    private MedicationRepository() {};

    // public testing instantiator to compare with instance
    public static MedicationRepository getDummy(){ return new MedicationRepository();}

    // singleton stuff
    private static MedicationRepository instance = new MedicationRepository();
    public static MedicationRepository getInstance() {
        return instance;
    }

    // commit the medication to repo
    public void commitMedication(Medication ms) {
        this.storage.add(ms);
    }

    private int numStored() {
        return storage.size();
    }

    /**
     * 
     * @return the accumulative dosage per medication
     */
    private HashMap<String, Integer> totalDosagePerMedication() {
        HashMap<String, Integer> dpm = new HashMap<String, Integer>();
        String id;
        Integer dose;
        for (Medication ms : this.storage) {
            id = ms.getId();
            dose = ms.getdosageCount();
            dpm.put(id, dpm.get(id) != null ? dpm.get(id) + dose : dose);
        }
        return dpm;
    }

    /**
     * 
     * @return accumulative dosage per bottle size
     */
    private HashMap<String, Integer> totalDoseagePerBottleSize() {
        HashMap<String, Integer> dps = new HashMap<String, Integer>();
        String size;
        Integer dose;
        for (Medication ms : this.storage) {
            size = ms.getBottleSize();
            dose = ms.getdosageCount();
            dps.put(size, dps.get(size) != null ? dps.get(size) + dose : dose);
        }
        return dps;
    }

    /**
     * 
     * @return the amount of medications per medication
     */
    private HashMap<String, Integer> totalAmountPerMedication() {
        HashMap<String, Integer> apm = new HashMap<String, Integer>();
        String id;
        for (Medication ms : this.storage) {
            id = ms.getId();
            apm.put(id, apm.get(id) != null ? apm.get(id) + 1 : 1);
        }
        return apm;
    }

    /**
     *  helper function for nested stats json
     * 
     * @param statistic
     * @return
     */
    private String toStatisticGson(HashMap<String, Object> statistic) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(statistic);
        return jsonString;
    }

    /**
     * 
     * @return a json verison of the assement criteria
     *   	- totalMedicationsStored
	 * 	    - totalDosagesPerMedication
	 * 		- numberMedicationPerSize
	 *      - numberMedicationPerMedication
     * 
     */
    public String serialize() {
        if (this.storage.isEmpty()) {
            JsonObject json = new JsonObject();
            json.addProperty("message", "there are no medications currently stored");
            Gson gson = new Gson();
            return gson.toJson(json);
        }

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("totalMedicationsStored", this.numStored());
        map.put("totalDosagesPerMedication", this.totalDosagePerMedication());
        map.put("numberMedicationPerSize", this.totalDoseagePerBottleSize());
        map.put("numberMedicationPerMedication", this.totalAmountPerMedication());
        return toStatisticGson(map);
    }
}