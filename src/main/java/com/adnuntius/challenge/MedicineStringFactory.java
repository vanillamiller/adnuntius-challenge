package com.adnuntius.challenge;

import java.util.ArrayList;


class MedicineStringFactory {
    static ArrayList<MedicineString> create(String rawString) throws MedicineStringFormatException, NumberFormatException{
        ArrayList<MedicineString> msList = new ArrayList<MedicineString>();
        String [] parts = rawString.split(";");
        for(String s : parts) {
            msList.add(new MedicineString(s));
        }
        return msList;
    }   
}