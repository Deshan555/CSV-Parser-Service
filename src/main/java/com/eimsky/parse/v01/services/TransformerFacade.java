package com.eimsky.parse.v01.services;

import com.eimsky.parse.v01.constants.Patterns;
import com.eimsky.parse.v01.dto.RFID.RFID_Validator;
import com.eimsky.parse.v01.models.RFID;
import org.springframework.stereotype.Service;

@Service
public class TransformerFacade {
    private final Patterns patterns;
    private final RFIDService rfidService;
    public TransformerFacade(Patterns patterns, RFIDService rfidService) {
        this.patterns = patterns;
        this.rfidService = rfidService;
    }
    /*public Boolean checkAndTransform_RFID(RFID_Validator RFID_Validator){
        try{
            RFID rfid = new RFID();
            rfid.setUserID(RFID_Validator.getUserID());
            rfid.setCardID(RFID_Validator.getCardID());
            rfid.setCardStatus(RFID_Validator.getCardStatus());
            rfid.setSiteID(RFID_Validator.getSiteID());
            rfidService.importRFID(rfid);
            return true;
        }catch(Exception e){
            System.out.println("Error Occurred : "+e);
            return false;
        }
    }*/




}
