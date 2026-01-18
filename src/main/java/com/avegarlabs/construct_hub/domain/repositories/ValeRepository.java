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

    // Métodos con ordenamiento por fecha descendente
    List<Vale> getValesByObraIdOrderByFechaDesc(Long obraId);
    List<Vale> getValeByEmpresaIdOrderByFechaDesc(Long empresaId);
    List<Vale> getValesByObjetoIdOrderByFechaDesc(Long objetoId);

    List<Vale> getValeByActive(Boolean state);

    /**
     * Obtiene los vales activos ordenados por fecha descendente (más reciente primero)
     * @param state Estado del vale (true = activo, false = inactivo)
     * @return Lista de vales ordenados por fecha descendente
     */
    List<Vale> getValeByActiveOrderByFechaDesc(Boolean state);

   }
