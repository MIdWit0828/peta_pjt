package prs.midwit.PetaProject.auth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import prs.midwit.PetaProject.auth.common.exception.dto.ExceptionResponse;

import java.io.IOException;

import static prs.midwit.PetaProject.auth.common.exception.type.ExceptionCode.ACCESS_DENIED;

/* 인가 실패 시 동작 */
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Autowired
    private ObjectMapper objectMapper;
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        /* 리소스에 접근 권한이 없는데 접근하여 인가 되지 않은 경우 => 403 오류 */
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(new ExceptionResponse(ACCESS_DENIED)));
    }
}
