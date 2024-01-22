package com.eimsky.parse.v01.json;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Response<T> {
    public static final String SUCCESS = "Success";
    public static final String WARNING = "Warning";
    public static final String ERROR = "Error";

    private String status;
    private String message;
    private T value;
}

