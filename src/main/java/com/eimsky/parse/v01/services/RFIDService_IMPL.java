package com.eimsky.parse.v01.services;

import com.eimsky.parse.v01.models.RFID;
import com.eimsky.parse.v01.repositories.RFIDRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RFIDService_IMPL implements RFIDService{
    private final RFIDRepository rfidRepository;
    public RFIDService_IMPL(RFIDRepository rfidRepository) {
        this.rfidRepository = rfidRepository;
    }
    @Override
    public RFID importRFID(RFID rfidEntity) {
        try{
            log.info("RFIDService_IMPL.importRFID() - rfidEntity: {}", rfidEntity);
            return rfidRepository.save(rfidEntity);
        }catch(Exception e){
            log.warn("RFIDService_IMPL.importRFID() - error: {}", e.getMessage());
            return null;
        }
    }
    @Override
    public Boolean deleteByRecordId(Long recordId) {
        try{
            log.info("RFIDService_IMPL.deleteByRecordId() - recordId: {}", recordId);
            rfidRepository.deleteById(recordId);
            return true;
        }catch(Exception e){
            log.warn("RFIDService_IMPL.deleteByRecordId() - error: {}", e.getMessage());
            return false;
        }
    }
}
