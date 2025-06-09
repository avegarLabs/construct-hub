package com.avegarlabs.construct_hub.application.services;

import com.avegarlabs.construct_hub.application.dto.ObraDTO;
import com.avegarlabs.construct_hub.application.dto.ObraListItem;
import com.avegarlabs.construct_hub.domain.model.Obra;

import java.util.List;
import java.util.Set;

public interface IObraService {

    ObraListItem persist(ObraDTO dto);

    ObraListItem update(Long obraId, ObraDTO dto);

    Boolean deleteObra(Long obraId);

    List<ObraListItem> listObras();

    ObraListItem findByObraId(Long obraId);

    Obra getEntity(Long obraId);

    ObraListItem addEmpresa(Long obraId, Set<Long> empIds);

    ObraListItem deleteEmpresa(Long obraId, Long empId);
}
