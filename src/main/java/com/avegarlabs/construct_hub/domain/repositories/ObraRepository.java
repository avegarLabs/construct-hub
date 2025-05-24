package com.avegarlabs.construct_hub.domain.repositories;

import com.avegarlabs.construct_hub.domain.model.Obra;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ObraRepository extends JpaRepository<Obra, Long> {
}
