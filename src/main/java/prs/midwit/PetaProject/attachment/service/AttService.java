package prs.midwit.PetaProject.attachment.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import prs.midwit.PetaProject.attachment.domain.entity.Attachment;
import prs.midwit.PetaProject.attachment.domain.repo.AttRepository;
import prs.midwit.PetaProject.attachment.dto.AttDto;
import prs.midwit.PetaProject.attachment.dto.res.AttListResponse;
import prs.midwit.PetaProject.common.exception.NotFoundException;
import prs.midwit.PetaProject.common.exception.type.ExceptionCode;
import prs.midwit.PetaProject.common.utils.FileUploadUtils;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AttService {

    private static final Logger log = LoggerFactory.getLogger(AttService.class);
    private final AttRepository attRepository;


    private String getRandomName() { return UUID.randomUUID().toString().replace("-", ""); }

    private Pageable getPageable(final Integer page) {
        return PageRequest.of(page - 1, 10, Sort.by("fileCreateDt").descending());
    }

    public void save(MultipartFile file, String fileType, String fileDir) {
        log.info("저장소 위치...{}",fileDir);
        String finalDir = fileDir + fileType + "/";
        String safeName = getRandomName();
        Attachment attachment = Attachment.of(file.getOriginalFilename(), safeName, finalDir, fileType);

        //디렉토리에 저장
        FileUploadUtils.saveFile(finalDir, safeName, file);

        //db에 저장
        attRepository.save(attachment);


    }

    public Page<AttListResponse> findAllList(Integer page, String fileType) {
        Page<Attachment> atts =null;
        if (fileType == null || fileType == "") {
            atts = attRepository.findAll(getPageable(page));
        } else {
            atts = attRepository.findByFileType(getPageable(page),fileType);
        }

        return atts.map(AttListResponse::from);
    }

    public AttDto findAttByAttCode(Long attachmentCode) {
        Attachment attachment = attRepository.findById(attachmentCode).orElseThrow(
                () -> new NotFoundException(ExceptionCode.NOT_FOUND_ATT_CODE)
        );
        return AttDto.from(attachment);

    }
}
