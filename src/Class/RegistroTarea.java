package Class;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import Enum.tipoTarea;

public class RegistroTarea 
{
    //Establecer con qué formato quiero que salga la fecha. Se usa en el toString
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");


    private tipoTarea tipo;
    private LocalDate fecha;
    private String descipcion;
    private String responsable;
    private double costeEstimado;

    //Constructor
    public RegistroTarea(tipoTarea tipo, LocalDate fecha, String descipcion, String responsable, double costeEstimado) {
        this.tipo = tipo;
        this.fecha = fecha;
        this.descipcion = descipcion;
        this.responsable = responsable;
        this.costeEstimado = costeEstimado;
    }
    

    //Getters y Setters
    public tipoTarea getTipo() {
        return tipo;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public String getDescipcion() {
        return descipcion;
    }

    public String getResponsable() {
        return responsable;
    }

    public double getCosteEstimado() {
        return costeEstimado;
    }

    //Metodos
    @Override
    public String toString() 
    {
        return "[" + fecha.format(FORMATO_FECHA) + "] " + tipo.getDescripcion() + " Responsable: " + responsable;
    }
}
