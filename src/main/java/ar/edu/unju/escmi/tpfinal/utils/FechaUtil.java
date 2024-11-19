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
}
