/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package proyectoarbolgenealogico;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Se ejecuta la GUI en el hilo de eventos de Swing para evitar cuelgues
        SwingUtilities.invokeLater(() -> {
            
            // 1. Instanciamos el Modelo (Grafo)
            ArbolGenealogico modelo = ArbolGenealogico.getInstancia();
            
            // 2. Instanciamos la Vista (Swing)
            VistaSwing vista = new VistaSwing();
            
            // 3. Instanciamos el Controlador y los vinculamos
            ControladorFamiliar controlador = new ControladorFamiliar(modelo, vista);
            vista.setControlador(controlador);
            
            // 4. Hacemos visible la ventana
            vista.setLocationRelativeTo(null); // Centrar en pantalla
            vista.setVisible(true);
        });
    }
}