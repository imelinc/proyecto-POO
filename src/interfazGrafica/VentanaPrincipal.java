package interfazGrafica;

import elementos.Evento;
import elementos.GestorEventos;
import recursos.GestorRecursos;
import com.toedter.calendar.JCalendar; // esto lo tuve que bajar de internet como.jar para poder implementar el calendario

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;
import java.util.Date;

public class VentanaPrincipal extends JFrame {

    private GestorEventos gestor;
    private DefaultListModel<Evento> listModel;
    private JList<Evento> listaEventos;
    private JComboBox<String> filtroComboBox;
    private static final String[] FILTROS = {"Filtrar por día", "Ver todos los días", "Futuros", "Pasados"}; // para ver los eventos futuros o pasados
    private GestorRecursos gestorRecursos;
    private JCalendar calendario;
    
    // Color palette for modern UI
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private static final Color SECONDARY_COLOR = new Color(52, 152, 219);
    private static final Color BACKGROUND_COLOR = new Color(236, 240, 241);
    private static final Color ACCENT_COLOR = new Color(26, 188, 156);
    private static final Color TEXT_COLOR = new Color(44, 62, 80);
    private static final Color BUTTON_HOVER = new Color(31, 97, 141);

    public VentanaPrincipal(GestorEventos gestor, GestorRecursos gestorRecursos) {

        this.gestor = gestor;
        this.gestorRecursos = gestorRecursos;

        setTitle("Gestor de Eventos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 650);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        
        // Set background color
        getContentPane().setBackground(BACKGROUND_COLOR);

        // calendario with styling
        calendario = new JCalendar();
        calendario.setBackground(Color.WHITE);
        JPanel panelIzquierdo = new JPanel(new BorderLayout(5, 5));
        panelIzquierdo.setBackground(BACKGROUND_COLOR);
        panelIzquierdo.setBorder(new EmptyBorder(15, 15, 15, 10));
        
        JLabel tituloCalendario = new JLabel("Calendario");
        tituloCalendario.setFont(new Font("Arial", Font.BOLD, 16));
        tituloCalendario.setForeground(TEXT_COLOR);
        tituloCalendario.setBorder(new EmptyBorder(0, 0, 10, 0));
        panelIzquierdo.add(tituloCalendario, BorderLayout.NORTH);
        panelIzquierdo.add(calendario, BorderLayout.CENTER);
        add(panelIzquierdo, BorderLayout.WEST);

        // Panel superior with improved styling
        JPanel panelSuperior = new JPanel(new BorderLayout(5, 5));
        panelSuperior.setBackground(BACKGROUND_COLOR);
        panelSuperior.setBorder(new EmptyBorder(15, 10, 15, 15));

        // Filter section with label
        JPanel panelFiltro = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panelFiltro.setBackground(BACKGROUND_COLOR);
        JLabel labelFiltro = new JLabel("Filtrar eventos:");
        labelFiltro.setFont(new Font("Arial", Font.BOLD, 14));
        labelFiltro.setForeground(TEXT_COLOR);
        panelFiltro.add(labelFiltro);
        
        filtroComboBox = new JComboBox<>(FILTROS);
        filtroComboBox.setFont(new Font("Arial", Font.PLAIN, 13));
        filtroComboBox.setBackground(Color.WHITE);
        filtroComboBox.setPreferredSize(new Dimension(200, 30));
        panelFiltro.add(filtroComboBox);
        panelSuperior.add(panelFiltro, BorderLayout.NORTH);

        listModel = new DefaultListModel<>();
        listaEventos = new JList<>(listModel);
        listaEventos.setFont(new Font("Arial", Font.PLAIN, 13));
        listaEventos.setBackground(Color.WHITE);
        listaEventos.setSelectionBackground(SECONDARY_COLOR);
        listaEventos.setSelectionForeground(Color.WHITE);
        listaEventos.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 1));
        listaEventos.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, 
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setBorder(new EmptyBorder(8, 10, 8, 10));
                if (!isSelected) {
                    setBackground(index % 2 == 0 ? Color.WHITE : new Color(249, 249, 249));
                }
                return this;
            }
        });
        
        JScrollPane jScrollPane = new JScrollPane(listaEventos);
        jScrollPane.setBorder(BorderFactory.createEmptyBorder());
        panelSuperior.add(jScrollPane, BorderLayout.CENTER);

        add(panelSuperior, BorderLayout.CENTER);

        // cargo los eventos
        for (Evento e : gestor.getEventos()) {
            listModel.addElement(e);
        }

        notificarEventosProximos(); // si llega a haber un evento al otro dia llamar a la ventana

        // botones en el panel de abajo with improved styling
        JPanel panelDeBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        panelDeBotones.setBackground(BACKGROUND_COLOR);
        panelDeBotones.setBorder(new EmptyBorder(10, 15, 15, 15));

        JButton agregarBoton = crearBotonEstilizado("+ Agregar Evento", PRIMARY_COLOR);
        JButton editarBoton = crearBotonEstilizado("✎ Editar Evento", ACCENT_COLOR);
        JButton detalleBoton = crearBotonEstilizado("🔍 Ver Detalle", SECONDARY_COLOR);

        panelDeBotones.add(agregarBoton);
        panelDeBotones.add(editarBoton);
        panelDeBotones.add(detalleBoton);

        add(panelDeBotones, BorderLayout.SOUTH); // abajo

        // acciones de los botones
        agregarBoton.addActionListener(e -> {
            Formulario formulario = new Formulario(this, "Agregar Eventos", null, gestorRecursos);
            Evento nuevo = formulario.getEvento();
            if (nuevo != null) {
                gestor.agregarEvento(nuevo);

                LocalDate fechaEvento = nuevo.getFechaHora().toLocalDate();
                LocalDate fechaSeleccionada = calendario.getDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
                if (!fechaEvento.equals(fechaSeleccionada)) {
                    calendario.setDate(java.util.Date.from(
                            fechaEvento.atStartOfDay(java.time.ZoneId.systemDefault()).toInstant()
                    ));
                }
                actualizarListaEventosPorFecha();
            }
        });


        editarBoton.addActionListener(e -> {
           Evento nuevo = listaEventos.getSelectedValue();
           if (nuevo != null) {
               int index = listaEventos.getSelectedIndex();
               Formulario formulario = new Formulario(this, "Editar Eventos", nuevo, gestorRecursos);
               Evento eventoModif = formulario.getEvento();
               if (eventoModif != null) {
                   gestor.actualizarEvento(index,eventoModif);
                   listModel.set(index, eventoModif);
               }
           } else {
               JOptionPane.showMessageDialog(this, "Seleccione un evento para editar");
           }
        });

        detalleBoton.addActionListener(e -> {
            Evento nuevo = listaEventos.getSelectedValue();
            if (nuevo != null) {
                new DetalleEvento(this, nuevo, gestor);
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione un evento para ver los detalles");
            }
        });

        filtroComboBox.addActionListener(e -> actualizarListaEventosPorFecha());
        calendario.addPropertyChangeListener("date", evt -> actualizarListaEventosPorFecha());
        calendario.getDayChooser().addPropertyChangeListener("day", evt -> actualizarListaEventosPorFecha());

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                gestor.guardarEnArchivo("eventos.txt");
                dispose();
            }
        });
        setVisible(true);
    }
    
    private JButton crearBotonEstilizado(String texto, Color colorFondo) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Arial", Font.BOLD, 14));
        boton.setBackground(colorFondo);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setOpaque(true);
        boton.setPreferredSize(new Dimension(180, 40));
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

    private void actualizarListaEventosPorFecha() {
        listModel.clear();
        boolean hayEventos = false;
        String filtro = (String) filtroComboBox.getSelectedItem();

        // solo si se filtra por día
        Date fecha = calendario.getDate();
        LocalDate fechaSeleccionada = fecha.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();

        for (Evento ev : gestor.getEventos()) {
            LocalDate fechaEvento = ev.getFechaHora().toLocalDate();

            if ("Filtrar por día".equals(filtro)) {
                if (!fechaEvento.equals(fechaSeleccionada)) continue;
            }
            if ("Futuros".equals(filtro) && !ev.esFuturo()) continue;
            if ("Pasados".equals(filtro) && !ev.esPasado()) continue;
            listModel.addElement(ev);
            hayEventos = true;
        }
        if (!hayEventos) {
            listModel.addElement(new Evento("No hay eventos para esta seleccion", fechaSeleccionada.atStartOfDay(), "", ""));
        }
    }

    private void notificarEventosProximos() {
        StringBuilder sb = new StringBuilder();
        for (Evento evento : gestor.getEventos()) {
            long horas = java.time.LocalDateTime.now().until(evento.getFechaHora(), java.time.temporal.ChronoUnit.HOURS);
            if (horas > 0 && horas <= 24) {
                sb.append("Evento: ").append(evento.getNombreDelEvento())
                        .append("\nFecha y hora: ").append(evento.getFechaHora())
                        .append("\nUbicación: ").append(evento.getUbicacion())
                        .append("\nAsistentes: ");
                if (evento.getAsistentes().isEmpty()) {
                    sb.append("Ninguno\n\n");
                } else {
                    for (individuos.Asistente asistente : evento.getAsistentes()) {
                        sb.append(asistente.getNombre()).append(" (").append(asistente.getEmail()).append("), ");
                    }
                    sb.setLength(sb.length() - 2);
                    sb.append("\n\n");
                }
            }
        }
        if (sb.length() > 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "¡Tienes eventos próximos en las próximas 24 horas!\n\n" + sb,
                    "Recordatorio de Eventos",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }


}
