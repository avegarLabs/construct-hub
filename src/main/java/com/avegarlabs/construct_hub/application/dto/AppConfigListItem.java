package com.avegarlabs.construct_hub.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppConfigListItem {
    private Long id;
    private String factEmiter;
    private String  factLabal1;
    private String  factLabal2;
    private String  factLabal3;
}