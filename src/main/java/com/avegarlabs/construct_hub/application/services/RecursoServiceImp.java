package com.avegarlabs.construct_hub.application.services;

import com.avegarlabs.construct_hub.application.dto.ImportResultDTO;
import com.avegarlabs.construct_hub.application.dto.RecursoDTO;
import com.avegarlabs.construct_hub.application.dto.RecursoListItem;
import com.avegarlabs.construct_hub.domain.model.Despacho;
import com.avegarlabs.construct_hub.domain.model.Obra;
import com.avegarlabs.construct_hub.domain.model.Recurso;
import com.avegarlabs.construct_hub.domain.repositories.DespachoRepository;
import com.avegarlabs.construct_hub.domain.repositories.ObraRepository;
import com.avegarlabs.construct_hub.domain.repositories.RecursoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecursoServiceImp implements IRecursosService {

    private final RecursoRepository repository;
    private final ObraRepository obraRepository;
    private final DespachoRepository despachoRepository;
    private final ExcelImportService excelImportService;

    @Override
    public RecursoListItem persist(RecursoDTO dto) {
        Obra obra =  obraRepository.getReferenceById(dto.getObraId());
        Recurso recurso = new Recurso();
        recurso.setDataFromDto(dto);
        recurso.setObra(obra);
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
    public List<RecursoListItem> listRecursosByObra(Long obraId) {
        return repository.findAllByObraId(obraId).stream().map(this::mappEntityToListItem).toList();
    }

    @Override
    public RecursoListItem findByrecursoById(Long recursoId) {
        return mappEntityToListItem(repository.getReferenceById(recursoId));
    }

    @Override
    public Recurso getEntity(Long recursoId) {
        return repository.getReferenceById(recursoId);
    }

    @Override
    public ImportResultDTO importRecursosFromExcel(MultipartFile file, Long obraId) throws IOException {
        return excelImportService.importRecursosFromExcel(file, obraId);
    }

    private RecursoListItem mappEntityToListItem(Recurso recurso) {
        Double despachos = despachoRepository.getDespachosByRecursoId(recurso.getId()).stream().filter(item -> item.getVale().getActive()).map(item -> item.getCantidadDespachada().doubleValue()).reduce(0.0, Double::sum);
        Double disponible = recurso.getCantidad().doubleValue() - despachos;
        return RecursoListItem.builder()
                .id(recurso.getId())
                .codigo(recurso.getCodigo())
                .cantidad(recurso.getCantidad())
                .um(recurso.getUm())
                .precio(recurso.getPrecio())
                .descripcion(recurso.getDescripcion())
                .disponible(new BigDecimal(disponible))
                .build();
    }
}
