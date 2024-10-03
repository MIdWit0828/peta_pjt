package prs.midwit.PetaProject.member.domain.entity;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import prs.midwit.PetaProject.member.domain.type.MemberRole;

import java.time.LocalDate;
import java.util.UUID;

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
    private String memberPwd;
    private String memberName;
    private String email;
    private String phone;
    private String attachmentCode;
    private LocalDate regDt;
    private LocalDate delDt;
    private boolean isDelete;
    private String refreshToken;
    @Enumerated(EnumType.STRING)
    private MemberRole memberRole;

    public Member(String memberId, String memberPassword,String memberName, String email) {
        this.memberId = memberId;
        this.memberPwd = memberPassword;
        this.memberName = memberName;
        this.email = email;
    }

    @PrePersist
    protected void onCreate() {
        this.regDt = LocalDate.now();
        this.memberRole = MemberRole.USER;
    }

    public static Member of(String memberId, String memberPassword,String memberName, String memberEmail) {
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
