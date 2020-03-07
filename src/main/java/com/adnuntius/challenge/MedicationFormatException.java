package com.adnuntius.challenge;

/**
 *  Custom exception for medical string format
 */
public class MedicationFormatException extends Exception { 

    public MedicationFormatException(String errorMessage) {
        super(errorMessage);
    }
}