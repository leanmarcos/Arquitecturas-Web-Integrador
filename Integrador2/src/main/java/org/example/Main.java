package org.example;

import org.example.dto.CarreraReporteDTO;
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

import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            CarreraService carreraService = new CarreraService(new CarreraRepositoryImpl());
            EstudianteService estudianteService = new EstudianteService(new EstudianteRepositoryImpl());
            EstudianteCarreraService estudianteCarreraService = new EstudianteCarreraService(
                    new EstudianteCarreraRepositoryImpl(), estudianteService, carreraService);

            DataResult resultado = new DataLoader(carreraService, estudianteService, estudianteCarreraService)
                    .cargar();

            imprimir("carreras", resultado.carreras());
            imprimir("estudiantes", resultado.estudiantes());
            imprimir("inscripciones", resultado.inscripciones());

            List<CarreraReporteDTO> reporte = carreraService.generarReporteCarreras();
            imprimirReporte(reporte);
        } finally {
            JPAUtil.close();
        }
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
