package prs.midwit.PetaProject.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
/* 스프링 시큐리티의 기존 UsernamePasswordAuthenticationFilter를 대체할 CustomFilter */
public class CustomAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    @Autowired
    private ObjectMapper objectMapper;
    public CustomAuthenticationFilter() {
        /* 해당 요청이 올 때 이 필터가 동작하도록 설정 */
        super(new AntPathRequestMatcher("/api/v1/members/login", "POST"));
    }

    /* 설정 된 요청이 발생하면 필터의 메소드가 호출 된다. */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {

        /* Request Content Type 확인 */
        if(request.getContentType() == null || !request.getContentType().equals("application/json")) {
            throw new AuthenticationServiceException("Content-Type not supported");
        }

        /* Request Body 읽어오기 */
        String body = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);

        /* JSON 문자열을 Java Map 타입으로 변환 */
        Map<String, String> bodyMap = objectMapper.readValue(body, Map.class);

        /* key 값을 전달해서 Map에서 id, pwd 꺼내기 */
        String memberId = bodyMap.get("memberId");
        String memberPassword = bodyMap.get("memberPassword");

        log.info("CustomAuthenticationFilter memberId : {}", memberId);
        log.info("CustomAuthenticationFilter memberPassword : {}", memberPassword);

        /* id와 pwd가 설정 된 인증 토큰 생성 */
        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(memberId, memberPassword);

        /* Authentication Manager에게 Authentication Token 전달 */
        return this.getAuthenticationManager().authenticate(authenticationToken);
    }
}

