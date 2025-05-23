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
public class RecursoListItem {
    private Long id;
    private String codigo;
    private String descripcion;
    private String um;
    private BigDecimal cantidad;
    private BigDecimal precio;

}
