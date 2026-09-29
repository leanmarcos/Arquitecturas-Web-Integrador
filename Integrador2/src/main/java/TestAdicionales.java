import jakarta.persistence.EntityManager;
import org.example.dto.CarreraRequestDTO;
import org.example.dto.CarreraResponseDTO;
import org.example.dto.EstudianteCarreraRequestDTO;
import org.example.dto.EstudianteCarreraResponseDTO;
import org.example.loader.DataLoader;
import org.example.loader.DataResult;
import org.example.model.Carrera;
import org.example.model.Estudiante;
import org.example.repository.CarreraRepositoryImpl;
import org.example.repository.EstudianteCarreraRepositoryImpl;
import org.example.repository.EstudianteRepositoryImpl;
import org.example.service.CarreraService;
import org.example.service.EstudianteCarreraService;
import org.example.service.EstudianteService;
import org.example.utils.JPAUtil;

import java.time.Year;
import java.util.List;

public class TestAdicionales {
    public static void main(String[] args) {
        CarreraService car = new CarreraService(new CarreraRepositoryImpl());
        EstudianteService est = new EstudianteService(new EstudianteRepositoryImpl());
        EstudianteCarreraService estCar = new EstudianteCarreraService(new EstudianteCarreraRepositoryImpl(), est, car);

        DataResult resultado = new DataLoader(car, est, estCar)
                .cargar();

        imprimir("carreras", resultado.carreras());
        imprimir("estudiantes", resultado.estudiantes());
        imprimir("inscripciones", resultado.inscripciones());

        // Adicionales de Carrera
        TestDelete(car);
        TestGetCarreraById(car);
        TestGetAll(car);

        // Adicionales de Inscriptos
        TestMatricularEstudianteEnCarrera(estCar);

    }

    // ============== CARRERA ============== //

    private static void TestDelete(CarreraService service){
      CarreraRequestDTO car = new CarreraRequestDTO("Testing" , 4)
              .builder()
              .nombre("Testing")
              .duracion(3)
              .build();

      CarreraResponseDTO res = service.save(car);
      System.out.println(res.toString());

      Carrera carrera = TestCarreraByNombre(service, res.nombre());
      System.out.println(carrera.getId());

      CarreraResponseDTO eliminada = service.delete(carrera);

      System.out.println("Nombre de la carrera eliminada: " + eliminada.nombre());
    }

    private static Carrera TestCarreraByNombre(CarreraService service, String nombre){
        EntityManager em = JPAUtil.getEntityManager();
        Carrera car = service.findEntityByName(em, nombre);
        System.out.println(car.toString());
        return car;
    }

    private static void TestGetAll(CarreraService service){
        List<CarreraResponseDTO> all = service.getAll();

        if(all.isEmpty()) System.out.println("No hay carreras");

        for(CarreraResponseDTO c : all){
            System.out.println(c.toString());
        }
    }

    private static void TestGetCarreraById(CarreraService service){
            Long id = 5L;

            CarreraResponseDTO car = service.getCarreraById(id);

            System.out.println("Carrera con ID: " + id + ": " + car.toString());
    }

    // ============== ESTUDIANTE ============== //

    /*
       El único que queda sin testear es findEntityByDni que es privado
     */

    // ============== INSCRIPCION ============== //
    private static void TestMatricularEstudianteEnCarrera(EstudianteCarreraService service){
        Estudiante est = new Estudiante();
        Year inscrip = Year.now();
        Year egreso = null;

        Carrera car = new Carrera("Tecnica en Maquinaria Agricola", 5);

        EstudianteCarreraRequestDTO request = new EstudianteCarreraRequestDTO(67116086, "TUDAI", inscrip, egreso);

        EstudianteCarreraResponseDTO inscripcion = service.matricularEstudianteEnCarrera(request);

        System.out.println("Inscripción exitosa: " + inscripcion.toString());
    }

    private static void imprimir(String nombre, DataResult.Conteo conteo) {
        System.out.printf("%s: %d cargados, %d rechazados%n",
                nombre, conteo.cargados(), conteo.rechazados().size());
        conteo.rechazados().forEach(rechazo ->
                System.out.printf("  fila %d: %s%n", rechazo.fila(), rechazo.motivo()));
    }

}


