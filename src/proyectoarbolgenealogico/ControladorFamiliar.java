package proyectoarbolgenealogico;
// Capa Controlador (Controller)
import java.util.Set;

public class ControladorFamiliar {
    private IGrafoFamiliar modelo;
    private IVistaFamiliar vista;

    public ControladorFamiliar(IGrafoFamiliar modelo, IVistaFamiliar vista) {
        this.modelo = modelo;
        this.vista = vista;
    }

    public void registrarPersona(Persona p) {
        modelo.agregarPersona(p);
        vista.mostrarMensaje("Persona registrada: " + p.getNombre());
        vista.actualizarListaPersonas(modelo.obtenerTodasLasPersonas()); // Actualiza la GUI
    }

    public void registrarRelacion(Persona padre, Persona hijo) {
        try {
            modelo.agregarRelacion(padre, hijo);
            vista.mostrarMensaje("Relación agregada: " + padre.getNombre() + " es padre/madre de " + hijo.getNombre());
        } catch (Exception e) {
            vista.mostrarError(e.getMessage());
        }
    }

    public void analizarConsanguineidad(Persona p1, Persona p2) {
        Set<Persona> comunes = modelo.obtenerAncestrosComunes(p1, p2);
        vista.mostrarAncestrosComunes(p1, p2, comunes);
    }
}
