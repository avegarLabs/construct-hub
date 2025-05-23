package com.avegarlabs.construct_hub.application.services;

import com.avegarlabs.construct_hub.application.dto.EmpresaDTO;
import com.avegarlabs.construct_hub.application.dto.EmpresaListItem;
import com.avegarlabs.construct_hub.application.dto.ObraListItem;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmpresaServiceImp implements IEmpresaService {
    @Override
    public EmpresaListItem save(EmpresaDTO dto) {
        return null;
    }

    @Override
    public EmpresaListItem update(Long empId, EmpresaDTO dto) {
        return null;
    }

    @Override
    public Boolean deleteEmpresa(Long empId) {
        return null;
    }

    @Override
    public List<EmpresaListItem> listEmpresas() {
        return null;
    }

    @Override
    public ObraListItem findByEmpresaById(Long empresaId) {
        return null;
    }
}
