package com.avegarlabs.construct_hub.application.services;

import com.avegarlabs.construct_hub.application.dto.*;
import com.avegarlabs.construct_hub.domain.model.*;
import com.avegarlabs.construct_hub.domain.repositories.DespachoRepository;
import com.avegarlabs.construct_hub.domain.repositories.ValeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DespachosServicesImp implements IDespachosService {

    private final DespachoRepository repository;
    private final ValeRepository valeRepository;
    private final IObraService obraService;
    private final IRecursosService recursosService;
    private final IEmpresaService empresaService;
    private final IObjetoService objetoService;

    @Override
    public ValesListItemDTO persist(ValesDTO dto) {
        Vale vale = mapDtoToEntity(dto);
        vale = valeRepository.save(vale);

        Vale finalVale = vale;
        List<Despacho> despachos = dto.getDespachos().stream()
                .map(despachoDTO -> {
                    Despacho despacho = mapDespachoDtoToEntity(despachoDTO);
                    despacho.setVale(finalVale);
                    return repository.save(despacho);
                })
                .toList();
        finalVale.setDespachos(despachos);
        return mapEntityToListItem(finalVale);
    }

    @Override
    public List<ValesListItemDTO> listDespachos() {
        List<Vale> valeList =  valeRepository.getValeByActive(true);
       /* System.out.println(valeList.size());
        for (Vale vale : valeList) {
            System.out.println("Vale: " + vale.getCodigo());
            System.out.println("Objeto: " + (vale.getObjeto() != null ? vale.getObjeto().getCodigo() : "NULL"));
        }*/
        return valeList.stream().map(this::mapEntityToListItem).toList();
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
    public Boolean deleteVale(Long objId) {
        try {
            Vale vale = valeRepository.getReferenceById(objId);
            vale.setActive(false);
            valeRepository.save(vale);
            return true;
        } catch (EmptyResultDataAccessException | DataIntegrityViolationException e) {
            return false;
        }
    }

    @Override
    public List<ValesListItemDTO> findDespachosByObraId(Long idObra) {
        return valeRepository.getValesByObraId(idObra).stream().map(this::mapEntityToListItem).toList();
    }

    @Override
    public List<ValesListItemDTO> findDespachosByEmpresaId(Long idEmp) {
        return valeRepository.getValeByEmpresaId(idEmp).stream().map(this::mapEntityToListItem).toList();
    }

    @Override
    public List<ValesListItemDTO> findDespachosByObjetosId(Long idObj) {
        return valeRepository.getValesByObjetoId(idObj).stream().map(this::mapEntityToListItem).toList();
    }


    private Vale mapDtoToEntity(ValesDTO dto){
        Obra obra = obraService.getEntity(dto.getObraId());
        Empresa empresa = empresaService.getEntity(dto.getEmpresaId());
        Objeto objeto = objetoService.getEntity(dto.getObjetoId());
        return Vale.builder()
                .codigo(dto.getCodigo())
                .empresa(empresa)
                .objeto(objeto)
                .obra(obra)
                .build();
    }

    private Despacho mapDespachoDtoToEntity(DespachoDTO despachoDTO){
        Recurso recurso = recursosService.getEntity(despachoDTO.getRecursoId());
        return Despacho.builder()
                .recurso(recurso)
                .cantidadDespachada(despachoDTO.getCantidadDespachada())
                .build();
    }



    private ValesListItemDTO mapEntityToListItem(Vale vale){
        ObraListItem obra = obraService.findByObraId(vale.getObra().getId());
        EmpresaListItem empresa = empresaService.findByEmpresaById(vale.getEmpresa().getId());
        ObjetoListItem objeto = objetoService.findByObjetoId(vale.getObjeto().getId());
        List<DespachoListItemDTO> despachoListItemDTOS = vale.getDespachos().stream().map(this::mapDespEntityToDto).toList();
        return ValesListItemDTO.builder()
                .id(vale.getId())
                .codigo(vale.getCodigo())
                .empresa(empresa)
                .objeto(objeto)
                .fecha(vale.getFecha())
                .obra(obra)
                .despachos(despachoListItemDTOS)
                .build();
    }

    private DespachoListItemDTO mapDespEntityToDto(Despacho despacho){
        RecursoListItem recursoListItem = recursosService.findByrecursoById(despacho.getRecurso().getId());
        return DespachoListItemDTO.builder()
                .id(despacho.getId())
                .recurso(recursoListItem)
                .cantidadDespachada(despacho.getCantidadDespachada())
                .build();
    }



}
