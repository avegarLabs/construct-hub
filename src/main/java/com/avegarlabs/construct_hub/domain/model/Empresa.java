package com.avegarlabs.construct_hub.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "empresas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Empresa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String codigo;

    private String nombre;

    @ManyToMany(mappedBy = "empresas")
    @ToString.Exclude
    @Builder.Default
    private Set<Obra> obras = new HashSet<>();
}