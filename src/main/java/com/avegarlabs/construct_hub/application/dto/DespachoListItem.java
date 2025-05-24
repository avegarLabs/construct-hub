package com.avegarlabs.construct_hub.application.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DespachoListItem {
    private Long id;
    private String codigo;
    private BigDecimal cantidadDespachada;

    private ObraDTO obra;
    private EmpresaDTO empresa;
    private ObjetoDTO objeto;
    private RecursoDTO recurso;
}

