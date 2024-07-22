package prs.midwit.pjtformat.member.domain.entity;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import prs.midwit.pjtformat.member.domain.type.MemberRole;

@Entity
@Table(name = "tbl_member")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberCode;
    private String memberId;
    private String memberPassword;
    private String memberName;
    private String memberEmail;
    @Enumerated(value = EnumType.STRING)
    private MemberRole memberRole = MemberRole.USER;
    private String refreshToken;

    public Member(String memberId, String memberPassword, String memberName, String memberEmail) {
        this.memberId = memberId;
        this.memberPassword = memberPassword;
        this.memberName = memberName;
        this.memberEmail = memberEmail;
    }

    public static Member of(String memberId, String memberPassword, String memberName, String memberEmail) {
        return new Member(
                memberId,
                memberPassword,
                memberName,
                memberEmail
        );
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
