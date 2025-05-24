package com.avegarlabs.construct_hub.application.services;

import com.avegarlabs.construct_hub.application.dto.EmpresaDTO;
import com.avegarlabs.construct_hub.application.dto.EmpresaListItem;
import com.avegarlabs.construct_hub.application.dto.ObraListItem;
import com.avegarlabs.construct_hub.domain.model.Empresa;
import com.avegarlabs.construct_hub.domain.repositories.EmpresaRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class EmpresaServiceImp implements IEmpresaService {

    private final EmpresaRepository empresaRepository;

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

    public Set<Empresa> getEmpresasByIds(Set<Long> empresaIds) {
        if (empresaIds == null || empresaIds.isEmpty()) {
            return Set.of();
        }
        return empresaIds.stream()
                .map(empresaRepository::getReferenceById)
                .collect(Collectors.toSet());
    }

    public EmpresaListItem mapperEntityToListItem(Empresa empresa){
        return EmpresaListItem.builder()
                .codigo(empresa.getCodigo())
                .nombre(empresa.getNombre())
                .id(empresa.getId())
                .build();
    }
}
