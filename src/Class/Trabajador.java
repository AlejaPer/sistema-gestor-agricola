package Class;

import java.time.LocalDate;

import Enum.rangoTrabajador;
import Enum.tipoContrato;
import Interface.Consultable;

public class Trabajador implements Consultable
{
    private String nombre;
    private String apellidos;
    private String dni;
    private String telefono; 
    private String email;
    private rangoTrabajador rango;
    private tipoContrato contrato;
    private LocalDate fechaContratación;
    private double salarioBase;
    

    //CONSTRUCTOR
    public Trabajador(String nombre, String apellidos, String dni, String telefono, String email, rangoTrabajador rango,
            tipoContrato contrato, LocalDate fechaContratación, double salarioBase) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.dni = dni;
        this.telefono = telefono;
        this.email = email;
        this.rango = rango;
        this.contrato = contrato;
        this.fechaContratación = fechaContratación;
        this.salarioBase = salarioBase;
    }


    //GETTERS Y SETTERS
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null ||nombre.isEmpty() || nombre.length() > 30)
        {
            throw new IllegalArgumentException("Debes introducir un nombre");
        }
        else
        {
            this.nombre = nombre;
        }
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        if (apellidos == null || apellidos.isBlank() || apellidos.length() > 50 )
        {
            throw new IllegalArgumentException("Debes introducir un apellido");
        }
        this.apellidos = apellidos;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        if (dni == null || dni.isBlank() || dni.length() > 9)
        {
            throw new IllegalArgumentException("Debes introducir un dni o el dni introducido tiene un error");
        }
        
        this.dni = dni;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) 
    {
        
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public rangoTrabajador getRango() {
        return rango;
    }

    public void setRango(rangoTrabajador rango) {
        this.rango = rango;
    }

    public tipoContrato getContrato() {
        return contrato;
    }

    public void setContrato(tipoContrato contrato) {
        this.contrato = contrato;
    }

    public LocalDate getFechaContratación() {
        return fechaContratación;
    }

    public void setFechaContratación(LocalDate fechaContratación) {
        this.fechaContratación = fechaContratación;
    }

    public double getSalarioBase() {
        return salarioBase;
    }

    public void setSalarioBase(double salarioBase) {
        this.salarioBase = salarioBase;
    }


    //METODOS
    @Override
    public String toString() 
    {
        return "Nombre: " + nombre + "\nApellidos: " + apellidos + "\nDNI: " + dni + "\nTelefono: " + telefono + "\nEmail: " + email +
        "\nRango: " + rango + "\nContrato: " + contrato + "\nFecha de contratación: " + fechaContratación + "\nSalario base: " + salarioBase;
    }

    @Override
    public String getResumen() 
    {
        return toString();
    }
}
