package Main;

import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.*;
import Class.Campo;
import Class.Cultivo;
// import Class.RegistroTarea;
import Class.Trabajador;
import Enum.*;
import Interface.Consultable;

public class VentanaPrincipal extends JFrame 
{

    // GUARDAR DATOS
    private final ArrayList<Campo> listaCampos = new ArrayList<>();
    private final ArrayList<Cultivo> listaCultivos = new ArrayList<>();
    private final ArrayList<Trabajador> listaTrabajadores = new ArrayList<>();
    private final ArrayList<String> historial = new ArrayList<>();

    // JList (la UI los lee de aquí) 
    private final DefaultListModel<String> modeloCampos = new DefaultListModel<>();
    private final DefaultListModel<String> modeloCultivos = new DefaultListModel<>();
    private final DefaultListModel<String> modeloTrabajadores = new DefaultListModel<>();
    private final DefaultListModel<String> modeloHistorial = new DefaultListModel<>();


    //  BASE
    public VentanaPrincipal() 
    {
        setTitle("Gestor de Campos Agrícolas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(850, 550);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("🌾  Campos", crearPanelCampos());
        tabs.addTab("🌱  Cultivos", crearPanelCultivos());
        tabs.addTab("👷  Trabajadores", crearPanelTrabajadores());
        tabs.addTab("📋  Historial", crearPanelHistorial());

        add(tabs);
        cargarDatos();
    }

    //PARA CARGAR LOS DATOS AL ENTRAR
    private void cargarDatos() 
    {
        // Primero cultivos porque campos depende de esto
        for (Cultivo a : GestorFicheros.cargarCultivos()) 
        {
            listaCultivos.add(a);
            modeloCultivos.addElement(a.getNombre() + "  (" + a.gettTipoCultivo().name() + ")");
        }

        for (Campo a : GestorFicheros.cargarCampos(listaCultivos)) 
        {
            listaCampos.add(a);
            modeloCampos.addElement(a.getID() + "  –  " + a.getNombre());
        }
        
        for (Trabajador t : GestorFicheros.cargarTrabajadores()) 
        {
            listaTrabajadores.add(t);
            modeloTrabajadores.addElement(t.getNombre() + " " + t.getApellidos() + "  (DNI: " + t.getDni() + ")");
        }
        
        for (String entrada : GestorFicheros.cargarHistorial()) 
        {
            historial.add(entrada);
            modeloHistorial.addElement(entrada);
        }
    }

    //EL MAIN
    public static void main(String[] args) 
    {
        SwingUtilities.invokeLater(() -> new VentanaPrincipal().setVisible(true));
    }

    //PARA AÑADIRLE COSITAS AL HISTORIAL 
    private void log(String mensaje) 
    {
        String entrada = "[" + LocalDate.now() + "] " + mensaje;
        historial.add(entrada);
        modeloHistorial.addElement(entrada);
    }



    //  ZONA PARA CAMPOS
    private JPanel crearPanelCampos() 
    {
        JList<String> uiLista = new JList<>(modeloCampos);
        uiLista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JButton botonAnadir = new JButton("Añadir");
        JButton botonEliminar = new JButton("Eliminar");
        JButton botonRiego = new JButton("Registrar Riego");
        JButton botonPlantar = new JButton("Plantar Cultivo");
        JButton botonInformacion = new JButton("Ver Info");

        botonAnadir.addActionListener(e -> anadirCampo());
        botonEliminar.addActionListener(e -> eliminarCampo(uiLista));
        botonRiego.addActionListener(e -> registrarRiego(uiLista));
        botonPlantar.addActionListener(e -> plantarCultivo(uiLista));

        botonInformacion.addActionListener(e -> {int idx = uiLista.getSelectedIndex();
        if (validarSeleccion(idx, "campo"))
        {
            verInfo(listaCampos.get(idx), "Info – " + listaCampos.get(idx).getNombre());
        }
        });

        return construirPanel(uiLista, botonAnadir, botonEliminar, botonRiego, botonPlantar, botonInformacion);
    }


    private void anadirCampo() 
    {
        String id = pedir("ID del campo:");
        if (id == null) return;

        String nombre = pedir("Nombre del campo:");
        if (nombre == null)
        {
            return;
        } 
                

        String ubicacion = pedir("Ubicación:");
        if (ubicacion == null)
        {
            return;
        } 

        Double hectareas = pedirDouble("Hectáreas:");
        if (hectareas == null)
        {
            return;
        }

        tipoSuelo suelo = (tipoSuelo) elegirEnum("Tipo de suelo:", tipoSuelo.values());
        if (suelo == null) 
        {
            return;
        }

        Campo campo = new Campo(id, nombre, ubicacion, hectareas, suelo, null, null, null, null);
        listaCampos.add(campo);
        modeloCampos.addElement(id + "  –  " + nombre);
        log("Campo añadido: " + nombre + " (ID: " + id + ")");
        guardarTodo();
        info("Campo añadido correctamente.");
    }

    
    private void eliminarCampo(JList<String> lista) 
    {
        int idx = lista.getSelectedIndex();

        if (!validarSeleccion(idx, "campo"))
        {
            return;
        }

        Campo c = listaCampos.remove(idx);
        modeloCampos.remove(idx);
        log("Campo eliminado: " + c.getNombre() + " (ID: " + c.getID() + ")");
        guardarTodo();
        info("Campo eliminado.");
    }


    private void registrarRiego(JList<String> lista) 
    {
        int idx = lista.getSelectedIndex();

        if (!validarSeleccion(idx, "campo"))
        {
            return;
        }

        Campo c = listaCampos.get(idx);

        LocalDate fecha = pedirFecha("Fecha del riego (YYYY-MM-DD):");

        if (fecha == null)
        {
            return;
        }

        String responsable = pedir("Responsable:");

        if (responsable == null) 
        {
            return;
        }

        c.registrarRiego(fecha, responsable);
        log("Riego en «" + c.getNombre() + "» por " + responsable);
        guardarTodo();
        info("Riego registrado correctamente.");
    }


    private void plantarCultivo(JList<String> lista) 
    {
        int idx = lista.getSelectedIndex();

        if (!validarSeleccion(idx, "campo")) 
        {
            return;
        }

        if (listaCultivos.isEmpty()) 
        { 
            error("No hay cultivos registrados."); 
            return; 
        }

        Campo c = listaCampos.get(idx);
        String[] nombres = listaCultivos.stream().map(Cultivo::getNombre).toArray(String[]::new);
        int opcion = JOptionPane.showOptionDialog(this, "Selecciona el cultivo:", "Plantar",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, nombres, nombres[0]);

        if (opcion < 0) 
        {
            return;
        }

        LocalDate fecha = pedirFecha("Fecha de plantación (YYYY-MM-DD):");
        if (fecha == null) 
        {
            return;
        }

        Cultivo cultivo = listaCultivos.get(opcion);
        c.plantarCultivo(cultivo, fecha);
        log("Cultivo «" + cultivo.getNombre() + "» plantado en campo «" + c.getNombre() + "»");
        guardarTodo();
        info("Cultivo plantado correctamente.");
    }



    //  ZONA PARA CULTIVOS
    private JPanel crearPanelCultivos() 
    {
        JList<String> uiLista = new JList<>(modeloCultivos);
        uiLista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JButton botonAnadir = new JButton("Añadir");
        JButton botonEliminar = new JButton("Eliminar");
        JButton botonInformacion = new JButton("Ver Info");

        botonAnadir.addActionListener(e -> anadirCultivo());
        botonEliminar.addActionListener(e -> eliminarCultivo(uiLista));
        botonInformacion.addActionListener(e -> { int idx = uiLista.getSelectedIndex();
        if (validarSeleccion(idx, "cultivo"))
        {
            verInfo(listaCultivos.get(idx), "Info – " + listaCultivos.get(idx).getNombre());
        }
        });

        return construirPanel(uiLista, botonAnadir, botonEliminar, botonInformacion);
    }


    private void anadirCultivo() {
        String nombre = pedir("Nombre del cultivo:");
        if (nombre == null) 
        {
            return;
        }

        Integer dias = pedirInt("Duración estimada (días):");
        if (dias == null) 
        {
            return;
        }

        Double litros = pedirDouble("Litros de agua por m² a la semana:");
        if (litros == null) 
        {
            return;
        }

        String notas = pedir("Notas (puede dejarse vacío):");
        if (notas == null) 
        {
            notas = "";
        }

        tipoCultivo tipo = (tipoCultivo) elegirEnum("Tipo de cultivo:", tipoCultivo.values());
        if (tipo == null)
        {
            return;
        }

        Cultivo cultivo = new Cultivo(nombre, dias, litros, notas, tipo);
        listaCultivos.add(cultivo);
        modeloCultivos.addElement(nombre + "  (" + tipo.name() + ")");
        log("Cultivo añadido: " + nombre);
        guardarTodo();
        info("Cultivo añadido correctamente.");
    }


    private void eliminarCultivo(JList<String> lista) 
    {
        int idx = lista.getSelectedIndex();

        if (!validarSeleccion(idx, "cultivo")) 
        {
            return;
        }

        Cultivo c = listaCultivos.remove(idx);
        modeloCultivos.remove(idx);
        log("Cultivo eliminado: " + c.getNombre());
        guardarTodo();
        info("Cultivo eliminado.");
    }



    //  ZONA PARA TRABAJADORES
    private JPanel crearPanelTrabajadores() 
    {
        JList<String> uiLista = new JList<>(modeloTrabajadores);
        uiLista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JButton botonAnadir = new JButton("Añadir");
        JButton botonEliminar = new JButton("Eliminar");
        JButton botonInformacion = new JButton("Ver Info");
        JButton botonSubirS = new JButton("Subir Salario");
        JButton botonBajarS = new JButton("Bajar Salario");
        JButton botonOrden = new JButton("Lista por Salario");

        botonAnadir.addActionListener(e -> anadirTrabajador());
        botonEliminar.addActionListener(e -> eliminarTrabajador(uiLista));

        botonInformacion.addActionListener(e -> {int idx = uiLista.getSelectedIndex();
        if (validarSeleccion(idx, "trabajador"))
        {
            verInfo(listaTrabajadores.get(idx), "Info – " + listaTrabajadores.get(idx).getNombre());
        }
        });

        botonSubirS.addActionListener(e -> modificarSalario(uiLista, true));
        botonBajarS.addActionListener(e -> modificarSalario(uiLista, false));
        botonOrden.addActionListener(e -> mostrarOrdenadosPorSalario());

        return construirPanel(uiLista, botonAnadir, botonEliminar, botonInformacion, botonSubirS, botonBajarS, botonOrden);
    }


    private void anadirTrabajador() 
    {
        String nombre = pedir("Nombre:");
        if (nombre == null) 
        {
            return;
        }

        String apellidos = pedir("Apellidos:");
        if (apellidos == null) 
        {
            return;
        }

        String dni = pedir("DNI:");
        if (dni == null)
        {
            return;
        } 

        String telefono = pedir("Teléfono:");
        if (telefono == null) 
        {
            telefono = "";
        }

        String email = pedir("Email:");
        if (email == null) 
        {
            email = "";
        }

        rangoTrabajador rango = (rangoTrabajador) elegirEnum("Rango:", rangoTrabajador.values());
        if (rango == null) 
        {
            return;
        }

        tipoContrato contrato = (tipoContrato) elegirEnum("Tipo de contrato:", tipoContrato.values());
        if (contrato == null) 
        {
            return;
        }

        LocalDate fechaContrato = pedirFecha("Fecha de contratación (YYYY-MM-DD):");
        if (fechaContrato == null) 
        {
            return;
        }

        Double salario = pedirDouble("Salario base (€):");
        if (salario == null) 
        {
            return;
        }

        Trabajador t = new Trabajador(nombre, apellidos, dni, telefono, email, rango, contrato, fechaContrato, salario);
        listaTrabajadores.add(t);
        modeloTrabajadores.addElement(nombre + " " + apellidos + "  (DNI: " + dni + ")");
        log("Trabajador añadido: " + nombre + " " + apellidos + " – DNI: " + dni);
        guardarTodo();
        info("Trabajador añadido correctamente.");
    }


    private void eliminarTrabajador(JList<String> lista) 
    {
        int idx = lista.getSelectedIndex();

        if (!validarSeleccion(idx, "trabajador"))
        {
            return;
        } 

        Trabajador t = listaTrabajadores.remove(idx);
        modeloTrabajadores.remove(idx);
        log("Trabajador eliminado: " + t.getNombre() + " " + t.getApellidos() + " – DNI: " + t.getDni());
        guardarTodo();
        info("Trabajador eliminado.");
    }


    private void modificarSalario(JList<String> lista, boolean subir) 
    {
        int idx = lista.getSelectedIndex();

        if (!validarSeleccion(idx, "trabajador")) 
        {
            return;
        }

        Trabajador t = listaTrabajadores.get(idx);

        Double cantidad = pedirDouble((subir ? "Cantidad a subir" : "Cantidad a bajar") + " (€):");
        if (cantidad == null)
        {
            return;
        } 

        double nuevo = subir ? t.getSalarioBase() + cantidad : t.getSalarioBase() - cantidad;
        t.setSalarioBase(nuevo);
        log("Salario " + (subir ? "subido" : "bajado") + " – " + t.getNombre() + ": " + nuevo + " €");
        guardarTodo();
        info("Salario actualizado. Nuevo salario: " + nuevo + " €");
    }


    private void mostrarOrdenadosPorSalario() 
    {
        if (listaTrabajadores.isEmpty()) 
        { 
            error("No hay trabajadores registrados."); 
            return; 
        }

        // Stream + Comparator
        List<Trabajador> ordenados = listaTrabajadores.stream()
                .sorted(Comparator.comparingDouble(Trabajador::getSalarioBase).reversed())
                .collect(Collectors.toList());

        StringBuilder sb = new StringBuilder("Trabajadores ordenados por salario (mayor → menor):\n\n");

        for (int i = 0; i < ordenados.size(); i++) 
        {
            Trabajador t = ordenados.get(i);
            sb.append(i + 1).append(".  ")
                .append(t.getNombre()).append(" ").append(t.getApellidos())
                .append("  –  ").append(t.getSalarioBase()).append(" €\n");
        }
        
        mostrarTexto("Lista por Salario", sb.toString());
    }


    //  ZONA DEL HISTORIAL
    private JPanel crearPanelHistorial() 
    {
        JList<String> uiLista = new JList<>(modeloHistorial);
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        panel.add(new JScrollPane(uiLista), BorderLayout.CENTER);

        JButton btnLimpiar = new JButton("Limpiar historial");
        btnLimpiar.addActionListener(e -> {
            historial.clear();
            modeloHistorial.clear();
        });
        panel.add(btnLimpiar, BorderLayout.SOUTH);
        return panel;
    }



    //  AYUDAS PARA LA CONSTRUCCIÓN DE PANELES Y DIALOGOS

    // CREAR BORDERLAYOUT CON LA LISTA QUE SALE ARRIBA Y LOS BOTONES DE ABAJO 
    private JPanel construirPanel(JList<String> lista, JButton... botones) //JButton... -> Para meter tantos botones como necesites
    {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        panel.add(new JScrollPane(lista), BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 4));
        for (JButton b : botones)
        {
            panelBotones.add(b);
        }

        panel.add(panelBotones, BorderLayout.SOUTH);
        return panel;
    }


