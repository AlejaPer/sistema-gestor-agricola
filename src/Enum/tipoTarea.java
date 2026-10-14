package Enum;

public enum tipoTarea 
{
    RIEGO("Riego"),
    ABONO("Abono"),
    PODA("Poda"),
    SIEMBRA("Siembra"),
    RECOLECCION("Recolección"),
    TRATAMIENTO("Tratamiento fitosanitario"),
    ARADO("Arado / Preparación del terreno"),
    MANTENIMIENTO("Mantenimiento general"),
    INSPECCION("Inspección");

    private final String descripción;

    private tipoTarea(String descipcion) 
    {
        this.descripción = descipcion;
    }

    public String getDescripcion() {
        return descripción;
    }

    @Override
    public String toString() 
    {
        return descripción;
    }
}
