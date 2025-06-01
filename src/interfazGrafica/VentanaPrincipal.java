package interfazGrafica;

import elementos.Evento;
import elementos.GestorEventos;
import recursos.GestorRecursos;
import com.toedter.calendar.JCalendar; // esto lo tuve que bajar de internet como.jar para poder implementar el calendario

import javax.swing.*;
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

    public VentanaPrincipal(GestorEventos gestor, GestorRecursos gestorRecursos) {

        this.gestor = gestor;
        this.gestorRecursos = gestorRecursos;

        setTitle("Gestor de Eventos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // calendario
        calendario = new JCalendar();
        JPanel panelIzquierdo = new JPanel(new BorderLayout());
        panelIzquierdo.add(calendario, BorderLayout.NORTH);
        add(panelIzquierdo, BorderLayout.WEST);

        // Panel superior
        JPanel panelSuperior = new JPanel(new BorderLayout());

        filtroComboBox = new JComboBox<>(FILTROS);
        panelSuperior.add(filtroComboBox, BorderLayout.NORTH);

        listModel = new DefaultListModel<>();
        listaEventos = new JList<>(listModel);
        JScrollPane jScrollPane = new JScrollPane(listaEventos);
        panelSuperior.add(jScrollPane, BorderLayout.CENTER);

        add(panelSuperior, BorderLayout.CENTER);

        // cargo los eventos
        for (Evento e : gestor.getEventos()) {
            listModel.addElement(e);
        }

        notificarEventosProximos(); // si llega a haber un evento al otro dia llamar a la ventana

        // botones en el panel de abajo
        JPanel panelDeBotones = new JPanel();

        JButton agregarBoton = new JButton("Agregar Evento");
        JButton editarBoton = new JButton("Editar Evento");
        JButton detalleBoton = new JButton("Ver Detalle Evento");

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
