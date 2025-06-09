package com.avegarlabs.construct_hub.domain.repositories;

import com.avegarlabs.construct_hub.domain.model.Despacho;
import com.avegarlabs.construct_hub.domain.model.Vale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ValeRepository extends JpaRepository<Vale, Long> {

    List<Vale> getValesByObraId(Long obraId);
    List<Vale> getValeByEmpresaId(Long empresaId);
    List<Vale> getValesByObjetoId(Long objetoId);

    List<Vale> getValeByActive(Boolean state);

   }
