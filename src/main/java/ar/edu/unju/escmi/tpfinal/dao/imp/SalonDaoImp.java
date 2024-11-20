package ar.edu.unju.escmi.tpfinal.dao.imp;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import java.util.List;
import ar.edu.unju.escmi.tpfinal.config.EmfSingleton;
import ar.edu.unju.escmi.tpfinal.dao.ISalonDao;
import ar.edu.unju.escmi.tpfinal.entities.Salon;	
	
public class SalonDaoImp implements ISalonDao{
	
	private static EntityManager manager = EmfSingleton.getInstance().getEmf().createEntityManager();
	
	@Override
	public void mostrarTodosLosSalones() {
		TypedQuery<Salon>query = manager.createQuery("SELECT e FROM Salon e",Salon.class);
		List<Salon> salones = query.getResultList();
		for(Salon salones1 : salones) {
			salones1.mostrarDatos();
		}
	
	}

	@Override
	public Salon buscarSalonPorId(Long idSalon) {
			return manager.find(Salon.class, idSalon);
	}
	
	@Override
	public void guardarSalones() {
	    try {
	        manager.getTransaction().begin();

	        if (manager.find(Salon.class, 1L) == null) {
	            Salon salon1 = new Salon(1L, "Cosmos", 60, 5000.0, false);
	            Salon salon2 = new Salon(2L, "Esmeralda", 20, 3000.0, false);
	            Salon salon3 = new Salon(3L, "Galaxy", 100, 8000.0, true);
	            manager.merge(salon1);
	            manager.merge(salon2);
	            manager.merge(salon3);
	        }

	        manager.getTransaction().commit();
	    } catch (Exception e) {
	    	if (manager.getTransaction().isActive()) {
	            manager.getTransaction().rollback();
	        }
	        System.out.println("Ocurrió un error al guardar los salones: " + e.getMessage());
	    }
	}

	    
        @Override      
	    public List<Salon> obtenerTodosLosSalones() {
		    TypedQuery<Salon> query = manager.createQuery("SELECT e FROM Salon e", Salon.class);
		    return query.getResultList(); 
		}
}