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
    private BigDecimal cantidadDespachada;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recurso_id")
    @ToString.Exclude
    private Recurso recurso;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vale_id")
    @ToString.Exclude
    private Vale vale;
}