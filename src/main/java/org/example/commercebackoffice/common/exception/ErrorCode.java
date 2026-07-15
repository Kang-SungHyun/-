package org.example.commercebackoffice.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
public enum ErrorCode {
    PASSWORD_INCORRECT(HttpStatus.BAD_REQUEST,"PASSWORD INCORRECT", "비밀번호가 일치하지 않습니다."),
    SCHEDULE_NOT_FOUND(HttpStatus.NOT_FOUND, "SCHEDULE NOT FOUND", "해당 일정을 찾을 수 없습니다."),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "COMMENT NOT FOUND", "해당 댓글을 찾을 수 없습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER NOT FOUND", "사용자를 찾을 수 없습니다."),
    NOT_AUTHOR(HttpStatus.UNAUTHORIZED, "NOT AUTHOR", "작성자가 아닙니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", "접근이 거부되었습니다.");

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String errorCode, String message) {
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.message = message;
    }


    public String message(Object... args) {
        return String.format(message, args);
    }
}