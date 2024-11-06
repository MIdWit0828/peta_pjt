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
    private String memberName;
    private String safeName;
    private String filePath;

    public AttDto(String originName, String safeName, String filePath) {
        this.originName = originName;
        this.safeName = safeName;
        this.filePath = filePath;
    }

    public static AttDto from(Attachment attachment) {
        return new AttDto(
                attachment.getOriginName(),
                attachment.getSafeName(),
                attachment.getFilePath()
        );
    }

    public String getActualFilePath() {
        return filePath + safeName + getExtension();
    }


    public String getExtension() {
        int dotIndex = originName.lastIndexOf(".");
        String extension = "";
        if (dotIndex != -1) {
            extension = originName.substring(dotIndex);
        }
        return extension;
    }
}
