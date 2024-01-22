package com.eimsky.parse.v01.services.facades;

import java.util.HashMap;

public class DataValidator {
    public HashMap<String, Boolean> validateDataPattern(String siteID, String userID, String cardID, int cardStatus) {

        HashMap<String, Boolean> validationResults = new HashMap<>();

        // Validate the format of each parameter separately
        boolean siteIDValid = siteID.matches("\\d+");
        boolean userIDValid = userID.matches("[A-Z]+-\\d+");
        boolean cardIDValid = cardID.matches("\\d+");
        boolean cardStatusValid = (cardStatus == 0 || cardStatus == 1);

        // Store the validation results in the HashMap
        validationResults.put("siteID", siteIDValid);
        validationResults.put("userID", userIDValid);
        validationResults.put("cardID", cardIDValid);
        validationResults.put("cardStatus", cardStatusValid);

        return validationResults;
    }

    /*public static void main(String[] args) {
        String siteID1 = "12345";
        String userID1 = "USR-1001";
        String cardID1 = "987654321";
        int cardStatus1 = 1;

        String siteID2 = "54321";
        String userID2 = "USR-1002";
        String cardID2 = "123456789";
        int cardStatus2 = 0;

        HashMap<String, Boolean> validationResults1 = validateDataPattern(siteID1, userID1, cardID1, cardStatus1);
        HashMap<String, Boolean> validationResults2 = validateDataPattern(siteID2, userID2, cardID2, cardStatus2);

        // Print the validation results
        System.out.println("Validation Results for Data 1:");
        for (String fieldName : validationResults1.keySet()) {
            System.out.println(fieldName + ": " + validationResults1.get(fieldName));
        }

        System.out.println("Validation Results for Data 2:");
        for (String fieldName : validationResults2.keySet()) {
            System.out.println(fieldName + ": " + validationResults2.get(fieldName));
        }
    }*/
}
