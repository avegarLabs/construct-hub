package com.avegarlabs.construct_hub.application.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.copy.CopyManager;
import org.postgresql.core.BaseConnection;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BackupServiceImp implements IBackupService {

    private final DataSource dataSource;

    @Override
    public StreamingResponseBody generateBackup() {
        return outputStream -> {
            try (Connection conn = dataSource.getConnection()) {
                CopyManager copyManager = new CopyManager(conn.unwrap(BaseConnection.class));

                write(outputStream, "-- Construct Hub Database Backup\n");
                write(outputStream, "-- Fecha: " + LocalDateTime.now() + "\n");
                write(outputStream, "-- Formato: PostgreSQL COPY (text)\n\n");
                write(outputStream, "BEGIN;\n");
                write(outputStream, "SET session_replication_role = replica;\n\n");

                List<String> tables = getTables(conn);

                if (!tables.isEmpty()) {
                    String tableList = tables.stream()
                            .map(t -> "\"" + t + "\"")
                            .collect(Collectors.joining(", "));
                    write(outputStream, "TRUNCATE " + tableList + " RESTART IDENTITY CASCADE;\n\n");
                }

                for (String table : tables) {
                    String columns = getColumns(conn, table);
                    write(outputStream, "-- Tabla: " + table + "\n");
                    write(outputStream, "COPY \"" + table + "\" (" + columns + ") FROM stdin;\n");
                    outputStream.flush();

                    copyManager.copyOut(
                            "COPY \"" + table + "\" (" + columns + ") TO STDOUT",
                            outputStream
                    );

                    write(outputStream, "\\.\n\n");
                }

                List<String[]> sequences = getSequences(conn);
                if (!sequences.isEmpty()) {
                    write(outputStream, "-- Secuencias\n");
                    for (String[] seq : sequences) {
                        write(outputStream, "SELECT setval('\"" + seq[0] + "\"', " + seq[1] + ", " + seq[2] + ");\n");
                    }
                    write(outputStream, "\n");
                }

                write(outputStream, "SET session_replication_role = DEFAULT;\n");
                write(outputStream, "COMMIT;\n");
                write(outputStream, "-- Fin del backup\n");
                outputStream.flush();

                log.info("Backup de base de datos completado exitosamente");

            } catch (SQLException e) {
                log.error("Error de base de datos durante backup", e);
                throw new IOException("Error al generar backup: " + e.getMessage(), e);
            }
        };
    }

    @Override
    public String generateBackupFilename() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HHmmss");
        String timestamp = LocalDateTime.now().format(formatter);
        return String.format("construct-hub_backup_%s.sql", timestamp);
    }

    private void write(OutputStream os, String s) throws IOException {
        os.write(s.getBytes(StandardCharsets.UTF_8));
    }

    private List<String> getTables(Connection conn) throws SQLException {
        List<String> tables = new ArrayList<>();
        try (ResultSet rs = conn.getMetaData().getTables(null, "public", null, new String[]{"TABLE"})) {
            while (rs.next()) {
                tables.add(rs.getString("TABLE_NAME"));
            }
        }
        return tables;
    }

    private String getColumns(Connection conn, String table) throws SQLException {
        List<String> columns = new ArrayList<>();
        try (ResultSet rs = conn.getMetaData().getColumns(null, "public", table, null)) {
            while (rs.next()) {
                columns.add("\"" + rs.getString("COLUMN_NAME") + "\"");
            }
        }
        return String.join(", ", columns);
    }

    private List<String[]> getSequences(Connection conn) throws SQLException {
        List<String[]> sequences = new ArrayList<>();
        String sql = "SELECT sequencename, last_value, start_value FROM pg_sequences WHERE schemaname = 'public'";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String name = rs.getString("sequencename");
                long lastValue = rs.getLong("last_value");
                if (rs.wasNull()) {
                    long startValue = rs.getLong("start_value");
                    sequences.add(new String[]{name, String.valueOf(startValue), "false"});
                } else {
                    sequences.add(new String[]{name, String.valueOf(lastValue), "true"});
                }
            }
        }
        return sequences;
    }
}
