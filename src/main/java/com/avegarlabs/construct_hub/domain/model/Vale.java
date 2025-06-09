package com.avegarlabs.construct_hub.domain.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "vales")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Vale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String codigo;
    @Column(name = "fecha", updatable = false)
    @CreationTimestamp
    private LocalDateTime fecha;

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

    @OneToMany(mappedBy = "vale", cascade = CascadeType.ALL)
    @ToString.Exclude
    @Builder.Default
    private List<Despacho> despachos = new ArrayList<>();

    @Column
    @Builder.Default
    private Boolean active = true;

}