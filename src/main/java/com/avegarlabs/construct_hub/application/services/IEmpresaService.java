package com.avegarlabs.construct_hub.application.services;

import com.avegarlabs.construct_hub.application.dto.EmpresaDTO;
import com.avegarlabs.construct_hub.application.dto.EmpresaListItem;
import com.avegarlabs.construct_hub.domain.model.Empresa;

import java.util.List;
import java.util.Set;

public interface IEmpresaService {

    EmpresaListItem persist(EmpresaDTO dto);

    EmpresaListItem update(Long empId, EmpresaDTO dto);

    Boolean deleteEmpresa(Long empId);

    List<EmpresaListItem> listEmpresas();

    EmpresaListItem findByEmpresaById(Long empresaId);

    Set<Empresa> getEmpresasByIds(Set<Long> empresaIds);

    EmpresaListItem mapperEmpToListItem(Empresa empresa);

    Empresa getEntity(Long empresaId);
}