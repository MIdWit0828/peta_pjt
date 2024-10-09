package prs.midwit.PetaProject.attachment.presentation;


import com.lowagie.text.DocumentException;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.jni.FileInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import prs.midwit.PetaProject.attachment.dto.AttDto;
import prs.midwit.PetaProject.attachment.dto.res.AttListResponse;
import prs.midwit.PetaProject.attachment.service.AttService;
import prs.midwit.PetaProject.common.exception.BadRequestException;
import prs.midwit.PetaProject.common.paging.Pagenation;
import prs.midwit.PetaProject.common.paging.PagingButtonInfo;
import prs.midwit.PetaProject.common.paging.PagingResponse;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import static prs.midwit.PetaProject.attachment.util.ImageConverter.*;
import static prs.midwit.PetaProject.common.exception.type.ExceptionCode.TYPE_DOSE_NOT_MATCH;

@Controller
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class AttController {

    private static final Logger log = LoggerFactory.getLogger(AttController.class);
    private final AttService attService;

    @Value("${file.uploaded-dir}")
    private String fileDir;


    @PostMapping("/atts/upload")
    public ResponseEntity<Void> upload(
            @RequestPart final MultipartFile file,
            @RequestPart final String fileType
    ) {

        attService.save(file, fileType, fileDir);


        return ResponseEntity.ok().build();
    }

    @GetMapping("/atts")
    public ResponseEntity<PagingResponse> getList(
            @RequestParam(defaultValue = "1") final Integer page,
            @RequestParam(required = false) final String fileType
    ) {
        final Page<AttListResponse> pages = attService.findAllList(page, fileType);
        final PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo(pages);
        final PagingResponse res = PagingResponse.of(pages, pagingButtonInfo);

        return ResponseEntity.ok(res);
    }

    @GetMapping("/atts/download/{fileCode}")
    public ResponseEntity<Resource> downloadFile(
            @PathVariable Long fileCode
    ) {
        AttDto dto = attService.findAttByAttCode(fileCode);

        try {

            Path filePath = Paths.get(dto.getActualFilePath());
            Resource resource = new UrlResource(filePath.toUri());
            log.info("{}", dto.getActualFilePath());

            if (resource.exists()) {
                String encodedFileName = URLEncoder.encode(dto.getOriginName(), StandardCharsets.UTF_8)
                                                   .replaceAll("\\+", "%20"); // 공백을 제대로 처리하기 위해 +를 %20으로 변환

                return ResponseEntity.ok()
                                     .contentType(MediaType.APPLICATION_OCTET_STREAM)
                                     .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedFileName + "\"")
                                     .body(resource);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/atts/pre-view/{fileCode}/{pageNumber}")
    public ResponseEntity<byte[]> getPreViewImage(@PathVariable Long fileCode, @PathVariable int pageNumber) throws IOException, DocumentException, FontFormatException {
        AttDto dto = attService.findAttByAttCode(fileCode);
        String filePath = dto.getActualFilePath();

        BufferedImage image;

        log.info("조회한 파일의 확장자 : {}", dto.getExtension());
        if (Objects.equals(dto.getExtension(), ".pdf")) {
            image = convertPdfPageToImage(filePath, pageNumber);
        } else if (Objects.equals(dto.getExtension(), ".ppt") || Objects.equals(dto.getExtension(), ".pptx")) {
            image = convertSlideToImage(filePath, pageNumber);
        }
        /*
        else if (Objects.equals(dto.getExtension(), ".doc") || Objects.equals(dto.getExtension(), ".docx")) {
            image = convertWordPageToImage(filePath, pageNumber);
//            image = convertWordPageToImageAspose(filePath, pageNumber);
        } */
        else {
            throw new BadRequestException(TYPE_DOSE_NOT_MATCH);
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        byte[] imageBytes = baos.toByteArray();

        return ResponseEntity.ok()
                             .header(HttpHeaders.CONTENT_TYPE, "image/png")
                             .body(imageBytes);
    }

    @DeleteMapping("/atts/{fileCode}")
    public ResponseEntity<Void> deleteFile(@PathVariable Long fileCode) {
        attService.delete(fileCode);

        return ResponseEntity.noContent().build();
    }
}
