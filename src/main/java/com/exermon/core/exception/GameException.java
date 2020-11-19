package com.exermon.core.exception;

public class GameException extends Exception {

    ErrorType type;

    public GameException(ErrorType type) {
        super(type.getDesc());
        this.type = type;
    }

    public int getCode() { return type.getCode(); }
    public String getDesc() { return type.getDesc(); }
}
