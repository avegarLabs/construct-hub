package com.avegarlabs.construct_hub.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "obras")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Obra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String codigo;

    private String descripcion;

    @ManyToMany
    @JoinTable(
            name = "obra_empresa",
            joinColumns = @JoinColumn(name = "obra_id"),
            inverseJoinColumns = @JoinColumn(name = "empresa_id")
    )
    @ToString.Exclude
    @Builder.Default
    private Set<Empresa> empresas = new HashSet<>();

    @OneToMany(mappedBy = "obra", cascade = CascadeType.ALL)
    @ToString.Exclude
    @Builder.Default
    private List<Objeto> objetos = new ArrayList<>();
}