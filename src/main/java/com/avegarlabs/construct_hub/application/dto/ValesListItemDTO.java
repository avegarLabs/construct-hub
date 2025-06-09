package com.avegarlabs.construct_hub.application.dto;

import com.avegarlabs.construct_hub.domain.model.Objeto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ValesListItemDTO {
    private Long id;
    private String codigo;
    private LocalDateTime fecha;
    private ObraListItem obra;
    private EmpresaListItem empresa;
    private ObjetoListItem objeto;
    private List<DespachoListItemDTO> despachos;
}