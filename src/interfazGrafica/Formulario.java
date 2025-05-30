package interfazGrafica;

import elementos.Evento;
import recursos.GestorRecursos;
import recursos.Recurso;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Formulario extends JDialog {

    private JTextField nombre;
    private JTextField fechaHora; // formato: yyyy-mm-dd hh:mm
    private JTextField ubicacion;
    private JTextArea descripcion; // area para que sea mas grande
    private Evento evento1;
    private ArrayList<JCheckBox> checkBoxesRecursos = new ArrayList<>();
    private GestorRecursos gestorRecursos;

    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public Formulario(Frame ventanaPpal, String titulo, Evento evento, GestorRecursos gestorRecursos) {
        super(ventanaPpal, titulo, true);

        this.gestorRecursos = gestorRecursos;

        setSize(400, 300);
        setLayout(new GridLayout(5,2));
        setLocationRelativeTo(ventanaPpal);

        nombre = new JTextField();
        fechaHora = new JTextField();
        ubicacion = new JTextField();
        descripcion = new JTextArea();



        add(new JLabel("Nombre del evento: "));
        add(nombre);
        add(new JLabel("Fecha y Hora:"));
        add(fechaHora);
        add(new JLabel("Ubicacion: "));
        add(ubicacion);
        add(new JLabel("Descripcion: "));
        add(descripcion);

        // Panel de recursos (usamos GridLayout para ordenarlos)
        JPanel panelRecursos = new JPanel();
        panelRecursos.setLayout(new BoxLayout(panelRecursos, BoxLayout.Y_AXIS));
        panelRecursos.setBorder(BorderFactory.createTitledBorder("Recursos disponibles:"));

        for (Recurso recurso : gestorRecursos.getRecursos()) {
            JCheckBox checkBox = new JCheckBox(recurso.toString());
            checkBoxesRecursos.add(checkBox);
            panelRecursos.add(checkBox);

            if (evento != null && evento.getRecursos().contains(recurso)) {
                checkBox.setSelected(true);
            }
        }

        JScrollPane scrollRecursos = new JScrollPane(panelRecursos);
        scrollRecursos.setPreferredSize(new Dimension(420, 100));
        add(scrollRecursos, BorderLayout.CENTER);

        JButton botonGuardar = new JButton("Guardar");
        JPanel panelBoton = new JPanel();
        panelBoton.add(botonGuardar);
        add(panelBoton, BorderLayout.SOUTH);

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

                evento1 = new Evento(tituloEvento, fechaHoraEvento, ubicacionEvento, descripcionEvento);

                // Asignamos los recursos
                ArrayList<Recurso> recursosAsignados = new ArrayList<>();
                for (int i = 0; i < checkBoxesRecursos.size(); i++) {
                    if (checkBoxesRecursos.get(i).isSelected()) {
                        recursosAsignados.add(gestorRecursos.getRecursos().get(i));
                    }
                }
                evento1.setRecursos(recursosAsignados);

                dispose(); // cerramos el formulario
            } catch (Exception err) {
                JOptionPane.showMessageDialog(this, "Error en los datos: " + err.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        setVisible(true);

    }

    public Evento getEvento() {
        return evento1;
    }

}
