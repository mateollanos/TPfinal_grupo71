package ar.edu.unju.escmi.tpfinal.dao;

import java.util.List;

import ar.edu.unju.escmi.tpfinal.entities.Cliente;

public interface IClienteDao {
	void guardarCliente(Cliente cliente);
	void modificarCliente(Cliente cliente);
	Cliente buscarClientePorId(Long idCliente);
	void mostrarTodosLosClientes();
	List<Cliente> obtenerTodasLosClientes();
	Cliente buscarClientePorDni(int dni);
}