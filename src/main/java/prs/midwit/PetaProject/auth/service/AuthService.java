package prs.midwit.PetaProject.auth.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import prs.midwit.PetaProject.auth.dto.LoginDto;
import prs.midwit.PetaProject.auth.dto.TokenDto;
import prs.midwit.PetaProject.auth.type.CustomUser;
import prs.midwit.PetaProject.auth.util.TokenUtils;
import prs.midwit.PetaProject.member.service.MemberService;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {


    private static final Logger log = LoggerFactory.getLogger(AuthService.class);
    private final MemberService memberService;

    @Override
    public UserDetails loadUserByUsername(String memberId) throws UsernameNotFoundException {

        LoginDto loginDto = memberService.findByMemberId(memberId);

        return User.builder()
                   .username(loginDto.getMemberId())
                   .password(loginDto.getMemberPassword())
                   .roles(loginDto.getMemberRole().name())
                   .build();
    }

    public void updateRefreshToken(String memberId, String refreshToken) {
        log.info("리프레시 토큰 저장실행됨");
        memberService.updateRefreshToken(memberId, refreshToken);
    }

    public TokenDto checkRefreshTokenAndReIssueToken(String refreshToken) {

        LoginDto loginDto = memberService.findByRefreshToken(refreshToken);
        String reIssuedRefreshToken = TokenUtils.createRefreshToken();
        String reIssuedAccessToken = TokenUtils.createAccessToken(getMemberInfo(loginDto));
        memberService.updateRefreshToken(loginDto.getMemberId(), reIssuedRefreshToken);
        return TokenDto.of(reIssuedAccessToken, reIssuedRefreshToken);
    }

    private Map<String,Object> getMemberInfo(LoginDto loginDto) {
        return Map.of(
                "memberId", loginDto.getMemberId(),
                "memberRole", "ROLE_" + loginDto.getMemberRole()
        );
    }

    public void saveAuthentication(String memberId) {

        LoginDto loginDto = memberService.findByMemberId(memberId);

        UserDetails user = User.builder()
                               .username(loginDto.getMemberId())
                               .password(loginDto.getMemberPassword())
                               .roles(loginDto.getMemberRole().name())
                               .build();

        CustomUser customUser = new CustomUser(loginDto.getMemberCode(), user);

        Authentication authentication
                = new UsernamePasswordAuthenticationToken(customUser, null, customUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }








}
