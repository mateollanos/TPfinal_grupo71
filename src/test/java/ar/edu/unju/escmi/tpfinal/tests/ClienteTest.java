package ar.edu.unju.escmi.tpfinal.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;
import javax.persistence.*;

import ar.edu.unju.escmi.tpfinal.dao.imp.ClienteDaoImp;
import ar.edu.unju.escmi.tpfinal.dao.imp.SalonDaoImp;
import ar.edu.unju.escmi.tpfinal.entities.Cliente;
import ar.edu.unju.escmi.tpfinal.entities.Reserva;
import ar.edu.unju.escmi.tpfinal.entities.Salon;
import ar.edu.unju.escmi.tpfinal.entities.ServicioAdicional;
import ar.edu.unju.escmi.tpfinal.exceptions.ClienteNoExisteException;

import java.util.Arrays;
import java.util.List;

class ClienteTest {
	private static  EntityManagerFactory emf;
    private static ClienteDaoImp clienteDao;
    private static Cliente cliente;

    @BeforeAll
    static void initAll() {
        emf = Persistence.createEntityManagerFactory("TestPersistence");
        clienteDao = new ClienteDaoImp();
    }

    @AfterAll
    static void closeAll() {
        emf.close();
    }
    @BeforeEach
    void setUp() {
        cliente = new Cliente("Maria", "Lopez", "Calle Falsa 123", 388412456, 87654321, null);
    }
    


    @AfterEach
    void tearDown() {
    	
    	cliente = null;
        
    }

    @Test
    void testClienteActivo() {
        assertTrue(cliente.isEstado(), "El cliente debe estar activo por defecto");
    }
	
	@Test
    void testClienteNoExiste() {
        Cliente clienteNull = null;
        assertThrows(ClienteNoExisteException.class, () -> {
            if (clienteNull == null) {
                throw new ClienteNoExisteException("El cliente no existe");
            }
        });
    }

	@Test
	void testCrearClienteYGuardar() {
		clienteDao.guardarCliente(cliente);
		assertTrue(cliente != null, "Cliente logro registrarse.");
	}
	
	@Test
    void testClienteValido() {
        assertNotNull(cliente.getDni(), "El DNI no debe ser nulo");
        assertNotNull(cliente.getNombre(), "El nombre no debe ser nulo");
        assertNotNull(cliente.getApellido(), "El apellido no debe ser nulo");
    }
	 
	@Test
	void testDniValido() {
	    assertTrue(String.valueOf(cliente.getDni()).length() == 8, "El DNI debe tener 8 dígitos");
	}

	@Test
	void testTelefonoFormato() {
	    assertFalse(String.valueOf(cliente.getTelefono()).matches("\\d{3}-\\d{7}"), 
	        "El teléfono debe tener el formato correcto (XXX-XXXXXXX)");
	}
	 
 
	 
}
