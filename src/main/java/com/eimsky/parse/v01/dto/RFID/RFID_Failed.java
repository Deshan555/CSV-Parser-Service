package com.eimsky.parse.v01.dto.RFID;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RFID_Failed {
    private String userID;
    private String cardID;
    private String cardStatus;
    private String siteID;
}
