package com.avegarlabs.construct_hub.application.dto;

import lombok.*;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ObraListItem {
    private Long id;
    private String codigo;
    private String descripcion;
    private Set<EmpresaListItem> empresas;
}