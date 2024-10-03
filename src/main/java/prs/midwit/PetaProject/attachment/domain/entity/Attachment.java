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

    public Attachment(String originalFilename, String safeName, String finalDir, FileType fileType) {
        this.originName = originalFilename;
        this.safeName = safeName;
        this.filePath = finalDir;
        this.fileType = fileType;
    }

    public static Attachment of(String originalFilename, String safeName, String finalDir, String fileType) {
        return new Attachment(
                originalFilename,
                safeName,
                finalDir,
                FileType.valueOf(fileType)
        );
    }

}
