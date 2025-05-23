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
    public class DespachoListItem {
        private Long id;
        private String codigo;
        private BigDecimal cantidadDespachada;

        private ObraDTO obra;
        private EmpresaDTO empresa;
        private ObjetoDTO objeto;
        private RecursoDTO recurso;
    }

