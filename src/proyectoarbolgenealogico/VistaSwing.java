package proyectoarbolgenealogico;
// Capa Vista (View)
import javax.swing.*;
import java.awt.*;
import java.util.Set;
import java.util.Vector;

public class VistaSwing extends JFrame implements IVistaFamiliar {
    private ControladorFamiliar controlador;
    
    // Componentes de la interfaz
    private JTextField txtId, txtNombre;
    private JComboBox<Persona> cbPadre, cbHijo, cbP1, cbP2;
    private JTextArea txtConsola;

    public VistaSwing() {
        // Configuración de la ventana principal
        setTitle("Sistema de Árbol Genealógico y Consanguineidad");
        setSize(750, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // PANEL SUPERIOR: Formularios
        JPanel panelFormularios = new JPanel(new GridLayout(3, 1, 5, 5));
        panelFormularios.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Registrar Persona
        JPanel panelPersona = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelPersona.setBorder(BorderFactory.createTitledBorder("1. Registrar Nueva Persona"));
        txtId = new JTextField(5);
        txtNombre = new JTextField(15);
        JButton btnAddPersona = new JButton("Registrar Vértice");
        
        panelPersona.add(new JLabel("ID Único:"));
        panelPersona.add(txtId);
        panelPersona.add(new JLabel("Nombre Completo:"));
        panelPersona.add(txtNombre);
        panelPersona.add(btnAddPersona);

        // Registrar Relación
        JPanel panelRelacion = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelRelacion.setBorder(BorderFactory.createTitledBorder("2. Vincular Personas (Crear Arista)"));
        cbPadre = new JComboBox<>();
        cbHijo = new JComboBox<>();
        JButton btnAddRelacion = new JButton("Crear Relación (Padre -> Hijo)");
        
        panelRelacion.add(new JLabel("Es Padre/Madre de:"));
        panelRelacion.add(cbPadre);
        panelRelacion.add(new JLabel("Este Hijo/a:"));
        panelRelacion.add(cbHijo);
        panelRelacion.add(btnAddRelacion);

        // Analizar Consanguineidad
        JPanel panelAnalisis = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelAnalisis.setBorder(BorderFactory.createTitledBorder("3. Analizar Consanguineidad (Intersección DFS/BFS)"));
        cbP1 = new JComboBox<>();
        cbP2 = new JComboBox<>();
        JButton btnAnalizar = new JButton("Analizar");
        
        panelAnalisis.add(new JLabel("Persona A:"));
        panelAnalisis.add(cbP1);
        panelAnalisis.add(new JLabel("Persona B:"));
        panelAnalisis.add(cbP2);
        panelAnalisis.add(btnAnalizar);

        panelFormularios.add(panelPersona);
        panelFormularios.add(panelRelacion);
        panelFormularios.add(panelAnalisis);

        add(panelFormularios, BorderLayout.NORTH);

        // PANEL CENTRAL: Consola de Salida
        txtConsola = new JTextArea();
        txtConsola.setEditable(false);
        txtConsola.setFont(new Font("Monospaced", Font.PLAIN, 13));
        JScrollPane scroll = new JScrollPane(txtConsola);
        scroll.setBorder(BorderFactory.createTitledBorder("Registro de Actividad (Consola)"));
        add(scroll, BorderLayout.CENTER);

        // EVENTOS (Listeners)
        btnAddPersona.addActionListener(e -> {
            if(controlador != null) {
                String id = txtId.getText().trim();
                String nombre = txtNombre.getText().trim();
                if(!id.isEmpty() && !nombre.isEmpty()) {
                    controlador.registrarPersona(new Persona(id, nombre));
                    txtId.setText("");
                    txtNombre.setText("");
                } else {
                    mostrarError("Los campos ID y Nombre son obligatorios.");
                }
            }
        });

        btnAddRelacion.addActionListener(e -> {
            if(controlador != null) {
                Persona padre = (Persona) cbPadre.getSelectedItem();
                Persona hijo = (Persona) cbHijo.getSelectedItem();
                if(padre != null && hijo != null) {
                    controlador.registrarRelacion(padre, hijo);
                } else {
                    mostrarError("Debe seleccionar a dos personas registradas.");
                }
            }
        });

        btnAnalizar.addActionListener(e -> {
            if(controlador != null) {
                Persona p1 = (Persona) cbP1.getSelectedItem();
                Persona p2 = (Persona) cbP2.getSelectedItem();
                if(p1 != null && p2 != null) {
                    controlador.analizarConsanguineidad(p1, p2);
                } else {
                    mostrarError("Debe seleccionar a dos personas para comparar.");
                }
            }
        });
    }

    public void setControlador(ControladorFamiliar controlador) {
        this.controlador = controlador;
    }

    // --- IMPLEMENTACIÓN DE LA INTERFAZ IVistaFamiliar ---

    @Override
    public void mostrarMensaje(String mensaje) {
        txtConsola.append("[ÉXITO] " + mensaje + "\n");
    }

    @Override
    public void mostrarError(String error) {
        txtConsola.append("[ALERTA] " + error + "\n");
        JOptionPane.showMessageDialog(this, error, "Operación Inválida", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void mostrarAncestrosComunes(Persona p1, Persona p2, Set<Persona> comunes) {
        txtConsola.append("\n================ RESULTADOS DE BÚSQUEDA ================\n");
        txtConsola.append("Evaluando a: " + p1.getNombre() + " y " + p2.getNombre() + "\n");
        if (comunes.isEmpty()) {
            txtConsola.append("-> NO hay consanguineidad. No comparten líneas genéticas.\n");
        } else {
            txtConsola.append("-> SÍ hay consanguineidad detectada.\n");
            txtConsola.append("-> Ancestros comunes (Intersección de ramas):\n");
            for (Persona p : comunes) {
                txtConsola.append("   * " + p.getNombre() + " (ID: " + p.getId() + ")\n");
            }
        }
        txtConsola.append("========================================================\n\n");
    }

    @Override
    public void actualizarListaPersonas(Set<Persona> personas) {
        // Convierte el Set en un Vector para rellenar los ComboBox de Swing
        Vector<Persona> vectorPersonas = new Vector<>(personas);
        
        // Actualiza los 4 desplegables con la misma información
        cbPadre.setModel(new DefaultComboBoxModel<>(vectorPersonas));
        cbHijo.setModel(new DefaultComboBoxModel<>(vectorPersonas));
        cbP1.setModel(new DefaultComboBoxModel<>(vectorPersonas));
        cbP2.setModel(new DefaultComboBoxModel<>(vectorPersonas));
    }
}
