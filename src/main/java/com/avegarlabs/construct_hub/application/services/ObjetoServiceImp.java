package com.avegarlabs.construct_hub.application.services;

import com.avegarlabs.construct_hub.application.dto.ObjetoDTO;
import com.avegarlabs.construct_hub.application.dto.ObjetoListItem;
import com.avegarlabs.construct_hub.domain.model.Objeto;
import com.avegarlabs.construct_hub.domain.model.Obra;
import com.avegarlabs.construct_hub.domain.repositories.ObjetoRepository;
import com.avegarlabs.construct_hub.domain.repositories.ObraRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ObjetoServiceImp implements IObjetoService{
    private final ObjetoRepository repository;
    private final ObraRepository obraRepository;

    @Override
    public ObjetoListItem persist(ObjetoDTO dto) {
        Objeto objeto = new Objeto();
        objeto.setDataFromDto(dto);
        Obra obra = obraRepository.getReferenceById(dto.getObraId());
        if(obra != null){
            objeto.setObra(obra);
            repository.save(objeto);
        }
        return mappEntiyToListItem(objeto);
    }

    @Override
    public ObjetoListItem update(Long objId, ObjetoDTO dto) {
        Objeto objeto = repository.getReferenceById(objId);
        objeto.setDataFromDto(dto);
        Obra obra = obraRepository.getReferenceById(dto.getObraId());
        if(obra != null){
            objeto.setObra(obra);
            repository.save(objeto);
        }
        return mappEntiyToListItem(objeto);
    }

    @Override
    public Boolean deleteObjeto(Long obraId) {
        try {
            repository.deleteById(obraId);
            return true;
        } catch (EmptyResultDataAccessException | DataIntegrityViolationException e) {
            return false;
        }
    }

    @Override
    public List<ObjetoListItem> listObjetos() {
       return repository.findAll().stream().map(this::mappEntiyToListItem).toList();
    }

    @Override
    public List<ObjetoListItem> findObjetosByObraId(Long idObra) {
       return repository.getObjetoByObraId(idObra).stream().map(this::mappEntiyToListItem).toList();
    }

    @Override
    public ObjetoListItem findByObjetoId(Long objetoId) {
        return mappEntiyToListItem(repository.getReferenceById(objetoId));


    }

    @Override
    public Objeto getEntity(Long objId) {
        return repository.getReferenceById(objId);
    }

    private ObjetoListItem mappEntiyToListItem(Objeto objeto){
        return ObjetoListItem.builder()
                .codigo(objeto.getCodigo())
                .descripcion(objeto.getDescripcion())
                .id(objeto.getId())
                .build();
    }


}
