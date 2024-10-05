package prs.midwit.PetaProject.common.exception;

import prs.midwit.PetaProject.common.exception.type.ExceptionCode;
import lombok.Getter;

@Getter
public class NotFoundException extends CustomException {
    public NotFoundException(final ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
