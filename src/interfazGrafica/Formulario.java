package interfazGrafica;

import elementos.Evento;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Formulario extends JDialog {

    private JTextField nombre;
    private JTextField fechaHora; // formato: yyyy-mm-dd hh:mm
    private JTextField ubicacion;
    private JTextArea descripcion; // area para que sea mas grande
    private Evento evento;

    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public Formulario(Frame ventanaPpal, String titulo, Evento evento) {
        super(ventanaPpal, titulo, true);

        setSize(400, 300);
        setLayout(new GridLayout(5,2));
        setLocationRelativeTo(ventanaPpal);

        nombre = new JTextField();
        fechaHora = new JTextField();
        ubicacion = new JTextField();
        descripcion = new JTextArea();

        JButton botonGuardar = new JButton("Guardar");

        add(new JLabel("Nombre del evento: "));
        add(nombre);
        add(new JLabel("Fecha y Hora:"));
        add(fechaHora);
        add(new JLabel("Ubicacion: "));
        add(ubicacion);
        add(new JLabel("Descripcion: "));
        add(descripcion);

        add(botonGuardar);

        if (evento != null) {
            nombre.setText(evento.getNombreDelEvento());
            fechaHora.setText(FORMATO.format(evento.getFechaHora()));
            ubicacion.setText(evento.getUbicacion());
            descripcion.setText(evento.getDescripcion());
        }

        botonGuardar.addActionListener(e -> {
            try {
                String tituloEvento = nombre.getText();
                LocalDateTime fechaHoraEvento = LocalDateTime.parse(fechaHora.getText(), FORMATO);
                String ubicacionEvento = ubicacion.getText();
                String descripcionEvento = descripcion.getText();

                Evento eventoNuevo = new Evento(tituloEvento, fechaHoraEvento, ubicacionEvento, descripcionEvento);
                dispose();
            } catch (Exception err) {
                JOptionPane.showMessageDialog(this, err.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        setVisible(true);

    }

    public Evento getEvento() {
        return evento;
    }

}
