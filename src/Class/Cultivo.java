package Class;


import Enum.tipoCultivo;
import Interface.Consultable;

public class Cultivo implements Consultable
{
    private String nombre;
    private int duracionDias;
    private double litrosAguaPorM2Semana;
    private String notas;
    private tipoCultivo tipoCultivo;

    //Constructor
    public Cultivo(String nombre, int duracionDias, double litrosAguaPorM2Semana,
            String notas, tipoCultivo tipoCultivo) {
        this.nombre = nombre;
        this.duracionDias = duracionDias;
        this.litrosAguaPorM2Semana = litrosAguaPorM2Semana;
        this.notas = notas;
        this.tipoCultivo = tipoCultivo;
    }

    //Metodos extra

    //Para calcular cuanta agua va a necesitar a la semana
    public double calcularAguaTotalSemana(double hectareas) {
        return litrosAguaPorM2Semana * hectareas * 10000;
    }


    //Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getDuracionDias() {
        return duracionDias;
    }

    public void setDuracionDias(int duracionDias) {
        this.duracionDias = duracionDias;
    }

    public double getLitrosAguaPorM2Semana() {
        return litrosAguaPorM2Semana;
    }

    public void setLitrosAguaPorM2Semana(double litrosAguaPorM2Semana) {
        this.litrosAguaPorM2Semana = litrosAguaPorM2Semana;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public tipoCultivo gettTipoCultivo() {
        return tipoCultivo;
    }

    public void setTipoCultivo(tipoCultivo tipoCultivo) {
        this.tipoCultivo = tipoCultivo;
    }


    //Metodos
    @Override
    public String toString() 
    {
        return "Nombre: " + nombre + "\nDuración: " + duracionDias + "\nAgua (L/m2 * semana): " + litrosAguaPorM2Semana;
    }

    @Override
    public String getResumen() 
    {
        return toString() + "\nNotas: " + notas + "\nTipo : " + tipoCultivo.name();
    }
    
}
