import elementos.*;
import interfazGrafica.VentanaPrincipal;
import recursos.GestorRecursos;

import javax.swing.*;
import java.time.LocalDateTime;


public class Main {
    public static void main(String[] args) {

        GestorRecursos gestorRecursos = new GestorRecursos();
        gestorRecursos.cargarDesdeArchivo("recursos.txt");
        GestorEventos gestorEventos = new GestorEventos(gestorRecursos);
        gestorEventos.cargarDesdeArchivo("eventos.txt");
        new VentanaPrincipal(gestorEventos);
    }
}