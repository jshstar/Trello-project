package com.nbc.trello.global.response;

import com.nbc.trello.global.dto.EmptyObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {

    private Integer status;
    private String message;
    private T data;

    public static <K> ApiResponse<K> of(Integer status, String message, K data) {
        return new ApiResponse<>(status, message, data);
    }

    public static  ApiResponse<EmptyObject> of(Integer status, String message) {
        EmptyObject emptyObject = EmptyObject.get();
        return new ApiResponse<>(status, message, emptyObject);
    }
}