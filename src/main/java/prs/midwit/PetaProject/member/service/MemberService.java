package prs.midwit.PetaProject.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import prs.midwit.PetaProject.auth.common.exception.NotFoundException;
import prs.midwit.PetaProject.auth.dto.LoginDto;
import prs.midwit.PetaProject.member.domain.entity.Card;
import prs.midwit.PetaProject.member.domain.entity.Member;
import prs.midwit.PetaProject.member.domain.repo.MemberRepository;
import prs.midwit.PetaProject.member.dto.req.MemberSignupRequest;
import prs.midwit.PetaProject.member.dto.res.ProfileResponse;

import static prs.midwit.PetaProject.auth.common.exception.type.ExceptionCode.NOT_FOUND_REFRESH_TOKEN;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final CardService cardService;
    private final PasswordEncoder passwordEncoder;
    public void signup(final MemberSignupRequest memberRequest) {

        final Member newMember = Member.of(
                memberRequest.getMemberId(),
                passwordEncoder.encode(memberRequest.getMemberPassword()),
                memberRequest.getMemberName(),
                memberRequest.getMemberEmail()
        );

        Member member =  memberRepository.save(newMember);
//        Card newCard = Card.simpleOf(member.getMemberCode().toString(), memberRequest.getMemberName());
//        cardService.save(newCard);

    }

    @Transactional(readOnly = true)
    public LoginDto findByMemberId(String memberId) {

        Member member = memberRepository.findByMemberId(memberId)
                                        .orElseThrow(() -> new UsernameNotFoundException("해당 아이디가 존재하지 않습니다."));

        return LoginDto.from(member);
    }

    public void updateRefreshToken(String memberId, String refreshToken) {

        Member member = memberRepository.findByMemberId(memberId)
                                        .orElseThrow(() -> new UsernameNotFoundException("해당 아이디가 존재하지 않습니다."));
        member.updateRefreshToken(refreshToken);

    }

    @Transactional(readOnly = true)
    public LoginDto findByRefreshToken(String refreshToken) {
        Member member = memberRepository.findByRefreshToken(refreshToken)
                                        .orElseThrow(() -> new NotFoundException(NOT_FOUND_REFRESH_TOKEN));

        return LoginDto.from(member);
    }

    @Transactional(readOnly = true)
    public ProfileResponse getProfile(String memberId) {

        Member member = memberRepository.findByMemberId(memberId)
                                        .orElseThrow(() -> new UsernameNotFoundException("해당 아이디가 존재하지 않습니다."));

        return ProfileResponse.from(member);

    }
}
