package com.avegarlabs.construct_hub.domain.repositories;

import com.avegarlabs.construct_hub.domain.model.Objeto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ObjetoRepository extends JpaRepository<Objeto, Long> {

    List<Objeto> getObjetoByObraId(Long obraId);
}
