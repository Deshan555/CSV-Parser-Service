package com.eimsky.parse.v01.dto.RFID;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class RFID_DTO_Response {
    private int totalTransactions;
    private int successTransactions;
    private int failedTransactions;
    private List<RFID_Failed> failedTransactionsList;
}

