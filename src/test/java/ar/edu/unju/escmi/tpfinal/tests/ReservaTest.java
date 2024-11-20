package ar.edu.unju.escmi.tpfinal.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ar.edu.unju.escmi.tpfinal.entities.*;

class ReservaTest {

    private static EntityManagerFactory emf;
    private Reserva reserva;
    private Cliente cliente;
    private Salon salon;
    private List<ServicioAdicional> servicios;

    @BeforeAll
    static void initAll() {
        emf = Persistence.createEntityManagerFactory("TestPersistence");
    }

    @AfterAll
    static void closeAll() {
        emf.close();
    }

    @BeforeEach
    void setUp() {
        cliente = new Cliente("Juan", "Perez", "Av. Test 123", 388412345, 12345678);
        
        salon = new Salon(1L, "Cosmos", 60, 5000.0, false);
        servicios = new ArrayList<>();
        servicios.add(new ServicioAdicional(1L, "Cámara 360", 20000.0, true));

        reserva = new Reserva();
        reserva.setId(1L);
        reserva.setCliente(cliente);
        reserva.setSalon(salon);
        reserva.setFecha(LocalDate.now());
        reserva.setHoraInicio(LocalTime.of(14, 0));
        reserva.setHoraFin(LocalTime.of(18, 0));
        reserva.setServiciosAdicionales(servicios);
        reserva.setPagoAdelantado(50000.0);
        reserva.setMontoPagado(50000.0);
        reserva.setCancelado(false);
        reserva.setEstado(true);
    }

    @AfterEach
    void tearDown() {
        reserva = null;
        cliente = null;
        salon = null;
        servicios.clear();
    }

    @Test
    void testCalcularCostoHorarioExtendido() {
        reserva.setHoraFin(LocalTime.of(20, 0));
        double costoExtra = reserva.calcularCostoHorarioExtendido();
        assertEquals(20000.0, costoExtra, "El costo de 2 horas extras debe ser $20000");
    }

    @Test
    void testCalcularMontoTotal() {
        double montoTotal = reserva.calcularMontoTotal();
        assertEquals(25000.0, montoTotal, "El monto total debe ser $25000");
    }

    @Test
    void testCalcularPagoPendiente() {
        reserva.setMontoPagado(10000.0);
        double pagoPendiente = reserva.calcularPagoPendiente();
        assertEquals(110000.0, pagoPendiente, "El pago pendiente debe ser $110000 después del pago adelantado");
    }

    @Test
    void testReservaValida() {
        assertNotNull(reserva.getCliente(), "El cliente no debe ser null");
        assertNotNull(reserva.getSalon(), "El salón no debe ser null");
        assertTrue(reserva.getServiciosAdicionales().size() > 0, "Debe tener servicios adicionales");
    }

    @Test
    void testEstadoReserva() {
        reserva.setMontoPagado(120000.0);
        reserva.setCancelado(reserva.calcularPagoPendiente() <= 0);
        assertTrue(reserva.isCancelado(), "La reserva debe estar cancelada al pagar el total");

        reserva.setMontoPagado(50000.0);
        reserva.setCancelado(reserva.calcularPagoPendiente() <= 0);
        assertFalse(reserva.isCancelado(), "La reserva no debe estar cancelada con pago parcial");
    }
    @Test
    void testHorarioValido() {
        LocalTime horaInicio = reserva.getHoraInicio();
        LocalTime horaFin = reserva.getHoraFin();

        assertTrue(horaInicio.isAfter(LocalTime.of(9, 59)) &&
                   horaInicio.isBefore(LocalTime.of(23, 1)),
                   "La hora de inicio debe estar entre 10:00 y 23:00");

        assertTrue(horaFin.isAfter(horaInicio),
                   "La hora de fin debe ser posterior a la hora de inicio");
    }

    @Test
    void testServiciosAdicionales() {
        ServicioAdicional servicio = servicios.get(0);
        assertSame(servicio, reserva.getServiciosAdicionales().get(0),
                   "Debe ser la misma instancia del servicio adicional");
    }

    @Test
    void testValidarClienteConDatosCorrectos() {
        assertNotNull(cliente.getNombre(), "El nombre del cliente no debe ser null");
        assertTrue(cliente.getTelefono() > 0, "El teléfono del cliente debe ser mayor que 0");
    }

    @Test
    void testServiciosAdicionalesOpcionales() {
        ServicioAdicional servicio = new ServicioAdicional(2L, "Proyector", 5000.0, false);
        reserva.getServiciosAdicionales().add(servicio);
        assertTrue(reserva.getServiciosAdicionales().contains(servicio), "El servicio adicional opcional debería ser agregado");
    }

    @Test
    void testCancelarReservaConPagoCompleto() {
        reserva.setMontoPagado(reserva.calcularMontoTotal());
        reserva.setCancelado(true);
        assertTrue(reserva.isCancelado(), "La reserva debería ser cancelada cuando el monto pagado es igual al total");
    }

    @Test
    void testReservaHorarioIncorrecto() {
        try {
            reserva.setHoraInicio(LocalTime.of(8, 0));
            fail("Se esperaba una excepción por hora de inicio incorrecta");
        } catch (IllegalArgumentException e) {
            assertEquals("El horario de inicio no puede ser antes de las 10:00", e.getMessage());
        }
    }


}
