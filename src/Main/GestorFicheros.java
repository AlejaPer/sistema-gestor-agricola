package Main;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;

import Class.Campo;
import Class.Cultivo;
// import Class.RegistroTarea;
import Class.Trabajador;
import Enum.*;

public class GestorFicheros {

    // Nombres de los archivos que se crearán en la carpeta del proyecto y separadores
    private static final String SEP = "|";
    private static final String ARCHIVO_CAMPOS = "campos.txt";
    private static final String ARCHIVO_CULTIVOS = "cultivos.txt";
    private static final String ARCHIVO_TRABAJADORES = "trabajadores.txt";
    private static final String ARCHIVO_HISTORIAL = "historial.txt";

    
    // PARA GUARDAR LOS CULTIVOS
    public static void guardarCultivos(ArrayList<Cultivo> cultivos) 
    {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARCHIVO_CULTIVOS))) 
        {
            for (Cultivo a : cultivos) 
            {
                pw.println(a.getNombre() + SEP + a.getDuracionDias() + SEP + a.getLitrosAguaPorM2Semana() + SEP + a.getNotas() + SEP + a.gettTipoCultivo().name());
            }
        } 
        catch (IOException e) 
        {
            System.err.println("Error guardando cultivos: " + e.getMessage());
        }
    }

    //GUARDAR LOS CAMPOS
    public static void guardarCampos(ArrayList<Campo> campos) 
    {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARCHIVO_CAMPOS))) 
        {
            for (Campo c : campos) 
            {
                // Si algún campo es null lo guardamos como cadena vacía
                String cultivoNombre = c.getCultivoActual() != null ? c.getCultivoActual().getNombre() : "";
                String fechaPlant = c.getFechaPlantación() != null ? c.getFechaPlantación().toString() : "";
                String fechaRiego = c.getFechaUltimoRiego() != null ? c.getFechaUltimoRiego().toString() : "";
                String fechaRecolecion = c.getFechaEstimadaRecolección() != null ? c.getFechaEstimadaRecolección().toString() : "";


                pw.println(c.getID() + SEP + c.getNombre() + SEP + c.getUbicación() + SEP + c.getHectareas() + SEP + c.getTipoDeSuelo().name() + SEP +
                            cultivoNombre + SEP + fechaPlant + SEP + fechaRiego + SEP + fechaRecolecion);
            }
        } 
        catch (IOException e) 
        {
            System.err.println("Error guardando campos: " + e.getMessage());
        }
    }

    //GUARDAR LOS TRABAJADORES
    public static void guardarTrabajadores(ArrayList<Trabajador> trabajadores) 
    {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARCHIVO_TRABAJADORES))) 
        {
            for (Trabajador t : trabajadores) 
            {
                pw.println(t.getNombre() + SEP + t.getApellidos() + SEP + t.getDni() + SEP + t.getTelefono() + SEP + t.getEmail() + SEP + 
                            t.getRango().name() + SEP + t.getContrato().name()  + SEP + t.getFechaContratación()+ SEP + t.getSalarioBase());
            }
        } 
        catch (IOException e) 
        {
            System.err.println("Error guardando trabajadores: " + e.getMessage());
        }
    }

    //GUARDAR EL HISTORIAL
    public static void guardarHistorial(ArrayList<String> historial) 
    {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARCHIVO_HISTORIAL))) 
        {
            for (String entrada : historial) 
            {
                pw.println(entrada);
            }
        } 
        catch (IOException e) 
        {
            System.err.println("Error guardando historial: " + e.getMessage());
        }
    }



    //  AL MOMENTO DE CARGAR
    public static ArrayList<Cultivo> cargarCultivos() 
    {
        ArrayList<Cultivo> lista = new ArrayList<>();
        File f = new File(ARCHIVO_CULTIVOS);
        if (!f.exists()) return lista; // Si no existe el archivo devuelve lista vacía

        try (BufferedReader br = new BufferedReader(new FileReader(f))) 
        {
            String linea;

            while ((linea = br.readLine()) != null) 
            {
                if (linea.isBlank()) continue;

                String[] p = linea.split("\\|", -1); // -1 para no perder campos vacíos al final

                if (p.length < 5) continue;

                lista.add(new Cultivo(
                    p[0],                            // nombre
                    Integer.parseInt(p[1]),          // duracionDias
                    Double.parseDouble(p[2]),        // litrosAgua
                    p[3],                            // notas
                    tipoCultivo.valueOf(p[4])        // tipo
                ));
            }
        } 
        catch (IOException e) 
        {
            System.err.println("Error cargando cultivos: " + e.getMessage());
        }

        return lista;
    }


    // Necesita la lista de cultivos ya cargada para poder enlazar el cultivoActual
    public static ArrayList<Campo> cargarCampos(ArrayList<Cultivo> cultivos) 
    {
        ArrayList<Campo> lista = new ArrayList<>();
        File f = new File(ARCHIVO_CAMPOS);
        if (!f.exists()) return lista;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) 
        {
            String linea;

            while ((linea = br.readLine()) != null) 
            {
                if (linea.isBlank()) continue;

                String[] p = linea.split("\\|", -1); //El -1 es para que no descarte los campos vacios del final de la linea. Por ej si una fecha está vacía es ""

                if (p.length < 9) continue; //Porque al hacer el regex \\| -1 obtienes un array de 9 elementos. Pones esto para que no pete si hay algun error

                Campo campo = new Campo(
                    p[0], // Buscar el dni
                    p[1], // El nombre
                    p[2], // La ubicacion
                    Double.parseDouble(p[3]), // Las hectareas
                    tipoSuelo.valueOf(p[4]), // El tipo de Suelo
                    null,
                    null,
                    null,
                    null
                );

                // Esto buscaa el cultivo por nombre y si existe lo "enlaza"
                if (!p[5].isEmpty()) 
                {
                    for (Cultivo c : cultivos) 
                    {
                        if (c.getNombre().equals(p[5])) 
                        {
                            campo.setCultivoActual(c);
                            break;
                        }
                    }
                }

                if (!p[6].isEmpty())
                {
                    campo.setFechaPlantación(LocalDate.parse(p[6]));
                }

                if (!p[7].isEmpty())
                {
                    campo.setFechaUltimoRiego(LocalDate.parse(p[7]));
                }

                if (!p[8].isEmpty())
                {
                    campo.setFechaEstimadaRecolección(LocalDate.parse(p[8]));
                }

                lista.add(campo);
            }
        } 
        catch (IOException e) 
        {
            System.err.println("Error cargando campos: " + e.getMessage());
        }

        return lista;
    }

    public static ArrayList<Trabajador> cargarTrabajadores() 
    {
        ArrayList<Trabajador> lista = new ArrayList<>();

        File f = new File(ARCHIVO_TRABAJADORES);

        if (!f.exists()) return lista;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) 
        {
            String linea;

            while ((linea = br.readLine()) != null) 
            {
                if (linea.isBlank()) continue;

                String[] p = linea.split("\\|", -1);

                if (p.length < 9) continue;

                lista.add(new Trabajador(
                    p[0], // El nombre
                    p[1], // Los apellidos
                    p[2], // El DNI
                    p[3], // El telefono
                    p[4], // EL email
                    rangoTrabajador.valueOf(p[5]), // El rango
                    tipoContrato.valueOf(p[6]), // El contrato
                    LocalDate.parse(p[7]), // La fecha de contratacion
                    Double.parseDouble(p[8]) // El salario
                ));
            }
        } 
        catch (IOException e) 
        {
            System.err.println("Error cargando trabajadores: " + e.getMessage());
        }

        return lista;
    }

    public static ArrayList<String> cargarHistorial() 
    {
        ArrayList<String> lista = new ArrayList<>();

        File f = new File(ARCHIVO_HISTORIAL);

        if (!f.exists()) 
        {
            return lista;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(f))) 
        {
            String linea;

            while ((linea = br.readLine()) != null) 
            {
                if (!linea.isBlank()) lista.add(linea);
            }
        } 
        catch (IOException e) 
        {
            System.err.println("Error cargando historial: " + e.getMessage());
        }
        
        return lista;
    }
}
