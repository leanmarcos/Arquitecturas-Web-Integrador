
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.example.dto.CarreraResponseDTO;
import org.example.exceptions.CarreraExistingException;
import org.example.exceptions.CarreraNotFoundException;
import org.example.exceptions.UnexpectedException;
import org.example.model.Carrera;
import org.example.repository.CarreraRepository;
import org.example.service.CarreraService;
import org.example.utils.JPAUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CarreraServiceTest {

    private CarreraRepository repository;
    private CarreraService service;
    private EntityManager entityManager;
    private EntityTransaction transaction;

    @BeforeEach
    void setUp() {
        repository = mock(CarreraRepository.class);
        service = new CarreraService(repository);

        entityManager = mock(EntityManager.class);
        transaction = mock(EntityTransaction.class);

        when(entityManager.getTransaction()).thenReturn(transaction);
        when(transaction.isActive()).thenReturn(true);
    }

    @Test
    void getAllDebeDevolverListaDeDTOs() {
        Carrera carrera1 = crearCarrera(1L, "Ingeniería en Sistemas");
        Carrera carrera2 = crearCarrera(2L, "Ingeniería Industrial");

        when(repository.findAll(entityManager))
                .thenReturn(List.of(carrera1, carrera2));

        try (MockedStatic<JPAUtil> jpaUtil = mockStatic(JPAUtil.class)) {
            jpaUtil.when(JPAUtil::getEntityManager)
                    .thenReturn(entityManager);

            List<CarreraResponseDTO> resultado = service.getAll();

            assertNotNull(resultado);
            assertEquals(2, resultado.size());
            assertEquals("Ingeniería en Sistemas", resultado.get(0).nombre());
            assertEquals("Ingeniería Industrial", resultado.get(1).nombre());

            verify(repository).findAll(entityManager);
        }
    }

    @Test
    void getAllDebeDevolverListaVaciaSiNoHayCarreras() {
        when(repository.findAll(entityManager))
                .thenReturn(List.of());

        try (MockedStatic<JPAUtil> jpaUtil = mockStatic(JPAUtil.class)) {
            jpaUtil.when(JPAUtil::getEntityManager)
                    .thenReturn(entityManager);

            List<CarreraResponseDTO> resultado = service.getAll();

            assertNotNull(resultado);
            assertTrue(resultado.isEmpty());

            verify(repository).findAll(entityManager);
        }
    }

    @Test
    void saveDebeGuardarCarreraValida() {
        Carrera carrera = crearCarrera(null, "Ingeniería en Sistemas");

        when(repository.findByNombre(entityManager, "Ingeniería en Sistemas"))
                .thenReturn(Optional.empty());

        when(repository.save(entityManager, carrera))
                .thenReturn(Optional.of(carrera));

        try (MockedStatic<JPAUtil> jpaUtil = mockStatic(JPAUtil.class)) {
            jpaUtil.when(JPAUtil::getEntityManager)
                    .thenReturn(entityManager);

            CarreraResponseDTO resultado = service.save(carrera);

            assertNotNull(resultado);
            assertEquals("Ingeniería en Sistemas", resultado.nombre());

            verify(transaction).begin();
            verify(transaction).commit();
            verify(repository).findByNombre(entityManager, "Ingeniería en Sistemas");
            verify(repository).save(entityManager, carrera);
        }
    }

    @Test
    void saveDebeLanzarCarreraExistingExceptionSiLaCarreraYaExiste() {
        Carrera carrera = crearCarrera(null, "Ingeniería en Sistemas");

        when(repository.findByNombre(entityManager, "Ingeniería en Sistemas"))
                .thenReturn(Optional.of(carrera));

        try (MockedStatic<JPAUtil> jpaUtil = mockStatic(JPAUtil.class)) {
            jpaUtil.when(JPAUtil::getEntityManager)
                    .thenReturn(entityManager);

            assertThrows(CarreraExistingException.class, () -> service.save(carrera));

            verify(transaction).begin();
            verify(transaction).rollback();
            verify(repository, never()).save(any(), any());
        }
    }

    @Test
    void saveDebeLanzarIllegalArgumentExceptionSiCarreraEsNula() {
        try (MockedStatic<JPAUtil> jpaUtil = mockStatic(JPAUtil.class)) {
            assertThrows(IllegalArgumentException.class, () -> service.save(null));
            verifyNoInteractions(repository);
        }
    }

    @Test
    void saveDebeLanzarIllegalArgumentExceptionSiNombreEsNulo() {
        Carrera carrera = crearCarrera(null, null);

        try (MockedStatic<JPAUtil> jpaUtil = mockStatic(JPAUtil.class)) {
            assertThrows(IllegalArgumentException.class, () -> service.save(carrera));
            verifyNoInteractions(repository);
        }
    }

    @Test
    void saveDebeLanzarIllegalArgumentExceptionSiNombreEsVacio() {
        Carrera carrera = crearCarrera(null, "   ");

        try (MockedStatic<JPAUtil> jpaUtil = mockStatic(JPAUtil.class)) {
            assertThrows(IllegalArgumentException.class, () -> service.save(carrera));
            verifyNoInteractions(repository);
        }
    }

    @Test
    void saveDebeLanzarIllegalArgumentExceptionSiNombreSupera255() {
        Carrera carrera = crearCarrera(null, "a".repeat(256));

        try (MockedStatic<JPAUtil> jpaUtil = mockStatic(JPAUtil.class)) {
            assertThrows(IllegalArgumentException.class, () -> service.save(carrera));
            verifyNoInteractions(repository);
        }
    }

    @Test
    void saveDebeHacerRollbackSiLaPersistenciaFalla() {
        Carrera carrera = crearCarrera(null, "Ingeniería en Sistemas");

        when(repository.findByNombre(entityManager, "Ingeniería en Sistemas"))
                .thenReturn(Optional.empty());

        when(repository.save(entityManager, carrera))
                .thenReturn(Optional.empty());

        try (MockedStatic<JPAUtil> jpaUtil = mockStatic(JPAUtil.class)) {
            jpaUtil.when(JPAUtil::getEntityManager)
                    .thenReturn(entityManager);

            assertThrows(UnexpectedException.class, () -> service.save(carrera));

            verify(transaction).begin();
            verify(transaction).rollback();
            verify(transaction, never()).commit();
        }
    }

    @Test
    void getCarreraByIdDebeDevolverDTOCuandoExiste() {
        Long id = 1L;
        Carrera carrera = crearCarrera(id, "Ingeniería en Sistemas");

        when(repository.findById(entityManager, id))
                .thenReturn(Optional.of(carrera));

        try (MockedStatic<JPAUtil> jpaUtil = mockStatic(JPAUtil.class)) {
            jpaUtil.when(JPAUtil::getEntityManager)
                    .thenReturn(entityManager);

            CarreraResponseDTO resultado = service.getCarreraById(id);

            assertNotNull(resultado);
            assertEquals("Ingeniería en Sistemas", resultado.nombre());

            verify(repository).findById(entityManager, id);
        }
    }

    @Test
    void getCarreraByIdDebeLanzarCarreraNotFoundExceptionCuandoNoExiste() {
        Long id = 99L;

        when(repository.findById(entityManager, id))
                .thenReturn(Optional.empty());

        try (MockedStatic<JPAUtil> jpaUtil = mockStatic(JPAUtil.class)) {
            jpaUtil.when(JPAUtil::getEntityManager)
                    .thenReturn(entityManager);

            assertThrows(CarreraNotFoundException.class, () -> service.getCarreraById(id));
        }
    }

    @Test
    void getCarreraByIdDebeRechazarIdNulo() {
        try (MockedStatic<JPAUtil> jpaUtil = mockStatic(JPAUtil.class)) {
            assertThrows(IllegalArgumentException.class, () -> service.getCarreraById(null));
            verifyNoInteractions(repository);
        }
    }

    @Test
    void getCarreraByIdDebeRechazarIdMenorOIgualACero() {
        try (MockedStatic<JPAUtil> jpaUtil = mockStatic(JPAUtil.class)) {
            assertThrows(IllegalArgumentException.class, () -> service.getCarreraById(0L));
            verifyNoInteractions(repository);
        }
    }

    @Test
    void deleteDebeEliminarCarreraExistente() {
        Long id = 1L;
        Carrera carrera = crearCarrera(id, "Ingeniería en Sistemas");

        when(repository.findById(entityManager, id))
                .thenReturn(Optional.of(carrera));

        when(repository.deleteById(entityManager, id))
                .thenReturn(Optional.of(carrera));

        try (MockedStatic<JPAUtil> jpaUtil = mockStatic(JPAUtil.class)) {
            jpaUtil.when(JPAUtil::getEntityManager)
                    .thenReturn(entityManager);

            CarreraResponseDTO resultado = service.delete(carrera);

            assertNotNull(resultado);
            assertEquals("Ingeniería en Sistemas", resultado.nombre());

            verify(transaction).begin();
            verify(transaction).commit();
            verify(repository).findById(entityManager, id);
            verify(repository).deleteById(entityManager, id);
        }
    }

    @Test
    void deleteDebeLanzarCarreraNotFoundExceptionSiNoExiste() {
        Long id = 99L;
        Carrera carrera = crearCarrera(id, "Carrera inexistente");

        when(repository.findById(entityManager, id))
                .thenReturn(Optional.empty());

        try (MockedStatic<JPAUtil> jpaUtil = mockStatic(JPAUtil.class)) {
            jpaUtil.when(JPAUtil::getEntityManager)
                    .thenReturn(entityManager);

            assertThrows(CarreraNotFoundException.class, () -> service.delete(carrera));

            verify(transaction).begin();
            verify(transaction).rollback();
            verify(repository, never()).deleteById(any(), anyLong());
        }
    }

    @Test
    void deleteDebeRechazarCarreraNula() {
        try (MockedStatic<JPAUtil> jpaUtil = mockStatic(JPAUtil.class)) {
            assertThrows(IllegalArgumentException.class, () -> service.delete(null));
            verifyNoInteractions(repository);
        }
    }

    @Test
    void deleteDebeRechazarCarreraConIdNulo() {
        Carrera carrera = crearCarrera(null, "Ingeniería en Sistemas");

        try (MockedStatic<JPAUtil> jpaUtil = mockStatic(JPAUtil.class)) {
            assertThrows(IllegalArgumentException.class, () -> service.delete(carrera));
            verifyNoInteractions(repository);
        }
    }

    @Test
    void deleteDebeRechazarCarreraConIdInvalido() {
        Carrera carrera = crearCarrera(0L, "Ingeniería en Sistemas");

        try (MockedStatic<JPAUtil> jpaUtil = mockStatic(JPAUtil.class)) {
            assertThrows(IllegalArgumentException.class, () -> service.delete(carrera));
            verifyNoInteractions(repository);
        }
    }

    @Test
    void deleteDebeHacerRollbackSiLaEliminacionFalla() {
        Long id = 1L;
        Carrera carrera = crearCarrera(id, "Ingeniería en Sistemas");

        when(repository.findById(entityManager, id))
                .thenReturn(Optional.of(carrera));

        when(repository.deleteById(entityManager, id))
                .thenReturn(Optional.empty());

        try (MockedStatic<JPAUtil> jpaUtil = mockStatic(JPAUtil.class)) {
            jpaUtil.when(JPAUtil::getEntityManager)
                    .thenReturn(entityManager);

            assertThrows(UnexpectedException.class, () -> service.delete(carrera));

            verify(transaction).begin();
            verify(transaction).rollback();
            verify(transaction, never()).commit();
        }
    }

    @Test
    void findEntityByNameDebeDevolverCarreraCuandoExiste() {
        Carrera carrera = crearCarrera(1L, "Ingeniería en Sistemas");

        when(repository.findByNombre(entityManager, "Ingeniería en Sistemas"))
                .thenReturn(Optional.of(carrera));

        Carrera resultado = service.findEntityByName(entityManager, "Ingeniería en Sistemas");

        assertTrue(!resultado.equals(null));
        assertEquals("Ingeniería en Sistemas", resultado.getNombre());

        verify(repository).findByNombre(entityManager, "Ingeniería en Sistemas");
    }

    @Test
    void findEntityByNameDebeDevolverRuntimeExceptionCuandoNoExiste() {
        assertThrows(RuntimeException.class,
                () -> service.findEntityByName(entityManager, "Carrera inexistente"));
    }

    @Test
    void findEntityByNameDebeLanzarIllegalArgumentExceptionSiNombreEsNulo() {
        assertThrows(IllegalArgumentException.class,
                () -> service.findEntityByName(entityManager, null));

        verifyNoInteractions(repository);
    }

    @Test
    void findEntityByNameDebeLanzarIllegalArgumentExceptionSiNombreEsVacio() {
        assertThrows(IllegalArgumentException.class,
                () -> service.findEntityByName(entityManager, "   "));

        verifyNoInteractions(repository);
    }

    private Carrera crearCarrera(Long id, String nombre) {
        Carrera carrera = new Carrera();

        try {
            Field idField = Carrera.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(carrera, id);

            Field nombreField = Carrera.class.getDeclaredField("nombre");
            nombreField.setAccessible(true);
            nombreField.set(carrera, nombre);

            return carrera;
        } catch (ReflectiveOperationException e) {
            throw new AssertionError("No se pudo crear la carrera de prueba", e);
        }
    }

    private CarreraResponseDTO crearDto(String nombre) {
        CarreraResponseDTO dto = new CarreraResponseDTO(nombre);

        try {
            Field nombreField = CarreraResponseDTO.class.getDeclaredField("nombre");
            nombreField.setAccessible(true);
            nombreField.set(dto, nombre);
            return dto;

        } catch (ReflectiveOperationException e) {
            throw new AssertionError("No se pudo crear el DTO de prueba", e);
        }
    }
}