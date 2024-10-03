package prs.midwit.PetaProject.attachment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import prs.midwit.PetaProject.attachment.domain.entity.Attachment;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AttService {
    private final String fileDir = "src/main/resources/file/";

    private String getRandomName() { return UUID.randomUUID().toString().replace("-", ""); }

    public void save(MultipartFile file, String fileType) {
        String finalDir = fileDir + fileType + "/";
        String safeName = getRandomName();
        Attachment attachment = Attachment.of(file.getOriginalFilename(), safeName, finalDir, fileType);

//        Path uploadPath = Paths.get()

        try(InputStream inputStream = file.getInputStream()) {
            //디렉토리가 존재하지 않을경우
            File directory = new File(finalDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            //파일 저장 경로 설정
            File destinationFile = new File(finalDir + safeName);
            file.transferTo(destinationFile);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
