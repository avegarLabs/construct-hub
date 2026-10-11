package com.avegarlabs.construct_hub.infrastructure.controllers;

import com.avegarlabs.construct_hub.application.dto.AppConfigDTO;
import com.avegarlabs.construct_hub.application.dto.AppConfigListItem;
import com.avegarlabs.construct_hub.application.services.IAppConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/appconfig")
@RequiredArgsConstructor
public class AppConfigDtoController {

    private final IAppConfigService appConfigService;

    @GetMapping
    public ResponseEntity<AppConfigListItem> getConfig() {
        return ResponseEntity.ok(appConfigService.getConfig());
    }

    @PutMapping
    public ResponseEntity<AppConfigListItem> updateConfig(@RequestBody AppConfigDTO dto) {
        return ResponseEntity.ok(appConfigService.update(dto));
    }

}
