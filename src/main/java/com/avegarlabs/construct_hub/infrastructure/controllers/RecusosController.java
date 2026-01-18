package com.avegarlabs.construct_hub.infrastructure.controllers;

import com.avegarlabs.construct_hub.application.dto.ImportResultDTO;
import com.avegarlabs.construct_hub.application.dto.RecursoDTO;
import com.avegarlabs.construct_hub.application.dto.RecursoListItem;
import com.avegarlabs.construct_hub.application.services.IRecursosService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
            @PathVariable Long recId,
            @RequestBody RecursoDTO dto
    ) {
        RecursoListItem response = service.update(recId, dto);
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

    @GetMapping("/obra/{obraId}")
    public ResponseEntity<List<RecursoListItem>> getAllRecursoByObra(@PathVariable Long obraId) {
        List<RecursoListItem> recursos = service.listRecursosByObra(obraId);
        return ResponseEntity.ok(recursos);
    }

    @GetMapping("/{recId}")
    public ResponseEntity<RecursoListItem> getrecursosById(@PathVariable Long recId) {
        RecursoListItem recursoListItem = service.findByrecursoById(recId);
        return ResponseEntity.ok(recursoListItem);
    }

    @PostMapping("/import/{obraId}")
    public ResponseEntity<ImportResultDTO> importRecursosFromExcel(
            @PathVariable Long obraId,
            @RequestParam("file") MultipartFile file) {
        try {
            // Validate file
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            String filename = file.getOriginalFilename();
            if (filename == null || (!filename.endsWith(".xlsx") && !filename.endsWith(".xls"))) {
                return ResponseEntity.badRequest().build();
            }

            ImportResultDTO result = service.importRecursosFromExcel(file, obraId);

            if (result.getFailedRows() == 0) {
                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(result);
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/template")
    public ResponseEntity<byte[]> downloadTemplate() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Recursos");

        // Create header row
        Row headerRow = sheet.createRow(0);
        String[] headers = {"Código", "Descripción", "U/M", "Cantidad", "Precio"};

        CellStyle headerStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        headerStyle.setFont(font);

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
            sheet.setColumnWidth(i, 4000);
        }

        // Example rows
        Row example1 = sheet.createRow(1);
        example1.createCell(0).setCellValue("REC-001");
        example1.createCell(1).setCellValue("Cemento gris tipo I");
        example1.createCell(2).setCellValue("kg");
        example1.createCell(3).setCellValue(1500.50);
        example1.createCell(4).setCellValue(0.85);

        Row example2 = sheet.createRow(2);
        example2.createCell(0).setCellValue("REC-002");
        example2.createCell(1).setCellValue("Arena fina");
        example2.createCell(2).setCellValue("m³");
        example2.createCell(3).setCellValue(25);
        example2.createCell(4).setCellValue(15.00);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        HttpHeaders headersHttp = new HttpHeaders();
        headersHttp.add("Content-Disposition", "attachment; filename=plantilla_recursos.xlsx");
        headersHttp.add("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        return new ResponseEntity<>(outputStream.toByteArray(), headersHttp, HttpStatus.OK);
    }
}
