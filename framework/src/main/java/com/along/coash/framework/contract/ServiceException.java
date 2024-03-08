package com.along.coash.framework.contract;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public final class ServiceException extends RuntimeException {

    private ErrorCode code;

    private String message;

    public ServiceException() {
    }

    public ServiceException(ErrorCode errorCode) {
        this.code = errorCode;
        this.message = errorCode.getMsg();
    }

    public ServiceException(ErrorCode code, String message) {
        this.code = code;
        this.message = message;
    }
}
