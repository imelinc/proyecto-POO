package interfazGrafica;

import elementos.Evento;
import elementos.GestorEventos;

import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {

    private GestorEventos gestor;
    private DefaultListModel<Evento> listModel;
    private JList<Evento> listaEventos;

    public VentanaPrincipal() {

        setTitle("Gestor de Eventos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Panel de arriba
        listModel = new DefaultListModel<>();
        listaEventos = new JList<>(listModel);
        JScrollPane jScrollPane = new JScrollPane(listaEventos);
        add(jScrollPane, BorderLayout.CENTER); // centro/arriba

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
            Formulario formulario = new Formulario();
        })
    }
}
