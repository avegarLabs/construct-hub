package com.avegarlabs.construct_hub.application.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DespachoListItem {
    private Long id;
    private String codigo;
    private BigDecimal cantidadDespachada;
    private LocalDateTime fecha;

    private ObraListItem obra;
    private EmpresaListItem empresa;
    private ObjetoListItem objeto;
    private RecursoListItem recurso;
}

