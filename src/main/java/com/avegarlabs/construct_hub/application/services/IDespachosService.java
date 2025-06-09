package com.avegarlabs.construct_hub.application.services;

import com.avegarlabs.construct_hub.application.dto.*;

import java.util.List;

public interface IDespachosService {

    ValesListItemDTO persist(ValesDTO dto);

    List<ValesListItemDTO> listDespachos();

    Boolean deleteDespacho(Long objId);
    Boolean deleteVale(Long objId);
    List<ValesListItemDTO> findDespachosByObraId(Long idObj);
    List<ValesListItemDTO> findDespachosByEmpresaId(Long idObj);
    List<ValesListItemDTO> findDespachosByObjetosId(Long idObj);

   /*DespachoListItem update(Long despId, DespachoDTO dto);

    Boolean deleteDespacho(Long objId);

    List<DespachoListItem> listDespachos();

    List<DespachoListItem> findDespachosByObraId(Long idObj);
    List<DespachoListItem> findDespachosByEmpresaId(Long idObj);
    List<DespachoListItem> findDespachosByObjetosId(Long idObj);
    List<DespachoListItem> findDespachosByRecursoId(Long idObj);

    DespachoListItem findByDespachoId(Long objetoId);*/
}
