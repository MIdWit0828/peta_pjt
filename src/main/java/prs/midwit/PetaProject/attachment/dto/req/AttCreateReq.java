package prs.midwit.PetaProject.attachment.dto.req;

import lombok.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestPart;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AttCreateReq {
    private String fileType;
    private Long sessionCode;
    private Long dayNum;

}
