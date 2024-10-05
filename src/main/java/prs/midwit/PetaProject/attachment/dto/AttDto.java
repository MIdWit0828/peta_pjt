package prs.midwit.PetaProject.attachment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import prs.midwit.PetaProject.attachment.domain.entity.Attachment;
import prs.midwit.PetaProject.attachment.domain.type.FileType;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class AttDto {
    private String originName;
    private String safeName;
    private String filePath;

    public static AttDto from(Attachment attachment) {
        return new AttDto(
                attachment.getOriginName(),
                attachment.getSafeName(),
                attachment.getFilePath()
        );
    }
}
