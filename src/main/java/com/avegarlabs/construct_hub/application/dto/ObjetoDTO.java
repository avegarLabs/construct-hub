package com.avegarlabs.construct_hub.application.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ObjetoDTO {
    private String codigo;
    private String descripcion;
    private Long ObraId;
}
