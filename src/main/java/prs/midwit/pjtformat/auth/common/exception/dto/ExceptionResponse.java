package prs.midwit.pjtformat.auth.common.exception.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import prs.midwit.pjtformat.auth.common.exception.type.ExceptionCode;

@Getter
@RequiredArgsConstructor
public class ExceptionResponse {
    private final int code;
    private final String message;

    public ExceptionResponse(ExceptionCode exceptionCode) {
        this.code = exceptionCode.getCode();
        this.message = exceptionCode.getMessage();
    }

    public static ExceptionResponse of(int code, String message) {
        return new ExceptionResponse(code, message);
    }
}
