package com.avegarlabs.construct_hub.domain.repositories;

import com.avegarlabs.construct_hub.domain.model.Despacho;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DespachoRepository extends JpaRepository<Despacho, Long> {

    List<Despacho> getDespachosByRecursoId(Long recursoId);

}
