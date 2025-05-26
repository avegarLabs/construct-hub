package com.avegarlabs.construct_hub.infrastructure.controllers;

import com.avegarlabs.construct_hub.application.dto.DespachoDTO;
import com.avegarlabs.construct_hub.application.dto.DespachoListItem;
import com.avegarlabs.construct_hub.application.services.IDespachosService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/despachos")
@RequiredArgsConstructor
public class DespachosController {

    private final IDespachosService service;

    @PostMapping
    public ResponseEntity<DespachoListItem> createDespacho(@RequestBody DespachoDTO dto) {
        DespachoListItem response =service.persist(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{despId}")
    public ResponseEntity<DespachoListItem> update(
            @PathVariable Long obraId,
            @RequestBody DespachoDTO dto
    ) {
        DespachoListItem response = service.update(obraId, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{despId}")
    public ResponseEntity<Boolean> deleteDesapacho(@PathVariable Long despId) {
        boolean success = service.deleteDespacho(despId);
        return success ?
                ResponseEntity.ok(true) :
                ResponseEntity.status(HttpStatus.CONFLICT).body(false);
    }

    @GetMapping
    public ResponseEntity<List<DespachoListItem>> getAllDespachos() {
        List<DespachoListItem> objetos = service.listDespachos();
        return ResponseEntity.ok(objetos);
    }

    @GetMapping("/{despId}")
    public ResponseEntity<DespachoListItem> getDesapachoById(@PathVariable Long despId) {
        DespachoListItem objeto = service.findByDespachoId(despId);
        return ResponseEntity.ok(objeto);
    }

    @GetMapping("/byObra/{obraId}")
    public ResponseEntity<List<DespachoListItem>> listByObra(@PathVariable Long obraId) {
        List<DespachoListItem> despachoListItems = service.findDespachosByObraId(obraId);
        return ResponseEntity.ok(despachoListItems);
    }

    @GetMapping("/byObjeto/{objId}")
    public ResponseEntity<List<DespachoListItem>> listByObjeto(@PathVariable Long objId) {
        List<DespachoListItem> despachoListItems = service.findDespachosByObjetosId(objId);
        return ResponseEntity.ok(despachoListItems);
    }

    @GetMapping("/byEmpresa/{empId}")
    public ResponseEntity<List<DespachoListItem>> listByEmpresa(@PathVariable Long empId) {
        List<DespachoListItem> despachoListItems = service.findDespachosByEmpresaId(empId);
        return ResponseEntity.ok(despachoListItems);
    }

    @GetMapping("/byRecurso/{recId}")
    public ResponseEntity<List<DespachoListItem>> listByRecurso(@PathVariable Long recId) {
        List<DespachoListItem> despachoListItems = service.findDespachosByRecursoId(recId);
        return ResponseEntity.ok(despachoListItems);
    }
}