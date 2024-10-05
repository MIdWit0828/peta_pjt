package prs.midwit.PetaProject.attachment.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;
import prs.midwit.PetaProject.attachment.domain.type.FileType;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_attachment")
@Getter
public class Attachment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attachmentCode;
    private String originName;
    private String safeName;
    private String filePath;
    @Enumerated(EnumType.STRING)

    private FileType fileType;

    @CreationTimestamp
    private LocalDateTime fileCreateDt;

    public Attachment(String originalFilename, String safeName, String finalDir, FileType fileType) {
        this.originName = originalFilename;
        this.safeName = safeName;
        this.filePath = finalDir;
        this.fileType = fileType;
    }

    public Attachment() {

    }

    public static Attachment of(String originalFilename, String safeName, String finalDir, String fileType) {
        return new Attachment(
                originalFilename,
                safeName,
                finalDir,
                FileType.nameOf(fileType)
        );
    }

}
