---
name: superpowers-flow
description: "Use when the user invokes /flow, or asks to run the complete Superpowers workflow for a task (analysis, plan, implementation, verification, refactor and close), or says continuar/continúa while a flow under docs/flow/ is not TERMINADO."
---

# Flujo Superpowers completo

## Visión general

Lleva una tarea de principio a fin en **cualquier proyecto**, encadenando los skills de Superpowers.
El estado se guarda en disco y en git, así que el flujo sobrevive a cortes, compactación de
contexto y sesiones nuevas.

| # | Fase | Skills | Artefacto / resultado |
|---|---|---|---|
| 1 | ANALISIS | brainstorming | `spec.md` con criterios `CA-n` |
| 2 | PLAN | writing-plans | `plan.md` → **única aprobación del usuario** |
| 3 | IMPLEMENTACION | executing-plans + test-driven-development (+ systematic-debugging) | código + tests, commit por tarea |
| 4 | VERIFICACION | verification-before-completion | `verificacion.md` con evidencia por `CA-n` |
| 5 | REFACTOR | requesting-code-review + receiving-code-review + test-driven-development | código limpio, tests en verde |
| 6 | CIERRE | verification-before-completion + finishing-a-development-branch | merge a la rama principal, `Estado: TERMINADO` |

Carpeta del flujo: `docs/flow/FLOW-NNN-SLUG/` (`spec.md`, `plan.md`, `verificacion.md`).
Plantillas junto a este fichero: `spec-template.md`, `plan-template.md`, `verification-template.md`.
Verificador de documentos: `verificar-artefacto.ps1` (mismo directorio).
Commits siempre con prefijo `FLOW-NNN`.

## Prioridades

1. Las reglas del `AGENTS.md` del proyecto (si existe) prevalecen sobre este skill en entorno,
   comandos, dependencias y estilo.
2. Este skill prevalece sobre el comportamiento por defecto de los skills de Superpowers.

## REGLA 1 — Compuerta de documentos (obligatoria, sin excepciones)

Un documento (`spec.md`, `plan.md`, `verificacion.md`) **no existe** hasta que el verificador
lo confirma. Decir "he creado el fichero" no cuenta; solo cuenta la salida real del verificador.

Después de escribir o modificar un documento, ejecuta de verdad:

```powershell
& ".opencode\skills\superpowers-flow\verificar-artefacto.ps1" -Tipo <spec|plan|verificacion> -Ruta "docs\flow\FLOW-NNN-SLUG\<fichero>.md"
```

- Salida `ARTEFACTO OK` → haz el commit del documento y ejecuta el verificador otra vez con `-Commit`.
- Salida `COMMIT OK` → solo entonces puedes cambiar `Estado:` o pasar a la siguiente fase.
- Salida `ARTEFACTO INCOMPLETO` → lee los motivos, completa lo que falta (por partes) y vuelve a verificar.
- Tres intentos fallidos sobre el mismo documento → **detente**, muestra la última salida del
  verificador y espera al usuario.

Muestra al usuario la salida del verificador tal como sale. Nunca la resumas ni la escribas tú.

## REGLA 2 — Escritura de ficheros (modelo local)

1. Ningún documento se escribe de una vez si va a superar ~120 líneas.
2. Crea primero el esqueleto copiando la plantilla y rellena cada sección con una edición aparte.
   En `plan.md`, una edición por tarea.
3. Sustituye **todos** los marcadores `[[...]]` de la plantilla; el verificador rechaza los que queden.
4. No uses marcadores entre ángulos (`<...>`) en los documentos: escribe valores reales.
5. Si una escritura falla, no la repitas igual: divídela en partes más pequeñas.

## Paso 0 — Preparación (siempre, al empezar)

1. Sistema y shell: en Windows usa sintaxis PowerShell; nunca comandos Unix.
2. Lee `AGENTS.md` si existe.
3. Git: ejecuta de verdad `git status` y `git log --oneline -10`.
   Si no es un repositorio: `git init`, `.gitignore` adecuado al lenguaje y commit
   `FLOW-000: repositorio inicial`. Anúncialo.
