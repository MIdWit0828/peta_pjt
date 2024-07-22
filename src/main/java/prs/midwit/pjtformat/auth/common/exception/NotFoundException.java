package prs.midwit.pjtformat.auth.common.exception;

import lombok.Getter;
import prs.midwit.pjtformat.auth.common.exception.type.ExceptionCode;

@Getter
public class NotFoundException extends CustomException {
    public NotFoundException(final ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
