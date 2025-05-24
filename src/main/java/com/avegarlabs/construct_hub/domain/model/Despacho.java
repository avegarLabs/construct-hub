package com.avegarlabs.construct_hub.domain.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "despachos")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Despacho {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String codigo;
    @Column(name = "fecha", updatable = false)
    @CreationTimestamp
    private LocalDateTime fecha;
    private BigDecimal cantidadDespachada;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "obra_id")
    @ToString.Exclude
    private Obra obra;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id")
    @ToString.Exclude
    private Empresa empresa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "objeto_id")
    @ToString.Exclude
    private Objeto objeto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recurso_id")
    @ToString.Exclude
    private Recurso recurso;
}