package prs.midwit.PetaProject.auth.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import prs.midwit.PetaProject.auth.service.AuthService;
import prs.midwit.PetaProject.auth.util.TokenUtils;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final AuthService authService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        /* 로그인 성공 후 저장 된 인증 객체에서 정보를 꺼낸다. */
        Map<String, Object> memberInfo = getMemberInfo(authentication);
        log.info("로그인 성공 후 인증 객체에서 꺼낸 정보 : {}", memberInfo);

        /* access token과 refresh token 생성 */
        String accessToken = TokenUtils.createAccessToken(memberInfo);
        String refreshToken = TokenUtils.createRefreshToken();
        log.info("발급 된 accessToken : {}", accessToken);
        log.info("발급 된 refreshToken : {}", refreshToken);

        /* 발급한 refresh token을 DB에 저장해둔다. */
        authService.updateRefreshToken((String)memberInfo.get("memberId"), refreshToken);

        /* 응답 헤더에 발급 된 토큰을 담는다. */
        response.setHeader("Access-Token", accessToken);
        response.setHeader("Refresh-Token", refreshToken);
        response.setStatus(HttpServletResponse.SC_OK);

    }

    private Map<String, Object> getMemberInfo(Authentication authentication) {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String memberRole = userDetails.getAuthorities()
                                       .stream().map(auth -> auth.getAuthority().toString())
                                       .collect(Collectors.joining());

        return Map.of(
                "memberId", userDetails.getUsername(),
                "memberRole", memberRole
        );


    }
}
