package elementos;

import individuos.Asistente;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Evento {

    private String nombreDelEvento;
    private LocalDateTime fechaHora;
    private String ubicacion;
    private String descripcion;
    private ArrayList<Asistente> asistentes;

    public Evento(String nombreDelEvento, LocalDateTime fechaHora, String ubicacion, String descripcion, ArrayList<Asistente> asistentes) {
        this.nombreDelEvento = nombreDelEvento;
        this.fechaHora = fechaHora;
        this.ubicacion = ubicacion;
        this.descripcion = descripcion;
        this.asistentes = new ArrayList<>();
    }

    public String getNombreDelEvento() {
        return nombreDelEvento;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public ArrayList<Asistente> getAsistentes() {
        return asistentes;
    }

    public void agregarAsistente(Asistente asistente) {
        asistentes.add(asistente);
    }

    public boolean esPasado(){
        return fechaHora.isBefore(LocalDateTime.now());
    }

    public boolean esFuturo(){
        return fechaHora.isAfter(LocalDateTime.now());
    }

    @Override
    public String toString() {
        return "Nombre del evento='" + nombreDelEvento + '\'' + "Fecha y hora=" + fechaHora;
    }
}
