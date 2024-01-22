package com.eimsky.parse.v01.dto.RFID;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@ToString
public class RFID_DTO {
    private String userID;
    private String cardID;
    private String cardStatus;
    private String siteID;
    private MultipartFile file;
}
