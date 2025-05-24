package com.avegarlabs.construct_hub.domain.model;

import com.avegarlabs.construct_hub.application.dto.EmpresaDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "empresas")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
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

    public void setDataFromDto(EmpresaDTO dto){
        codigo = dto.getCodigo();
        nombre = dto.getNombre();
    }
}