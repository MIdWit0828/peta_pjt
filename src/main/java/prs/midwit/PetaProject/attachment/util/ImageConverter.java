package prs.midwit.PetaProject.attachment.util;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.aspose.words.Document;
import prs.midwit.PetaProject.common.exception.BadRequestException;

import static prs.midwit.PetaProject.common.exception.type.ExceptionCode.PAGE_OVER_BAD_REQUEST;

public class ImageConverter {

    public static BufferedImage convertWordPageToImage(String filePath, int pageNumber) throws IOException, FontFormatException {
        // 폰트 파일 경로
        String fontPath = "src/main/resources/fonts/malgun.ttf"; // 실제 폰트 파일 경로로 변경하세요
        Font font = Font.createFont(Font.TRUETYPE_FONT, new File(fontPath)).deriveFont(12f); // 폰트 크기 설정

        try (FileInputStream fis = new FileInputStream(filePath); XWPFDocument document = new XWPFDocument(fis)) {
            List<XWPFParagraph> paragraphs = document.getParagraphs();

            // 페이지 기준으로 단락 수 계산
            int paragraphsPerPage = 25; // 한 페이지에 출력할 단락 수 설정
            int totalPages = (int) Math.ceil((double) paragraphs.size() / paragraphsPerPage); // 총 페이지 수 계산

            // 페이지 번호가 유효한지 확인
            if (pageNumber < 1 || pageNumber > totalPages) {
                throw new IllegalArgumentException("Invalid page number");
            }

            // 시작 단락과 끝 단락 인덱스 계산
            int startParagraph = (pageNumber - 1) * paragraphsPerPage;
            int endParagraph = Math.min(startParagraph + paragraphsPerPage, paragraphs.size());

            // 이미지 크기 설정
            int imageWidth = 800; // 고정 너비
            int imageHeight = 1000; // 고정 높이

            // 이미지 생성
            BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = image.createGraphics();
            g2d.setPaint(Color.WHITE);
            g2d.fillRect(0, 0, image.getWidth(), image.getHeight());

            g2d.setFont(font); // 로드한 폰트 설정
            g2d.setPaint(Color.BLACK);

            FontMetrics metrics = g2d.getFontMetrics();
            int lineHeight = metrics.getHeight(); // 단락 높이

            // 각 단락의 텍스트를 이미지에 그리기
            int yPosition = 30; // 시작 Y 위치
            int maxCharsPerLine = 50; // 한 줄에 허용할 최대 문자 수

            for (int i = startParagraph; i < endParagraph; i++) {
                XWPFParagraph paragraph = paragraphs.get(i);
                String text = paragraph.getText();

                // 텍스트 줄바꿈 처리
                String[] lines = wrapText(text, metrics, imageWidth - 40, maxCharsPerLine); // 여백을 고려한 너비
                for (String line : lines) {
                    // 텍스트가 이미지 높이를 초과하면 종료
                    if (yPosition + lineHeight > imageHeight) {
                        break; // 현재 페이지의 이미지 반환
                    }
                    g2d.drawString(line, 20, yPosition);
                    yPosition += lineHeight; // 다음 단락의 Y 위치 조정
                }
                yPosition += 10; // 단락 간격 추가
            }
            g2d.dispose();

            return image;
        }
    }

    // 텍스트 줄바꿈 처리 메소드
    private static String[] wrapText(String text, FontMetrics metrics, int maxWidth, int maxCharsPerLine) {
        String[] words = text.split(" ");
        StringBuilder wrappedText = new StringBuilder();
        StringBuilder line = new StringBuilder();

        for (String word : words) {
            // 최대 너비 또는 최대 문자 수를 초과하는지 확인
            if (metrics.stringWidth(line + word) < maxWidth && (line.length() + word.length() <= maxCharsPerLine)) {
                line.append(word).append(" ");
            } else {
                if (line.length() > 0) {
                    wrappedText.append(line.toString().trim()).append("\n"); // 현재 줄 추가
                }
                line = new StringBuilder(word + " "); // 새로운 줄 시작
            }
        }
        if (line.length() > 0) {
            wrappedText.append(line.toString().trim()); // 마지막 줄 추가
        }

        return wrappedText.toString().split("\n"); // 각 줄을 배열로 반환
    }

    public static BufferedImage convertWordPageToImageAspose(String filePath, int pageNumber) throws IOException {
        // PDF 파일 경로 설정 (Word 파일과 동일한 위치에 PDF 파일 생성)
        String pdfFilePath = filePath.replace(".docx", ".pdf").replace(".doc", ".pdf");

        // PDF 파일이 이미 생성된 경우, 변환 과정을 스킵하고 이미지를 바로 생성
        File pdfFile = new File(pdfFilePath);
        if (!pdfFile.exists()) {
            // Aspose 라이브러리를 사용하여 Word 파일을 PDF로 변환
            try (FileInputStream fis = new FileInputStream(filePath)) {
                com.aspose.words.Document wordDocument = new com.aspose.words.Document(fis);
                wordDocument.save(pdfFilePath, com.aspose.words.SaveFormat.PDF);
            } catch (Exception e) {
                throw new IOException("Failed to convert Word to PDF", e);
            }
        }

        // PDF를 이미지로 변환
        return convertPdfPageToImage(pdfFilePath, pageNumber);
    }


    private void convertWordToPdf(String wordFilePath, String pdfFilePath) throws Exception {
        Document doc = new Document(wordFilePath);
        doc.save(pdfFilePath); // Word를 PDF로 저장
    }


    public static BufferedImage convertSlideToImage(String filePath, int slideNumber) throws IOException {
        try (FileInputStream fis = new FileInputStream(filePath); XMLSlideShow ppt = new XMLSlideShow(fis)) {
            List<XSLFSlide> slides = ppt.getSlides();

            if (slideNumber < 0 || slideNumber >= slides.size()) {
                throw new BadRequestException(PAGE_OVER_BAD_REQUEST);
            }
            XSLFSlide slide = slides.get(slideNumber);

            // 슬라이드의 크기를 가져옴
            Dimension pageSize = ppt.getPageSize();
            int slideWidth = (int) pageSize.getWidth();
            int slideHeight = (int) pageSize.getHeight();

            // 슬라이드 크기에 맞게 BufferedImage 생성
            BufferedImage img = new BufferedImage(slideWidth, slideHeight, BufferedImage.TYPE_INT_RGB);

            // Graphics2D 객체 생성
            Graphics2D graphics = img.createGraphics();

            // 슬라이드를 그래픽에 그리기
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            slide.draw(graphics);
            graphics.dispose();

            return img;
        }
    }

    public static BufferedImage convertPdfPageToImage(String filePath, int pageNumber) throws IOException {
        try (PDDocument document = PDDocument.load(new File(filePath))) {
            if (pageNumber < 1 || pageNumber > document.getNumberOfPages()) {
                throw new BadRequestException(PAGE_OVER_BAD_REQUEST);
            }

            PDFRenderer pdfRenderer = new PDFRenderer(document);
            return pdfRenderer.renderImageWithDPI(pageNumber - 1, 150, ImageType.RGB);
        }
    }
}
