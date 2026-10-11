---
description: Flujo Superpowers completo para una tarea (análisis, plan, implementación, verificación, refactor, cierre)
agent: build
---
Carga el skill `superpowers-flow` con la herramienta de skills y síguelo al pie de la letra.

Petición del usuario: $ARGUMENTS

- Si la petición está vacía o es "continuar", "continúa" o "estado": aplica el Paso 0 del skill
  para localizar y reanudar el flujo activo (con "estado", solo informa).
- En cualquier otro caso: es una tarea nueva; empieza el flujo desde el Paso 0.
