package prs.midwit.pjtformat.member.domain.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import prs.midwit.pjtformat.member.domain.entity.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long> {
    Optional<Member> findByRefreshToken(String refreshToken);

    Optional<Member> findByMemberId(String memberId);
}
