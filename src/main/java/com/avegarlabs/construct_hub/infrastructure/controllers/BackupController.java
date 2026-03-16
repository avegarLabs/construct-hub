package com.avegarlabs.construct_hub.infrastructure.controllers;

import com.avegarlabs.construct_hub.application.services.IBackupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@RestController
@RequestMapping("/api/backup")
@RequiredArgsConstructor
@Slf4j
public class BackupController {

    private final IBackupService backupService;

    @GetMapping
    public ResponseEntity<StreamingResponseBody> downloadBackup() {
        try {
            String filename = backupService.generateBackupFilename();
            StreamingResponseBody body = backupService.generateBackup();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(new MediaType("application", "sql", java.nio.charset.StandardCharsets.UTF_8));
            headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");
            headers.set(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate");

            return new ResponseEntity<>(body, headers, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error al iniciar el backup de la base de datos", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
