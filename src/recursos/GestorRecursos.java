package recursos;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class GestorRecursos {

    private ArrayList<Recurso> recursos;

    public GestorRecursos() {
        recursos = new ArrayList<>();
    }

    public ArrayList<Recurso> getRecursos() {
        return recursos;
    }

    public void cargarDesdeArchivo(String archivo) {
        recursos.clear();
        try(BufferedReader br = new BufferedReader(new FileReader(archivo)))  {

            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(";");
                String tipo = partes[0];
                String nombre = partes[1];
                String descripcion = partes[2];
                if (tipo.equals("Salon")) {
                    int capacidad = Integer.parseInt(partes[3]);
                    recursos.add(new Salon(nombre, descripcion, capacidad));
                } else if (tipo.equals("Equipo")) {
                    recursos.add(new EquipoAudiovisual(nombre, descripcion));
                } else if (tipo.equals("Catering")) {
                    recursos.add(new Catering(nombre, descripcion));
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("No se encontro el archivo");
        } catch (IOException e) {
            System.err.println("Error al leer archivo");
        }
    }
}
