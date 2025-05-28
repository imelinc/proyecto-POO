package elementos;

import individuos.Asistente;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Evento {

    private String tipoEvento;
    private String nombreDelEvento;
    private Date fechaEvento;
    private String ubicacionDelEvento;
    private String descripcionDelEvento;
    private Time horaEvento;
    private List<Asistente> asistentesDelEvento;

    GestorEventos gestorEventos = new GestorEventos();

    public Evento(String tipoEvento, String nombreDelEvento, Date fechaEvento, String ubicacionDelEvento, String descripcionDelEvento, Time horaEvento) {
        this.tipoEvento = tipoEvento;
        this.nombreDelEvento = nombreDelEvento;
        this.fechaEvento = fechaEvento;
        this.ubicacionDelEvento = ubicacionDelEvento;
        this.descripcionDelEvento = descripcionDelEvento;
        this.horaEvento = horaEvento;
        this.asistentesDelEvento = new ArrayList<Asistente>();
        gestorEventos.agregarEvento(this); // Cuando creo un evento, automaticamente se agrega a la lista de eventos
    }

    public String getTipoEvento() {
        return tipoEvento;
    }

    public String getNombreDelEvento() {
        return nombreDelEvento;
    }

    public Date getFechaEvento() {
        return fechaEvento;
    }

    public String getUbicacionDelEvento() {
        return ubicacionDelEvento;
    }

    public String getDescripcionDelEvento() {
        return descripcionDelEvento;
    }

    public Time getHoraEvento() {
        return horaEvento;
    }

    public List<Asistente> getAsistentesDelEvento() {
        return asistentesDelEvento;
    }

}
