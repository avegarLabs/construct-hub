package com.avegarlabs.construct_hub.application.services;

import com.avegarlabs.construct_hub.application.dto.EmpresaDTO;
import com.avegarlabs.construct_hub.application.dto.EmpresaListItem;
import com.avegarlabs.construct_hub.application.dto.ObraDTO;
import com.avegarlabs.construct_hub.application.dto.ObraListItem;

import java.util.List;

public interface IEmpresaService {

    EmpresaListItem save(EmpresaDTO dto);

    EmpresaListItem update(Long empId, EmpresaDTO dto);

    Boolean deleteEmpresa(Long empId);

    List<EmpresaListItem> listEmpresas();

    ObraListItem findByEmpresaById(Long empresaId);
}
