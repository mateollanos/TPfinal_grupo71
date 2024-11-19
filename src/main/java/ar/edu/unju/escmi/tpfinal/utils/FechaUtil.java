package ar.edu.unju.escmi.tpfinal.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class FechaUtil {

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static LocalDate convertirStringAFecha(String fechaStr) throws DateTimeParseException {
        return LocalDate.parse(fechaStr, FORMATO_FECHA);
    }

    public static String formatearFecha(LocalDate fecha) {
        return fecha.format(FORMATO_FECHA);
    }

    public static boolean esFechaValida(LocalDate fecha, LocalDate fechaMinima) {
        return !fecha.isBefore(fechaMinima);
    }
    
    public static void validarComponentesFecha(String fechaStr) {
        String[] partes = fechaStr.split("/");
        if (partes.length != 3) {
            throw new IllegalArgumentException("Formato de fecha incorrecto. Use dd/MM/yyyy.");
        }

        int dia = Integer.parseInt(partes[0]);
        int mes = Integer.parseInt(partes[1]);
        int anio = Integer.parseInt(partes[2]);

        if (dia < 1 || dia > 31) {
            throw new IllegalArgumentException("El día debe estar entre 1 y 31.");
        }
        if (mes < 1 || mes > 12) {
            throw new IllegalArgumentException("El mes debe estar entre 1 y 12.");
        }
    }
}
