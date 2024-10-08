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
    private String originName;
    private String filePath;
    private FileType fileType;
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime fileCreateTime;

    public static AttListResponse from(Attachment att) {
        return new AttListResponse(
              att.getAttachmentCode(),
              att.getOriginName(),
              att.getFilePath(),
              att.getFileType(),
              att.getFileCreateDt()
        );
    }
}