    // Muestra un JOptionPane con un JTextArea de solo lectura
    private void mostrarTexto(String titulo, String texto) 
    {
        JTextArea area = new JTextArea(texto);
        area.setEditable(false);
        area.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(420, 300));
        JOptionPane.showMessageDialog(this, scroll, titulo, JOptionPane.INFORMATION_MESSAGE);
    }


    // Devuelve null si el usuario cancela o lo deja vacio
    private String pedir(String mensaje) 
    {
        String r = JOptionPane.showInputDialog(this, mensaje);
        return (r == null || r.isBlank()) ? null : r.trim();
    }


    private Double pedirDouble(String mensaje) 
    {
        String r = pedir(mensaje);
        if (r == null) 
        {
            return null;
        }

        try 
        { 
            return Double.parseDouble(r); 
        }
        catch (NumberFormatException e) 
        { 
            error("Valor numérico no válido."); 
            return null; 
        }
    }


    private Integer pedirInt(String mensaje) 
    {
        String r = pedir(mensaje);
        if (r == null)
        {
            return null;
        }

        try 
        { 
            return Integer.parseInt(r); 
        }
        catch (NumberFormatException e) 
        { 
            error("Valor entero no válido."); return null; 
        }
    }


    private LocalDate pedirFecha(String mensaje) 
    {
        while (true) 
        {
            String r = JOptionPane.showInputDialog(this, mensaje);

            if (r == null) 
            {
                return null; // canceló
            }

            r = r.trim();

            if (r.isBlank()) 
            {
                error("La fecha no puede estar vacía.");
                continue;
            }

            try 
            {
                return LocalDate.parse(r);
            } 
            catch (Exception e) 
            {
                error("Formato incorrecto. Usa YYYY-MM-DD (ejemplo: 2025-06-15).");
            }
        }
    }

    private void verInfo(Consultable elemento, String titulo) 
    {
        mostrarTexto(titulo, elemento.getResumen());
    }

    // Muestra los valores de un enum como botones y devuelve el elegido (o null). POSIBLE PREGUNTA, COMO HAS HECHO QUE LOS ENUMS SALGAN COMO BOTONES
    private Enum<?> elegirEnum(String mensaje, Enum<?>[] valores) 
    {
        String[] nombres = new String[valores.length];

        for (int i = 0; i < valores.length; i++)
        {
            nombres[i] = valores[i].toString();
        }

        int op = JOptionPane.showOptionDialog(this, mensaje, "Selección", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, nombres, nombres[0]);

        return op < 0 ? null : valores[op];
    }

    private boolean validarSeleccion(int idx, String tipo) 
    {
        if (idx >= 0) return true;
        error("Selecciona un " + tipo + " de la lista primero.");
        return false;
    }

    private void guardarTodo() 
    {
        GestorFicheros.guardarCultivos(listaCultivos);
        GestorFicheros.guardarCampos(listaCampos);
        GestorFicheros.guardarTrabajadores(listaTrabajadores);
        GestorFicheros.guardarHistorial(historial);
    }

    private void info(String msg)
    { 
        JOptionPane.showMessageDialog(this, msg, "OK",    JOptionPane.INFORMATION_MESSAGE); 
    }

    private void error(String msg) 
    { 
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE); 
    }
}
