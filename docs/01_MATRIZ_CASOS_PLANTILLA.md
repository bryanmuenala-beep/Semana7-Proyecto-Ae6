# Matriz de casos

| ID | Regla | Escenario | Entrada | Esperado | Tipo | Riesgo |
|---|---|---|---|---|---|---|
| CP-01 | Cancelación | Cancelación con anticipación suficiente | 5 horas | true | Normal | Permitir o impedir incorrectamente una cancelación válida |
| CP-02 | Cancelación | Límite exacto permitido | 2 horas | true | Límite | Rechazar una cancelación justo en el límite |
| CP-03 | Cancelación | Debajo del límite | 1 hora | false | Límite | Permitir una cancelación fuera de la política |
| CP-04 | Descuento NORMAL | Total sin descuento | NORMAL, 100 | 100.00 | Normal | Aplicar un descuento no correspondiente |
| CP-05 | Descuento VIP | Aplicar 15% de descuento | VIP, 100 | 85.00 | Alternativo | Calcular incorrectamente el descuento VIP |
| CP-06 | Descuento ESTUDIANTE | Aplicar 10% de descuento | ESTUDIANTE, 100 | 90.00 | Alternativo | Calcular incorrectamente el descuento de estudiante |
| CP-07 | Descuento | Total base igual a cero | VIP, 0 | 0.00 | Límite | Procesar incorrectamente un valor límite válido |
| CP-08 | Descuento | Total base negativo | NORMAL, -1 | IllegalArgumentException | Inválido | Aceptar valores monetarios inválidos |
| CP-09 | Confirmación | Reserva con disponibilidad | Reserva + disponibilidad true | Reserva CONFIRMADA, guardada y notificada | Normal | Confirmar sin completar las operaciones requeridas |
| CP-10 | Confirmación | Horario no disponible | Reserva + disponibilidad false | IllegalStateException | Alternativo | Confirmar una reserva sin disponibilidad |
| CP-11 | Confirmación | Reserva nula | null | IllegalArgumentException | Inválido | Procesar una reserva inexistente |
| CP-12 | Confirmación | Verificar persistencia y notificación | Reserva + disponibilidad true | guardar() y enviarConfirmacion() ejecutados | Interacción | Omitir persistencia o notificación después de confirmar |