package prs.midwit.PetaProject.attachment.presentation;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import prs.midwit.PetaProject.attachment.service.AttService;

import java.io.File;
import java.io.IOException;

@Controller
@RequestMapping("api/v1/atts")
@RequiredArgsConstructor
public class AttController {

    private final AttService attService;

    private final String fileDir = "src/main/resources/file/";

    @PostMapping("/upload")
    public ResponseEntity<Void> upload(
            @RequestPart final MultipartFile file,
            @RequestPart final String fileType
            ) {
        if (file.isEmpty()) {
            //TODO 파일이 비었다는 예외처리 하기
        }
        attService.save(file, fileType);

        return ResponseEntity.ok().build();
    }
}
