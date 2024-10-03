package prs.midwit.PetaProject.member.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "tbl_Card")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cardCode;
    private Long memberCode;
    private String memberName;
    private String groupName;
    private String positionName;
    private String address;
    private String securityCode;

    public Card(Long memberCode, String memberName) {
        this.memberCode = memberCode;
        this.memberName = memberName;
    }

    public static Card simpleOf(Long memberCode, String memberName) {
        return new Card(memberCode, memberName);
    }
}
