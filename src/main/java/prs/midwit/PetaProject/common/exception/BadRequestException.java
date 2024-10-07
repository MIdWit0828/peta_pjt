package prs.midwit.PetaProject.common.exception;

import lombok.Getter;
import prs.midwit.PetaProject.common.exception.type.ExceptionCode;

@Getter
public class BadRequestException extends CustomException {
    public BadRequestException(final ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
