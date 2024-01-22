package com.eimsky.parse.v01.dto.RFID;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RFID_Validator_Response {
    private boolean transactionStatus;
    private boolean userID;
    private boolean cardID;
    private boolean cardStatus;
    private boolean siteID;
}
