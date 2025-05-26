package com.avegarlabs.construct_hub.application.services;

import com.avegarlabs.construct_hub.application.dto.EmpresaDTO;
import com.avegarlabs.construct_hub.application.dto.EmpresaListItem;
import com.avegarlabs.construct_hub.domain.model.Empresa;
import com.avegarlabs.construct_hub.domain.repositories.EmpresaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmpresaServiceImp implements IEmpresaService {

    private final EmpresaRepository empresaRepository;

    @Override
    public EmpresaListItem persist(EmpresaDTO dto) {
        Empresa empresa = mapperEntityToDto(dto);
        empresaRepository.save(empresa);
        return mapperEmpToListItem(empresa);
    }

    @Override
    public EmpresaListItem update(Long empId, EmpresaDTO dto) {
        Empresa empresa = empresaRepository.getReferenceById(empId);
        empresa.setDataFromDto(dto);
        empresaRepository.save(empresa);
        return mapperEmpToListItem(empresa);
    }

    @Override
    public Boolean deleteEmpresa(Long empId) {
        try {
            empresaRepository.deleteById(empId);
            return true;
        } catch (EmptyResultDataAccessException | DataIntegrityViolationException e) {
            return false;
        }
    }

    @Override
    public List<EmpresaListItem> listEmpresas() {
        return empresaRepository.findAll().stream().map(this::mapperEmpToListItem).toList();

    }

    @Override
    public EmpresaListItem findByEmpresaById(Long empresaId) {
        Empresa empresa = empresaRepository.getReferenceById(empresaId);
        return mapperEmpToListItem(empresa);
    }

    @Override
    public Set<Empresa> getEmpresasByIds(Set<Long> empresaIds) {
        if (empresaIds == null || empresaIds.isEmpty()) {
            return Set.of();
        }
        return empresaIds.stream()
                .map(empresaRepository::getReferenceById)
                .collect(Collectors.toSet());
    }


    public EmpresaListItem mapperEmpToListItem(Empresa empresa){
        return EmpresaListItem.builder()
                .codigo(empresa.getCodigo())
                .nombre(empresa.getNombre())
                .id(empresa.getId())
                .build();
    }

    @Override
    public Empresa getEntity(Long empresaId) {
        return empresaRepository.getReferenceById(empresaId);
    }

    private Empresa mapperEntityToDto(EmpresaDTO empresaDTO){
        return  Empresa.builder()
                .codigo(empresaDTO.getCodigo())
                .nombre(empresaDTO.getNombre())
                .build();
    }
}
