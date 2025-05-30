package interfazGrafica;

import elementos.Evento;
import recursos.*;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Formulario extends JDialog {

    private JTextField nombre;
    private JTextField fechaHora;
    private JTextField ubicacion;
    private JTextArea descripcion;
    private Evento evento1;
    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private ArrayList<JCheckBox> checkBoxesRecursos = new ArrayList<>();
    private GestorRecursos gestorRecursos;

    public Formulario(Frame ventanaPpal, String titulo, Evento evento, GestorRecursos gestorRecursos) {
        super(ventanaPpal, titulo, true);
        this.gestorRecursos = gestorRecursos;

        setSize(1200, 400);
        setLocationRelativeTo(ventanaPpal);

        // panel principal
        JPanel panelPrincipal = new JPanel(new GridLayout(1, 2, 10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 10, 15));

        // campos
        JPanel panelCampos = new JPanel();
        panelCampos.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 4, 8, 4);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 0;

        nombre = new JTextField();
        fechaHora = new JTextField();
        ubicacion = new JTextField();
        descripcion = new JTextArea(3, 18);

        panelCampos.add(new JLabel("Nombre del evento: "), gbc);
        gbc.gridy++;
        panelCampos.add(new JLabel("Fecha y Hora (yyyy-MM-dd HH:mm):"), gbc);
        gbc.gridy++;
        panelCampos.add(new JLabel("Ubicación: "), gbc);
        gbc.gridy++;
        panelCampos.add(new JLabel("Descripción: "), gbc);

        // entrada
        gbc.gridx = 1; gbc.gridy = 0;
        panelCampos.add(nombre, gbc);
        gbc.gridy++;
        panelCampos.add(fechaHora, gbc);
        gbc.gridy++;
        panelCampos.add(ubicacion, gbc);
        gbc.gridy++;
        JScrollPane scrollDescripcion = new JScrollPane(descripcion);
        scrollDescripcion.setPreferredSize(new Dimension(180, 50));
        panelCampos.add(scrollDescripcion, gbc);

        // panel de recursos
        JPanel panelRecursos = new JPanel();
        panelRecursos.setLayout(new BoxLayout(panelRecursos, BoxLayout.Y_AXIS));
        panelRecursos.setBorder(BorderFactory.createTitledBorder("Recursos disponibles:"));
        panelRecursos.setAlignmentY(Component.TOP_ALIGNMENT);

        // Margen interno
        panelRecursos.setBorder(BorderFactory.createCompoundBorder(
                panelRecursos.getBorder(),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        for (Recurso recurso : gestorRecursos.getRecursos()) {
            JCheckBox checkBox = new JCheckBox(recurso.toString());
            checkBoxesRecursos.add(checkBox);
            checkBox.setAlignmentX(Component.LEFT_ALIGNMENT);
            panelRecursos.add(checkBox);

            if (evento != null && evento.getRecursos().contains(recurso)) {
                checkBox.setSelected(true);
            }
        }

        JScrollPane panelRecursosScroll = new JScrollPane(panelRecursos);
        panelRecursosScroll.setPreferredSize(new Dimension(260, 250));
        panelRecursosScroll.setMinimumSize(new Dimension(180, 100));


        panelPrincipal.add(panelCampos);
        panelPrincipal.add(panelRecursosScroll);


        JButton botonGuardar = new JButton("Guardar");
        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBoton.add(botonGuardar);


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

                ArrayList<Recurso> recursosAsignados = new ArrayList<>();
                for (int i = 0; i < checkBoxesRecursos.size(); i++) {
                    if (checkBoxesRecursos.get(i).isSelected()) {
                        recursosAsignados.add(gestorRecursos.getRecursos().get(i));
                    }
                }
                evento1.setRecursos(recursosAsignados);

                dispose();
            } catch (Exception err) {
                JOptionPane.showMessageDialog(this, "Error en los datos: " + err.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });


        setLayout(new BorderLayout());
        add(panelPrincipal, BorderLayout.CENTER);
        add(panelBoton, BorderLayout.SOUTH);

        setVisible(true);
    }

    public Evento getEvento() {
        return evento1;
    }
}
