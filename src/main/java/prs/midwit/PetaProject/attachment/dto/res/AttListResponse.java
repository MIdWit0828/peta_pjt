package prs.midwit.PetaProject.attachment.dto.res;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import prs.midwit.PetaProject.attachment.domain.entity.Attachment;
import prs.midwit.PetaProject.attachment.domain.type.FileType;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AttListResponse {
    private Long attCode;
    private Long memberCode;
    private String memberName;
    private Long sessionCode;
    private Long dayNum;
    private String originName;
    private String filePath;
    private FileType fileType;
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime fileCreateTime;

    public AttListResponse(Long attCode, Long memberCode,Long sessionCode, Long dayNum, String originName, String filePath, FileType fileType, LocalDateTime fileCreateTime) {
        this.attCode = attCode;
        this.memberCode = memberCode;
        this.sessionCode = sessionCode;
        this.dayNum = dayNum;
        this.originName = originName;
        this.filePath = filePath;
        this.fileType = fileType;
        this.fileCreateTime = fileCreateTime;
    }

    public static AttListResponse from(Attachment att) {
        return new AttListResponse(
              att.getAttachmentCode(),
              att.getMemberCode(),
              att.getSessionCode(),
              att.getDayNum(),
              att.getOriginName(),
              att.getFilePath(),
              att.getFileType(),
              att.getFileCreateDt()
        );
    }
}
