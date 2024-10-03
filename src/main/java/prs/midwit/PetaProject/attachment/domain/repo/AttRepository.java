package prs.midwit.PetaProject.attachment.domain.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import prs.midwit.PetaProject.attachment.domain.entity.Attachment;

public interface AttRepository extends JpaRepository<Attachment,Long> {
}
