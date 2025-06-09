package com.avegarlabs.construct_hub.application.services;

import com.avegarlabs.construct_hub.application.dto.EmpresaListItem;
import com.avegarlabs.construct_hub.application.dto.ObraDTO;
import com.avegarlabs.construct_hub.application.dto.ObraListItem;
import com.avegarlabs.construct_hub.domain.model.Empresa;
import com.avegarlabs.construct_hub.domain.model.Obra;
import com.avegarlabs.construct_hub.domain.repositories.ObraRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ObraServiceImp implements IObraService {

    private final ObraRepository obraRepository;
    private final IEmpresaService empresaService;

    @Override
    public ObraListItem persist(ObraDTO dto) {
        Obra obra = mapperDtoToEntity(dto);
        obraRepository.save(obra);
        return mapperEntityToListItem(obra);


    }

    @Override
    public ObraListItem update(Long obraId, ObraDTO dto) {
        Obra obra = obraRepository.getReferenceById(obraId);
        obraRepository.save(setDataFromDto(obra, dto));
        return mapperEntityToListItem(obra);
    }

    @Override
    public Boolean deleteObra(Long obraId) {
        try {
            obraRepository.deleteById(obraId);
            return true;
        } catch (EmptyResultDataAccessException | DataIntegrityViolationException e) {
           return false;
        }
    }


    @Override
    @Transactional
    public List<ObraListItem> listObras() {
        List<Obra> obras = obraRepository.findAll();
        return obras.stream().map(this::mapperEntityToListItem).toList();
    }

    @Override
    public ObraListItem findByObraId(Long obraId) {
        Obra obra = obraRepository.getReferenceById(obraId);
        return mapperEntityToListItem(obra);
    }

    @Override
    public Obra getEntity(Long obraId) {
        return obraRepository.getReferenceById(obraId);
    }

    @Override
    public ObraListItem addEmpresa(Long obraId, Set<Long> empIds) {
        Obra obra = obraRepository.getReferenceById(obraId);
        if(obra != null){
            Set<Empresa> empresas = empresaService.getEmpresasByIds(empIds);
            obra.getEmpresas().addAll(empresas);
            obraRepository.save(obra);
        }
        return mapperEntityToListItem(obra);
    }

    @Override
    public ObraListItem deleteEmpresa(Long obraId, Long empId) {
        Obra obra = obraRepository.getReferenceById(obraId);
        if(obra != null){
            Empresa empresa = empresaService.getEntity(empId);
            obra.getEmpresas().remove(empresa);
            obraRepository.save(obra);
        }
        return mapperEntityToListItem(obra);
    }


    private Obra mapperDtoToEntity(ObraDTO obraDTO){
       Set<Empresa> empresas = empresaService.getEmpresasByIds(obraDTO.getEmpresasId());
        return Obra.builder()
                .codigo(obraDTO.getCodigo())
                .descripcion(obraDTO.getDescripcion())
                .empresas(empresas)
                .build();
    }

    private ObraListItem mapperEntityToListItem(Obra obra){
      Set<EmpresaListItem> empresaListItems = obra.getEmpresas().stream().map(empresaService::mapperEmpToListItem).collect(Collectors.toSet());
       return ObraListItem.builder()
               .codigo(obra.getCodigo())
               .descripcion(obra.getDescripcion())
               .id(obra.getId())
               .empresas(empresaListItems)
               .build();
    }

    private Obra setDataFromDto(Obra obra, ObraDTO obraDTO){
        obra.setCodigo(obraDTO.getCodigo());
        obra.setDescripcion(obraDTO.getDescripcion());
        return obra;
    }
}
