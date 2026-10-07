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
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class InscripcionesIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CarreraRepository carreraRepository;

    @Autowired
    private EstudianteRepository estudianteRepository;

    @Autowired
    private EstudianteCarreraRepository estudianteCarreraRepository;

    /**
     * Arrange de todos los tests. En TUDAI y Rauch hay 3 estudiantes, guardados en un orden distinto al esperado
     * (apellido y después nombres). Además hay uno de TUDAI en otra ciudad y uno de Rauch en otra carrera, que no
     * tienen que aparecer.
     */
    @BeforeEach
    void cargarInscripciones() {
        Carrera tudai = carreraRepository.save(createCarrera("TUDAI", 2));
        Carrera abogacia = carreraRepository.save(createCarrera("Abogacia", 4));

        inscribir(estudianteRepository.save(createEstudiante(34978L, 71779527, "Isidro", "Zapata", "Rauch")), tudai);
        inscribir(estudianteRepository.save(createEstudiante(51244L, 33865264, "Lucía", "Alvarez", "Rauch")), tudai);
        inscribir(estudianteRepository.save(createEstudiante(20311L, 40123456, "Ana", "Alvarez", "Rauch")), tudai);

        inscribir(estudianteRepository.save(createEstudiante(48810L, 38990211, "Martín", "Gomez", "Tandil")), tudai);
        inscribir(estudianteRepository.save(createEstudiante(61502L, 42118745, "Sofía", "Perez", "Rauch")), abogacia);
    }

    @Test
    void findByCarreraAndCiudad_returnsOnlyMatchingEstudiantes() throws Exception {
        // Act
        mockMvc.perform(get("/inscripciones")
                        .param("carrera", "TUDAI")
                        .param("ciudad", "Rauch"))

        // Assert: solo los 3 de TUDAI que viven en Rauch, sin Gomez (Tandil) ni Perez (Abogacia)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.carrera").value("TUDAI"))
                .andExpect(jsonPath("$.ciudad").value("Rauch"))
                .andExpect(jsonPath("$.estudiantes.length()").value(3))
                .andExpect(jsonPath("$.estudiantes[?(@.apellido == 'Gomez')]").isEmpty())
                .andExpect(jsonPath("$.estudiantes[?(@.apellido == 'Perez')]").isEmpty());
    }

    @Test
    void findByCarreraAndCiudad_ordersByApellidoAndNombres() throws Exception {
        // Act
        mockMvc.perform(get("/inscripciones")
                        .param("carrera", "TUDAI")
                        .param("ciudad", "Rauch"))

        // Assert: por apellido; las dos Alvarez se desempatan por nombre
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estudiantes[0].apellido").value("Alvarez"))
                .andExpect(jsonPath("$.estudiantes[0].nombres").value("Ana"))
                .andExpect(jsonPath("$.estudiantes[1].apellido").value("Alvarez"))
                .andExpect(jsonPath("$.estudiantes[1].nombres").value("Lucía"))
                .andExpect(jsonPath("$.estudiantes[2].apellido").value("Zapata"));
    }

    @Test
    void findByCarreraAndCiudad_withoutEstudiantes_returnsEmptyList() throws Exception {
        // Act: la carrera existe, pero nadie de TUDAI vive en Azul
        mockMvc.perform(get("/inscripciones")
                        .param("carrera", "TUDAI")
                        .param("ciudad", "Azul"))

        // Assert: 200 con la lista vacía, no 404
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.carrera").value("TUDAI"))
                .andExpect(jsonPath("$.ciudad").value("Azul"))
                .andExpect(jsonPath("$.estudiantes.length()").value(0));
    }

    @Test
    void findByCarreraAndCiudad_nonExistingCarrera_returnsNotFound() throws Exception {
        // Act
        mockMvc.perform(get("/inscripciones")
                        .param("carrera", "Medicina")
                        .param("ciudad", "Rauch"))

        // Assert: 404 con el ErrorDto de la carrera
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("major_not_found"))
                .andExpect(jsonPath("$.message").value("No se encontró la carrera con nombre Medicina"));
    }

    @Test
    void findByCarreraAndCiudad_withoutCiudad_returnsBadRequest() throws Exception {
        // Act: falta la ciudad, que es obligatoria y no tiene valor por defecto
        mockMvc.perform(get("/inscripciones")
                        .param("carrera", "TUDAI"))

        // Assert: 400 indicando qué parámetro falta
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("invalid_parameter"))
                .andExpect(jsonPath("$.message").value("Falta el parámetro obligatorio 'ciudad'"));
    }

    @Test
    void matricular_validRequest_returnsCreatedAndPersists() throws Exception {
        // Act: Gomez (Tandil, TUDAI) se inscribe también en Abogacia, ya graduado
        mockMvc.perform(post("/inscripciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "dni": 38990211,
                                  "nombreCarrera": "Abogacia",
                                  "anioInscripcion": 2020,
                                  "anioGraduacion": 2024
                                }
                                """))

        // Assert: 201 con la inscripción; sin antigüedad en el request, se calcula con los años
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.luEstudiante").value(48810))
                .andExpect(jsonPath("$.nombreCarrera").value("Abogacia"))
                .andExpect(jsonPath("$.graduado").value(true))
                .andExpect(jsonPath("$.antiguedad").value(4));

        // Assert: ahora aparece entre los estudiantes de Abogacia en Tandil
        mockMvc.perform(get("/inscripciones")
                        .param("carrera", "Abogacia")
                        .param("ciudad", "Tandil"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estudiantes.length()").value(1))
                .andExpect(jsonPath("$.estudiantes[0].apellido").value("Gomez"));
    }

    @Test
    void matricular_alreadyEnrolled_returnsConflict() throws Exception {
        // Act: Gomez ya está inscripto en TUDAI
        mockMvc.perform(post("/inscripciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "dni": 38990211,
                                  "nombreCarrera": "TUDAI",
                                  "anioInscripcion": 2023
                                }
                                """))

        // Assert: 409, no se pisa la inscripción que ya tenía
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("enrollment_duplicated"))
                .andExpect(jsonPath("$.message")
                        .value("El estudiante con DNI: 38990211 ya está inscripto en la carrera TUDAI"));
    }

    @Test
    void matricular_nonExistingEstudiante_returnsNotFound() throws Exception {
        // Act
        mockMvc.perform(post("/inscripciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "dni": 11111111,
                                  "nombreCarrera": "TUDAI",
                                  "anioInscripcion": 2023
                                }
                                """))

        // Assert
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("student_not_found"))
                .andExpect(jsonPath("$.message").value("No se encontró el estudiante con DNI 11111111"));
    }

    @Test
    void matricular_nonExistingCarrera_returnsNotFound() throws Exception {
        // Act
        mockMvc.perform(post("/inscripciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "dni": 38990211,
                                  "nombreCarrera": "Medicina",
                                  "anioInscripcion": 2023
                                }
                                """))

        // Assert
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("major_not_found"))
                .andExpect(jsonPath("$.message").value("No se encontró la carrera con nombre Medicina"));
    }

    @Test
    void matricular_graduacionBeforeInscripcion_returnsBadRequest() throws Exception {
        // Act: se gradúa antes de inscribirse
        mockMvc.perform(post("/inscripciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "dni": 38990211,
                                  "nombreCarrera": "Abogacia",
                                  "anioInscripcion": 2024,
                                  "anioGraduacion": 2020
                                }
                                """))

        // Assert: 400 con el campo que falló en el detail
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("validation_error"))
                .andExpect(jsonPath("$.detail.graduacionValida")
                        .value("El año de graduación no puede ser anterior al de inscripción"));
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

    private Estudiante createEstudiante(Long lu, Integer dni, String nombres, String apellido,
                                        String ciudadResidencia) {
        return Estudiante.builder()
                .lu(lu)
                .dni(dni)
                .nombres(nombres)
                .apellido(apellido)
                .edad(21)
                .genero(EstudianteGenero.FEMALE)
                .ciudadResidencia(ciudadResidencia)
                .build();
    }
}
