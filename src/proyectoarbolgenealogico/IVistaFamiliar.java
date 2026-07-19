package proyectoarbolgenealogico;
// Capa Vista (View)
import java.util.Set;

public interface IVistaFamiliar {
    void mostrarMensaje(String mensaje);
    void mostrarError(String error);
    void mostrarAncestrosComunes(Persona p1, Persona p2, Set<Persona> comunes);
    void actualizarListaPersonas(Set<Persona> personas);
}