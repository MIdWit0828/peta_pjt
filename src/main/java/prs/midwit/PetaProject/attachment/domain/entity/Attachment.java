package prs.midwit.PetaProject.attachment.domain.entity;

import jakarta.persistence.*;
import prs.midwit.PetaProject.attachment.domain.type.FileType;

@Entity
@Table(name = "tbl_attachment")
public class Attachment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attachmentCode;
    private String originName;
    private String safeName;
    private String filePath;
    private FileType fileType;
}
