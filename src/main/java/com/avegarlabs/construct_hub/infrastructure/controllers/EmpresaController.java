package com.avegarlabs.construct_hub.infrastructure.controllers;

import com.avegarlabs.construct_hub.application.dto.EmpresaDTO;
import com.avegarlabs.construct_hub.application.dto.EmpresaListItem;
import com.avegarlabs.construct_hub.application.services.IEmpresaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/empresas")
@RequiredArgsConstructor
public class EmpresaController {

    private final IEmpresaService empresaService;

    @PostMapping
    public ResponseEntity<EmpresaListItem> createEmpresa(@RequestBody EmpresaDTO empresaDTO) {
        EmpresaListItem response = empresaService.persist(empresaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{empId}")
    public ResponseEntity<EmpresaListItem> updateEmpresa(
            @PathVariable Long empId,
            @RequestBody EmpresaDTO empresaDTO
    ) {
        EmpresaListItem response = empresaService.update(empId, empresaDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{empId}")
    public ResponseEntity<Boolean> deleteEmpresa(@PathVariable Long empId) {
        boolean success = empresaService.deleteEmpresa(empId);
        return success ?
                ResponseEntity.ok(true) :
                ResponseEntity.status(HttpStatus.CONFLICT).body(false);
    }

    @GetMapping
    public ResponseEntity<List<EmpresaListItem>> getAllEmpresas() {
        List<EmpresaListItem> empresas = empresaService.listEmpresas();
        return ResponseEntity.ok(empresas);
    }

    @GetMapping("/{empresaId}")
    public ResponseEntity<EmpresaListItem> getEmpresaById(@PathVariable Long empresaId) {
        EmpresaListItem empresa = empresaService.findByEmpresaById(empresaId);
        return ResponseEntity.ok(empresa);
    }
}
