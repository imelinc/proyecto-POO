package elementos;

import individuos.Asistente;
import recursos.GestorRecursos;
import recursos.Recurso;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class GestorEventos {

    private ArrayList<Evento> eventos;
    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private GestorRecursos gestorRecursos;

    public GestorEventos(GestorRecursos gestorRecursos) {
        eventos = new ArrayList<>();
        this.gestorRecursos = gestorRecursos;
    }

    public ArrayList<Evento> getEventos() {
        return eventos;
    }

    public void agregarEvento(Evento evento) {
        eventos.add(evento);
    }

    public void actualizarEvento(int index, Evento evento) {
        eventos.set(index, evento);
    }

    public Evento getEvento(int index) {
        return eventos.get(index);
    }

    public void eliminarEvento(int index) {
        eventos.remove(index);
    }

    // Persistencia con el archivo

    public void guardarEnArchivo(String rutaArchivo) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(rutaArchivo))) {
            for (Evento e : eventos) {
                writer.write(e.getNombreDelEvento());
                writer.newLine();
                writer.write(e.getFechaHora().format(FORMATO));
                writer.newLine();
                writer.write(e.getUbicacion());
                writer.newLine();
                writer.write(e.getDescripcion());
                writer.newLine();
                writer.write(String.valueOf(e.getAsistentes().size()));
                writer.newLine();
                for (Asistente a : e.getAsistentes()) {
                    writer.write(a.getNombre());
                    writer.newLine();
                    writer.write(a.getEmail());
                    writer.newLine();
                }

                // Manejo el tema de los recursos
                writer.write(String.valueOf(e.getRecursos().size()));
                writer.newLine();
                for (Recurso rec : e.getRecursos()) {
                    writer.write(rec.getNombre());
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("Error al guardar archivo");
        }
    }

    public void cargarDesdeArchivo(String rutaArchivo) {
        eventos.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String tituloEvento = linea;
                LocalDateTime fechaHora = LocalDateTime.parse(reader.readLine(), FORMATO);
                String ubicacion = reader.readLine();
                String descripcion = reader.readLine();
                int cantidadAsistentes = Integer.parseInt(reader.readLine());
                Evento evento = new Evento(tituloEvento, fechaHora, ubicacion, descripcion);
                for (int i = 0; i < cantidadAsistentes; i++) {
                    String nombre = reader.readLine();
                    String email = reader.readLine();
                    evento.agregarAsistente(new Asistente(nombre, email));
                }
                eventos.add(evento);

                // manejo el tema de los recursos
                int cantidadRecursos = Integer.parseInt(reader.readLine());
                ArrayList<Recurso> recursosAsignados = new ArrayList<>();
                for (int i = 0; i < cantidadRecursos; i++) {
                    String nombre = reader.readLine();
                    for (Recurso rec : gestorRecursos.getRecursos()) {
                        if (rec.getNombre().equals(nombre)) {
                            recursosAsignados.add(rec);
                            break;
                        }
                    }
                }
                evento.setRecursos(recursosAsignados);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Archivo no encontrado.");
        } catch (IOException e) {
            System.err.println("Error al leer archivo");
        }
    }
}