4. Detecta el comando de tests. Si no hay framework de tests instalado, propón uno en la spec
   y **no instales nada sin aprobación**.

   | Indicador en la raíz | Comando de tests |
   |---|---|
   | `pyproject.toml`, `requirements.txt`, `*.py` | `.venv\Scripts\python.exe -m pytest` si existe `.venv`; si no, `python -m pytest` |
   | `package.json` | `npm test` (Angular: `npx ng test --watch=false`) |
   | `pom.xml` | `mvn -q test` |
   | `build.gradle*` | `.\gradlew.bat test` |
   | `*.csproj` / `*.sln` | `dotnet test` |
   | `go.mod` | `go test ./...` |
   | `Cargo.toml` | `cargo test` |
   | `AGENTS.md` define otro | el de `AGENTS.md` |

5. Busca un flujo activo: `docs/flow/*/spec.md` con `Estado:` distinto de `TERMINADO`.
   - Existe → **ejecuta el verificador sobre los documentos de las fases ya cerradas**
     (spec; plan si `Estado:` es IMPLEMENTACION o posterior; verificacion si es REFACTOR o posterior).
     Si alguno da `ARTEFACTO INCOMPLETO`, la fase actual es la de ese documento: complétalo primero.
     Después reanuda en la fase de `Estado:`. Si la petición es una tarea nueva distinta,
     avisa de que hay un flujo abierto y pregunta si lo cierra o lo aparca.
   - No existe → crea `FLOW-NNN-SLUG` con el siguiente número libre y empieza en ANALISIS.
6. Anuncia en una línea: `FLOW-NNN — fase FASE — tamaño T — tests: COMANDO`.

## Tamaño y modo de sesión

Se decide al final de ANALISIS y se escribe en `spec.md` (`Tamaño:`).

| Tamaño | Criterio | Modo |
|---|---|---|
| PEQUEÑA | ≤ 3 tareas y ≤ 3 ficheros | **Continuo**: tras la aprobación del plan, fases 3→6 en esta sesión sin parar |
| MEDIANA | 4-6 tareas | Continuo hasta VERIFICACION; REFACTOR y CIERRE en sesión nueva |
| GRANDE | > 6 tareas | Una fase por sesión; el plan se divide en `plan-1.md`, `plan-2.md`… de ≤ 6 tareas |

En cualquier modo: si la conversación se ha hecho larga o notas que pierdes el hilo,
haz commit del punto actual, actualiza `Estado:` y aplica "Parada de fase".

## Fase 1 — ANALISIS

1. Invoca `brainstorming` con estas adaptaciones: lee primero el código y `AGENTS.md`;
   pregunta **solo si hay ambigüedad real**, máximo 3 preguntas, de una en una.
   Si la petición ya es precisa, no preguntes: decide y documenta las suposiciones.
2. Escribe `spec.md` a partir de `spec-template.md` (REGLA 2): alcance, diseño (firmas exactas,
   ficheros), criterios `CA-n` verificables con un test o comando, incluidos casos límite y errores.
3. Decide el tamaño y escribe `Estado: PLAN`.
4. **Compuerta** (REGLA 1) con `-Tipo spec`. Commit `FLOW-NNN analisis: ...`. `COMMIT OK`.
5. Pasa directamente a la fase 2 (no pidas aprobación aún).

## Fase 2 — PLAN

1. Invoca `writing-plans` sobre `spec.md`. No vuelvas a brainstorming.
2. Escribe `plan.md` a partir de `plan-template.md`, **una edición por tarea**. Cada tarea:
   `CA-n` que cubre, ficheros exactos, el código del test que debe fallar primero, firmas del
   cambio mínimo y comando de verificación. **No incluyas el código completo de la implementación.**
3. **Compuerta** con `-Tipo plan`. Commit `FLOW-NNN plan: ...`. `COMMIT OK`.
4. Presenta al usuario un resumen breve: objetivo, `CA-n`, tareas, tamaño y modo.
   **Espera su aprobación** ("adelante"). Es la única parada obligatoria del flujo.
   Si pide cambios: ajusta spec/plan, compuerta otra vez, commit, vuelve a presentar.
5. Con la aprobación: marca la casilla de aprobación en `spec.md`, `Estado: IMPLEMENTACION`,
   compuerta `-Tipo spec`, commit.

## Fase 3 — IMPLEMENTACION

1. Rama `flow/NNN-SLUG` desde la rama principal. No uses git worktrees.
2. Invoca `executing-plans` desde la primera tarea sin commit `FLOW-NNN tarea k`.
   No uses `subagent-driven-development` ni `dispatching-parallel-agents`.
3. Cada tarea con `test-driven-development`: ejecuta el test y muestra la salida real en rojo,
   implementa lo mínimo, muestra el verde, commit `FLOW-NNN tarea k: ...`.
