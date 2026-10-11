<#
Verifica que un documento del flujo /flow existe, tiene contenido real y la estructura minima.
  -Tipo    spec | plan | verificacion
  -Ruta    ruta del .md
  -Commit  ademas comprueba que el fichero esta versionado y sin cambios pendientes
Salida: ARTEFACTO OK / ARTEFACTO INCOMPLETO (+ motivos), y con -Commit: COMMIT OK / COMMIT PENDIENTE.
Codigo de salida: 0 si todo OK, 1 si falla algo.
#>
param(
    [Parameter(Mandatory = $true)][ValidateSet("spec", "plan", "verificacion")][string]$Tipo,
    [Parameter(Mandatory = $true)][string]$Ruta,
    [switch]$Commit
)

$fallos = New-Object System.Collections.Generic.List[string]
$minLineas = @{ spec = 15; plan = 8; verificacion = 8 }

if (-not (Test-Path -LiteralPath $Ruta -PathType Leaf)) {
    Write-Output "ARTEFACTO INCOMPLETO: $Ruta"
    Write-Output " - El fichero NO EXISTE en disco."
    exit 1
}

$texto  = [System.IO.File]::ReadAllText((Resolve-Path -LiteralPath $Ruta).Path)
$lineas = @($texto -split "`r?`n" | Where-Object { $_.Trim() -ne "" })
$bytes  = (Get-Item -LiteralPath $Ruta).Length

if ($bytes -eq 0) { $fallos.Add("El fichero esta VACIO (0 bytes).") }
if ($lineas.Count -lt $minLineas[$Tipo]) {
    $fallos.Add("Solo $($lineas.Count) lineas con contenido; minimo $($minLineas[$Tipo]) para '$Tipo'.")
}
$marcadores = [regex]::Matches($texto, "\[\[[^\]]*\]\]")
if ($marcadores.Count -gt 0) {
    $ej = ($marcadores | Select-Object -First 3 | ForEach-Object { $_.Value }) -join ", "
    $fallos.Add("Quedan $($marcadores.Count) marcadores de plantilla sin rellenar, p. ej.: $ej")
}

$casEnDoc = @([regex]::Matches($texto, "CA-\d+") | ForEach-Object { $_.Value } | Sort-Object -Unique)

switch ($Tipo) {
    "spec" {
        if ($texto -notmatch "(?m)^Estado:\s*(ANALISIS|PLAN|IMPLEMENTACION|VERIFICACION|REFACTOR|CIERRE|TERMINADO)\s*$") {
            $fallos.Add("Falta una linea 'Estado:' con un valor valido.")
        }
        if ($texto -notmatch "(?m)^##.*Criterios de aceptaci") {
            $fallos.Add("Falta la seccion '## 5. Criterios de aceptacion'.")
        }
        if ([regex]::Matches($texto, "(?m)^\|\s*CA-\d+\s*\|").Count -lt 1) {
            $fallos.Add("La tabla de criterios no tiene ninguna fila CA-n.")
        }
    }
    "plan" {
        $tareas = [regex]::Matches($texto, "(?m)^##\s+Tarea\s+\d+").Count
        if ($tareas -lt 1) { $fallos.Add("No hay ninguna seccion '## Tarea N'.") }
        if ($tareas -gt 6) { $fallos.Add("$tareas tareas: el maximo por plan es 6. Divide en plan-1.md, plan-2.md...") }
        $cubre = [regex]::Matches($texto, "(?m)^-\s*Cubre:\s*CA-\d+").Count
        if ($cubre -lt $tareas) { $fallos.Add("Hay $tareas tareas pero solo $cubre lineas '- Cubre: CA-n'.") }
        if ($texto -notmatch "(?m)^-\s*Verificaci") { $fallos.Add("Ninguna tarea tiene linea '- Verificacion:'.") }
        $spec = Join-Path (Split-Path -Parent $Ruta) "spec.md"
        if (Test-Path -LiteralPath $spec) {
            $casSpec = @([regex]::Matches((Get-Content -Raw -LiteralPath $spec), "(?m)^\|\s*(CA-\d+)\s*\|") |
                ForEach-Object { $_.Groups[1].Value } | Sort-Object -Unique)
            $sinCubrir = $casSpec | Where-Object { $casEnDoc -notcontains $_ }
            if ($sinCubrir) { $fallos.Add("CA de la spec sin tarea en el plan: $($sinCubrir -join ', ')") }
        }
    }
    "verificacion" {
        if ($texto -notmatch "(?m)^Suite completa:") { $fallos.Add("Falta la linea 'Suite completa:' con la salida real.") }
        $spec = Join-Path (Split-Path -Parent $Ruta) "spec.md"
        if (Test-Path -LiteralPath $spec) {
            $casSpec = @([regex]::Matches((Get-Content -Raw -LiteralPath $spec), "(?m)^\|\s*(CA-\d+)\s*\|") |
                ForEach-Object { $_.Groups[1].Value } | Sort-Object -Unique)
            $faltan = $casSpec | Where-Object { $casEnDoc -notcontains $_ }
            if ($faltan) { $fallos.Add("CA de la spec sin evidencia en verificacion.md: $($faltan -join ', ')") }
        } else {
            $fallos.Add("No se encuentra spec.md junto a verificacion.md para comprobar los CA.")
        }
    }
}

$ok = $fallos.Count -eq 0
if ($ok) {
    Write-Output ("ARTEFACTO OK: {0} ({1} bytes, {2} lineas con contenido, CA: {3})" -f $Ruta, $bytes, $lineas.Count, ($casEnDoc -join " "))
} else {
    Write-Output "ARTEFACTO INCOMPLETO: $Ruta"
    $fallos | ForEach-Object { Write-Output " - $_" }
}

if ($Commit) {
    $versionado = git ls-files -- $Ruta 2>$null
    $pendiente  = git status --porcelain -- $Ruta 2>$null
    if (-not $versionado) {
        Write-Output "COMMIT PENDIENTE: el fichero no esta en git (falta git add + commit)."
        $ok = $false
    } elseif ($pendiente) {
        Write-Output "COMMIT PENDIENTE: hay cambios sin commit -> $pendiente"
        $ok = $false
    } else {
        $ultimo = git log -1 --format="%h %s" -- $Ruta
        Write-Output "COMMIT OK: $ultimo"
    }
}

if ($ok) { exit 0 } else { exit 1 }
