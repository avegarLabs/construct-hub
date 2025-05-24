package com.avegarlabs.construct_hub.application.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmpresaListItem {
    private Long id;
    private String codigo;
    private String nombre;
}