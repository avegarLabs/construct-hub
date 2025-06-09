package com.avegarlabs.construct_hub.application.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecursoListItem {
    private Long id;
    private String codigo;
    private String descripcion;
    private String um;
    private BigDecimal cantidad;
    private BigDecimal precio;
    private BigDecimal disponible;

}
