package com.avegarlabs.construct_hub.application.services;

import com.avegarlabs.construct_hub.application.dto.ImportErrorDTO;
import com.avegarlabs.construct_hub.application.dto.ImportResultDTO;
import com.avegarlabs.construct_hub.application.dto.RecursoDTO;
import com.avegarlabs.construct_hub.domain.model.Obra;
import com.avegarlabs.construct_hub.domain.model.Recurso;
import com.avegarlabs.construct_hub.domain.repositories.ObraRepository;
import com.avegarlabs.construct_hub.domain.repositories.RecursoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExcelImportService {

    private final RecursoRepository recursoRepository;
    private final ObraRepository obraRepository;

    private static final int MAX_ROWS = 1000;
    private static final String[] EXPECTED_HEADERS = {"Código", "Descripción", "U/M", "Cantidad", "Precio"};

    @Transactional
    public ImportResultDTO importRecursosFromExcel(MultipartFile file, Long obraId) throws IOException {
        log.info("Starting Excel import for obra ID: {}", obraId);

        // Validate obra exists
        Obra obra = obraRepository.findById(obraId)
                .orElseThrow(() -> new IllegalArgumentException("Obra not found with ID: " + obraId));

        ImportResultDTO result = ImportResultDTO.builder()
                .totalRows(0)
                .successfulRows(0)
                .failedRows(0)
                .errors(new ArrayList<>())
                .build();

        Workbook workbook = null;
        try {
            workbook = createWorkbook(file);
            Sheet sheet = workbook.getSheetAt(0);

            // Validate headers
            if (!validateHeaders(sheet.getRow(0))) {
                throw new IllegalArgumentException("Invalid Excel format. Expected headers: Código, Descripción, U/M, Cantidad, Precio");
            }

            // Pre-load existing códigos to check duplicates efficiently
            Set<String> existingCodigos = new HashSet<>(
                recursoRepository.findAllByObraId(obraId).stream()
                    .map(Recurso::getCodigo)
                    .toList()
            );

            // Batch processing
            List<Recurso> recursosToSave = new ArrayList<>();
            Set<String> newCodigosInBatch = new HashSet<>();

            int rowNum = 1; // Skip header
            int processedRows = 0;

            while (rowNum <= sheet.getLastRowNum() && processedRows < MAX_ROWS) {
                Row row = sheet.getRow(rowNum);
                if (row == null || isRowEmpty(row)) {
                    rowNum++;
                    continue;
                }

                try {
                    RecursoDTO dto = parseRowToDTO(row, obraId);

                    // Validate codigo uniqueness
                    if (existingCodigos.contains(dto.getCodigo()) ||
                        newCodigosInBatch.contains(dto.getCodigo())) {
                        result.getErrors().add(ImportErrorDTO.builder()
                            .rowNumber(rowNum + 1)
                            .codigo(dto.getCodigo())
                            .message("Código duplicado: " + dto.getCodigo())
                            .build());
                        result.setFailedRows(result.getFailedRows() + 1);
                    } else {
                        Recurso recurso = buildRecursoFromDTO(dto, obra);
                        recursosToSave.add(recurso);
                        newCodigosInBatch.add(dto.getCodigo());
                        result.setSuccessfulRows(result.getSuccessfulRows() + 1);
                    }
                } catch (Exception e) {
                    log.error("Error processing row {}: {}", rowNum + 1, e.getMessage());
                    result.getErrors().add(ImportErrorDTO.builder()
                        .rowNumber(rowNum + 1)
                        .codigo(getCellValueAsString(row.getCell(0)))
                        .message(e.getMessage())
                        .build());
                    result.setFailedRows(result.getFailedRows() + 1);
                }

                rowNum++;
                processedRows++;
            }

            result.setTotalRows(processedRows);

            // Batch save
            if (!recursosToSave.isEmpty()) {
                log.info("Saving {} recursos in batch", recursosToSave.size());
                recursoRepository.saveAll(recursosToSave);
            }

            log.info("Import completed. Success: {}, Failed: {}", result.getSuccessfulRows(), result.getFailedRows());
            return result;

        } finally {
            if (workbook != null) {
                workbook.close();
            }
        }
    }

    private Workbook createWorkbook(MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();
        if (filename == null) {
            throw new IllegalArgumentException("Filename is null");
        }

        if (filename.endsWith(".xlsx")) {
            return new XSSFWorkbook(file.getInputStream());
        } else if (filename.endsWith(".xls")) {
            return new HSSFWorkbook(file.getInputStream());
        } else {
            throw new IllegalArgumentException("Invalid file format. Only .xlsx and .xls are supported");
        }
    }

    private boolean validateHeaders(Row headerRow) {
        if (headerRow == null) return false;

        for (int i = 0; i < EXPECTED_HEADERS.length; i++) {
            Cell cell = headerRow.getCell(i);
            String cellValue = getCellValueAsString(cell).trim();
            if (!EXPECTED_HEADERS[i].equalsIgnoreCase(cellValue)) {
                return false;
            }
        }
        return true;
    }

    private boolean isRowEmpty(Row row) {
        for (int i = 0; i < 5; i++) {
            Cell cell = row.getCell(i);
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                return false;
            }
        }
        return true;
    }

    private RecursoDTO parseRowToDTO(Row row, Long obraId) {
        String codigo = getCellValueAsString(row.getCell(0)).trim();
        String descripcion = getCellValueAsString(row.getCell(1)).trim();
        String um = getCellValueAsString(row.getCell(2)).trim();

        if (codigo.isEmpty()) {
            throw new IllegalArgumentException("El código es requerido");
        }
        if (descripcion.isEmpty()) {
            throw new IllegalArgumentException("La descripción es requerida");
        }
        if (um.isEmpty()) {
            throw new IllegalArgumentException("La U/M es requerida");
        }

        BigDecimal cantidad;
        try {
            cantidad = new BigDecimal(getCellValueAsString(row.getCell(3)));
            if (cantidad.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Cantidad inválida: debe ser un número");
        }

        BigDecimal precio = BigDecimal.ZERO;
        Cell precioCell = row.getCell(4);
        if (precioCell != null && precioCell.getCellType() != CellType.BLANK) {
            try {
                precio = new BigDecimal(getCellValueAsString(precioCell));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Precio inválido: debe ser un número");
            }
        }

        return RecursoDTO.builder()
                .codigo(codigo)
                .descripcion(descripcion)
                .um(um)
                .cantidad(cantidad)
                .precio(precio)
                .obraId(obraId)
                .build();
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) return "";

        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> {
                if (DateUtil.isCellDateFormatted(cell)) {
                    yield cell.getDateCellValue().toString();
                } else {
                    // Remove decimal for whole numbers
                    double numValue = cell.getNumericCellValue();
                    if (numValue == Math.floor(numValue)) {
                        yield String.valueOf((long) numValue);
                    } else {
                        yield String.valueOf(numValue);
                    }
                }
            }
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> cell.getCellFormula();
            default -> "";
        };
    }

    private Recurso buildRecursoFromDTO(RecursoDTO dto, Obra obra) {
        Recurso recurso = new Recurso();
        recurso.setCodigo(dto.getCodigo());
        recurso.setDescripcion(dto.getDescripcion());
        recurso.setUm(dto.getUm());
        recurso.setCantidad(dto.getCantidad());
        recurso.setPrecio(dto.getPrecio());
        recurso.setObra(obra);
        return recurso;
    }
}
