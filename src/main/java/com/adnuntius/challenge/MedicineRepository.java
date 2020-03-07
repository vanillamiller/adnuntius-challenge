package com.adnuntius.challenge;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 *  Singleton class acts as persistent storage in local memory. As this only exists
 *  in local memory it will be lost between deployments and restarts.
 */
class MedicineRepository {
    private ArrayList<MedicineString> storage = new ArrayList<MedicineString>();

    private MedicineRepository() {
    };

    private static MedicineRepository instance = new MedicineRepository();

    public static MedicineRepository getInstance() {
        return instance;
    }

    public void commitMedicineString(MedicineString ms) {
        this.storage.add(ms);
    }

    private int numStored() {
        return storage.size();
    }

    private HashMap<String, Integer> totalDosagePerMedicine() {
        HashMap<String, Integer> dpm = new HashMap<String, Integer>();
        String id;
        Integer dose;
        for (MedicineString ms : this.storage) {
            id = ms.getId();
            dose = ms.getdosageCount();
            dpm.put(id, dpm.get(id) != null ? dpm.get(id) + dose : dose);
        }
        return dpm;
    }

    private HashMap<String, Integer> totalDoseagePerBottleSize() {
        HashMap<String, Integer> dps = new HashMap<String, Integer>();
        String size;
        Integer dose;
        for (MedicineString ms : this.storage) {
            size = ms.getBottleSize();
            dose = ms.getdosageCount();
            dps.put(size, dps.get(size) != null ? dps.get(size) + dose : dose);
        }
        return dps;
    }

    private HashMap<String, Integer> totalAmountPerMedicine() {
        HashMap<String, Integer> apm = new HashMap<String, Integer>();
        String id;
        for (MedicineString ms : this.storage) {
            id = ms.getId();
            apm.put(id, apm.get(id) != null ? apm.get(id) + 1 : 1);
        }
        return apm;
    }

    private String toStatisticGson(HashMap<String, Object> statistic) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(statistic);
        return jsonString;
    }

    public String serialize() {
        if (this.storage.isEmpty()) {
            JsonObject json = new JsonObject();
            json.addProperty("message", "there are no medications currently stored");
            Gson gson = new Gson();
            return gson.toJson(json);
        }

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("totalMedicationsStored", this.numStored());
        map.put("toalDosagesPerMedication", this.totalDosagePerMedicine());
        map.put("numberMedicationPerSize", this.totalDoseagePerBottleSize());
        map.put("numberMedicationPerMedication", this.totalAmountPerMedicine());
        return toStatisticGson(map);
    }
}