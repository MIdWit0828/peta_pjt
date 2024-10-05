package prs.midwit.PetaProject.common.exception;

import prs.midwit.PetaProject.common.exception.type.ExceptionCode;
import lombok.Getter;

@Getter
public class ConflictException extends CustomException {
    public ConflictException(final ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
