package com.avegarlabs.construct_hub.application.services;

import com.avegarlabs.construct_hub.application.dto.ObraDTO;
import com.avegarlabs.construct_hub.application.dto.ObraListItem;
import com.avegarlabs.construct_hub.domain.model.Obra;
import com.avegarlabs.construct_hub.domain.repositories.ObraRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class ObraServiceImp implements IObraService {

    private final ObraRepository obraRepository;
    private final IEmpresaService empresaService;

    @Override
    public ObraListItem save(ObraDTO dto) {
        return null;
    }

    @Override
    public ObraListItem update(Long obraId, ObraDTO dto) {
        return null;
    }

    @Override
    public Boolean deleteObra(Long obraId) {
        return null;
    }

    @Override
    public List<ObraListItem> listObras() {
        return null;
    }

    @Override
    public ObraListItem findByObraId(Long obraId) {
        return null;
    }

    private Obra mapperDtoToEntity(ObraDTO obraDTO){
        return Obra.builder()
                .codigo(obraDTO.getCodigo())
                .descripcion(obraDTO.getDescripcion())
                .empresas()
                .build();
    }
}
