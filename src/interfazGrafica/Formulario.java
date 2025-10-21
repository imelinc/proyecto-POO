package interfazGrafica;

import elementos.Evento;
import recursos.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
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
    
    // Color palette matching VentanaPrincipal
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private static final Color BACKGROUND_COLOR = new Color(236, 240, 241);
    private static final Color TEXT_COLOR = new Color(44, 62, 80);
    private static final Color LABEL_COLOR = new Color(52, 73, 94);

    public Formulario(Frame ventanaPpal, String titulo, Evento evento, GestorRecursos gestorRecursos) {
        super(ventanaPpal, titulo, true);
        this.gestorRecursos = gestorRecursos;

        setSize(1200, 500);
        setLocationRelativeTo(ventanaPpal);
        getContentPane().setBackground(BACKGROUND_COLOR);

        // panel principal with improved spacing
        JPanel panelPrincipal = new JPanel(new GridLayout(1, 2, 15, 15));
        panelPrincipal.setBorder(new EmptyBorder(20, 20, 15, 20));
        panelPrincipal.setBackground(BACKGROUND_COLOR);

        // campos with better styling
        JPanel panelCampos = new JPanel();
        panelCampos.setLayout(new GridBagLayout());
        panelCampos.setBackground(Color.WHITE);
        panelCampos.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            new EmptyBorder(20, 20, 20, 20)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 8, 10, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 0;

        nombre = new JTextField();
        fechaHora = new JTextField();
        ubicacion = new JTextField();
        descripcion = new JTextArea(4, 20);
        descripcion.setLineWrap(true);
        descripcion.setWrapStyleWord(true);
        
        // Style text fields
        Font labelFont = new Font("Arial", Font.BOLD, 13);
        Font fieldFont = new Font("Arial", Font.PLAIN, 13);
        
        nombre.setFont(fieldFont);
        fechaHora.setFont(fieldFont);
        ubicacion.setFont(fieldFont);
        descripcion.setFont(fieldFont);
        
        nombre.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            new EmptyBorder(5, 8, 5, 8)
        ));
        fechaHora.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            new EmptyBorder(5, 8, 5, 8)
        ));
        ubicacion.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            new EmptyBorder(5, 8, 5, 8)
        ));

        JLabel lblNombre = new JLabel("Nombre del evento:");
        JLabel lblFecha = new JLabel("Fecha y Hora (yyyy-MM-dd HH:mm):");
        JLabel lblUbicacion = new JLabel("Ubicación:");
        JLabel lblDescripcion = new JLabel("Descripción:");
        
        lblNombre.setFont(labelFont);
        lblFecha.setFont(labelFont);
        lblUbicacion.setFont(labelFont);
        lblDescripcion.setFont(labelFont);
        
        lblNombre.setForeground(LABEL_COLOR);
        lblFecha.setForeground(LABEL_COLOR);
        lblUbicacion.setForeground(LABEL_COLOR);
        lblDescripcion.setForeground(LABEL_COLOR);

        panelCampos.add(lblNombre, gbc);
        gbc.gridy++;
        panelCampos.add(lblFecha, gbc);
        gbc.gridy++;
        panelCampos.add(lblUbicacion, gbc);
        gbc.gridy++;
        panelCampos.add(lblDescripcion, gbc);

        // entrada
        gbc.gridx = 1; gbc.gridy = 0;
        gbc.weightx = 1.0;
        panelCampos.add(nombre, gbc);
        gbc.gridy++;
        panelCampos.add(fechaHora, gbc);
        gbc.gridy++;
        panelCampos.add(ubicacion, gbc);
        gbc.gridy++;
        JScrollPane scrollDescripcion = new JScrollPane(descripcion);
        scrollDescripcion.setPreferredSize(new Dimension(200, 80));
        scrollDescripcion.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 1));
        panelCampos.add(scrollDescripcion, gbc);

        // panel de recursos with improved styling
        JPanel panelRecursos = new JPanel();
        panelRecursos.setLayout(new BoxLayout(panelRecursos, BoxLayout.Y_AXIS));
        panelRecursos.setBackground(Color.WHITE);
        panelRecursos.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                "Recursos disponibles:",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION,
                new Font("Arial", Font.BOLD, 13),
                LABEL_COLOR
            ),
            new EmptyBorder(15, 15, 15, 15)
        ));
        panelRecursos.setAlignmentY(Component.TOP_ALIGNMENT);

        for (Recurso recurso : gestorRecursos.getRecursos()) {
            JCheckBox checkBox = new JCheckBox(recurso.toString());
            checkBox.setFont(new Font("Arial", Font.PLAIN, 12));
            checkBox.setBackground(Color.WHITE);
            checkBox.setCursor(new Cursor(Cursor.HAND_CURSOR));
            checkBoxesRecursos.add(checkBox);
            checkBox.setAlignmentX(Component.LEFT_ALIGNMENT);
            panelRecursos.add(checkBox);
            panelRecursos.add(Box.createVerticalStrut(5));

            if (evento != null && evento.getRecursos().contains(recurso)) {
                checkBox.setSelected(true);
            }
        }

        JScrollPane panelRecursosScroll = new JScrollPane(panelRecursos);
        panelRecursosScroll.setPreferredSize(new Dimension(300, 300));
        panelRecursosScroll.setMinimumSize(new Dimension(200, 150));
        panelRecursosScroll.setBorder(BorderFactory.createEmptyBorder());


        panelPrincipal.add(panelCampos);
        panelPrincipal.add(panelRecursosScroll);


        JButton botonGuardar = crearBotonEstilizado("💾 Guardar", PRIMARY_COLOR);
        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        panelBoton.setBackground(BACKGROUND_COLOR);
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
    
    private JButton crearBotonEstilizado(String texto, Color colorFondo) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Arial", Font.BOLD, 14));
        boton.setBackground(colorFondo);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setOpaque(true);
        boton.setPreferredSize(new Dimension(150, 38));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add hover effect
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(colorFondo.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(colorFondo);
            }
        });
        
        return boton;
    }
}
