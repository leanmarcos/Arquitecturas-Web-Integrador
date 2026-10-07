package org.example.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Year;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EstudianteCarreraTest {

    private Carrera carrera;
    private Estudiante estudiante;

    @BeforeEach
    void setUp() {
        carrera = createCarrera();
        estudiante = createEstudiante();
    }

    @Test
    void getAntiguedad_graduated_returnsYearsUntilGraduacion() {
        // Arrange
        EstudianteCarrera inscripcion = createInscripcion(2018, 2025);

        // Act
        int antiguedad = inscripcion.getAntiguedad();

        // Assert
        assertEquals(7, antiguedad);
    }

    @Test
    void getAntiguedad_notGraduated_returnsYearsUntilCurrentYear() {
        // Arrange: el esperado se calcula con el año actual para que el test no falle al cambiar de año
        EstudianteCarrera inscripcion = createInscripcion(2018, null);
        int antiguedadEsperada = Year.now().getValue() - 2018;

        // Act
        int antiguedad = inscripcion.getAntiguedad();

        // Assert
        assertEquals(antiguedadEsperada, antiguedad);
    }

    @Test
    void getAntiguedad_graduatedSameYear_returnsZero() {
        // Arrange
        EstudianteCarrera inscripcion = createInscripcion(2018, 2018);

        // Act
        int antiguedad = inscripcion.getAntiguedad();

        // Assert
        assertEquals(0, antiguedad);
    }

    private EstudianteCarrera createInscripcion(Integer anioInscripcion, Integer anioGraduacion) {
        return EstudianteCarrera.builder()
                .estudiante(estudiante)
                .carrera(carrera)
                .anioInscripcion(Year.of(anioInscripcion))
                .anioGraduacion(anioGraduacion != null ? Year.of(anioGraduacion) : null)
                .build();
    }

    private Carrera createCarrera() {
        return Carrera.builder()
                .nombre("TUARI")
                .duracion(5)
                .build();
    }

    private Estudiante createEstudiante() {
        return Estudiante.builder()
                .lu(1234L)
                .dni(45666666)
                .nombres("Faustino")
                .apellido("Oro")
                .edad(25)
                .genero(EstudianteGenero.MALE)
                .ciudadResidencia("Tandil")
                .build();
    }
}
