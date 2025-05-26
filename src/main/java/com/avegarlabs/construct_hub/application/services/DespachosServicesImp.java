package com.avegarlabs.construct_hub.application.services;

import com.avegarlabs.construct_hub.application.dto.*;
import com.avegarlabs.construct_hub.domain.model.*;
import com.avegarlabs.construct_hub.domain.repositories.DespachoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DespachosServicesImp implements IDespachosService {

    private final DespachoRepository repository;
    private final IObraService obraService;
    private final IRecursosService recursosService;
    private final IEmpresaService empresaService;
    private final IObjetoService objetoService;

    @Override
    public DespachoListItem persist(DespachoDTO dto) {
        Despacho despacho = mapDtoToEntity(dto);
        repository.save(despacho);
        return mapEntityToListItem(despacho);
    }

    @Override
    public DespachoListItem update(Long despId, DespachoDTO dto) {
        Despacho despacho = repository.getReferenceById(despId);
        Despacho update = setDataToEntity(despacho, dto);
        repository.save(update);
        return mapEntityToListItem(update);
    }

    @Override
    public Boolean deleteDespacho(Long objId) {
        try {
            repository.deleteById(objId);
            return true;
        } catch (EmptyResultDataAccessException | DataIntegrityViolationException e) {
            return false;
        }
    }

    @Override
    public List<DespachoListItem> listDespachos() {
        return repository.findAll().stream().map(this::mapEntityToListItem).toList();
    }

    @Override
    public List<DespachoListItem> findDespachosByObraId(Long idObr) {
        return repository.getDespachosByObraId(idObr).stream().map(this::mapEntityToListItem).toList();
    }

    @Override
    public List<DespachoListItem> findDespachosByEmpresaId(Long idEmp) {
        return repository.getDespachosByEmpresaId(idEmp).stream().map(this::mapEntityToListItem).toList();
    }

    @Override
    public List<DespachoListItem> findDespachosByObjetosId(Long idObj) {
        return repository.getDespachosByObjetoId(idObj).stream().map(this::mapEntityToListItem).toList();
    }

    @Override
    public List<DespachoListItem> findDespachosByRecursoId(Long idRec) {
        return repository.getDespachosByRecursoId(idRec).stream().map(this::mapEntityToListItem).toList();
    }

    @Override
    public DespachoListItem findByDespachoId(Long despId) {
        return mapEntityToListItem(repository.getReferenceById(despId));
    }

    private Despacho mapDtoToEntity(DespachoDTO dto){
        Obra obra = obraService.getEntity(dto.getObraId());
        Empresa empresa = empresaService.getEntity(dto.getEmpresaId());
        Objeto objeto = objetoService.getEntity(dto.getObjetoId());
        Recurso recurso = recursosService.getEntity(dto.getRecursoId());
        return Despacho.builder()
                .cantidadDespachada(dto.getCantidadDespachada())
                .codigo(dto.getCodigo())
                .recurso(recurso)
                .empresa(empresa)
                .objeto(objeto)
                .obra(obra)
                .build();
    }

    private DespachoListItem mapEntityToListItem(Despacho despacho){
        ObraListItem obra = obraService.findByObraId(despacho.getObra().getId());
        EmpresaListItem empresa = empresaService.findByEmpresaById(despacho.getEmpresa().getId());
        ObjetoListItem objeto = objetoService.findByObjetoId(despacho.getObjeto().getId());
        RecursoListItem recurso = recursosService.findByrecursoById(despacho.getRecurso().getId());
        return DespachoListItem.builder()
                .id(despacho.getId())
                .codigo(despacho.getCodigo())
                .cantidadDespachada(despacho.getCantidadDespachada())
                .empresa(empresa)
                .objeto(objeto)
                .fecha(despacho.getFecha())
                .obra(obra)
                .recurso(recurso)
                .build();
    }

    private Despacho setDataToEntity(Despacho despacho, DespachoDTO dto){
        Obra obra = obraService.getEntity(dto.getObraId());
        Empresa empresa = empresaService.getEntity(dto.getEmpresaId());
        Objeto objeto = objetoService.getEntity(dto.getObjetoId());
        Recurso recurso = recursosService.getEntity(dto.getRecursoId());
        despacho.setCodigo(dto.getCodigo());
        despacho.setCantidadDespachada(dto.getCantidadDespachada());
        despacho.setObjeto(objeto);
        despacho.setObra(obra);
        despacho.setEmpresa(empresa);
        despacho.setRecurso(recurso);
        return despacho;
    }
}
