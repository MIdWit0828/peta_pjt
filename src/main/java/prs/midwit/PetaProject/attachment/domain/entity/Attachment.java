package prs.midwit.PetaProject.attachment.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.bcel.verifier.statics.LONG_Upper;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;
import prs.midwit.PetaProject.attachment.domain.type.FileType;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_attachment")
@Getter
@AllArgsConstructor
public class Attachment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attachmentCode;
    private Long memberCode;
    private Long sessionCode;
    private Long dayNum;
    private String originName;
    private String safeName;
    private String filePath;
    @Enumerated(EnumType.STRING)

    private FileType fileType;

    @CreationTimestamp
    private LocalDateTime fileCreateDt;
    private Long ptOrder;

    public Attachment(String originalFilename,Long memberCode,Long sessionCode,Long dayNum, String safeName, String finalDir, FileType fileType,Long ptOrder) {
        this.originName = originalFilename;
        this.memberCode = memberCode;
        this.sessionCode = sessionCode;
        this.dayNum = dayNum;
        this.safeName = safeName;
        this.filePath = finalDir;
        this.fileType = fileType;
        this.ptOrder = ptOrder;
    }

    public Attachment() {

    }

    public static Attachment of(String originalFilename, Long memberCode, String safeName, String finalDir, String fileType,Long sessionCode,Long dayNum,Long ptOrder) {
        return new Attachment(
                originalFilename,
                memberCode,
                sessionCode,
                dayNum,
                safeName,
                finalDir,
                FileType.nameOf(fileType),
                ptOrder
        );
    }

}
