package prs.midwit.PetaProject.attachment.presentation;


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

import static prs.midwit.PetaProject.attachment.util.ImageConverter.convertSlideToImage;
import static prs.midwit.PetaProject.attachment.util.ImageConverter.convertWordPageToImage;
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

        attService.save(file, fileType,fileDir);


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
            log.info("{}",dto.getActualFilePath());

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

    @GetMapping("/atts/word/{fileCode}/{pageNumber}")
    public ResponseEntity<byte[]> getWordPageAsImage(@PathVariable Long fileCode, @PathVariable int pageNumber) throws IOException {

        AttDto dto = attService.findAttByAttCode(fileCode);


        //타입이 맞지 않을 경우 탈출
        if (!Objects.equals(dto.getExtension(), ".doc") && !Objects.equals(dto.getExtension(), ".docx")) {
            log.info("확인한 타입 : {}",dto.getExtension());
            throw new BadRequestException(TYPE_DOSE_NOT_MATCH);
        }

        String filePath = dto.getActualFilePath();
        BufferedImage image = convertWordPageToImage(filePath, pageNumber);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        byte[] imageBytes = baos.toByteArray();

        return ResponseEntity.ok()
                             .header(HttpHeaders.CONTENT_TYPE, "image/png")
                             .body(imageBytes);
    }

    @GetMapping("/atts/ppt/{fileCode}/{slideNumber}")
    public ResponseEntity<byte[]> getSlideAsImage(@PathVariable Long fileCode, @PathVariable int slideNumber) throws IOException {

        AttDto dto = attService.findAttByAttCode(fileCode);


        //타입이 맞지 않을 경우 탈출
        if (!Objects.equals(dto.getExtension(), ".ppt") && !Objects.equals(dto.getExtension(), ".pptx")) {
            throw new BadRequestException(TYPE_DOSE_NOT_MATCH);
        }
        String filePath = dto.getActualFilePath();

        BufferedImage image = convertSlideToImage(filePath, slideNumber);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        byte[] imageBytes = baos.toByteArray();

        return ResponseEntity.ok()
                             .header(HttpHeaders.CONTENT_TYPE, "image/png")
                             .body(imageBytes);
    }
}
