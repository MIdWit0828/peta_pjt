package prs.midwit.PetaProject.attachment.util;

import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class ImageConverter {
    public static BufferedImage convertWordPageToImage(String filePath, int pageNumber) throws IOException {
        try (FileInputStream fis = new FileInputStream(filePath); XWPFDocument document = new XWPFDocument(fis)) {
            List<XWPFParagraph> paragraphs = document.getParagraphs();

            // 특정 페이지의 텍스트를 이미지로 변환
            if (pageNumber < 0 || pageNumber >= paragraphs.size()) {
                throw new IllegalArgumentException("Invalid page number");
            }
            String text = paragraphs.get(pageNumber).getText();

            // 이미지 생성
            BufferedImage image = new BufferedImage(800, 600, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = image.createGraphics();
            g2d.setPaint(Color.WHITE);
            g2d.fillRect(0, 0, image.getWidth(), image.getHeight());
            g2d.setPaint(Color.BLACK);
            g2d.drawString(text, 20, 30);
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

            // 슬라이드를 이미지로 변환
            BufferedImage img = new BufferedImage(800, 600, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics = img.createGraphics();
            slide.draw(graphics);
            graphics.dispose();

            return img;
        }
    }
}
