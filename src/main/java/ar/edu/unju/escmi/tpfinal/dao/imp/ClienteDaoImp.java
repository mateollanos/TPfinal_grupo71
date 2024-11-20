package ar.edu.unju.escmi.tpfinal.dao.imp;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import ar.edu.unju.escmi.tpfinal.config.EmfSingleton;
import ar.edu.unju.escmi.tpfinal.dao.IClienteDao;
import ar.edu.unju.escmi.tpfinal.entities.Cliente;

public class ClienteDaoImp implements IClienteDao{
	
	private static EntityManager manager = EmfSingleton.getInstance().getEmf().createEntityManager();
	


	@Override
	public void guardarCliente(Cliente cliente) {
		try {
			manager.getTransaction().begin();
			manager.merge(cliente);
			manager.getTransaction().commit();
		} catch (Exception e) {
			if (manager.getTransaction().isActive()) {
	            manager.getTransaction().rollback();
	        }
			System.out.println("No se pudo guardar el cliente.");
		}
	}
	
	@Override
	public void modificarCliente(Cliente cliente) {
		try {
			manager.getTransaction().begin();
			manager.merge(cliente);
			manager.getTransaction().commit();
		} catch (Exception e) {
			if (manager.getTransaction().isActive()) {
	            manager.getTransaction().rollback();
	        }
			System.out.println("No se pudo modificar los datos del cliente.");
		}		
	}
	@Override
	public void mostrarTodosLosClientes() {
	    TypedQuery<Cliente> query = manager.createQuery("SELECT e FROM Cliente e", Cliente.class);
	    List<Cliente> clientes = query.getResultList();
	    
	    for (Cliente cli : clientes) {
	        cli.mostrarClientes();
	    }
	    
	}
	@Override
	public Cliente buscarClientePorId(Long idCliente) {
		
			return manager.find(Cliente.class, idCliente);
	}
	
	@Override
	public Cliente buscarClientePorDni(int dni) {
	    try {
	        return manager.createQuery("SELECT c FROM Cliente c WHERE c.dni = :dni", Cliente.class)
	                      .setParameter("dni", dni)
	                      .getSingleResult();
	    } catch (Exception e) {
	        return null;
	    }
	}
	
	@Override
   public List<Cliente> obtenerTodasLosClientes() {
       TypedQuery<Cliente> query = manager.createQuery("SELECT r FROM Cliente r WHERE r.estado = true", Cliente.class);
       return query.getResultList();
   }
	
}
