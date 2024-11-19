package ar.edu.unju.escmi.tpfinal.entities;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "reservas")
public class Reserva {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "cliente_id", nullable = false)
	private Cliente cliente;

	@ManyToOne
	@JoinColumn(name = "salon_id", nullable = false)
	private Salon salon;

	@Column(nullable = false)
	private LocalDate fecha;

	@Column(nullable = false)
	private LocalTime horaInicio;

	@Column(nullable = false)
	private LocalTime horaFin;

	@Column(nullable = true) // Si el monto puede ser nulo, se establece 'nullable = true'.
	private double montoPagado;

	@ManyToMany
	@JoinTable(
	    name = "reserva_servicios",
	    joinColumns = @JoinColumn(name = "reserva_id"),
	    inverseJoinColumns = @JoinColumn(name = "servicio_id")
	)
	private List<ServicioAdicional> serviciosAdicionales;

	@Column(nullable = true) // Si el pago adelantado puede ser nulo, se establece 'nullable = true'.
	private double pagoAdelantado;

	@Column(nullable = false) // Se mantiene como 'false' para evitar nulos, ya que un 'boolean' no puede ser null.
	private boolean cancelado;

	@Column(nullable = false) // Se mantiene como 'false' para evitar nulos, ya que un 'boolean' no puede ser null.

	private boolean estado;

	public Reserva() {
		this.estado = true;
	    this.cancelado = false;
	    this.montoPagado = 0.0;
	    this.pagoAdelantado = 0.0;
	}
	
	public Reserva(Cliente cliente, Salon salon, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin,
            List<ServicioAdicional> serviciosAdicionales, double pagoAdelantado, double montoPagado) {
		this.cliente = cliente;
		this.salon = salon;
		this.fecha = fecha;
		this.horaInicio = horaInicio;
		this.horaFin = horaFin;
		this.serviciosAdicionales = serviciosAdicionales;
		this.pagoAdelantado = pagoAdelantado;
		this.montoPagado = montoPagado;
		this.estado = true; 
		this.cancelado = false; 
	}



	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Salon getSalon() {
		return salon;
	}

	public void setSalon(Salon salon) {
		this.salon = salon;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	public LocalTime getHoraInicio() {
		return horaInicio;
	}

	public void setHoraInicio(LocalTime horaInicio) {
	    if (horaInicio.isBefore(LocalTime.of(10, 0))) {
	        throw new IllegalArgumentException("El horario de inicio no puede ser antes de las 10:00");
	    }
	    this.horaInicio = horaInicio;
	}


	public LocalTime getHoraFin() {
		return horaFin;
	}

	public void setHoraFin(LocalTime horaFin) {
		this.horaFin = horaFin;
	}

	public double getMontoPagado() {
		return montoPagado;
	}

	public void setMontoPagado(double montoPagado) {
	    this.montoPagado = montoPagado;
	    this.cancelado = montoPagado >= calcularMontoTotal();
	}


	public List<ServicioAdicional> getServiciosAdicionales() {
		return serviciosAdicionales;
	}

	public void setServiciosAdicionales(List<ServicioAdicional> serviciosAdicionales) {
		this.serviciosAdicionales = serviciosAdicionales;
	}

	public double getPagoAdelantado() {
		return pagoAdelantado;
	}

	public void setPagoAdelantado(double pagoAdelantado) {
		this.pagoAdelantado = pagoAdelantado;
	}

	public boolean isCancelado() {
		return cancelado;
	}

	public void setCancelado(boolean cancelado) {
	    this.cancelado = true;
	}

	public boolean isEstado() {
		return estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
	}
	
	public double calcularCostoHorarioExtendido() {
	    LocalTime horaBase = horaInicio.plusHours(4);
	    if (horaFin.isAfter(horaBase)) {
	        long horasExtras = java.time.Duration.between(horaBase, horaFin).toHours();
	        return horasExtras * 10000.0;
	    }
	    return 0.0;
	}

	public double calcularMontoTotal() {
	    double montoTotal = salon.getPrecio();
	    montoTotal += calcularCostoHorarioExtendido();

	    if (serviciosAdicionales != null) {
	        montoTotal += serviciosAdicionales.stream().mapToDouble(ServicioAdicional::getPrecio).sum();
	    }

	    return montoTotal;
	}


	public void mostrarDatos() {
	     System.out.println("Reserva ID: " + id);
	     System.out.println("Cliente: " + cliente.getNombre() + " " + cliente.getApellido());
	     System.out.println("Salón: " + salon.getNombre());
	     System.out.println("Fecha: " + fecha);
	     System.out.println("Hora inicio: " + horaInicio);
	     System.out.println("Hora fin: " + horaFin);
	     System.out.println("Monto pagado: $" + montoPagado);
	     System.out.println("Estado pago: " + (cancelado ? "CANCELADO" : "PAGO PENDIENTE"));
	     System.out.println("Servicios adicionales:");
	     if (serviciosAdicionales != null) {
	         serviciosAdicionales.forEach(s -> System.out.println("- " + s.getDescripcion()));
	     }
		 System.out.println("-------------------------------------------");
	}

	public double calcularPagoPendiente() {
	    double montoTotal = calcularMontoTotal();
	    double montoPagado = getMontoPagado() + getPagoAdelantado(); 

	    if (montoPagado >= montoTotal) {
	        return 0;
	    }

	    return montoTotal - montoPagado;
	}
}