package com.acharya.dikshanta.HospitalManagement.billing.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PdfGeneratorService {

    private final TemplateEngine templateEngine;

    public byte[] generatePdfFromTemplate(String templateName, Map<String, Object> variables) {
        Context context = new Context();
        context.setVariables(variables);

        String renderedHtml = templateEngine.process(templateName, context);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(renderedHtml);
            renderer.layout();
            renderer.createPDF(outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate PDF from template: " + templateName, e);
        }
    }
}