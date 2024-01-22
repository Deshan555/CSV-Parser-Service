package com.eimsky.parse.v01.dto.RFID;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RFID_Validator {
    private String userID;
    private String cardID;
    private Integer cardStatus;
    private String siteID;
}
