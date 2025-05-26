package com.avegarlabs.construct_hub.domain.model;

import com.avegarlabs.construct_hub.application.dto.RecursoDTO;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "recursos")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Recurso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String codigo;
    private String descripcion;
    private String um;
    private BigDecimal cantidad;
    private BigDecimal precio;

    public void setDataFromDto(RecursoDTO dto) {
        codigo = dto.getCodigo();
        descripcion = dto.getDescripcion();
        um = dto.getUm();
        cantidad = dto.getCantidad();
        precio = dto.getPrecio();
    }
}