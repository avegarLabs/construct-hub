package com.avegarlabs.construct_hub.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para la respuesta de generación de código de despacho
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CodigoDespachoDTO {
    private String codigo;
}
