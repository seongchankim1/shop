package com.sparta.shop.exception;

public class DuplicateNameException extends IllegalArgumentException {

    public DuplicateNameException(String s) {
        super(s);
    }
}
