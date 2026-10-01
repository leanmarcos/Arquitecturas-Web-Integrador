package org.example;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.io.IOException;

@SpringBootApplication
public class SistemaUniversitario {

    // Incluir DataLoader
    /*
    @Autowired
    private DataLoader dataLoader;
     */

    public static void main(String[] args) {
        SpringApplication.run(SistemaUniversitario.class, args);
    }

    /* Una vez construida la aplicación, carga de datos automática
    Evaluar llamar a Main tambien
    @PostConstruct
    public void init() throws IOException {
        cargaDeDatos.cargarDatosDesdeCSV();
    }
     */
}
