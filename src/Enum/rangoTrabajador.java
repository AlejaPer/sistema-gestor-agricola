package Enum;

public enum rangoTrabajador 
{
    
    PEON("Peon", 1.0),
    ESPECIALISTA("Especialista", 1.2),
    ENCARGADO("Encargado", 1.5),
    JEFE_CAMPO("Jefe de Campo", 2.0);

    private final String nombre;
    private final double multiplicadoSalario;


    private rangoTrabajador(String nombre, double multiplicadoSalario) 
    {
        this.nombre = nombre;
        this.multiplicadoSalario = multiplicadoSalario;
    }


    public String getNombre() {
        return nombre;
    }


    public double getMultiplicadoSalario() {
        return multiplicadoSalario;
    }

    @Override
    public String toString() 
    {
        return nombre;
    }
}
