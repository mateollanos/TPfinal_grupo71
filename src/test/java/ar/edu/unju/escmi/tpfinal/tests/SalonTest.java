package ar.edu.unju.escmi.tpfinal.tests;

import static org.junit.jupiter.api.Assertions.*;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import ar.edu.unju.escmi.tpfinal.dao.imp.SalonDaoImp;
import ar.edu.unju.escmi.tpfinal.entities.Salon;


import java.util.List;

class SalonTest {
	    private static EntityManagerFactory emf;
	    private Salon salonCosmos;
	    private Salon salonGalaxy;
	    private Salon salonEsmeralda;
	    private SalonDaoImp salonDao;
	  
	    
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
	        
	        salonDao = new SalonDaoImp();

	        salonCosmos = new Salon(1L, "Cosmos", 60, 5000.0, false);
	        salonEsmeralda = new Salon(2L, "Esmeralda", 20, 3000.0, false);
	        salonGalaxy = new Salon(3L, "Galaxy", 100, 8000.0, true);

	       
	    }

	    @AfterEach
	    void tearDown() {
	       salonCosmos=null;
	       salonEsmeralda=null;
	       salonGalaxy=null;
	       salonDao=null;
	    }
	    

	    @Test
	    void testBuscarSalonPorId() {
	        Salon salonEncontrado = salonDao.buscarSalonPorId(1L);

	        assertNotNull(salonEncontrado, "Debe encontrar un salón con ID 1");
	        assertEquals("Cosmos", salonEncontrado.getNombre(), "El nombre debe ser Cosmos");
	    }

	    @Test
	    void testObtenerTodosLosSalones() {
	        List<Salon> salones = salonDao.obtenerTodosLosSalones();

	        assertNotNull(salones, "La lista de salones no debe ser nula");
	        assertEquals(3, salones.size(), "Deben existir 3 salones");
	    }

	    @Test
	    void testSiTieneONoPileta() {
	        assertTrue(salonGalaxy.conPileta(), "El salon tiene pileta.");
	        assertFalse(salonEsmeralda.conPileta(), "El salon no tiene pileta");
	    }

	    @Test
	    void testCreacionSalon() {
	        assertNotNull(salonEsmeralda, "El servicio no debe ser null");
	        assertEquals("Cosmos", salonCosmos.getNombre(), "La descripción debe coincidir");
	        assertEquals(8000.0, salonGalaxy.getPrecio(), "El precio debe coincidir");
	    }

	    @Test
	    void testComparacionPrecios() {
	        double[] precios = {
	            salonCosmos.getPrecio(),
	            salonEsmeralda.getPrecio(),
	            salonGalaxy.getPrecio()
	        };

	        double[] preciosEsperados = {5000.0, 3000.0, 8000.0};
	        assertArrayEquals(preciosEsperados, precios, "Los precios deben coincidir con los valores esperados");
	    }

	    @Test
	    void testSalonesDistintos() {
	        assertNotSame(salonGalaxy, salonEsmeralda, "Deben ser salones diferentes");
	        assertNotEquals(salonCosmos.getPrecio(), salonEsmeralda.getPrecio(), "Los precios deben ser diferentes");
	    }

	    @Test
	    void testCompararIdDeSalones() {
	        assertNotEquals(salonCosmos.getId(), salonEsmeralda.getId(), "Los IDs de Cosmos y Esmeralda deben ser diferentes");
	        assertNotEquals(salonGalaxy.getId(), salonEsmeralda.getId(), "Los IDs de Galaxy y Esmeralda deben ser diferentes");
	        assertNotEquals(salonGalaxy.getId(), salonCosmos.getId(), "Los IDs de Galaxy y Cosmos deben ser diferentes");
	    }
}