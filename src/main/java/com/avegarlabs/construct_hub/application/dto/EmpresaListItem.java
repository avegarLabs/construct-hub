package com.avegarlabs.construct_hub.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmpresaListItem {
    private Long id;
    private String codigo;
    private String nombre;
}