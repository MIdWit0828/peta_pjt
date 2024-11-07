package prs.midwit.PetaProject.attachment.dto.res;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@ToString
@Getter
@Setter
@AllArgsConstructor
public class AttsRes {
    private List<AttListResponse> contents;
}
