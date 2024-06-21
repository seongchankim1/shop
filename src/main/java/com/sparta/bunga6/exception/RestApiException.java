package com.sparta.bunga6.exception;

import lombok.Builder;
import lombok.Getter;

@Builder
public class RestApiException {

    private int statusCode;
    private String msg;

}