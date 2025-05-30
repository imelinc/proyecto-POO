package elementos;

import individuos.Asistente;
import recursos.Recurso;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Evento {

    private String nombreDelEvento;
    private LocalDateTime fechaHora;
    private String ubicacion;
    private String descripcion;
    private ArrayList<Asistente> asistentes;
    private ArrayList<Recurso> recursos;

    public Evento(String nombreDelEvento, LocalDateTime fechaHora, String ubicacion, String descripcion) {
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

    public void setRecursos(ArrayList<Recurso> recursos) {
        this.recursos = recursos;
    }

    public ArrayList<Recurso> getRecursos() {
        return recursos;
    }

    public void agregarAsistente(Asistente asistente) {
        asistentes.add(asistente);
    }

    public void agregarRecurso(Recurso recurso) {
        recursos.add(recurso);
    }

    public boolean esPasado(){
        return fechaHora.isBefore(LocalDateTime.now());
    }

    public boolean esFuturo(){
        return fechaHora.isAfter(LocalDateTime.now());
    }

    @Override
    public String toString() {
        return nombreDelEvento + " - " + fechaHora.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }
}
