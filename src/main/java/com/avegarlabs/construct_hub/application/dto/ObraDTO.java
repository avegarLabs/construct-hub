package com.avegarlabs.construct_hub.application.dto;

import lombok.*;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ObraDTO {
    private String codigo;
    private String descripcion;
    private Set<Long> empresasId;
}