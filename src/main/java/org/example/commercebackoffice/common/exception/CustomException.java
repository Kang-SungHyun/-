package org.example.commercebackoffice.common.exception;

import lombok.Getter;
//의도적으로 발생시키는 예외를 담은 클래스
@Getter
public class CustomException extends RuntimeException {
    private final ErrorCode errorCode;

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
public CustomException(ErrorCode errorCode, Object... args) {
        super(errorCode.message(args));
        this.errorCode = errorCode;
}
}
