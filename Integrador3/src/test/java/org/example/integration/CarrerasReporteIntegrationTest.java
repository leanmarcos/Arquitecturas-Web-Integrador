package org.example.integration;

import org.example.model.Carrera;
import org.example.model.Estudiante;
import org.example.model.EstudianteCarrera;
import org.example.model.EstudianteGenero;
import org.example.repository.CarreraRepository;
import org.example.repository.EstudianteCarreraRepository;
import org.example.repository.EstudianteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class CarrerasReporteIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CarreraRepository carreraRepository;

    @Autowired
    private EstudianteRepository estudianteRepository;

    @Autowired
    private EstudianteCarreraRepository estudianteCarreraRepository;

    /**
     * Arrange de todos los tests. Inscripciones:
     * <ul>
     *     <li>TUDAI: Zapata 2020 → 2022, Alvarez 2020 sin graduarse, Gomez 2022 → 2024</li>
     *     <li>Abogacia: Perez 2021 sin graduarse</li>
     * </ul>
     * Reporte esperado (carrera A-Z, año ascendente):
     * <pre>
     * Abogacia 2021 | inscriptos 1 | egresados 0
     * TUDAI    2020 | inscriptos 2 | egresados 0   (solo inscriptos)
     * TUDAI    2022 | inscriptos 1 | egresados 1   (inscriptos y egresados el mismo año)
     * TUDAI    2024 | inscriptos 0 | egresados 1   (solo egresados)
     * </pre>
     * TUDAI se guarda antes que Abogacia, así el test solo pasa si el reporte ordena.
     */
    @BeforeEach
    void cargarInscripciones() {
        Carrera tudai = carreraRepository.save(createCarrera("TUDAI", 2));
        Carrera abogacia = carreraRepository.save(createCarrera("Abogacia", 4));

        Estudiante zapata = estudianteRepository.save(createEstudiante(34978L, 71779527, "Isidro", "Zapata"));
        Estudiante alvarez = estudianteRepository.save(createEstudiante(51244L, 33865264, "Lucía", "Alvarez"));
        Estudiante gomez = estudianteRepository.save(createEstudiante(20311L, 40123456, "Martín", "Gomez"));
        Estudiante perez = estudianteRepository.save(createEstudiante(48810L, 38990211, "Sofía", "Perez"));

        inscribir(zapata, tudai, 2020, 2022);
        inscribir(alvarez, tudai, 2020, null);
        inscribir(gomez, tudai, 2022, 2024);
        inscribir(perez, abogacia, 2021, null);
    }

    @Test
    void getReporte_ordersByCarreraAndAnio() throws Exception {
        // Act
        mockMvc.perform(get("/carreras/reporte"))

        // Assert: carreras de la A a la Z y, dentro de cada una, años ascendentes
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4))
                .andExpect(jsonPath("$[0].nombreCarrera").value("Abogacia"))
                .andExpect(jsonPath("$[0].anio").value(2021))
                .andExpect(jsonPath("$[1].nombreCarrera").value("TUDAI"))
                .andExpect(jsonPath("$[1].anio").value(2020))
                .andExpect(jsonPath("$[2].nombreCarrera").value("TUDAI"))
                .andExpect(jsonPath("$[2].anio").value(2022))
                .andExpect(jsonPath("$[3].nombreCarrera").value("TUDAI"))
                .andExpect(jsonPath("$[3].anio").value(2024));
    }

    @Test
    void getReporte_sameYear_combinesInscriptosAndEgresados() throws Exception {
        // Act
        mockMvc.perform(get("/carreras/reporte"))

        // Assert: TUDAI 2022 tiene un inscripto (Gomez) y un egresado (Zapata) en la misma fila
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[2].nombreCarrera").value("TUDAI"))
                .andExpect(jsonPath("$[2].anio").value(2022))
                .andExpect(jsonPath("$[2].inscriptos").value(1))
                .andExpect(jsonPath("$[2].egresados").value(1));
    }

    @Test
    void getReporte_yearWithOnlyInscriptos_hasZeroEgresados() throws Exception {
        // Act
        mockMvc.perform(get("/carreras/reporte"))

        // Assert: en TUDAI 2020 se inscribieron dos y nadie egresó
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[1].anio").value(2020))
                .andExpect(jsonPath("$[1].inscriptos").value(2))
                .andExpect(jsonPath("$[1].egresados").value(0));
    }

    @Test
    void getReporte_yearWithOnlyEgresados_hasZeroInscriptos() throws Exception {
        // Act
        mockMvc.perform(get("/carreras/reporte"))

        // Assert: en TUDAI 2024 egresó Gomez y nadie se inscribió
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[3].anio").value(2024))
                .andExpect(jsonPath("$[3].inscriptos").value(0))
                .andExpect(jsonPath("$[3].egresados").value(1));
    }

    @Test
    void getReporte_notGraduated_doesNotAddEgresadosRow() throws Exception {
        // Act
        mockMvc.perform(get("/carreras/reporte"))

        // Assert: Alvarez y Perez no se graduaron, así que no hay ninguna fila sin año
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4))
                .andExpect(jsonPath("$[?(@.anio == null)]").isEmpty());
    }

    @Test
    void getReporte_withoutInscripciones_returnsEmptyList() throws Exception {
        // Arrange: las carreras siguen existiendo, pero nadie está inscripto
        estudianteCarreraRepository.deleteAll();

        // Act
        mockMvc.perform(get("/carreras/reporte"))

        // Assert: 200 con lista vacía
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    private void inscribir(Estudiante estudiante, Carrera carrera, int anioInscripcion, Integer anioGraduacion) {
        estudianteCarreraRepository.save(EstudianteCarrera.builder()
                .estudiante(estudiante)
                .carrera(carrera)
                .anioInscripcion(Year.of(anioInscripcion))
                .anioGraduacion(anioGraduacion != null ? Year.of(anioGraduacion) : null)
                .build());
    }

    private Carrera createCarrera(String nombre, Integer duracion) {
        return Carrera.builder()
                .nombre(nombre)
                .duracion(duracion)
                .build();
    }

    private Estudiante createEstudiante(Long lu, Integer dni, String nombres, String apellido) {
        return Estudiante.builder()
                .lu(lu)
                .dni(dni)
                .nombres(nombres)
                .apellido(apellido)
                .edad(21)
                .genero(EstudianteGenero.FEMALE)
                .ciudadResidencia("Tandil")
                .build();
    }
}
