package interfazGrafica;

import elementos.Evento;
import elementos.GestorEventos;
import individuos.Asistente;
import recursos.Recurso;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class DetalleEvento extends JDialog {
    
    // Color palette matching VentanaPrincipal
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private static final Color ACCENT_COLOR = new Color(26, 188, 156);
    private static final Color BACKGROUND_COLOR = new Color(236, 240, 241);
    private static final Color TEXT_COLOR = new Color(44, 62, 80);
    private static final Color HEADER_COLOR = new Color(52, 73, 94);

    public DetalleEvento(Frame ventanaPpal, Evento evento, GestorEventos gestor) {
        super(ventanaPpal, "Detalle del Evento", true);
        setSize(600, 550);
        setLayout(new BorderLayout(10, 10));
        setLocationRelativeTo(ventanaPpal);
        getContentPane().setBackground(BACKGROUND_COLOR);

        // Main content panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Title section
        JLabel titleLabel = new JLabel(evento.getNombreDelEvento());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(PRIMARY_COLOR);
        titleLabel.setBorder(new EmptyBorder(0, 0, 15, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Details area with improved formatting
        JTextArea areaDetalleEvento = new JTextArea();
        areaDetalleEvento.setEditable(false);
        areaDetalleEvento.setFont(new Font("Arial", Font.PLAIN, 13));
        areaDetalleEvento.setBackground(Color.WHITE);
        areaDetalleEvento.setForeground(TEXT_COLOR);
        areaDetalleEvento.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        StringBuilder detalles = new StringBuilder();
        detalles.append("📅 Fecha y Hora:\n   ").append(evento.getFechaHora()).append("\n\n");
        detalles.append("📍 Ubicación:\n   ").append(evento.getUbicacion()).append("\n\n");
        detalles.append("📝 Descripción:\n   ").append(evento.getDescripcion()).append("\n\n");
        
        detalles.append("🛠️ Recursos asignados:\n");
        if (evento.getRecursos().isEmpty()) {
            detalles.append("   Ninguno\n\n");
        } else {
            for (Recurso r : evento.getRecursos()) {
                detalles.append("   • ").append(r.toString()).append("\n");
            }
            detalles.append("\n");
        }
        
        detalles.append("👥 Asistentes:\n");
        if (evento.getAsistentes().isEmpty()) {
            detalles.append("   Ninguno\n");
        } else {
            for (Asistente asistente : evento.getAsistentes()) {
                detalles.append("   • ").append(asistente.getNombre()).append("\n");
            }
        }
        
        areaDetalleEvento.setText(detalles.toString());

        JScrollPane scrollPane = new JScrollPane(areaDetalleEvento);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 1));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.setBorder(new EmptyBorder(10, 20, 15, 20));

        JButton botonAgregarAsistente = crearBotonEstilizado("+ Agregar Asistente", ACCENT_COLOR);
        buttonPanel.add(botonAgregarAsistente);

        botonAgregarAsistente.addActionListener(e -> {
            String nombre = JOptionPane.showInputDialog(this, "Ingrese el nombre del asistente:", 
                "Agregar Asistente", JOptionPane.QUESTION_MESSAGE);
            if (nombre != null && !nombre.trim().isEmpty()) {
                String email = JOptionPane.showInputDialog(this, "Ingrese el email del asistente:", 
                    "Agregar Asistente", JOptionPane.QUESTION_MESSAGE);
                if (email != null && !email.trim().isEmpty()) {
                    evento.agregarAsistente(new Asistente(nombre, email));
                    gestor.guardarEnArchivo("eventos.txt");
                    
                    // Update the display
                    String currentText = areaDetalleEvento.getText();
                    if (currentText.contains("Ninguno")) {
                        currentText = currentText.replace("👥 Asistentes:\n   Ninguno", 
                            "👥 Asistentes:\n   • " + nombre);
                    } else {
                        currentText = currentText + "   • " + nombre + "\n";
                    }
                    areaDetalleEvento.setText(currentText);
                    
                    JOptionPane.showMessageDialog(this, "Asistente agregado correctamente", 
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        // Wrapper for top padding
        JPanel wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.setBackground(BACKGROUND_COLOR);
        wrapperPanel.setBorder(new EmptyBorder(15, 15, 0, 15));
        wrapperPanel.add(mainPanel, BorderLayout.CENTER);

        add(wrapperPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }
    
    private JButton crearBotonEstilizado(String texto, Color colorFondo) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Arial", Font.BOLD, 13));
        boton.setBackground(colorFondo);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setOpaque(true);
        boton.setPreferredSize(new Dimension(180, 38));
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
