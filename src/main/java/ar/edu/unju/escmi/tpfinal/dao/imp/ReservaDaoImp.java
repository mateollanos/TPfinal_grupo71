package ar.edu.unju.escmi.tpfinal.dao.imp;

import ar.edu.unju.escmi.tpfinal.config.EmfSingleton;
import ar.edu.unju.escmi.tpfinal.dao.IReservaDao;
import ar.edu.unju.escmi.tpfinal.entities.Reserva;
import ar.edu.unju.escmi.tpfinal.entities.Salon;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class ReservaDaoImp implements IReservaDao {
   private static EntityManager manager = EmfSingleton.getInstance().getEmf().createEntityManager();
   @Override
   public void guardarReserva(Reserva reserva) {
       try {
           manager.getTransaction().begin();
           manager.persist(reserva);
           manager.getTransaction().commit();
       } catch (Exception e) {
    	   if (manager.getTransaction().isActive()) {
	            manager.getTransaction().rollback();
	        }
           throw e;
       }
   }
  
   @Override
   public void modificarReserva(Reserva reserva) {
       try {
           manager.getTransaction().begin();
           manager.merge(reserva);
           manager.getTransaction().commit();
       } catch (Exception e) {
    	   if (manager.getTransaction().isActive()) {
	            manager.getTransaction().rollback();
	        }
           throw e;
       }
   }
   @Override
   public void eliminarReservaLogicamente(Long id) {
       try {
           manager.getTransaction().begin();
           Reserva reserva = manager.find(Reserva.class, id);
           if (reserva != null) {
               reserva.setEstado(false);
               manager.merge(reserva);
           }
           manager.getTransaction().commit();
       } catch (Exception e) {
           manager.getTransaction().rollback();
           throw e;
       }
   }
   @Override
   public Reserva buscarReservaPorId(Long id) {
       return manager.find(Reserva.class, id);
   }
  
   @Override
   public List<Reserva> obtenerTodasLasReservas() {
       TypedQuery<Reserva> query = manager.createQuery("SELECT r FROM Reserva r WHERE r.estado = true", Reserva.class);
       return query.getResultList();
   }
   @Override
   public void mostrarTodasLasReservas() {
       List<Reserva> reservas = obtenerTodasLasReservas();
       if (reservas.isEmpty()) {
           System.out.println("No hay reservas activas.");
       } else {
           System.out.println("Lista de Reservas Activas:");
           for (Reserva reserva : reservas) {
               System.out.println("Reserva ID: " + reserva.getId());
               System.out.println("Cliente: " + reserva.getCliente().getNombre() + " " + reserva.getCliente().getApellido());
               System.out.println("Salón: " + reserva.getSalon().getNombre());
               System.out.println("Fecha: " + reserva.getFecha());
               System.out.println("Hora inicio: " + reserva.getHoraInicio());
               System.out.println("Hora fin: " + reserva.getHoraFin());
               System.out.println("Monto pagado: $" + reserva.getMontoPagado());
               System.out.println("Estado pago: " + (reserva.isCancelado() ? "CANCELADO" : "PAGO PENDIENTE"));
               System.out.println("Servicios adicionales:");
               if (reserva.getServiciosAdicionales() != null && !reserva.getServiciosAdicionales().isEmpty()) {
                   reserva.getServiciosAdicionales().forEach(s -> System.out.println("- " + s.getDescripcion()));
               } else {
                   System.out.println("- No hay servicios adicionales.");
               }
               System.out.println("-------------------------");
           }
       }
   }
   @Override
   public List<Reserva> buscarReservasConPagoPendiente() {
       TypedQuery<Reserva> query = manager.createQuery("SELECT r FROM Reserva r WHERE r.cancelado = false AND r.estado = true", Reserva.class);
       return query.getResultList();
   }
   @Override
   public List<Reserva> buscarReservasPorFecha(LocalDate fecha) {
       TypedQuery<Reserva> query = manager.createQuery("SELECT r FROM Reserva r WHERE r.fecha = :fecha AND r.estado = true", Reserva.class);
       query.setParameter("fecha", fecha);
       return query.getResultList();
   }
   @Override
   public List<Reserva> buscarReservasPorCliente(Long clienteId) {
       TypedQuery<Reserva> query = manager.createQuery("SELECT r FROM Reserva r WHERE r.cliente.id = :clienteId AND r.estado = true", Reserva.class);
       query.setParameter("clienteId", clienteId);
       return query.getResultList();
   }
   @Override
   public boolean existeReservaEnFechaYHorario(Long salonId, LocalDate fecha, String horaInicio, String horaFin) {
       TypedQuery<Long> query = manager.createQuery(
           "SELECT COUNT(r) FROM Reserva r WHERE r.salon.id = :salonId " +
           "AND r.fecha = :fecha " +
           "AND ((r.horaInicio <= :horaFin AND r.horaFin >= :horaInicio)) " +
           "AND r.estado = true", Long.class);
       query.setParameter("salonId", salonId);
       query.setParameter("fecha", fecha);
       query.setParameter("horaInicio", horaInicio);
       query.setParameter("horaFin", horaFin);
       return query.getSingleResult() > 0;
   }
  
   public boolean esSalonDisponible(Salon salon, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin) {
	    TypedQuery<Reserva> query = manager.createQuery(
	        "SELECT r FROM Reserva r WHERE r.salon = :salon AND r.fecha = :fecha AND r.estado = true", Reserva.class);
	    query.setParameter("salon", salon);
	    query.setParameter("fecha", fecha);

	    List<Reserva> reservas = query.getResultList();
	    for (Reserva reserva : reservas) {
	        if ((horaInicio.isBefore(reserva.getHoraFin()) && horaFin.isAfter(reserva.getHoraInicio()))) {
	            return false; 
	        }
	    }
	    return true;
	}
}
