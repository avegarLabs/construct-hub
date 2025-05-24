package com.avegarlabs.construct_hub.application.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DespachoDTO {
    private String codigo;
    private BigDecimal cantidadDespachada;

    private Long obraId;
    private Long empresaId;
    private Long objetoId;
    private Long recursoId;
}

