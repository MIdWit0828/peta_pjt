package prs.midwit.PetaProject.common.exception;

import prs.midwit.PetaProject.common.exception.type.ExceptionCode;
import lombok.Getter;

@Getter
public class ServerInternalException extends CustomException {
    public ServerInternalException(final ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
