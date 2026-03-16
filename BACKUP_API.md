# API de Backup de Base de Datos

## Endpoint

```
GET /api/backup
```

**Autenticacion**: Requiere token JWT en el header `Authorization: Bearer <token>`.

**Respuesta**: Archivo SQL con los datos completos de la base de datos en formato PostgreSQL COPY.

### Headers de respuesta

| Header | Valor | Descripcion |
|--------|-------|-------------|
| `Content-Type` | `application/sql; charset=UTF-8` | Archivo SQL |
| `Content-Disposition` | `attachment; filename="construct-hub_backup_2026-03-16_143022.sql"` | Nombre con timestamp |
| `Cache-Control` | `no-cache, no-store, must-revalidate` | Sin cache |

---

## Integracion en Angular

### 1. Servicio

```typescript
// backup.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({ providedIn: 'root' })
export class BackupService {

  private readonly url = '/api/backup';

  constructor(private http: HttpClient) {}

  downloadBackup(): void {
    this.http.get(this.url, {
      responseType: 'blob',
      observe: 'response'
    }).subscribe({
      next: (response) => {
        const contentDisposition = response.headers.get('Content-Disposition');
        const filename = this.extractFilename(contentDisposition)
          || 'construct-hub_backup.sql';

        const blob = response.body!;
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = filename;
        a.click();
        window.URL.revokeObjectURL(url);
      },
      error: (err) => {
        console.error('Error al descargar backup:', err);
      }
    });
  }

  private extractFilename(header: string | null): string | null {
    if (!header) return null;
    const match = header.match(/filename="?([^"]+)"?/);
    return match ? match[1] : null;
  }
}
```

El `HttpClient` agrega automaticamente el header `Authorization: Bearer <token>` a traves del interceptor JWT ya configurado en el proyecto.

### 2. Uso desde un componente

```typescript
import { BackupService } from './backup.service';

constructor(private backupService: BackupService) {}

onDownloadBackup(): void {
  this.backupService.downloadBackup();
}
```

```html
<button (click)="onDownloadBackup()">Descargar Backup</button>
```

---

## Restaurar un backup

```bash
psql -h host -p 5432 -U usuario -d nombre_bd -f construct-hub_backup_2026-03-16_143022.sql
```

El archivo SQL contiene:
- `TRUNCATE` de todas las tablas con `RESTART IDENTITY CASCADE`
- `COPY ... FROM stdin` con los datos de cada tabla (formato nativo PostgreSQL, eficiente)
- `SELECT setval()` para restaurar los valores de las secuencias (auto-increment)
- Todo envuelto en una transaccion (`BEGIN/COMMIT`) con `session_replication_role = replica` para desactivar triggers de FK durante la carga

## Notas

- No requiere `pg_dump` ni `pg_restore`. El backup se genera via JDBC usando el protocolo COPY nativo de PostgreSQL.
- Funciona en cualquier entorno: Docker, local, CI/CD.
- El navegador mostrara el dialogo de "Guardar como" para que el usuario elija la ubicacion.
