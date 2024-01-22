package com.eimsky.parse.v01.repositories;

import com.eimsky.parse.v01.models.RFID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RFIDRepository extends JpaRepository<RFID, Long> {
}
