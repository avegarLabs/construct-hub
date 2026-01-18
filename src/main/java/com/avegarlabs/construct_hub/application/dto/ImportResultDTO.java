package com.avegarlabs.construct_hub.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImportResultDTO {
    private int totalRows;
    private int successfulRows;
    private int failedRows;

    @Builder.Default
    private List<ImportErrorDTO> errors = new ArrayList<>();
}
