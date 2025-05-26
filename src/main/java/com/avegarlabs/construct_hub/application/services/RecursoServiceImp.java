package com.avegarlabs.construct_hub.application.services;

import com.avegarlabs.construct_hub.application.dto.RecursoDTO;
import com.avegarlabs.construct_hub.application.dto.RecursoListItem;
import com.avegarlabs.construct_hub.domain.model.Recurso;
import com.avegarlabs.construct_hub.domain.repositories.RecursoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecursoServiceImp implements IRecursosService {

    private final RecursoRepository repository;

    @Override
    public RecursoListItem persist(RecursoDTO dto) {
        Recurso recurso = new Recurso();
        recurso.setDataFromDto(dto);
        repository.save(recurso);
        return mappEntityToListItem(recurso);
    }

    @Override
    public RecursoListItem update(Long recId, RecursoDTO dto) {
        Recurso recurso = repository.getReferenceById(recId);
        recurso.setDataFromDto(dto);
        repository.save(recurso);
        return mappEntityToListItem(recurso);
    }

    @Override
    public Boolean deleteRecurso(Long recId) {
        try {
            repository.deleteById(recId);
            return true;
        } catch (EmptyResultDataAccessException | DataIntegrityViolationException e) {
            return false;
        }
    }

    @Override
    public List<RecursoListItem> listRecursos() {
        return repository.findAll().stream().map(this::mappEntityToListItem).toList();
    }

    @Override
    public RecursoListItem findByrecursoById(Long recursoId) {
        return mappEntityToListItem(repository.getReferenceById(recursoId));
    }

    @Override
    public Recurso getEntity(Long recursoId) {
        return repository.getReferenceById(recursoId);
    }

    private RecursoListItem mappEntityToListItem(Recurso recurso) {
        return RecursoListItem.builder()
                .id(recurso.getId())
                .cantidad(recurso.getCantidad())
                .um(recurso.getUm())
                .precio(recurso.getPrecio())
                .descripcion(recurso.getDescripcion())
                .build();
    }
}
