package prs.midwit.PetaProject.member.domain.entity;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import prs.midwit.PetaProject.member.domain.type.MemberRole;

import java.time.LocalDate;

@Entity
@Table(name = "tbl_member")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Member {
    @Id
    @GeneratedValue(generator = "uuids")
//    @GenericGenerator(name= "uuid2",strategy = "uuid")
    private String memberCode;
    private String memberId;
    private String memberPwd;
    private String email;
    private String phone;
    private String attachmentCode;
    @GeneratedValue
    private LocalDate regDt;
    private LocalDate delDt;
    private boolean isDelete;
    private String refreshToken;

    public Member(String memberId, String memberPassword, String email) {
        this.memberId = memberId;
        this.memberPwd = memberPassword;
        this.email = email;
    }

    @PrePersist
    protected void onCreate() {
        this.regDt = LocalDate.now();
    }

    public static Member of(String memberId, String memberPassword, String memberEmail) {
        return new Member(
                memberId,
                memberPassword,
                memberEmail
        );
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
