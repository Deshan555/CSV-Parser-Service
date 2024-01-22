package com.eimsky.parse.v01.json;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Json {
    private String version = JsonParameter.SERVICE_VERSION.getValue();
    private String serviceName = JsonParameter.SERVICE_NAME.getValue();
    private String serviceVersion = JsonParameter.SERVICE_VERSION.getValue();
    private String encoding = JsonParameter.CHARACTER_ENCODING.getValue();
    private String generatedTime = getCurrentDateTimeInFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    private Response response;

    public static String getCurrentDateTimeInFormat(String formatPattern) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatPattern);
        return currentDateTime.format(formatter);
    }
}
