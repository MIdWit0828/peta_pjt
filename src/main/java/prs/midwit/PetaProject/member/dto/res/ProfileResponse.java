package prs.midwit.PetaProject.member.dto.res;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import prs.midwit.PetaProject.member.domain.entity.Member;

@Getter
@RequiredArgsConstructor
public class ProfileResponse {

    private final String memberId;
    private final String memberName;
    private final String memberEmail;


    public static ProfileResponse from(Member member) {

        return new ProfileResponse(
                member.getMemberId(),
                member.getMemberName(),
                member.getMemberEmail()
        );
    }
}
