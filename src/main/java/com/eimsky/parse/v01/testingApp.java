package com.eimsky.parse.v01;

import com.eimsky.parse.v01.dto.RFID.RFID_DTO;
import java.util.HashMap;

public class testingApp {
    public static void main(String[] args) {

        RFID_DTO RFID_DTO = new RFID_DTO();
        RFID_DTO.setUserID("12345678901234567890");
        RFID_DTO.setCardID("123456789012345678FF90");
        RFID_DTO.setCardStatus("12345678901FF234567890");
        RFID_DTO.setSiteID("12345678901234567890");

        System.out.println(RFID_Pattern(RFID_DTO));
    }

    public static HashMap<String, Boolean> RFID_Pattern(RFID_DTO RFID_DTO) {
        HashMap<String, Boolean> pattern = new HashMap<>();
        pattern.put("userID", RFID_DTO.getUserID().matches("^[0-9]{1,20}$"));
        pattern.put("cardID", RFID_DTO.getCardID().matches("^[A-Z0-9]{1,20}$"));
        pattern.put("cardStatus", RFID_DTO.getCardStatus().matches("^[0-9]{1,20}$"));
        pattern.put("siteID", RFID_DTO.getSiteID().matches("^[0-9]{1,20}$"));
        return pattern;
    }

}
