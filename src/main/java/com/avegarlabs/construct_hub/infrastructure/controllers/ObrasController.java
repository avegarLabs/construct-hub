package com.avegarlabs.construct_hub.infrastructure.controllers;

import com.avegarlabs.construct_hub.application.dto.ObraDTO;
import com.avegarlabs.construct_hub.application.dto.ObraListItem;
import com.avegarlabs.construct_hub.application.services.IObraService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/obras")
@RequiredArgsConstructor
public class ObrasController {

    private final IObraService obraService;

    @PostMapping
    public ResponseEntity<ObraListItem> createObra(@RequestBody ObraDTO obraDTO) {
        ObraListItem response = obraService.persist(obraDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{obraId}")
    public ResponseEntity<ObraListItem> updateObra(
            @PathVariable Long obraId,
            @RequestBody ObraDTO obraDTO
    ) {
        ObraListItem response = obraService.update(obraId, obraDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{obraId}")
    public ResponseEntity<Boolean> deleteObra(@PathVariable Long obraId) {
        boolean success = obraService.deleteObra(obraId);
        return success ?
                ResponseEntity.ok(true) :
                ResponseEntity.status(HttpStatus.CONFLICT).body(false);
    }

    @GetMapping
    public ResponseEntity<List<ObraListItem>> getAllObras() {
        List<ObraListItem> obras = obraService.listObras();
        return ResponseEntity.ok(obras);
    }

    @GetMapping("/{obraId}")
    public ResponseEntity<?> addEmpresaToObra(@PathVariable Long obraId) {
        ObraListItem obra = obraService.findByObraId(obraId);
        if (obra == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Obra no encontrada con ID: " + obraId);
        }
        return ResponseEntity.ok(obra);
    }

    @PutMapping("/add/{obraId}")
     public ResponseEntity<ObraListItem> addEmpresasToObra(@PathVariable Long obraId, @RequestBody Set<Long> ids) {
     ObraListItem obra = obraService.addEmpresa(obraId, ids);
     return ResponseEntity.ok(obra);
     }
     @GetMapping("/removeIn/{obraId}/emp/{empId}")
     public ResponseEntity<ObraListItem> getObraById(@PathVariable Long obraId, @PathVariable Long empId) {
     ObraListItem obra = obraService.deleteEmpresa(obraId, empId);
     return ResponseEntity.ok(obra);
     }

}