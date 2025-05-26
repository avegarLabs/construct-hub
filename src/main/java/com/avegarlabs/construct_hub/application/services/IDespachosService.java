package com.avegarlabs.construct_hub.application.services;

import com.avegarlabs.construct_hub.application.dto.DespachoDTO;
import com.avegarlabs.construct_hub.application.dto.DespachoListItem;
import com.avegarlabs.construct_hub.application.dto.ObjetoDTO;
import com.avegarlabs.construct_hub.application.dto.ObjetoListItem;

import java.util.List;

public interface IDespachosService {

    DespachoListItem persist(DespachoDTO dto);

    DespachoListItem update(Long despId, DespachoDTO dto);

    Boolean deleteDespacho(Long objId);

    List<DespachoListItem> listDespachos();

    List<DespachoListItem> findDespachosByObraId(Long idObj);
    List<DespachoListItem> findDespachosByEmpresaId(Long idObj);
    List<DespachoListItem> findDespachosByObjetosId(Long idObj);
    List<DespachoListItem> findDespachosByRecursoId(Long idObj);

    DespachoListItem findByDespachoId(Long objetoId);
}
