package com.eimsky.parse.v01.services;

import com.eimsky.parse.v01.models.RFID;

public interface RFIDService {
    abstract RFID importRFID(RFID rfidEntity);
    abstract Boolean deleteByRecordId(Long recordId);
}
