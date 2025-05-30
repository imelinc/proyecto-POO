package interfazGrafica;

import elementos.Evento;
import elementos.GestorEventos;
import recursos.GestorRecursos;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class VentanaPrincipal extends JFrame {

    private GestorEventos gestor;
    private DefaultListModel<Evento> listModel;
    private JList<Evento> listaEventos;
    private JComboBox<String> filtroComboBox;
    private static final String[] FILTROS = {"Todos", "Futuros", "Pasados"}; // para ver los eventos futuros o pasados
    private GestorRecursos gestorRecursos;

    public VentanaPrincipal(GestorEventos gestor, GestorRecursos gestorRecursos) {

        this.gestor = gestor;
        this.gestorRecursos = gestorRecursos;

        setTitle("Gestor de Eventos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

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
                listModel.addElement(nuevo);
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

        filtroComboBox.addActionListener(e -> {
            listModel.clear();
            String filtro = (String) filtroComboBox.getSelectedItem();
            for (Evento ev : gestor.getEventos()) {
                if ("Futuros".equals(filtro) && !ev.esFuturo()) continue;
                if ("Pasados".equals(filtro) && !ev.esPasado()) continue;
                listModel.addElement(ev);
            }
        });

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                gestor.guardarEnArchivo("eventos.txt");
                dispose();
            }
        });
        setVisible(true);
    }
}
