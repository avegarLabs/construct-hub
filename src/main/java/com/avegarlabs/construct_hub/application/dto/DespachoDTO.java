package com.avegarlabs.construct_hub.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DespachoDTO {
    private String codigo;
    private BigDecimal cantidadDespachada;

    private Long obraId;
    private Long empresaId;
    private Long objetoId;
    private Long recursoId;
}

