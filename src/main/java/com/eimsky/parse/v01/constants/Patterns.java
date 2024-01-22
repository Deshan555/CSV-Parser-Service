package com.eimsky.parse.v01.constants;

import org.springframework.stereotype.Component;
import java.util.HashMap;

@Component
public class Patterns {
    public HashMap<String, Boolean> validateRFIDDataPattern(String siteID, String userID, String cardID, int cardStatus) {

        HashMap<String, Boolean> validationResults = new HashMap<>();
        boolean siteIDValid = siteID.matches("\\d+");
        boolean userIDValid = userID.matches("[A-Z]+-\\d+");
        boolean cardIDValid = cardID.matches("\\d+");
        boolean cardStatusValid = (cardStatus == 0 || cardStatus == 1);

        validationResults.put("siteID", siteIDValid);
        validationResults.put("userID", userIDValid);
        validationResults.put("cardID", cardIDValid);
        validationResults.put("cardStatus", cardStatusValid);

        return validationResults;
    }



}
