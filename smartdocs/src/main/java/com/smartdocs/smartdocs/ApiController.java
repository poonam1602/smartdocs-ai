package com.smartdocs.smartdocs;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final PdfService pdfService;
    private final ChatService chatService;

    public ApiController(PdfService pdfService, ChatService chatService) {
        this.pdfService = pdfService;
        this.chatService = chatService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadPdf(@RequestParam("file") MultipartFile file) {
        try {
            pdfService.processPdf(file);
            return ResponseEntity.ok("File processed and saved successfully!");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/chat")
    public ResponseEntity<String> chat(@RequestBody String question) {
        try {
            String answer = chatService.askQuestion(question);
            return ResponseEntity.ok(answer);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }
}