package interfazGrafica;

import elementos.Evento;
import elementos.GestorEventos;
import individuos.Asistente;
import recursos.Recurso;

import javax.swing.*;
import java.awt.*;

public class DetalleEvento extends JDialog {

    public DetalleEvento(Frame ventanaPpal, Evento evento, GestorEventos gestor) {
        super(ventanaPpal, "Detalle del Evento", true);
        setSize(500, 400);
        setLayout(new BorderLayout());
        setLocationRelativeTo(ventanaPpal);

        JTextArea areaDetalleEvento = new JTextArea();
        areaDetalleEvento.setEditable(false);
        areaDetalleEvento.setText("Nombre del evento: " + evento.getNombreDelEvento() + "\n" +
                                  "Fecha y Hora del evento: " + evento.getFechaHora() + "\n" +
                                  "Ubicacion del evento: " + evento.getUbicacion() + "\n" +
                                  "Descripcion del evento: " + evento.getDescripcion() + "\n" +
                                  "Asistentes: \n");

        for (Asistente asistente : evento.getAsistentes()) {
            areaDetalleEvento.append("- " + asistente.getNombre() + "\n");
        }

        JButton botonAgregarAsistente = new JButton("Agregar Asistente");
        botonAgregarAsistente.addActionListener(e -> {
            String nombre = JOptionPane.showInputDialog("Ingrese el nombre del asistente:");
            String email = JOptionPane.showInputDialog("Ingrese el email del asistente:");
            if (nombre != null && email != null) {
                evento.agregarAsistente(new Asistente(nombre, email));
                areaDetalleEvento.append("- " + nombre + "\n");
                gestor.guardarEnArchivo("eventos.txt");
            }
        });

        areaDetalleEvento.setText("Nombre del evento: " + evento.getNombreDelEvento() + "\n" +
                "Fecha y Hora del evento: " + evento.getFechaHora() + "\n" +
                "Ubicacion del evento: " + evento.getUbicacion() + "\n" +
                "Descripcion del evento: " + evento.getDescripcion() + "\n" +
                "Recursos asignados:\n");

        for (Recurso r : evento.getRecursos()) {
            areaDetalleEvento.append("- " + r.toString() + "\n");
        }
        areaDetalleEvento.append("Asistentes: \n");
        for (Asistente asistente : evento.getAsistentes()) {
            areaDetalleEvento.append("- " + asistente.getNombre() + "\n");
        }

        add(new JScrollPane(areaDetalleEvento), BorderLayout.CENTER);
        add(botonAgregarAsistente, BorderLayout.SOUTH);

        setVisible(true);
    }

}
