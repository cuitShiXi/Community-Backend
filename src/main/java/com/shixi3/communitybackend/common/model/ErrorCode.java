package com.shixi3.communitybackend.common.model;

import lombok.Data;

@Data
public class ErrorCode {
    Integer code;
    String msg;

    public ErrorCode(Integer code, String message) {
        this.code = code;
        this.msg = message;
    }
}
