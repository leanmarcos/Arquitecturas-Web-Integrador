package org.example;

import org.example.dto.CarreraReporteDTO;
import org.example.dto.EstudianteCarreraRequestDTO;
import org.example.dto.EstudianteRequestDTO;
import org.example.dto.EstudianteResponseDTO;
import org.example.loader.DataLoader;
import org.example.loader.DataResult;
import org.example.loader.DataResult.Conteo;
import org.example.repository.CarreraRepositoryImpl;
import org.example.repository.EstudianteCarreraRepositoryImpl;
import org.example.repository.EstudianteRepositoryImpl;
import org.example.service.CarreraService;
import org.example.service.EstudianteCarreraService;
import org.example.service.EstudianteService;
import org.example.utils.JPAUtil;

import java.time.Year;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            CarreraService carreraService = new CarreraService(new CarreraRepositoryImpl());
            EstudianteService estudianteService = new EstudianteService(new EstudianteRepositoryImpl());
            EstudianteCarreraService estudianteCarreraService = new EstudianteCarreraService(new EstudianteCarreraRepositoryImpl(), estudianteService, carreraService);

            DataResult resultado = new DataLoader(carreraService, estudianteService, estudianteCarreraService).cargar();

            imprimir("carreras", resultado.carreras());
            imprimir("estudiantes", resultado.estudiantes());
            imprimir("inscripciones", resultado.inscripciones());

            System.out.println("\n--- 2a) Alta de un estudiante ---");
            EstudianteResponseDTO nuevo = estudianteService.create(EstudianteRequestDTO.builder()
                    .lu(300001L)
                    .dni(40123456)
                    .nombres("Ana")
                    .apellido("Gomez")
                    .edad(21)
                    .genero("Femenino")
                    .ciudadResidencia("Tandil")
                    .build());
            System.out.println(nuevo);

            System.out.println("\n--- 2b) Matricular un estudiante en una carrera ---");
            System.out.println(estudianteCarreraService.matricularEstudianteEnCarrera(EstudianteCarreraRequestDTO.builder()
                    .dni(nuevo.dni())
                    .nombreCarrera("TUDAI")
                    .anioInscripcion(Year.of(2024))
                    .build()));

            imprimirEstudiantes("2c) Todos los estudiantes ordenados por apellido",
                    estudianteService.findAllOrderByApellido());

            System.out.println("\n--- 2d) Estudiante con LU 250018 ---");
            System.out.println(estudianteService.findByLu(250018L));

            imprimirEstudiantes("2e) Estudiantes de género femenino",
                    estudianteService.findAllByGenero("Femenino"));

            System.out.println("\n--- 2f) Carreras con inscriptos, ordenadas por cantidad ---");
            carreraService.obtenerCarrerasConCantInscriptos().forEach(System.out::println);

            imprimirEstudiantes("2g) Estudiantes de TUDAI que viven en Rauch",
                    estudianteService.findAllByCarreraAndCiudad("TUDAI", "Rauch"));

            List<CarreraReporteDTO> reporte = carreraService.generarReporteCarreras();
            imprimirReporte(reporte);
        } finally {
            JPAUtil.close();
        }
    }

    private static void imprimirEstudiantes(String titulo, List<EstudianteResponseDTO> estudiantes) {
        System.out.printf("%n--- %s (%d) ---%n", titulo, estudiantes.size());
        estudiantes.forEach(System.out::println);
    }

    private static void imprimir(String nombre, Conteo conteo) {
        System.out.printf("%s: %d cargados, %d rechazados%n",
                nombre, conteo.cargados(), conteo.rechazados().size());
        conteo.rechazados().forEach(rechazo -> System.out.printf("  fila %d: %s%n", rechazo.fila(), rechazo.motivo()));
    }

    private static void imprimirReporte(List<CarreraReporteDTO> reporte) {
        System.out.println("\n========================================================================");
        System.out.println("            REPORTE DE INSCRIPCIONES Y EGRESOS POR CARRERA              ");
        System.out.println("========================================================================");

        String carreraActual = null;
        for (CarreraReporteDTO fila : reporte) {
            if (!fila.nombreCarrera().equals(carreraActual)) {
                carreraActual = fila.nombreCarrera();
                System.out.println("\nCarrera: " + carreraActual);
                System.out.printf("  %-10s | %-15s | %-15s%n", "ANIO", "INSCRIPTOS", "EGRESADOS");
                System.out.println("  ---------------------------------------------");
            }
            System.out.printf("  %-10s | %-15d | %-15d%n",
                    fila.anio(), fila.inscriptos(), fila.egresados());
        }
        System.out.println("========================================================================\n");
    }
}
