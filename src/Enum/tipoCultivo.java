package Enum;

public enum tipoCultivo
{
    CEREAL("Trigo, Arroz, Avena, Cebada, Centeno...", "Rentabilidad media de: 4-4.5 t/ha"),
    CITRICO("Naranja, Mandarina, Limones, Pomelos...", "Rentabilidad media de: 28.5 t/ha"),
    HORTALIZAS("Lechuga, Tomate, Alcachofa, Patata", "Rentabilidad media de: 20-30 t/ha");

    private final String cultivos;
    private final String rentabilidadMedia;

    
    private tipoCultivo(String cultivos, String rentabilidadMedia) {
        this.cultivos = cultivos;
        this.rentabilidadMedia = rentabilidadMedia;
    }


    public String getCultivos() {
        return cultivos;
    }


    public String getRentabilidadMedia() {
        return rentabilidadMedia;
    }
}