package com.avegarlabs.construct_hub.application.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmpresaDTO {
    private String codigo;
    private String nombre;
}