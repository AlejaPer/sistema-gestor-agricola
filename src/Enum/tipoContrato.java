package Enum;

public enum tipoContrato 
{
    TEMPORAL("Temporal"),
    FIJO_DISCONTINUO("Fijo Discontinuo"),
    FIJO("Fijo"),
    POR_OBRA("Por Obra y Servicio");

    private final String descripcion;



    private tipoContrato(String descripcion) 
    {
        this.descripcion = descripcion;
    }


    public String getDescripcion() {
        return descripcion;
    }


    @Override
    public String toString() 
    {
        return descripcion;
    }
}
