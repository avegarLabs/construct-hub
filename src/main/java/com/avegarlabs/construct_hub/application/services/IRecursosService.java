package com.avegarlabs.construct_hub.application.services;

import com.avegarlabs.construct_hub.application.dto.EmpresaDTO;
import com.avegarlabs.construct_hub.application.dto.EmpresaListItem;
import com.avegarlabs.construct_hub.application.dto.RecursoDTO;
import com.avegarlabs.construct_hub.application.dto.RecursoListItem;
import com.avegarlabs.construct_hub.domain.model.Empresa;
import com.avegarlabs.construct_hub.domain.model.Recurso;

import java.util.List;
import java.util.Set;

public interface IRecursosService {

    RecursoListItem persist(RecursoDTO dto);

    RecursoListItem update(Long empId, RecursoDTO dto);

    Boolean deleteRecurso(Long empId);

    List<RecursoListItem> listRecursos();

    List<RecursoListItem> listRecursosByObra(Long obraId);

    RecursoListItem findByrecursoById(Long recursoId);

    Recurso getEntity(Long recursoId);

}