package elementos;

import java.util.ArrayList;
import java.util.List;

public class GestorEventos {

    private List<Evento> listaDeEventos;

    public GestorEventos() {
        this.listaDeEventos = new ArrayList<Evento>();
    }

    public List<Evento> getListaDeEventos() {
        return listaDeEventos;
    }

    public void agregarEvento(Evento evento) {
        listaDeEventos.add(evento);
    }

}
