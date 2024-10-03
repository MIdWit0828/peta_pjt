package prs.midwit.PetaProject.attachment.presentation;


import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;

@Controller
@RequestMapping("api/v1/atts")
public class AttController {

    private final String fileDir = "src/main/resources/file/";

    @PostMapping("/upload")
    public ResponseEntity<Void> upload(
            @RequestParam("file")MultipartFile file
            ) {
        if (file.isEmpty()) {
            //TODO 파일이 비었다는 예외처리 하기
        }
        try {
            //디렉토리가 존재하지 않을경우
            File directory = new File(fileDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            //파일 저장 경로 설정
            File destinationFile = new File(fileDir + file.getOriginalFilename());
            file.transferTo(destinationFile);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok().build();
    }
}
