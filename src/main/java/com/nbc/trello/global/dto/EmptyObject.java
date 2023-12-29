package com.nbc.trello.global.dto;

public class EmptyObject {

    private static final EmptyObject emptyObject = new EmptyObject();

    private EmptyObject() {}

    public static EmptyObject get() {
        return emptyObject;
    }
}
