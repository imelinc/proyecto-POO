package elementos;

import individuos.Asistente;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class GestorEventos {

    private ArrayList<Evento> eventos;
    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public GestorEventos() {
        eventos = new ArrayList<>();
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
            }
        } catch (IOException e) {
            System.err.println("Error al guardar archivo: " + e.getMessage());
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
            }
        } catch (FileNotFoundException e) {
            System.out.println("Archivo no encontrado.");
        } catch (IOException e) {
            System.err.println("Error al leer archivo: " + e.getMessage());
        }
    }
}
