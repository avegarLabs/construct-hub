package com.avegarlabs.construct_hub.domain.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "objetos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Objeto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String codigo;

    private String descripcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "obra_id")
    @ToString.Exclude
    private Obra obra;
}