package com.eimsky.parse.v01.services;

import com.eimsky.parse.v01.constants.Patterns;
import com.eimsky.parse.v01.dto.RFID.RFID_DTO;
import com.eimsky.parse.v01.dto.RFID.RFID_Failed;
import com.eimsky.parse.v01.models.RFID;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Component
public class CSVParserService {
    private final RFIDService rfidService;
    private final Patterns patterns;
    @Autowired
    public CSVParserService(RFIDService rfidService, Patterns patterns) {
        this.rfidService = rfidService;
        this.patterns = patterns;
    }
    public void readCSV(String filePath, RFID_DTO RFID_DTO) {
        try {
            int successRows = 0;
            int failedRows = 0;
            CSVReader reader = new CSVReaderBuilder(new FileReader(filePath)).withSkipLines(0).build(); // Skip header
            List<String[]> rows = reader.readAll();
            String[] headers = rows.get(0);
            System.out.println(headers);

            // That HashMap Store CSV Columns and Corresponding modelCol
            HashMap<String, String> mapModel = modelMapper(RFID_DTO);

            String userID = mapModel.get("userID");
            String cardID = mapModel.get("cardID");
            String cardStatus = mapModel.get("cardStatus");
            String siteID = mapModel.get("siteID");

            Integer userID_Index = 0;
            Integer cardID_Index = 0;
            Integer cardStatus_Index = 0;
            Integer siteID_Index = 0;

            // That HashMap Store CSV Headers and Corresponding index
            HashMap<String, Integer> headMapper = new HashMap<String, Integer>();
            for (int i = 0; i < headers.length; i++) {
                System.out.println(i + ": " + headers[i]);
                headMapper.put(headers[i], i);
            }

            if (siteID != null && !siteID.isEmpty()) {
                siteID_Index = headMapper.get(mapModel.get("siteID"));
            } else {
                siteID_Index = null;
            }

            if (userID != null && !userID.isEmpty()) {
                userID_Index = headMapper.get(mapModel.get("userID"));
            } else {
                userID_Index = null;
            }

            if (cardID != null && !cardID.isEmpty()) {
                cardID_Index = headMapper.get(mapModel.get("cardID"));
            } else {
                cardID_Index = null;
            }

            if (cardStatus != null && !cardStatus.isEmpty()) {
                cardStatus_Index = headMapper.get(mapModel.get("cardStatus"));
            } else {
                cardStatus_Index = null;
            }

            System.out.println(headMapper);

            List<RFID_Failed> failedList = new ArrayList<RFID_Failed>();

            for (int i = 1; i < rows.size(); i++) {

                RFID rfidEntity = new RFID();
                String[] row = rows.get(i);

                String siteValue = (siteID_Index == null) ? "0" : row[siteID_Index];
                String userValue = (userID_Index == null) ? "0" : row[userID_Index];
                String cardValue = (cardID_Index == null) ? "0" : row[cardID_Index];
                int cardStatusValue = (cardStatus_Index == null) ? 0 : Integer.valueOf(row[cardStatus_Index]);

                HashMap<String, Boolean> validationResults = patterns.validateRFIDDataPattern(siteValue, userValue, cardValue, cardStatusValue);

                boolean siteIDValid = validationResults.get("siteID");
                boolean userIDValid = validationResults.get("userID");
                boolean cardIDValid = validationResults.get("cardID");
                boolean cardStatusValid = validationResults.get("cardStatus");

                if ((siteIDValid) && (userIDValid || userID_Index == null) && (cardIDValid || cardID_Index == null) && (cardStatusValid || cardStatus_Index == null)) {
                    rfidEntity.setUserID(userValue);
                    rfidEntity.setCardID(Long.valueOf(cardValue));
                    rfidEntity.setCardStatus(cardStatusValue);
                    rfidEntity.setSiteID(Long.valueOf(siteValue));
                    try {
                        log.info("CSVParserService.readCSV() - rfidEntity: {}", rfidEntity);
                        successRows++;
                        rfidService.importRFID(rfidEntity);
                    } catch (Exception error) {
                        RFID_Failed failed = new RFID_Failed();
                        failed.setUserID(userValue);
                        failed.setCardID(cardValue);
                        failed.setCardStatus(String.valueOf(cardStatusValue));
                        failed.setSiteID(siteValue);
                        failedList.add(failed);
                        failedRows++;
                        log.warn("CSVParserService.readCSV() - error: {}", error.getMessage());
                    }
                } else {
                    RFID_Failed failed = new RFID_Failed();
                    failed.setUserID(userValue);
                    failed.setCardID(cardValue);
                    failed.setCardStatus(String.valueOf(cardStatusValue));
                    failed.setSiteID(siteValue);
                    failedList.add(failed);
                    failedRows++;
                }
            }
            System.out.println("Success Rows : " + successRows);
            System.out.println("Failed Rows : " + failedRows);
            System.out.println("Failed List : " + failedList);
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
    }

    // That Function Mapping modelCol to CSV Columns
    public HashMap<String, String> modelMapper(RFID_DTO RFIDDTOMapper) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("userID", RFIDDTOMapper.getUserID());
        map.put("cardID", RFIDDTOMapper.getCardID());
        map.put("cardStatus", RFIDDTOMapper.getCardStatus());
        map.put("siteID", RFIDDTOMapper.getSiteID());
        System.out.println(map);
        return map;
    }
}
