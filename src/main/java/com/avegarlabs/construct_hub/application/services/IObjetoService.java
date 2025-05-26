package com.avegarlabs.construct_hub.application.services;

import com.avegarlabs.construct_hub.application.dto.ObjetoDTO;
import com.avegarlabs.construct_hub.application.dto.ObjetoListItem;
import com.avegarlabs.construct_hub.application.dto.ObraDTO;
import com.avegarlabs.construct_hub.application.dto.ObraListItem;
import com.avegarlabs.construct_hub.domain.model.Objeto;

import java.util.List;

public interface IObjetoService {

    ObjetoListItem persist(ObjetoDTO dto);

    ObjetoListItem update(Long objId, ObjetoDTO dto);

    Boolean deleteObjeto(Long objId);

    List<ObjetoListItem> listObjetos();

    List<ObjetoListItem> findObjetosByObraId(Long idObj);

    ObjetoListItem findByObjetoId(Long objetoId);

    Objeto getEntity(Long objId);
}
