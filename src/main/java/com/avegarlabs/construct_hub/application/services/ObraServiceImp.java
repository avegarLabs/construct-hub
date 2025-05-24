package com.avegarlabs.construct_hub.application.services;

import com.avegarlabs.construct_hub.application.dto.EmpresaListItem;
import com.avegarlabs.construct_hub.application.dto.ObraDTO;
import com.avegarlabs.construct_hub.application.dto.ObraListItem;
import com.avegarlabs.construct_hub.domain.model.Empresa;
import com.avegarlabs.construct_hub.domain.model.Obra;
import com.avegarlabs.construct_hub.domain.repositories.ObraRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class ObraServiceImp implements IObraService {

    private final ObraRepository obraRepository;
    private final EmpresaServiceImp empresaService;

    @Override
    public ObraListItem save(ObraDTO dto) {
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
    public List<ObraListItem> listObras() {
        return obraRepository.findAll().stream().map(this::mapperEntityToListItem).toList();


    }

    @Override
    public ObraListItem findByObraId(Long obraId) {
        Obra obra = obraRepository.getReferenceById(obraId);
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
        Set<EmpresaListItem> empresaListItems = obra.getEmpresas().stream().map(empresa -> empresaService.mapperEntityToListItem(empresa)).collect(Collectors.toSet());
       return ObraListItem.builder()
               .codigo(obra.getCodigo())
               .descripcion(obra.getDescripcion())
               .id(obra.getId())
               .empresas(empresaListItems)
               .build();
    }

    private Obra setDataFromDto(Obra obra, ObraDTO obraDTO){
        Set<Empresa> empresas = empresaService.getEmpresasByIds(obraDTO.getEmpresasId());
        obra.setCodigo(obraDTO.getCodigo());
        obra.setDescripcion(obraDTO.getDescripcion());
        obra.setEmpresas(empresas);
        return obra;
    }
}
