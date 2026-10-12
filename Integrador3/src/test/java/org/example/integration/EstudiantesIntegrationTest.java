package org.example.integration;

import org.example.model.Estudiante;
import org.example.model.EstudianteGenero;
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

import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class EstudiantesIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EstudianteRepository repository;

    /**
     * Arrange de todos los tests: se guardan en un orden que no coincide con el de apellido, DNI ni LU, así cada test
     * solo pasa si el endpoint ordena por el criterio que se pide. El {@code @Transactional} los borra al terminar.
     */
    @BeforeEach
    void cargarEstudiantes() {
        repository.save(createEstudiante(34978L, 71779527, "Isidro", "Zapata", 22,
                EstudianteGenero.MALE, "Tandil"));
        repository.save(createEstudiante(51244L, 33865264, "Lucia", "Alvarez", 19,
                EstudianteGenero.FEMALE, "Rauch"));
        repository.save(createEstudiante(20311L, 40123456, "Martin", "Gomez", 25,
                EstudianteGenero.MALE, "Azul"));
        repository.save(createEstudiante(48810L, 38990211, "Sofia", "Perez", 21,
                EstudianteGenero.NON_BINARY, "Olavarria"));
    }

    @Test
    void listAll_withoutSort_getAllOrderByLastName() throws Exception {
        // Act: el request, como lo haría Postman
        mockMvc.perform(get("/estudiantes"))

        // Assert: status y contenido del JSON
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4))
                .andExpect(jsonPath("$[0].apellido").value("Alvarez"))
                .andExpect(jsonPath("$[1].apellido").value("Gomez"))
                .andExpect(jsonPath("$[2].apellido").value("Perez"))
                .andExpect(jsonPath("$[3].apellido").value("Zapata"))
                .andExpect(jsonPath("$[0].lu").value(51244))
                .andExpect(jsonPath("$[0].dni").value(33865264))
                .andExpect(jsonPath("$[0].nombres").value("Lucia"))
                .andExpect(jsonPath("$[0].ciudadResidencia").value("Rauch"));
    }

    @Test
    void listAll_sortByDni_getAllOrderByDni() throws Exception {
        // Act: se pide otro criterio con el query param sort (ascendente por defecto)
        mockMvc.perform(get("/estudiantes").param("sort", "dni"))

        // Assert: el orden por DNI difiere del de apellido (Perez queda antes que Gomez)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4))
                .andExpect(jsonPath("$[0].dni").value(33865264))
                .andExpect(jsonPath("$[1].dni").value(38990211))
                .andExpect(jsonPath("$[2].dni").value(40123456))
                .andExpect(jsonPath("$[3].dni").value(71779527))
                .andExpect(jsonPath("$[1].apellido").value("Perez"))
                .andExpect(jsonPath("$[2].apellido").value("Gomez"));
    }

    @Test
    void listAll_invalidSort_returnsBadRequest() throws Exception {
        // Act: se pide ordenar por un campo que Estudiante no tiene
        mockMvc.perform(get("/estudiantes").param("sort", "invalid"))

        // Assert: 400 indicando el campo, no un 500
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("invalid_parameter"))
                .andExpect(jsonPath("$.message").value("No existe el campo 'invalid' para ordenar"));
    }

    @Test
    void findByLu_existingLu_returnsEstudiante() throws Exception {
        // Act: se pide un estudiante cargado en el Arrange
        mockMvc.perform(get("/estudiantes/{lu}", 20311L))

        // Assert: devuelve ese estudiante y no otro
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lu").value(20311))
                .andExpect(jsonPath("$.dni").value(40123456))
                .andExpect(jsonPath("$.nombres").value("Martin"))
                .andExpect(jsonPath("$.apellido").value("Gomez"))
                .andExpect(jsonPath("$.edad").value(25))
                .andExpect(jsonPath("$.ciudadResidencia").value("Azul"));
    }

    @Test
    void findByLu_nonExistingLu_returnsNotFound() throws Exception {
        // Act: se pide una LU que no está en la base
        mockMvc.perform(get("/estudiantes/{lu}", 99999L))

        // Assert: 404 con el ErrorDto que arma el GlobalExceptionHandler
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("student_not_found"))
                .andExpect(jsonPath("$.message").value("No se encontró el estudiante con LU 99999"))
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void searchByGenero_existingGenero_returnsOnlyThatGenero() throws Exception {
        // Act: el género en español, como se muestra en la respuesta
        mockMvc.perform(get("/estudiantes").param("genero", "Masculino"))

        // Assert: solo los dos varones del Arrange, ordenados por apellido como el listado sin filtro
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].apellido").value("Gomez"))
                .andExpect(jsonPath("$[1].apellido").value("Zapata"))
                .andExpect(jsonPath("$[*].genero", everyItem(is("Masculino"))));
    }

    @Test
    void searchByGenero_englishLowercase_returnsEstudiantes() throws Exception {
        // Act: el nombre en inglés del CSV y en minúsculas
        mockMvc.perform(get("/estudiantes").param("genero", "female"))

        // Assert: encuentra a Lucía igual que si se pidiera "Femenino"
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].nombres").value("Lucia"))
                .andExpect(jsonPath("$[0].apellido").value("Alvarez"))
                .andExpect(jsonPath("$[0].genero").value("Femenino"));
    }

    @Test
    void searchByGenero_generoWithSpaces_returnsEstudiantes() throws Exception {
        // Act: un género de dos palabras, que viaja en la URL como "No%20binario"
        mockMvc.perform(get("/estudiantes").param("genero", "No binario"))

        // Assert: encuentra a Sofía
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].nombres").value("Sofia"))
                .andExpect(jsonPath("$[0].apellido").value("Perez"));
    }

    @Test
    void searchByGenero_generoWithoutEstudiantes_returnsEmptyList() throws Exception {
        // Act: un género válido que ningún estudiante del Arrange tiene
        mockMvc.perform(get("/estudiantes").param("genero", "Agénero"))

        // Assert: 200 con lista vacía, no un 404
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void searchByGenero_invalidGenero_returnsBadRequest() throws Exception {
        // Act: un género que no está en el enum
        mockMvc.perform(get("/estudiantes").param("genero", "XYZ"))

        // Assert: 400 indicando el valor y el parámetro
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("invalid_parameter"))
                .andExpect(jsonPath("$.message").value("Valor inválido 'XYZ' para el parámetro 'genero'"));
    }

    @Test
    void create_validRequest_returnsCreatedAndPersists() throws Exception {
        // Act: el género se manda en español, como lo cargaría un usuario
        mockMvc.perform(post("/estudiantes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "lu": 70001,
                                  "dni": 45678901,
                                  "nombres": "Valentina",
                                  "apellido": "Rios",
                                  "edad": 20,
                                  "genero": "Femenino",
                                  "ciudadResidencia": "Azul"
                                }
                                """))

        // Assert: 201 con el estudiante creado
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.lu").value(70001))
                .andExpect(jsonPath("$.dni").value(45678901))
                .andExpect(jsonPath("$.apellido").value("Rios"))
                .andExpect(jsonPath("$.genero").value("Femenino"));

        // Assert: quedó guardado y se puede recuperar por su LU
        mockMvc.perform(get("/estudiantes/{lu}", 70001L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombres").value("Valentina"));
    }

    @Test
    void create_duplicatedDni_returnsConflict() throws Exception {
        // Act: el DNI ya es de Lucía Alvarez, aunque la LU es nueva
        mockMvc.perform(post("/estudiantes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "lu": 70001,
                                  "dni": 33865264,
                                  "nombres": "Valentina",
                                  "apellido": "Rios",
                                  "edad": 20,
                                  "genero": "Female",
                                  "ciudadResidencia": "Azul"
                                }
                                """))

        // Assert: 409 y no se agrega nadie
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("student_document_duplicated"))
                .andExpect(jsonPath("$.message").value("Ya existe un estudiante con DNI 33865264"));

        mockMvc.perform(get("/estudiantes"))
                .andExpect(jsonPath("$.length()").value(4));
    }

    @Test
    void create_invalidGenero_returnsBadRequest() throws Exception {
        // Act: un género que no está en el enum
        mockMvc.perform(post("/estudiantes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "lu": 70001,
                                  "dni": 45678901,
                                  "nombres": "Valentina",
                                  "apellido": "Rios",
                                  "edad": 20,
                                  "genero": "XYZ",
                                  "ciudadResidencia": "Azul"
                                }
                                """))

        // Assert: 400 con el mensaje que tira el constructor del DTO
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("validation_error"))
                .andExpect(jsonPath("$.message").value("Género inválido: XYZ"));
    }

    @Test
    void create_withoutApellido_returnsBadRequest() throws Exception {
        // Act: falta un campo obligatorio
        mockMvc.perform(post("/estudiantes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "lu": 70001,
                                  "dni": 45678901,
                                  "nombres": "Valentina",
                                  "edad": 20,
                                  "genero": "Female",
                                  "ciudadResidencia": "Azul"
                                }
                                """))

        // Assert: 400 indicando qué campo está mal
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("validation_error"))
                .andExpect(jsonPath("$.message").value("El apellido no puede estar vacío."));
    }

    private Estudiante createEstudiante(Long lu, Integer dni, String nombres, String apellido, Integer edad,
                                        EstudianteGenero genero, String ciudadResidencia) {
        return Estudiante.builder()
                .lu(lu)
                .dni(dni)
                .nombres(nombres)
                .apellido(apellido)
                .edad(edad)
                .genero(genero)
                .ciudadResidencia(ciudadResidencia)
                .build();
    }
}
