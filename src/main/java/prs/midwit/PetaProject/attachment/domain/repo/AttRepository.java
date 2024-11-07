package prs.midwit.PetaProject.attachment.domain.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import prs.midwit.PetaProject.attachment.domain.entity.Attachment;

import java.util.List;

public interface AttRepository extends JpaRepository<Attachment,Long> {


    List<Attachment> findByFileType(String fileType);
}
