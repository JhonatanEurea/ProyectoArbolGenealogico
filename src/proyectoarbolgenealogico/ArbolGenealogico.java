package proyectoarbolgenealogico;
// Capa Modelo (Model)
import java.util.*;

public class ArbolGenealogico implements IGrafoFamiliar {
    private static ArbolGenealogico instancia;
    
    private Map<Persona, List<Persona>> descendientes;
    private Map<Persona, List<Persona>> ancestros;

    private ArbolGenealogico() {
        descendientes = new HashMap<>();
        ancestros = new HashMap<>();
    }

    // Patrón Singleton
    public static ArbolGenealogico getInstancia() {
        if (instancia == null) {
            instancia = new ArbolGenealogico();
        }
        return instancia;
    }

    @Override
    public void agregarPersona(Persona p) {
        descendientes.putIfAbsent(p, new ArrayList<>());
        ancestros.putIfAbsent(p, new ArrayList<>());
    }

    @Override
    public boolean agregarRelacion(Persona padre, Persona hijo) throws Exception {
        if (!descendientes.containsKey(padre) || !descendientes.containsKey(hijo)) {
            throw new IllegalArgumentException("Ambas personas deben existir en el árbol.");
        }

        descendientes.get(padre).add(hijo);
        ancestros.get(hijo).add(padre);

        if (detectarCiclo()) {
            descendientes.get(padre).remove(hijo);
            ancestros.get(hijo).remove(padre);
            throw new Exception("PARADOJA TEMPORAL: Agregar esta relación crea un ciclo.");
        }
        return true;
    }

    private boolean detectarCiclo() {
        Set<Persona> visitados = new HashSet<>();
        Set<Persona> pilaRecursion = new HashSet<>();

        for (Persona persona : descendientes.keySet()) {
            if (dfsCiclo(persona, visitados, pilaRecursion)) {
                return true;
            }
        }
        return false;
    }

    private boolean dfsCiclo(Persona actual, Set<Persona> visitados, Set<Persona> pilaRecursion) {
        if (pilaRecursion.contains(actual)) return true;
        if (visitados.contains(actual)) return false;

        visitados.add(actual);
        pilaRecursion.add(actual);

        for (Persona hijo : descendientes.get(actual)) {
            if (dfsCiclo(hijo, visitados, pilaRecursion)) {
                return true;
            }
        }
        pilaRecursion.remove(actual);
        return false;
    }

    @Override
    public Set<Persona> obtenerAncestros(Persona p) {
        Set<Persona> todosLosAncestros = new HashSet<>();
        Queue<Persona> cola = new LinkedList<>();
        
        cola.add(p);
        while (!cola.isEmpty()) {
            Persona actual = cola.poll();
            for (Persona padre : ancestros.getOrDefault(actual, new ArrayList<>())) {
                if (!todosLosAncestros.contains(padre)) {
                    todosLosAncestros.add(padre);
                    cola.add(padre);
                }
            }
        }
        return todosLosAncestros;
    }

    @Override
    public Set<Persona> obtenerAncestrosComunes(Persona p1, Persona p2) {
        Set<Persona> ancestrosP1 = obtenerAncestros(p1);
        Set<Persona> ancestrosP2 = obtenerAncestros(p2);
        
        ancestrosP1.retainAll(ancestrosP2);
        return ancestrosP1;
    }

    @Override
    public boolean hayConsanguineidad(Persona p1, Persona p2) {
        return !obtenerAncestrosComunes(p1, p2).isEmpty();
    }
    
    @Override
    public Set<Persona> obtenerTodasLasPersonas() {
        return descendientes.keySet();
    }
}