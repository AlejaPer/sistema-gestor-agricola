package Class;

// import java.util.ArrayList;
// import Enum.tipoTarea;
import java.time.LocalDate;
import Enum.tipoSuelo;
import Interface.Consultable;

public class Campo implements Consultable
{
    //Creamos los atributos de identificación
    private String ID;
    private String Nombre;
    private String Ubicación;

    //Atributos argonómicos
    private double Hectareas;
    private tipoSuelo tipoDeSuelo;
    private Cultivo cultivoActual;

    //Atributos de fechas (LocalDate)
    private LocalDate fechaPlantación;
    private LocalDate fechaUltimoRiego;
    private LocalDate fechaEstimadaRecolección;

    //Historial de actividad en el campo
    // private ArrayList<RegistroTarea> historial;


    //Constructor
    public Campo(String iD, String nombre, String ubicación, double hectareas, tipoSuelo tipoDeSuelo,
            Cultivo cultivoActual, LocalDate fechaPlantacion, LocalDate fechaUltimoRiego,
            LocalDate fechaEstimadaRecolección) {
        ID = iD;
        Nombre = nombre;
        Ubicación = ubicación;
        Hectareas = hectareas;
        this.tipoDeSuelo = tipoDeSuelo;
        //Estos valores se establecerán después POSIBLE PREGUNTA
        this.cultivoActual = null;
        this.fechaPlantación = null;
        this.fechaUltimoRiego = null;
        this.fechaEstimadaRecolección = null;
        //-------------------------------------
        // this.historial = historial;
    }

    
    public void plantarCultivo(Cultivo cultivo, LocalDate fechaPlantacion) {
        this.cultivoActual = cultivo;
        this.fechaPlantación = fechaPlantacion;

        //En la clase cultivo hay un apartado en el que introduces una estimación de cuánto se tarda en recolectar el cultivo
        this.fechaEstimadaRecolección =
            fechaPlantacion.plusDays(cultivo.getDuracionDias());

            
        // Registrar la siembra en el historial con el objeto (Clase llamada) RegistroTarea
        // historial.add(new RegistroTarea(
        //     tipoTarea.SIEMBRA,
        //     fechaPlantacion,
        //     "Sistema",
        //     "Siembra de " + cultivo.getNombre(),
        //     0.0
        // ));
    }

    //Para registrar que se ha regado
    public void registrarRiego(LocalDate fecha, String responsable) {
        this.fechaUltimoRiego = fecha;
        // historial.add(new RegistroTarea(
        //     tipoTarea.RIEGO,
        //     fecha,
        //     responsable,
        //     "Riego registrado",
        //     0.0
        // ));
    }

    //Para saber cuanto tiempo queda para la recolección. POSIBLE PREGUNTA EL POR QUE HE PUESTO QUE RETURN -1 O RETURN 0
    public long diasParaRecoleccion() {
        if (fechaEstimadaRecolección == null)
        {
            return -1; //Se pone esto porque si lo usamos el sistema intentaria hacer el calculo con un null y eso haría que el programa pete por un NullPointerException
        }

        return LocalDate.now().until(fechaEstimadaRecolección).getDays();
    }

    //Calcular cuando fue el ultimo riego
    public long diasDesdeUltimoRiego() {
        if (fechaUltimoRiego == null) 
        {
            return -1; //Lo mismo
        }

        return fechaUltimoRiego.until(LocalDate.now()).getDays();
    }

    //Saber cuanta agua se va a necesitar por semana
    public double aguaNecesariaSemanal() {
        if (cultivoActual == null)
            {
                return 0; //Lo mismo. Lo he puesto diferente porque al ser dos valores distintos (long y double) me parece más bonito
            } 
            
        return cultivoActual.calcularAguaTotalSemana(Hectareas);
    }



    //GETTERS Y SETTERS
    public String getID() {
        return ID;
    }

    public void setID(String iD) {
        ID = iD;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getUbicación() {
        return Ubicación;
    }

    public void setUbicación(String ubicación) {
        Ubicación = ubicación;
    }

    public double getHectareas() {
        return Hectareas;
    }

    public void setHectareas(double hectareas) {
        Hectareas = hectareas;
    }

    public tipoSuelo getTipoDeSuelo() {
        return tipoDeSuelo;
    }

    public void setTipoDeSuelo(tipoSuelo tipoDeSuelo) {
        this.tipoDeSuelo = tipoDeSuelo;
    }

    public Cultivo getCultivoActual() {
        return cultivoActual;
    }

    public void setCultivoActual(Cultivo cultivoActual) {
        this.cultivoActual = cultivoActual;
    }

    public LocalDate getFechaPlantación() {
        return fechaPlantación;
    }

    public void setFechaPlantación(LocalDate fechaPlantación) {
        this.fechaPlantación = fechaPlantación;
    }

    public LocalDate getFechaUltimoRiego() {
        return fechaUltimoRiego;
    }

    public void setFechaUltimoRiego(LocalDate fechaUltimoRiego) {
        this.fechaUltimoRiego = fechaUltimoRiego;
    }

    public LocalDate getFechaEstimadaRecolección() {
        return fechaEstimadaRecolección;
    }

    public void setFechaEstimadaRecolección(LocalDate fechaEstimadaRecolección) {
        this.fechaEstimadaRecolección = fechaEstimadaRecolección;
    }

    // public ArrayList<RegistroTarea> getHistorial() {
    //     return historial;
    // }

    // public void setHistorial(ArrayList<RegistroTarea> historial) {
    //     this.historial = historial;
    // }


    //Metodos 
    @Override
    public String toString() 
    {
        return "ID: " + ID + "\nNombre: " + Nombre + "\nUbicacion: " + Ubicación + "\nHectareas: " + Hectareas + "\nTipo de Suelo: " + tipoDeSuelo 
        + "\nCultivo Actual: " + cultivoActual + "\nFecha de Plantación: " + fechaPlantación + "\nFecha Ultimo Riego: " + fechaUltimoRiego + 
        "\nFecha Estimada de Recolección: " + fechaEstimadaRecolección;
    }

    public String getResumen() 
    {
        long diasRec = diasParaRecoleccion();
        long diasRiego = diasDesdeUltimoRiego();
        double agua = aguaNecesariaSemanal();

        StringBuilder sb = new StringBuilder();
        sb.append(toString());
        sb.append("\n\nDías para recolección : ").append(diasRec   < 0 ? "Sin cultivo / fecha pasada" : diasRec);
        sb.append("\nDías desde último riego: ").append(diasRiego < 0 ? "Sin registro" : diasRiego);
        sb.append("\nAgua semanal necesaria : ").append(agua).append(" L");


        return sb.toString();
    }
}