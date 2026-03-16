package com.avegarlabs.construct_hub.application.services;

import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

public interface IBackupService {

    StreamingResponseBody generateBackup();

    String generateBackupFilename();
}
