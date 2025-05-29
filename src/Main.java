import elementos.*;
import interfazGrafica.VentanaPrincipal;

import javax.swing.*;
import java.time.LocalDateTime;


public class Main {
    public static void main(String[] args) {

        GestorEventos gestorEventos = new GestorEventos();
        gestorEventos.cargarDesdeArchivo("eventos.txt");
        new VentanaPrincipal(gestorEventos);
    }
}