package prs.midwit.PetaProject.attachment.util;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class ImageConverter {
    public static BufferedImage convertWordPageToImage(String filePath, int pageNumber) throws IOException {
        try (FileInputStream fis = new FileInputStream(filePath); XWPFDocument document = new XWPFDocument(fis)) {
            List<XWPFParagraph> paragraphs = document.getParagraphs();

            // 페이지 기준으로 단락 수 계산
            int paragraphsPerPage = 25; // 한 페이지에 출력할 단락 수 설정
            int totalPages = (int) Math.ceil((double) paragraphs.size() / paragraphsPerPage); // 총 페이지 수 계산

            // 페이지 번호가 유효한지 확인 (pageNumber가 1부터 시작하는 경우)
            if (pageNumber < 1 || pageNumber > totalPages) {
                throw new IllegalArgumentException("Invalid page number");
            }

            // 시작 단락과 끝 단락 인덱스 계산
            int startParagraph = (pageNumber - 1) * paragraphsPerPage; // 1페이지 요청 시 첫 단락부터
            int endParagraph = Math.min(startParagraph + paragraphsPerPage, paragraphs.size());

            // 이미지 크기 설정
            int imageWidth = 800; // 고정 너비
            int imageHeight = 600; // 고정 높이

            // 이미지 생성
            BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = image.createGraphics();
            g2d.setPaint(Color.WHITE);
            g2d.fillRect(0, 0, image.getWidth(), image.getHeight());
            g2d.setPaint(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.PLAIN, 12));

            // 각 단락의 텍스트를 이미지에 그리기
            int yPosition = 30; // 시작 Y 위치
            for (int i = startParagraph; i < endParagraph; i++) {
                XWPFParagraph paragraph = paragraphs.get(i);
                String text = paragraph.getText();

                // 텍스트가 이미지 높이를 초과하면 새로운 이미지 생성
                if (yPosition + 20 > imageHeight) { // 20은 단락 높이
                    break; // 현재 페이지의 이미지 반환
                }
                g2d.drawString(text, 20, yPosition);
                yPosition += 20; // 다음 단락의 Y 위치 조정
            }
            g2d.dispose();

            return image;
        }
    }

    public static BufferedImage convertSlideToImage(String filePath, int slideNumber) throws IOException {
        try (FileInputStream fis = new FileInputStream(filePath); XMLSlideShow ppt = new XMLSlideShow(fis)) {
            List<XSLFSlide> slides = ppt.getSlides();

            if (slideNumber < 0 || slideNumber >= slides.size()) {
                throw new IllegalArgumentException("Invalid slide number");
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
                return null;
            }

            PDFRenderer pdfRenderer = new PDFRenderer(document);
            return pdfRenderer.renderImageWithDPI(pageNumber - 1, 150, ImageType.RGB);
        }
    }
}
