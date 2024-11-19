package ar.edu.unju.escmi.tpfinal.config;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EmfSingleton {

	private static EmfSingleton miInstancia = new EmfSingleton();
	private static final String PERSISTENCE_UNIT_NAME = "TestPersistence";
	private EntityManagerFactory emf = null;
	
	private EmfSingleton() {
		
	}
	
	public static EmfSingleton getInstance() {
		return miInstancia;
	}
	
	public EntityManagerFactory getEmf() {
		if(this.emf == null) 
			this.emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
		return this.emf;
	}
}
