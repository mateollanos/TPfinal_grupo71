package ar.edu.unju.escmi.tpfinal.main;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import ar.edu.unju.escmi.tpfinal.dao.imp.*;
import ar.edu.unju.escmi.tpfinal.entities.*;
import ar.edu.unju.escmi.tpfinal.exceptions.ClienteNoExisteException;
import ar.edu.unju.escmi.tpfinal.exceptions.SalonNoDisponibleException;
import ar.edu.unju.escmi.tpfinal.utils.*;

public class Main {
	private static ClienteDaoImp ClienteDao = new ClienteDaoImp();
	private static SalonDaoImp SalonDao = new SalonDaoImp();
	private static ReservaDaoImp ReservaDao = new ReservaDaoImp();
	private static ServicioAdicionalDaoImp ServiciosAdicionalesDao = new ServicioAdicionalDaoImp();
	
	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);

		SalonDao.guardarSalones(); 
		ServiciosAdicionalesDao.guardarServicio();
		
		boolean band=true;
		
		do {
			System.out.println(" ========== GESTION DE RESERVAS ==========");
			System.out.println("1. Alta de cliente");
			System.out.println("2. Consultar clientes");
			System.out.println("3. Modificar clientes");
			System.out.println("4. Realizar pago");
			System.out.println("5. Realizar reserva");
			System.out.println("6. Consultar todas las reservas");
			System.out.println("7. Consultar una reserva");
			System.out.println("8. Consultar salones");
			System.out.println("9. Consultar servicios adicionales");
			System.out.println("10. Salir");
			System.out.println(" ========================================");
			System.out.println("Ingrese una opcion: ");
			int op = sc.nextInt();
			sc.nextLine();
			switch(op) {
			case 1:
				altaCliente(sc);
			break;
			
			case 2:
				consultaClientes(sc);
			break;
			
			case 3:
				modificarCliente(sc);
			break;
			
			case 4:
				realizarPago1(sc);
			break;
			case 5:
				realizarReserva(sc);
			break;
			case 6:
				consultarTodasLasReservas(sc);
			break;
			case 7:
				 System.out.print("Ingrese el ID de la reserva: ");
                 Long numId = sc.nextLong();
                 consultarReserva(numId);
			break;
			
			case 8:
				consultaSalones(sc);
				
			break;
			
			case 9:
				consultaServiciosAdicionales(sc);
			break;
			
			case 10:
				System.out.println("\n=========== FIN DEL PROGRAMA ===========\n");
				band=false;
			break;
			
			default:
				System.out.println("\nOpcion no disponible\n");
			}
			
			
		} while (band);
		sc.close();
	}


	public static void altaCliente(Scanner sc) {
	    System.out.println("========== REGISTRO DE CLIENTES ==========");
	    
	    
	    String nombre = "";
        String apellido = "";
        String domicilio = "";
        boolean datoInvalido;
        do {
            datoInvalido = false;
            try {
                System.out.print("Ingrese Nombre: ");
                nombre = sc.nextLine().trim();
                if (nombre.isEmpty() || nombre.matches(".*\\d.*")) {
                    throw new IllegalArgumentException("El nombre no puede estar vacío ni contener números. Intente nuevamente.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                datoInvalido = true;
            }
        } while (datoInvalido);

        
        do {
            datoInvalido = false;
            try {
                System.out.print("Ingrese Apellido: ");
                apellido = sc.nextLine().trim();
                if (apellido.isEmpty() || apellido.matches(".*\\d.*")) {
                    throw new IllegalArgumentException("El apellido no puede estar vacío ni contener números. Intente nuevamente.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                datoInvalido = true;
            }
        } while (datoInvalido);

       
        do {
            datoInvalido = false;
            try {
                System.out.print("Ingrese Domicilio: ");
                domicilio = sc.nextLine().trim();
                if (domicilio.isEmpty()) {
                    throw new IllegalArgumentException("El domicilio no puede estar vacío. Intente nuevamente.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                datoInvalido = true;
            }
        } while (datoInvalido);
	   
	   
	    
	    int dni = 0;
	    do {
	        datoInvalido = false;
	        try {
	            System.out.print("Ingrese DNI: ");
	            dni = Integer.parseInt(sc.nextLine().trim());
	            
	            if (ClienteDao.buscarClientePorDni(dni) != null) {
	                System.out.println("El DNI ya fue registrado. Intente con otro.");
	                datoInvalido = true;
	            }
	            else if (dni <= 0) {
	                System.out.println("El DNI debe ser un número positivo. Intente nuevamente.");
	                datoInvalido = true;
	     
	            }
	        } catch (NumberFormatException e) {
	            System.out.println("Dato no válido. Por favor, ingrese un número entero para el DNI.");
	            datoInvalido = true;
	        }
	    } while (datoInvalido);
	    
	    int telefono = 0;
     
        
	    do {
            System.out.print("Ingrese Telefono: ");
            String entrada = sc.nextLine().trim(); 

            try {
                telefono = Integer.parseInt(entrada);
                
  
                int longitud = entrada.length();
                if (longitud < 7 || longitud > 9) {
                    System.out.println("Dato no válido. El teléfono debe contener entre 7 y 9 dígitos.");
                    datoInvalido = true;
                } else {
                    datoInvalido = false; 
                }
            } catch (Exception e) {
                System.out.println("Dato no válido. Por favor, ingrese un teléfono válido (solo números).");
                datoInvalido = true;
            }
        } while (datoInvalido);
	    
	    List<Reserva> reservas = null;
	    Cliente cliente = new Cliente(nombre, apellido, domicilio, telefono, dni, reservas);
	    ClienteDao.guardarCliente(cliente);
	    System.out.println("Cliente registrado exitosamente!");
	}
	    
	    public static void modificarCliente(Scanner sc) {
	        ClienteDao.mostrarTodosLosClientes();
	        
	        Cliente cliente = null;
	        Long idCliente = 0L;
	        boolean datoInvalido = true;
	        do {
	            try {
	                System.out.print("\nIngrese el ID del cliente a modificar: ");
	                idCliente = sc.nextLong();
	                
	                cliente = ClienteDao.buscarClientePorId(idCliente);
	                
	                if (cliente == null) {
	                    System.out.println("Cliente no encontrado. Intente nuevamente");
	                } else datoInvalido = false;
	            } catch (Exception e) {
	                System.out.println("\nDato no valido, ingrese nuevamente el ID");
	                sc.nextLine();
	            }
	        } while (datoInvalido);

	        sc.nextLine();
	        
	        
	        String nombre = "";
	        String apellido = "";
	        String domicilio = "";
	        int dni = 0;
	        int telefono = 0;
	        do {
	            datoInvalido = false;
	            try {
	                System.out.print("Ingrese nuevo Nombre: ");
	                nombre = sc.nextLine().trim();
	                if (nombre.isEmpty() || nombre.matches(".*\\d.*")) {
	                    throw new IllegalArgumentException("El nombre no puede estar vacío ni contener números. Intente nuevamente.");
	                }
	                
	                cliente.setNombre(nombre);
	                
	            } catch (IllegalArgumentException e) {
	                System.out.println(e.getMessage());
	                datoInvalido = true;
	            }
	        } while (datoInvalido);

	        
	        do {
	            datoInvalido = false;
	            try {
	                System.out.print("Ingrese nuevo Apellido: ");
	                apellido = sc.nextLine().trim();
	                if (apellido.isEmpty() || apellido.matches(".*\\d.*")) {
	                    throw new IllegalArgumentException("El apellido no puede estar vacío ni contener números. Intente nuevamente.");
	                }
	                
	                cliente.setApellido(apellido);
	                
	            } catch (IllegalArgumentException e) {
	                System.out.println(e.getMessage());
	                datoInvalido = true;
	            }
	        } while (datoInvalido);

	       
	        do {
	            datoInvalido = false;
	            try {
	                System.out.print("Ingrese nuevo Domicilio: ");
	                domicilio = sc.nextLine().trim();
	                if (domicilio.isEmpty()) {
	                    throw new IllegalArgumentException("El domicilio no puede estar vacío. Intente nuevamente.");
	                }
	                cliente.setDomicilio(domicilio);
	            } catch (IllegalArgumentException e) {
	                System.out.println(e.getMessage());
	                datoInvalido = true;
	            }
	        } while (datoInvalido);
		   
	    
	        do {
	            datoInvalido = false;
	            try {
	                System.out.print("Ingrese DNI: ");
	                dni = Integer.parseInt(sc.nextLine().trim());
	                
	                if (dni <= 0) {
	                    System.out.println("El DNI debe ser un número positivo. Intente nuevamente.");
	                    datoInvalido = true;
	                } else if (ClienteDao.buscarClientePorDni(dni) != null) {
	                    System.out.println("El DNI ya fue registrado. Intente con otro.");
	                    datoInvalido = true;
	                } else {
	                    cliente.setDni(dni);
	                }
	            } catch (NumberFormatException e) {
	                System.out.println("Dato no válido. Por favor, ingrese un número entero para el DNI.");
	                datoInvalido = true;
	            }
	        } while (datoInvalido);

	        do {
	            datoInvalido = false;
	            System.out.print("Ingrese Telefono: ");
	            String entrada = sc.nextLine().trim();

	            try {
	                telefono = Integer.parseInt(entrada);

	                if (entrada.length() < 7 || entrada.length() > 9) {
	                    System.out.println("Dato no válido. El teléfono debe contener entre 7 y 9 dígitos.");
	                    datoInvalido = true;
	                } else {
	                    cliente.setTelefono(telefono);
	                }
	            } catch (NumberFormatException e) {
	                System.out.println("Dato no válido. Por favor, ingrese un teléfono válido (solo números).");
	                datoInvalido = true;
	            }
	        } while (datoInvalido);

	        ClienteDao.modificarCliente(cliente);
	        System.out.println("\nDatos del cliente actualizados.\n");
	        
	        List<Reserva> reservas = cliente.getReserva();
	        if (reservas != null) { 
	            for (Reserva reser : reservas) {
	                reser.setCliente(cliente);
	                ReservaDao.modificarReserva(reser);
	            }
	        } else {
	            System.out.println("El cliente no tiene reservas asociadas.");
	        }
	    }

	    public static void consultaClientes(Scanner sc) {
	        System.out.println("========== CONSULTA CLIENTES ==========");
	        try {
	            List<Cliente> clientes = ClienteDao.obtenerTodasLosClientes(); 

	            if (clientes.isEmpty()) {
	                System.out.println("No hay ningún cliente registrado!");
	            } else {
	                System.out.println("Lista de Clientes:");
	                ClienteDao.mostrarTodosLosClientes();
	                
	            }
	        } catch (Exception e) {
	            System.out.println("Error al mostrar los Clientes");
	            System.out.println(e.getMessage());
	        }
	    }

	    public static void consultaSalones(Scanner sc) {
	        System.out.println("========== CONSULTA SALONES ==========");
	        SalonDao.guardarSalones();
	        try {
	            System.out.println("Lista de Salones:");
	            SalonDao.mostrarTodosLosSalones();
	        } catch (Exception e) {
	            System.out.println("Error al mostrar los Salones");
	            System.out.println(e.getMessage());
	        }
	    }

	    public static void consultarReserva(Long numId) {
	        Reserva reser = ReservaDao.buscarReservaPorId(numId);
	        System.out.println("-------------------------------------------");
	        if (reser != null) { 
	            reser.mostrarDatos();
	        } else {
	            System.out.println("Reserva no encontrada.");
	        }
	    }

	 
	    public static void consultarTodasLasReservas(Scanner sc) {
	        System.out.println("========== CONSULTA TODAS LAS RESERVAS ==========");
	        try {
	        	List<Reserva> reservas = ReservaDao.obtenerTodasLasReservas();
	        	if(reservas.isEmpty()) {
	        	System.out.println("No hay ninguna reserva realizada!");
	        	}else {
	            ReservaDao.mostrarTodasLasReservas();
	        	}
	        } catch (Exception e) {
	            System.out.println("Error al mostrar las Reservas");
	            System.out.println(e.getMessage());
	        }
	    }

	    public static void consultaServiciosAdicionales(Scanner sc) {
	        System.out.println("========== CONSULTA SERVICIOS ADICIONALES ==========");
	        ServiciosAdicionalesDao.guardarServicio();
	        try {
	            System.out.println("Lista de Servicios Adicionales: ");
	            ServiciosAdicionalesDao.mostrarServiciosAdicionales();
	        } catch (Exception e) {
	            System.out.println("Error al mostrar los Servicios Adicionales");
	            System.out.println(e.getMessage());
	        }
	    }
	    public static void realizarPago1(Scanner sc) {
	        System.out.println("========== REALIZAR PAGO ==========");

	        List<Reserva> reservasPendientes = ReservaDao.buscarReservasConPagoPendiente();
	        if (reservasPendientes.isEmpty()) {
	            System.out.println("No hay reservas con pago pendiente.");
	            return;
	        }

	        System.out.println("Reservas con pago pendiente:");
	        for (Reserva reserva : reservasPendientes) {
	            double montoPendiente = reserva.calcularPagoPendiente();
	            if (montoPendiente > 0) {
	                System.out.println("ID: " + reserva.getId() + ", Cliente: " + reserva.getCliente().getNombre() + " " + reserva.getCliente().getApellido() + ", Monto pendiente: $" + montoPendiente);
	            }
	        }

	        long reservaId = -1;
	        boolean entradaValida = false;

	        while (!entradaValida) {
	            System.out.print("Ingrese el ID de la reserva para realizar el pago: ");
	            try {
	                reservaId = sc.nextLong(); 
	                entradaValida = true; 
	            } catch (InputMismatchException e) {
	                System.out.println("Error: Debe ingresar un número válido. Intente nuevamente.");
	                sc.next(); 
	            }
	        }

	        Reserva reserva = ReservaDao.buscarReservaPorId(reservaId);
	        if (reserva == null || reserva.isCancelado()) {
	            System.out.println("La reserva no existe o ya está cancelada.");
	            return;
	        }

	        double montoPendiente = reserva.calcularPagoPendiente();
	        if (montoPendiente <= 0) {
	            System.out.println("La reserva ya está completamente pagada.");
	            return;
	        }

	        double montoAbono = 0.0;
	        boolean entradaValida2 = false;

	        while (!entradaValida2) { 
	            try {
	                System.out.print("Ingrese el monto a abonar: $");
	                montoAbono = sc.nextDouble();
	                sc.nextLine(); 
	                if (montoAbono <= 0) {
	                    System.out.println("Error: El monto debe ser mayor a 0. Intente nuevamente.\n");
	                } else {
	                    entradaValida2 = true; 
	                }
	            } catch (InputMismatchException e) {
	                System.out.println("Error: Debe ingresar un valor numérico válido. Intente nuevamente.\n");
	                sc.nextLine(); 
	            }
	        }
	        
	        if (montoAbono > montoPendiente) {
	            double exceso = montoAbono - montoPendiente;
	            System.out.println("Excedente: $" + exceso);
	            System.out.println("Se le devolverá un excedente de $" + exceso);
	            montoAbono = montoPendiente; 
	        }

	        
	        reserva.setMontoPagado(reserva.getMontoPagado() + montoAbono);

	       
	        if (reserva.calcularPagoPendiente() <= 0) {
	            reserva.setCancelado(true);
	            System.out.println("Reserva cancelada con éxito.");
	        }

	      
	        ReservaDao.modificarReserva(reserva);
	        System.out.println("Pago realizado exitosamente. Monto abonado: $" + montoAbono);
	    }
	

	    public static void realizarReserva(Scanner sc) {
	    	System.out.println("========== REALIZAR RESERVA ==========");
              ClienteDao.mostrarTodosLosClientes();

	        Long idCliente = -1L;
	        boolean entradaValida = false;

	        while (!entradaValida) {
	            System.out.print("Ingrese el ID del cliente: ");
	            try {
	                idCliente = sc.nextLong();
	                sc.nextLine();
	                entradaValida = true;
	            } catch (InputMismatchException e) {
	                System.out.println("Error: Debe ingresar un número válido. Intente nuevamente.");
	                sc.next();
	            }
	        }

	        boolean respuestaValida = false;
	        String respuesta = null;
	        Cliente cliente;
	        try {
	            cliente = ClienteDao.buscarClientePorId(idCliente);
	            if (cliente == null) {
	                throw new ClienteNoExisteException("El cliente con ID " + idCliente + " no está registrado.");
	            }
	        } catch (ClienteNoExisteException e) {
	            System.out.println(e.getMessage());

	            while (!respuestaValida) {
	                System.out.print("¿Desea registrarlo? (s/n): ");
	                respuesta = sc.nextLine().trim();

	                if (respuesta.equalsIgnoreCase("s") || respuesta.equalsIgnoreCase("n")) {
	                    respuestaValida = true;
	                } else {
	                    System.out.println("Respuesta inválida. Por favor ingrese 's' para sí o 'n' para no.");
	                }
	            }

	            if (respuesta.equalsIgnoreCase("s")) {
	                altaCliente(sc);
	                cliente = ClienteDao.buscarClientePorId(idCliente);
	                if (cliente == null) {
	                    System.out.println("No se pudo registrar al cliente. Intente nuevamente.");
	                    return;
	                }
	            } else {
	                System.out.println("No se puede continuar sin registrar al cliente.");
	                return;
	            }
	        }

	        LocalDate fecha = null;
	        LocalDate fechaMinima = LocalDate.of(2024, 11, 19);
	        boolean fechaValida = false;

	        while (!fechaValida) {
	            System.out.print("Ingrese la fecha de la reserva (dd/MM/yyyy): ");
	            String fechaStr = sc.nextLine();

	            try {
	            	
	            	 FechaUtil.validarComponentesFecha(fechaStr);
	               
	                fecha = FechaUtil.convertirStringAFecha(fechaStr);
	               
	                if (!FechaUtil.esFechaValida(fecha, fechaMinima)) {
	                    System.out.println("No se aceptan reservas para fechas anteriores al " + FechaUtil.formatearFecha(fechaMinima) + ".");
	                    continue;
	                }

	                fechaValida = true;
	                System.out.println("Fecha ingresada correctamente: " + FechaUtil.formatearFecha(fecha));

	            } catch (DateTimeParseException e) {
	                System.out.println("Formato de fecha incorrecto. Use dd/MM/yyyy.");
	            } catch (IllegalArgumentException e) {
	                System.out.println(e.getMessage());
	            }
	        }


	        String horaInicioStr;
	        LocalTime horaInicio = null;
	        boolean horaInicioValida = false;

	        while (!horaInicioValida) {
	            System.out.print("Ingrese la hora de inicio (HH:mm, entre 10:00 y 19:00): ");
	            horaInicioStr = sc.nextLine();

	            try {
	                horaInicio = LocalTime.parse(horaInicioStr);
	                if (horaInicio.isBefore(LocalTime.of(10, 0)) || horaInicio.isAfter(LocalTime.of(19, 0))) {
	                    System.out.println("La hora de inicio debe estar entre las 10:00 y las 19:00.");
	                } else {
	                    horaInicioValida = true;
	                }
	            } catch (DateTimeParseException e) {
	                System.out.println("Formato de hora inválido. Por favor ingrese una hora válida (HH:mm).");
	            }
	        }

	        LocalTime horaFin = horaInicio.plusHours(4);
	        if (horaFin.isAfter(LocalTime.of(23, 0))) {
	            horaFin = LocalTime.of(23, 0);
	        }

	        int horasAdicionales = 0;
	        double costoTotal = 0.0;

	        while (horaFin.isBefore(LocalTime.of(23, 0))) {
	            System.out.print("Su horario actual es de " + horaInicio + " a " + horaFin + ". ¿Desea añadir horas extra? (s/n): ");
	            String respuesta1 = sc.nextLine();

	            if (respuesta1.equalsIgnoreCase("s")) {
	                System.out.print("¿Cuántas horas extra desea añadir?: ");
	                try {
	                    int horasExtras = Integer.parseInt(sc.nextLine());
	                    if (horasExtras < 0) {
	                        System.out.println("No se pueden agregar horas negativas. Ingrese un número positivo.");
	                    } else {
	                        LocalTime limiteMaximo = LocalTime.of(23, 0);
	                        LocalTime nuevaHoraFin = horaFin.plusHours(horasExtras);

	                        if (nuevaHoraFin.isAfter(limiteMaximo) || nuevaHoraFin.isBefore(horaFin)) {
	                            long horasDisponibles = limiteMaximo.getHour() - horaFin.getHour();
	                            System.out.println("No es posible añadir tantas horas. Solo puede extenderse hasta las " + limiteMaximo +
	                                    ". Horas extras posibles: " + horasDisponibles + ".");
	                        } else {
	                            horaFin = nuevaHoraFin;
	                            horasAdicionales += horasExtras;
	                            costoTotal += horasExtras * 10000.0;
	                            System.out.println("Horario actualizado: el evento ahora finaliza a las " + horaFin + ".");
	                        }
	                    }
	                } catch (NumberFormatException e) {
	                    System.out.println("Entrada inválida. Por favor, ingrese un número válido de horas.");
	                }
	            } else if (respuesta1.equalsIgnoreCase("n")) {
	                break;
	            } else {
	                System.out.println("Respuesta inválida. Ingrese 's' para sí o 'n' para no.");
	            }
	        }

	        System.out.println("\nSalones disponibles para la fecha " + FechaUtil.formatearFecha(fecha) + " de " + horaInicio + " a " + horaFin + ":");
	        List<Salon> todosLosSalones = SalonDao.obtenerTodosLosSalones();
	        List<Salon> salonesDisponibles = new ArrayList<>();
	        for (Salon salon : todosLosSalones) {
	            if (ReservaDao.esSalonDisponible(salon, fecha, horaInicio, horaFin)) {
	                salonesDisponibles.add(salon);
	                System.out.printf("ID: %d | Nombre: %-10s | Precio: $%-10.2f | Con pileta: %-4s | Capacidad: %-4d\n",
	                        salon.getId(), salon.getNombre(), salon.getPrecio(), salon.conPileta() ? "Sí" : "No", salon.getCapacidad());
	            }
	        }

	        if (salonesDisponibles.isEmpty()) {
	            System.out.println("No hay salones disponibles en esta fecha y hora.");
	            return;
	        }

	       
	        Long salonId = null;
	        Salon salon = null;
	        boolean entradaValida1 = false;

	        while (!entradaValida1) {
	            System.out.print("\nIngrese el ID del salón (un número válido): ");

	            try {
	                // Verifica si la entrada es un número válido
	                if (!sc.hasNextLong()) {
	                    System.out.println("Error: Debe ingresar un número válido para el ID.");
	                    sc.next(); // Descartar entrada no válida
	                    continue;
	                }

	                salonId = sc.nextLong();
	                sc.nextLine(); 

	              
	                salon = SalonDao.buscarSalonPorId(salonId);

	                if (salon == null) {
	                    System.out.println("Error: El ID ingresado no corresponde a ningún salón. Intente nuevamente.");
	                } else if (!ReservaDao.esSalonDisponible(salon, fecha, horaInicio, horaFin)) {
	                        throw new SalonNoDisponibleException("El salón " + salonId + " no está disponible en la fecha y hora seleccionadas. Intente nuevamente.");
	                } else {
	                    entradaValida1 = true; 
	                }
	            } catch (SalonNoDisponibleException e) {
	                System.out.println(e.getMessage()); 
	            } catch (InputMismatchException e) {
	                System.out.println("Error: Debe ingresar un ID válido. Intente nuevamente.");
	                sc.next(); 
	            }
	        }

	        if (salon == null) {
	            System.out.println("Error crítico: No se encontró el salón seleccionado. Operación cancelada.");
	            return; 
	        }
	      
	        System.out.println("Ha seleccionado el salón: " + salon.getNombre());
	        double costoBase = salon.getPrecio();
	        costoTotal += costoBase; 
	        
	        
	        boolean respuestaValida2 =false;
	        
	        System.out.println("\nServicios adicionales disponibles:"); 
	        List<ServicioAdicional> serviciosAdicionales = ServiciosAdicionalesDao.obtenerTodosLosServicios();
	        List<ServicioAdicional> serviciosSeleccionados = new ArrayList<>();
	        for (ServicioAdicional servicio : serviciosAdicionales) {
	            System.out.printf("ID: %d | %s | Precio: $%-10.2f\n", servicio.getId(), servicio.getDescripcion(), servicio.getPrecio());

	            respuestaValida2 = false;

	            while (!respuestaValida2) {
	                System.out.print("¿Desea agregar este servicio? (s/n): ");
	                String respuesta1 = sc.nextLine();

	                if (respuesta1.equalsIgnoreCase("s") || respuesta1.equalsIgnoreCase("n")) {
	                    respuestaValida2 = true; 
	                    if (respuesta1.equalsIgnoreCase("s")) {
	                        serviciosSeleccionados.add(servicio);
	                        costoTotal += servicio.getPrecio();
	                    }
	                } else {
	                    System.out.println("Respuesta inválida. Por favor ingrese 's' para sí o 'n' para no.");
	                }
	            }
	        }
		    boolean respuestaValida3 = false;
		    double pagoAdelantado = 0.0;
		    
		    while (!respuestaValida3) {
		        System.out.print("\n¿Desea realizar un pago adelantado? (s/n): ");
		        String adelantoStr = sc.nextLine().trim();
		        if (adelantoStr.equalsIgnoreCase("s") || adelantoStr.equalsIgnoreCase("n")) {
		            respuestaValida3 = true;
		            	 if (adelantoStr.equalsIgnoreCase("s")) {
		            		System.out.print("Costo a pagar por el servicio: " + costoTotal );
				            System.out.println();
		     		        System.out.print("Ingrese el monto del pago adelantado: ");
		     		        while (true) {
		     		            try {
		     		                pagoAdelantado = sc.nextDouble();
		     		                sc.nextLine(); 
		     		                break;  
		     		            } catch (InputMismatchException e) {
		     		                System.out.println("Entrada inválida. Por favor, ingrese un monto válido.");
		     		                sc.nextLine();  
		     		            }
		     		        }
		            	 }
		           
		        } else {
		            System.out.println("Respuesta inválida. Por favor ingrese 's' para sí o 'n' para no.");
		        }
		    }
		            
		    if (pagoAdelantado > costoTotal) {
	            double exceso = pagoAdelantado - costoTotal;
	            System.out.println("Se le devolverá un excedente de $" + exceso);
	            pagoAdelantado = costoTotal; 
	          System.out.println("Pago realizado exitosamente. Monto abonado: $" + pagoAdelantado);
	    } else {
	        System.out.println("No se realizó un pago adelantado.");
	    }
		    

		    System.out.println("\n========== DETALLES DE LA RESERVA ==========");
		    System.out.println("Cliente: " + cliente.getNombre() + " " + cliente.getApellido());
		    System.out.println("Fecha de la reserva: " + FechaUtil.formatearFecha(fecha));
		    System.out.println("Horario: " + horaInicio + " - " + horaFin);
		    System.out.println("Horas extras: " + horasAdicionales);
		    System.out.println("Salón elegido: " + salon.getNombre());
		    System.out.println("Servicios adicionales: ");
		    if (serviciosSeleccionados.isEmpty()) {
		        System.out.println("Ninguno.");
		    } else {
		        for (ServicioAdicional servicio : serviciosSeleccionados) {
		            System.out.println(" - " + servicio.getDescripcion() + " ($" + servicio.getPrecio() + ")");
		        }
		    }
		    System.out.println("\nCosto base: $" + costoBase);
		    if (horasAdicionales != 0) {
		        System.out.println("Costo Horario Extra: $" + horasAdicionales * 10000);
		    } else {
		        System.out.println("Costo Horario extra: No solicitado");
		    }
		    System.out.println("Monto total (antes de pago adelantado): $" + costoTotal);
		    System.out.println("Pago adelantado: $" + pagoAdelantado);
		    System.out.println("Monto restante a pagar: $" + (costoTotal - pagoAdelantado));

		    
		    boolean respuestaValida4 = false;
		    while (!respuestaValida4) {  
		        try {
		            System.out.print("\n¿Desea confirmar la reserva? (s/n): ");
		            String confirmacion = sc.nextLine().trim(); 

		            if (confirmacion.equalsIgnoreCase("s")) {
		               
		                Reserva reserva = new Reserva(cliente, salon, fecha, horaInicio, horaFin, serviciosSeleccionados, pagoAdelantado, 0.0);

		                if ((costoTotal - pagoAdelantado) <= 0) {
		                    reserva.setCancelado(true); // 
		                    System.out.println("Reserva cancelada con éxito.");
		                }

		                
		                ReservaDao.guardarReserva(reserva);
		                System.out.println("Reserva confirmada con éxito.");
		                
		                respuestaValida4 = true;
		            } else if (confirmacion.equalsIgnoreCase("n")) {
		               
		                System.out.println("Reserva cancelada.");
		                
		                
		                respuestaValida4 = true;
		            } else {
		                
		                System.out.println("Error: Respuesta inválida. Debe ingresar 's' para sí o 'n' para no.");
		            }
		        } catch (Exception e) {
		           
		            System.out.println("Error inesperado: " + e.getMessage());
		        }
		    }
	    }
}

