package prs.midwit.PetaProject.auth.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import prs.midwit.PetaProject.member.domain.entity.Member;
import prs.midwit.PetaProject.member.domain.type.MemberRole;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginDto {

    private final Long memberCode;
    private final String memberId;
    private final String memberPassword;
    private final MemberRole memberRole;

    public static LoginDto from(Member member) {
        return new LoginDto(
                member.getMemberCode(),
                member.getMemberId(),
                member.getMemberPwd(),
                member.getMemberRole()
        );
    }
}
