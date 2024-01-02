package com.nbc.trello.global.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EmptyObject {

    private static final EmptyObject emptyObject = new EmptyObject();

    public static EmptyObject get() {
        return emptyObject;
    }
}