4. Fallo sin causa obvia: `systematic-debugging` antes de seguir cambiando código.
5. Un comando que falla dos veces con el mismo error: detente y reporta el error completo.
6. Plan completo: `Estado: VERIFICACION`, compuerta `-Tipo spec`, commit.

## Fase 4 — VERIFICACION

1. Invoca `verification-before-completion`.
2. Ejecuta la suite completa del proyecto (no solo los tests nuevos).
3. Escribe `verificacion.md` a partir de `verification-template.md`: por cada `CA-n` de la spec,
   el test o comando y su salida real.
4. **Compuerta** con `-Tipo verificacion` (comprueba que están todos los `CA-n`). Commit. `COMMIT OK`.
5. Algún `CA-n` sin evidencia o en fallo → añade la tarea que falta al plan (compuerta `-Tipo plan`),
   `Estado: IMPLEMENTACION`, commit y vuelve a la fase 3. No lo arregles aquí.
6. Todo verificado: `Estado: REFACTOR`, compuerta `-Tipo spec`, commit.

## Fase 5 — REFACTOR

1. Invoca `requesting-code-review` sobre `git diff RAMA-PRINCIPAL...HEAD` contra la spec,
   `AGENTS.md` y buenas prácticas: nombres, duplicación, funciones largas, tipos, docstrings,
   manejo de errores.
2. Aplica los hallazgos con `receiving-code-review`: cambios pequeños **sin cambio de
   comportamiento**, suite completa en verde después de cada uno, commit `FLOW-NNN refactor: ...`.
3. Registra los hallazgos en la sección de refactor de `verificacion.md` (también "sin hallazgos",
   con la justificación). Compuerta `-Tipo verificacion`, commit.
4. Hallazgo que exige cambiar comportamiento → no es refactor: anótalo como pendiente para el usuario.
5. `Estado: CIERRE`, compuerta `-Tipo spec`, commit.

## Fase 6 — CIERRE

1. `verification-before-completion` de nuevo: suite completa y repaso de todos los `CA-n`
   con salidas reales de esta sesión. Actualiza la sección de cierre de `verificacion.md`
   y pasa la compuerta `-Tipo verificacion`.
2. `git status` limpio.
3. `finishing-a-development-branch` con opción fija: merge `--no-ff` de `flow/NNN-SLUG`
   a la rama principal con mensaje `FLOW-NNN: resumen`. Sin PR salvo que el usuario lo pida.
4. `Estado: TERMINADO`, compuerta `-Tipo spec`, commit en la rama principal.
5. Ejecuta el verificador sobre los tres documentos con `-Commit` y muestra las tres salidas.
6. Informe final: qué se implementó, ficheros, tabla `CA-n` con resultado, pendientes
   y salida real de `git log --oneline -5`.

## Parada de fase

Cuando el modo exige parar (o el contexto se degrada), termina con este mensaje y no continúes:

```
FLOW-NNN: fase FASE cerrada. Siguiente: FASE.
Abre una sesión nueva (/new) y escribe: /flow continuar
```

Antes de este mensaje, el documento de la fase cerrada debe haber dado `COMMIT OK`.

## Señales de alarma — detente si piensas esto

| Pensamiento | Realidad |
|---|---|
| "Ya he creado el documento" | Solo existe si el verificador dice `ARTEFACTO OK` y `COMMIT OK` |
| "Lo escribo todo de una vez" | Esqueleto y una edición por sección (REGLA 2) |
| "Es trivial, me salto la spec" | Sin `CA-n` no hay nada que verificar en la fase 4 |
| "Escribo el código y luego el test" | TDD: test en rojo primero, siempre |
| "Los tests pasan, está terminado" | Terminado = todos los `CA-n` con evidencia + refactor + cierre |
| "Ya que estoy, añado esta mejora" | Fuera del plan. Anótala como sugerencia para el usuario |
| "Ajusto el test para que pase" | Prohibido. Corrige el código o consulta |
| "Resumo lo que devolvería el comando" | Solo salidas reales. Nunca escribas texto de llamadas a herramientas ni de resultados |
| "Instalo esta librería que falta" | Ninguna dependencia nueva sin aprobación |

## Errores comunes

- No leer `Estado:` al reanudar → se repite o se salta una fase.
- Commits sin prefijo `FLOW-NNN` → el flujo no puede reconstruir el estado.
- Verificar solo los tests nuevos → las regresiones pasan desapercibidas.
- Ejecutar programas interactivos (TUI, servidores) en primer plano → bloquean la sesión.
