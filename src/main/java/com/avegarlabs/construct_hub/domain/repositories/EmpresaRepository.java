package com.avegarlabs.construct_hub.domain.repositories;

import com.avegarlabs.construct_hub.domain.model.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {
}
