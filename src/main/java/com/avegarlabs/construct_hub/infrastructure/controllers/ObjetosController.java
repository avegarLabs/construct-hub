package com.avegarlabs.construct_hub.infrastructure.controllers;

import com.avegarlabs.construct_hub.application.dto.ObjetoDTO;
import com.avegarlabs.construct_hub.application.dto.ObjetoListItem;
import com.avegarlabs.construct_hub.application.services.IObjetoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/objetos")
@RequiredArgsConstructor
public class ObjetosController {

    private final IObjetoService service;

    @PostMapping
    public ResponseEntity<ObjetoListItem> createObjeto(@RequestBody ObjetoDTO dto) {
        ObjetoListItem response =service.persist(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{objetId}")
    public ResponseEntity<ObjetoListItem> update(
            @PathVariable Long obraId,
            @RequestBody ObjetoDTO dto
    ) {
        ObjetoListItem response = service.update(obraId, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{objetoId}")
    public ResponseEntity<Boolean> deleteObjeto(@PathVariable Long objetoId) {
        boolean success = service.deleteObjeto(objetoId);
        return success ?
                ResponseEntity.ok(true) :
                ResponseEntity.status(HttpStatus.CONFLICT).body(false);
    }

    @GetMapping
    public ResponseEntity<List<ObjetoListItem>> getAllObjetos() {
        List<ObjetoListItem> objetos = service.listObjetos();
        return ResponseEntity.ok(objetos);
    }

    @GetMapping("/{objetosId}")
    public ResponseEntity<ObjetoListItem> getObjetoById(@PathVariable Long objId) {
        ObjetoListItem objeto = service.findByObjetoId(objId);
        return ResponseEntity.ok(objeto);
    }

    @GetMapping("/obra/{obraId}")
    public ResponseEntity<List<ObjetoListItem>> getObjetoByObraId(@PathVariable Long objId) {
        List<ObjetoListItem> objetoListItems = service.findObjetosByObraId(objId);
        return ResponseEntity.ok(objetoListItems);
    }
}