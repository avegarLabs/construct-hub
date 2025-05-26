package com.avegarlabs.construct_hub.domain.model;

import com.avegarlabs.construct_hub.application.dto.ObjetoDTO;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "objetos")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
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

    public void setDataFromDto(ObjetoDTO dto){
        codigo = dto.getCodigo();
        descripcion = dto.getDescripcion();
    }
}