package recursos;

public class Salon extends Recurso {
    private int capacidad;

    public Salon(String nombre, String descripcion, int capacidad) {
        super(nombre, descripcion);
        this.capacidad = capacidad;
    }
    public int getCapacidad() { return capacidad; }
    @Override
    public String toString() {
        return super.toString() + " (Capacidad: " + capacidad + ")";
    }
}
