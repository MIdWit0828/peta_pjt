CREATE TABLE `tbl_attachment`
(
    `attachment_code`    VARCHAR(255) NOT NULL COMMENT '첨부파일 코드',
    `origin_name`    VARCHAR(255) NOT NULL COMMENT '본 파일명',
    `safe_name`    VARCHAR(255) NOT NULL COMMENT '안전한 파일명',
    `file_path`    VARCHAR(255) NOT NULL COMMENT '파일 경로',
    `file_type`    VARCHAR(50) NOT NULL COMMENT '파일 구분',
    PRIMARY KEY ( `attachment_code` )
) COMMENT = '첨부파일';


CREATE TABLE `tbl_auth-member`
(
    `auth_member_code`    VARCHAR(255) NOT NULL COMMENT '권한-맴버 코드',
    `auth_code`    VARCHAR(255) NOT NULL COMMENT '권한 코드',
    `member_code`    VARCHAR(255) NOT NULL COMMENT '맴버 코드',
    PRIMARY KEY ( `auth_member_code` )
) COMMENT = '권한-맴버';


CREATE TABLE `tbl_authority`
(
    `auth_code`    VARCHAR(255) NOT NULL COMMENT '권한 코드',
    `auth_name`    VARCHAR(50) NOT NULL COMMENT '권한 이름',
    `auth_level`    INT NOT NULL COMMENT '권한 수준',
    PRIMARY KEY ( `auth_code` )
) COMMENT = '권한';


CREATE TABLE `tbl_card`
(
    `card_code`    VARCHAR(255) NOT NULL COMMENT '명함코드',
    `member_code`    VARCHAR(255) NOT NULL COMMENT '맴버 코드',
    `member_name`    VARCHAR(50) NOT NULL COMMENT '이름',
    `group_name`    VARCHAR(50) NOT NULL COMMENT '조직명',
    `position_name`    VARCHAR(50) NOT NULL COMMENT '직함',
    `address`    VARCHAR(100) NOT NULL COMMENT '주소',
    `security_code`    VARCHAR(100) NOT NULL COMMENT '보안코드',
    PRIMARY KEY ( `card_code` )
) COMMENT = '명함';


CREATE TABLE `tbl_data`
(
    `data_code`    VARCHAR(255) NOT NULL COMMENT '자료 코드',
    `origin_name`    VARCHAR(255) NOT NULL COMMENT '본 자료명',
    `safe_name`    VARCHAR(255) NOT NULL COMMENT '안전한 자료명',
    `file_path`    VARCHAR(255) NOT NULL COMMENT '자료 경로',
    `data_cmt`    VARCHAR(255) COMMENT '자료 요약',
    `member_code`    VARCHAR(255) NOT NULL COMMENT '등록자 코드',
    `is_delete`    BOOLEAN NOT NULL COMMENT '삭제여부',
    `access_level_code`    VARCHAR(255) NOT NULL COMMENT '자료 접근 수준 코드',
    `data_category_code`    VARCHAR(255) NOT NULL COMMENT '자료 구분 코드',
    PRIMARY KEY ( `data_code` )
) COMMENT = '자료';


CREATE TABLE `tbl_data_access_level`
(
    `access_level_code`    VARCHAR(255) NOT NULL COMMENT '자료 접근 수준 코드',
    `reading_level`    INT NOT NULL COMMENT '열람 수준',
    `storage_level`    INT NOT NULL COMMENT '저장 수준',
    `modify_level`    INT NOT NULL COMMENT '수정 수준',
    `delete_level`    INT NOT NULL COMMENT '삭제 수준',
    PRIMARY KEY ( `access_level_code` )
) COMMENT = '자료 접근 수준';


CREATE TABLE `tbl_data_category`
(
    `data_category_code`    VARCHAR(255) NOT NULL COMMENT '자료 구분 코드',
    `data_category_name`    VARCHAR(50) NOT NULL COMMENT '자료 구분 이름',
    PRIMARY KEY ( `data_category_code` )
) COMMENT = '자료구분';


CREATE TABLE `tbl_member`
(
    `member_code`    VARCHAR(255) NOT NULL COMMENT '맴버 코드',
    `member_id`    VARCHAR(100) NOT NULL COMMENT '맴버 아이디',
    `member_pwd`    VARCHAR(100) NOT NULL COMMENT '맴버 비밀번호',
    `email`    VARCHAR(100) COMMENT '이메일',
    `phone`    VARCHAR(50) COMMENT '연락처',
    `attachment_code`    VARCHAR(255) NOT NULL COMMENT '명함사진 코드',
    `reg_dt`    DATE NOT NULL COMMENT '가입일자',
    `del_dt`    DATE COMMENT '탈퇴일자',
    `is_delete`    BOOLEAN COMMENT '탈퇴여부',
    PRIMARY KEY ( `member_code` )
) COMMENT = '맴버';



