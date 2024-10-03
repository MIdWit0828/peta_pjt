package prs.midwit.PetaProject.member.domain.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import prs.midwit.PetaProject.member.domain.entity.Card;

public interface CardRepository extends JpaRepository<Card,String> {
}
