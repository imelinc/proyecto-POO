import elementos.*;
import interfazGrafica.VentanaPrincipal;
import recursos.GestorRecursos;

public class Main {
    public static void main(String[] args) {

        GestorRecursos gestorRecursos = new GestorRecursos();
        gestorRecursos.cargarDesdeArchivo("recursos.txt");
        GestorEventos gestorEventos = new GestorEventos(gestorRecursos);
        gestorEventos.cargarDesdeArchivo("eventos.txt");
        new VentanaPrincipal(gestorEventos, gestorRecursos);

    }
}