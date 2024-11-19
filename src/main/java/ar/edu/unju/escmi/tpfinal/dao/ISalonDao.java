package ar.edu.unju.escmi.tpfinal.dao;

import java.util.List;

import ar.edu.unju.escmi.tpfinal.entities.Salon;

public interface ISalonDao {
	Salon buscarSalonPorId(Long idSalon);
	void mostrarTodosLosSalones();
	void guardarSalones();
	List<Salon> obtenerTodosLosSalones();	
}
