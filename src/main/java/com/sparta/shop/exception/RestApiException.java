package com.sparta.shop.exception;

import lombok.Builder;

@Builder
public class RestApiException {

    private int statusCode;
    private String msg;

}