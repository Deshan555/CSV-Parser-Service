package com.eimsky.parse.v01.json;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum JsonParameter {
    JSON_VERSION("1.0.0"),
    SERVICE_NAME("CSV Parser Service"),
    SERVICE_VERSION("1.0.0"),
    CHARACTER_ENCODING("UTF-8");

    private String value;

    JsonParameter(String value) {
        this.value = value;
    }
}
