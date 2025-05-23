package com.avegarlabs.construct_hub.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "recursos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
}