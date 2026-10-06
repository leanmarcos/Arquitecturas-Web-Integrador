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
class CarrerasIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CarreraRepository carreraRepository;

    @Autowired
    private EstudianteRepository estudianteRepository;

    @Autowired
    private EstudianteCarreraRepository estudianteCarreraRepository;

    /**
     * Given de todos los tests. Inscriptos por carrera:
     * <ul>
     *     <li>TUDAI: 3</li>
     *     <li>Medicina: 2 y Arquitectura: 2 (empatan, se desempata por nombre)</li>
     *     <li>Abogacia: 1</li>
     *     <li>Ingenieria: 0 (no tiene que aparecer)</li>
     * </ul>
     * Las carreras se guardan en un orden distinto al esperado, así el test solo pasa si la consulta ordena.
     */
    @BeforeEach
    void cargarInscripciones() {
        Carrera abogacia = carreraRepository.save(createCarrera("Abogacia", 4));
        carreraRepository.save(createCarrera("Ingenieria", 5)); // sin inscriptos
        Carrera medicina = carreraRepository.save(createCarrera("Medicina", 6));
        Carrera tudai = carreraRepository.save(createCarrera("TUDAI", 2));
        Carrera arquitectura = carreraRepository.save(createCarrera("Arquitectura", 5));

        Estudiante zapata = estudianteRepository.save(createEstudiante(34978L, 71779527, "Isidro", "Zapata"));
        Estudiante alvarez = estudianteRepository.save(createEstudiante(51244L, 33865264, "Lucía", "Alvarez"));
        Estudiante gomez = estudianteRepository.save(createEstudiante(20311L, 40123456, "Martín", "Gomez"));
        Estudiante perez = estudianteRepository.save(createEstudiante(48810L, 38990211, "Sofía", "Perez"));

        inscribir(zapata, tudai);
        inscribir(alvarez, tudai);
        inscribir(gomez, tudai);

        inscribir(zapata, medicina);
        inscribir(alvarez, medicina);

        inscribir(gomez, arquitectura);
        inscribir(perez, arquitectura);

        inscribir(perez, abogacia);
    }

    @Test
    void findWithInscriptos_returnsOrderedByInscriptosDesc() throws Exception {
        // When
        mockMvc.perform(get("/carreras/con-inscriptos"))

        // Then: de más a menos inscriptos, con la cantidad de cada una
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("TUDAI"))
                .andExpect(jsonPath("$[0].totalInscriptos").value(3))
                .andExpect(jsonPath("$[1].totalInscriptos").value(2))
                .andExpect(jsonPath("$[2].totalInscriptos").value(2))
                .andExpect(jsonPath("$[3].nombre").value("Abogacia"))
                .andExpect(jsonPath("$[3].totalInscriptos").value(1));
    }

    @Test
    void findWithInscriptos_sameInscriptos_ordersByNombre() throws Exception {
        // When
        mockMvc.perform(get("/carreras/con-inscriptos"))

        // Then: Medicina se guardó antes, pero Arquitectura va primero por nombre
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[1].nombre").value("Arquitectura"))
                .andExpect(jsonPath("$[2].nombre").value("Medicina"));
    }

    @Test
    void findWithInscriptos_excludesCarrerasWithoutInscriptos() throws Exception {
        // When
        mockMvc.perform(get("/carreras/con-inscriptos"))

        // Then: son 5 carreras, pero Ingenieria no tiene inscriptos
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4))
                .andExpect(jsonPath("$[?(@.nombre == 'Ingenieria')]").isEmpty());
    }

    @Test
    void findWithInscriptos_withoutInscripciones_returnsEmptyList() throws Exception {
        // Given: las carreras siguen existiendo, pero nadie está inscripto
        estudianteCarreraRepository.deleteAll();

        // When
        mockMvc.perform(get("/carreras/con-inscriptos"))

        // Then: 200 con lista vacía, no 404
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    private void inscribir(Estudiante estudiante, Carrera carrera) {
        estudianteCarreraRepository.save(EstudianteCarrera.builder()
                .estudiante(estudiante)
                .carrera(carrera)
                .anioInscripcion(Year.of(2022))
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
