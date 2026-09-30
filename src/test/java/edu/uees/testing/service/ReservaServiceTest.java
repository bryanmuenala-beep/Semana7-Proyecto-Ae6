package edu.uees.testing.service;

import edu.uees.testing.availability.DisponibilidadClient;
import edu.uees.testing.domain.EstadoReserva;
import edu.uees.testing.domain.Reserva;
import edu.uees.testing.notification.Notificador;
import edu.uees.testing.repository.ReservaRepository;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ReservaServiceTest {

    // CP-01
    @Test
    void cincoHorasPermitenCancelar() {
        // Arrange
        ReservaService servicio = new ReservaService(null, null, null);

        // Act
        boolean resultado = servicio.puedeCancelar(5);

        // Assert
        assertTrue(resultado);
    }

    // CP-02
    @Test
    void dosHorasPermitenCancelar() {
        // Arrange
        ReservaService servicio = new ReservaService(null, null, null);

        // Act
        boolean resultado = servicio.puedeCancelar(2);

        // Assert
        assertTrue(resultado);
    }

    // CP-03
    @Test
    void unaHoraNoPermiteCancelar() {
        // Arrange
        ReservaService servicio = new ReservaService(null, null, null);

        // Act
        boolean resultado = servicio.puedeCancelar(1);

        // Assert
        assertFalse(resultado);
    }

    // CP-04
    @Test
    void tipoNormalMantieneTotalBase() {
        // Arrange
        ReservaService servicio = new ReservaService(null, null, null);

        // Act
        double resultado = servicio.calcularTotal("NORMAL", 100);

        // Assert
        assertEquals(100.00, resultado);
    }

    // CP-05
    @Test
    void tipoVipAplicaQuincePorCientoDescuento() {
        // Arrange
        ReservaService servicio = new ReservaService(null, null, null);

        // Act
        double resultado = servicio.calcularTotal("VIP", 100);

        // Assert
        assertEquals(85.00, resultado);
    }

    // CP-06
    @Test
    void tipoEstudianteAplicaDiezPorCientoDescuento() {
        // Arrange
        ReservaService servicio = new ReservaService(null, null, null);

        // Act
        double resultado = servicio.calcularTotal("ESTUDIANTE", 100);

        // Assert
        assertEquals(90.00, resultado);
    }

    // CP-07
    @Test
    void totalBaseCeroEsValido() {
        // Arrange
        ReservaService servicio = new ReservaService(null, null, null);

        // Act
        double resultado = servicio.calcularTotal("VIP", 0);

        // Assert
        assertEquals(0.00, resultado);
    }

    // CP-08
    @Test
    void totalBaseNegativoLanzaExcepcion() {
        // Arrange
        ReservaService servicio = new ReservaService(null, null, null);

        // Act + Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> servicio.calcularTotal("NORMAL", -1)
        );
    }

    // CP-09
    @Test
    void reservaDisponibleSeConfirmaCorrectamente() {
        // Arrange
        DisponibilidadClient disponibilidad = reserva -> true;
        ReservaRepository repository = reserva -> {};
        Notificador notificador = reserva -> {};

        ReservaService servicio =
                new ReservaService(disponibilidad, repository, notificador);

        Reserva reserva = new Reserva("R001", "NORMAL");

        // Act
        servicio.confirmar(reserva);

        // Assert
        assertEquals(EstadoReserva.CONFIRMADA, reserva.getEstado());
    }

    // CP-10
    @Test
    void reservaNoDisponibleLanzaExcepcion() {
        // Arrange
        DisponibilidadClient disponibilidad = reserva -> false;
        ReservaRepository repository = reserva -> {};
        Notificador notificador = reserva -> {};

        ReservaService servicio =
                new ReservaService(disponibilidad, repository, notificador);

        Reserva reserva = new Reserva("R002", "NORMAL");

        // Act + Assert
        assertThrows(
                IllegalStateException.class,
                () -> servicio.confirmar(reserva)
        );
    }

    // CP-11
    @Test
    void reservaNulaLanzaExcepcion() {
        // Arrange
        DisponibilidadClient disponibilidad = reserva -> true;
        ReservaRepository repository = reserva -> {};
        Notificador notificador = reserva -> {};

        ReservaService servicio =
                new ReservaService(disponibilidad, repository, notificador);

        // Act + Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> servicio.confirmar(null)
        );
    }

    // CP-12
    @Test
    void confirmarReservaGuardaYEnviaNotificacion() {
        // Arrange
        DisponibilidadClient disponibilidad =
                mock(DisponibilidadClient.class);

        ReservaRepository repository =
                mock(ReservaRepository.class);

        Notificador notificador =
                mock(Notificador.class);

        Reserva reserva = new Reserva("R003", "NORMAL");

        // Stub: controlamos la respuesta de la dependencia externa
        when(disponibilidad.estaDisponible(reserva))
                .thenReturn(true);

        ReservaService servicio =
                new ReservaService(
                        disponibilidad,
                        repository,
                        notificador
                );

        // Act
        servicio.confirmar(reserva);

        // Assert - Mock: verificamos las interacciones esperadas
        verify(repository).guardar(reserva);
        verify(notificador).enviarConfirmacion(reserva);
    }

    @Test
void reservaConIdVacioLanzaExcepcion() {
    // Arrange
    String idInvalido = "";

    // Act + Assert
    assertThrows(
            IllegalArgumentException.class,
            () -> new Reserva(idInvalido, "NORMAL")
    );
}
}