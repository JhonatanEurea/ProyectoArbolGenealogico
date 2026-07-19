package proyectoarbolgenealogico;

import java.util.Set;

public interface IGrafoFamiliar {
    void agregarPersona(Persona p);
    boolean agregarRelacion(Persona padre, Persona hijo) throws Exception;
    Set<Persona> obtenerAncestros(Persona p);
    Set<Persona> obtenerAncestrosComunes(Persona p1, Persona p2);
    boolean hayConsanguineidad(Persona p1, Persona p2);
    Set<Persona> obtenerTodasLasPersonas();
}