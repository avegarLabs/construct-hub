package com.avegarlabs.construct_hub.application.dto;

import com.avegarlabs.construct_hub.domain.model.Despacho;
import com.avegarlabs.construct_hub.domain.model.Empresa;
import com.avegarlabs.construct_hub.domain.model.Objeto;
import com.avegarlabs.construct_hub.domain.model.Obra;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ValesDTO {
    private String codigo;
    private Long obraId;
    private Long empresaId;
    private Long objetoId;
    private List<DespachoDTO> despachos;
}