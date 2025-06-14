package com.avegarlabs.construct_hub.application.dto;

import com.avegarlabs.construct_hub.domain.model.Despacho;
import com.avegarlabs.construct_hub.domain.model.Empresa;
import com.avegarlabs.construct_hub.domain.model.Objeto;
import com.avegarlabs.construct_hub.domain.model.Obra;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DespachoDTO {
    private Long recursoId;
    private BigDecimal cantidadDespachada;
}