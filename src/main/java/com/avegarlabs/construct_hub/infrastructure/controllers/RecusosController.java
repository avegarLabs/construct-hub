package com.avegarlabs.construct_hub.infrastructure.controllers;

import com.avegarlabs.construct_hub.application.dto.RecursoDTO;
import com.avegarlabs.construct_hub.application.dto.RecursoListItem;
import com.avegarlabs.construct_hub.application.services.IRecursosService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recursos")
@RequiredArgsConstructor
public class RecusosController {

    private final IRecursosService service;

    @PostMapping
    public ResponseEntity<RecursoListItem> createRecurso(@RequestBody RecursoDTO dto) {
        RecursoListItem response = service.persist(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{recId}")
    public ResponseEntity<RecursoListItem> updateRecurso(
            @PathVariable Long empId,
            @RequestBody RecursoDTO dto
    ) {
        RecursoListItem response = service.update(empId, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{recId}")
    public ResponseEntity<Boolean> deleteRecurso(@PathVariable Long recId) {
        boolean success = service.deleteRecurso(recId);
        return success ?
                ResponseEntity.ok(true) :
                ResponseEntity.status(HttpStatus.CONFLICT).body(false);
    }

    @GetMapping
    public ResponseEntity<List<RecursoListItem>> getAllRecurso() {
        List<RecursoListItem> recursos = service.listRecursos();
        return ResponseEntity.ok(recursos);
    }

    @GetMapping("/{recId}")
    public ResponseEntity<RecursoListItem> getrecursosById(@PathVariable Long recId) {
        RecursoListItem recursoListItem = service.findByrecursoById(recId);
        return ResponseEntity.ok(recursoListItem);
    }
}
