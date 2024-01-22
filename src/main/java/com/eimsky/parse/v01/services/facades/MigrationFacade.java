package com.eimsky.parse.v01.services.facades;

import com.eimsky.parse.v01.constants.Patterns;
import com.eimsky.parse.v01.dto.RFID.RFID_DTO;
import com.eimsky.parse.v01.dto.RFID.RFID_DTO_Response;
import com.eimsky.parse.v01.dto.RFID.RFID_Validator;
import com.eimsky.parse.v01.dto.RFID.RFID_Validator_Response;
import com.eimsky.parse.v01.models.RFID;
import com.eimsky.parse.v01.services.Attendance_Parser;
import com.eimsky.parse.v01.services.CSVParserService;
import com.eimsky.parse.v01.services.RFIDService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;

// Migration Facade is a facade that is used to migrate data from one system to another.
@Slf4j
@Service
public class MigrationFacade {
    private final Patterns patterns;
    private final RFIDService rfidService;
    private final CSVParserService csvParserService;
    private final Attendance_Parser attendance_parser;
    public MigrationFacade(Patterns patterns, RFIDService rfidService, CSVParserService csvParserService,
                           Attendance_Parser attendanceParser) {
        this.patterns = patterns;
        this.rfidService = rfidService;
        this.csvParserService = csvParserService;
        attendance_parser = attendanceParser;
    }

    public RFID mapperToRFID(RFID_Validator RFID_Validator){
        RFID rfid = new RFID();
        rfid.setUserID(RFID_Validator.getUserID());
        rfid.setCardID(Long.parseLong(RFID_Validator.getCardID()));
        rfid.setCardStatus(RFID_Validator.getCardStatus());
        rfid.setSiteID(Long.parseLong(RFID_Validator.getSiteID()));
        return rfid;
    }

    @Transactional
    public RFID_Validator_Response checkAndTransform_RFID(RFID_Validator RFID_Validator){
        HashMap<String, Boolean> validationResults = patterns.validateRFIDDataPattern(RFID_Validator.getSiteID(),
                RFID_Validator.getUserID(),
                RFID_Validator.getCardID(),
                RFID_Validator.getCardStatus());
        // Set the validation results in the RFID_Validator_Response object
        RFID_Validator_Response RFID_Validator_Response = new RFID_Validator_Response();
        RFID_Validator_Response.setSiteID(validationResults.get("siteID"));
        RFID_Validator_Response.setUserID(validationResults.get("userID"));
        RFID_Validator_Response.setCardID(validationResults.get("cardID"));
        RFID_Validator_Response.setCardStatus(validationResults.get("cardStatus"));
        // Check if all the validation results are true
        try{
            RFID newRFID = rfidService.importRFID(mapperToRFID(RFID_Validator));
            if(newRFID != null){
                // Remove Inserted Sample Record Before Response to the Client
                rfidService.deleteByRecordId(newRFID.getRecordId());
                RFID_Validator_Response.setTransactionStatus(true);
                log.info("MigrationFacade.checkAndTransform_RFID() - RFID_Validator_Response: {}", RFID_Validator_Response);
                return RFID_Validator_Response;
            }else {
                RFID_Validator_Response.setTransactionStatus(false);
                log.info("MigrationFacade.checkAndTransform_RFID() - RFID_Validator_Response: {}", RFID_Validator_Response);
                return RFID_Validator_Response;
            }
        }catch(Exception error){
            log.warn("MigrationFacade.checkAndTransform_RFID() - error: {}", error.getMessage());
            RFID_Validator_Response.setTransactionStatus(false);
            return RFID_Validator_Response;
        }
    }

    @Transactional
    public RFID_DTO_Response checkAndImport_RFID(String filePath, RFID_DTO RFID_Dto){
        try{
            log.info("MigrationFacade.checkAndImport_RFID() - filePath: {}", filePath);
            return attendance_parser.attendanceParser(filePath, RFID_Dto);
        }catch(Exception error){
            log.warn("MigrationFacade.checkAndImport_RFID() - error: {}", error.getMessage());
            return null;
        }
    }


}
