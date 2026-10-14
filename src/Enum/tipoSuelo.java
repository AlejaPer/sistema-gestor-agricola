package Enum;

public enum tipoSuelo 
{
    ARCILLOSO("Arcilloso", "Alta retención de agua, drenaje lento."),
    ARENOSO("Arenoso", "Baja retención de agua, drenaje rápido."),
    LIMOSO("Limoso", "Buena retención, muy fertil."),
    FRANCO("Franco", "Equilibrado, ideal para la mayoría de cultivos."),
    FRANCO_ARCILLOS("Franco-Arcilloso", "Buena fertilidad, retención de agua moderada."),
    CALIZO("Calizo", "Alcalino, limita algunos cultivos.");

    private final String nombre;
    private final String descripcion;

    tipoSuelo(String nombre, String descripcion)
    {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String toString() 
    {
        return nombre;
    }
}
