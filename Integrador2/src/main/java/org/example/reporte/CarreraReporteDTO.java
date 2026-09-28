package org.example.reporte;

import lombok.Builder;
import org.example.dto.EstudianteResponseDTO;

import java.time.Year;
import java.util.List;

/**
 * Inscriptos y egresados de una carrera en un año puntual.
 */
@Builder
public record CarreraReporteDTO(
        CarreraAnioKey carreraAnioKey,
        List<EstudianteResponseDTO> inscriptos,
        List<EstudianteResponseDTO> egresados
) {

    public record CarreraAnioKey(String carrera, Year anio) {}

    public int getCantidadInscriptos() {
        return inscriptos.size();
    }

    public int getCantidadEgresados() {
        return egresados.size();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(carreraAnioKey.carrera()).append(" - ").append(carreraAnioKey.anio()).append("\n");
        sb.append("  Inscriptos (").append(getCantidadInscriptos()).append("):\n");
        agregarEstudiantes(sb, inscriptos);
        sb.append("  Egresados (").append(getCantidadEgresados()).append("):\n");
        agregarEstudiantes(sb, egresados);
        return sb.toString();
    }

    private static void agregarEstudiantes(StringBuilder sb, List<EstudianteResponseDTO> estudiantes) {
        if (estudiantes.isEmpty()) {
            sb.append("    (ninguno)\n");
            return;
        }

        for (EstudianteResponseDTO e : estudiantes) {
            sb.append("    - ").append(e.apellido()).append(", ").append(e.nombres())
                    .append(" | LU ").append(e.lu())
                    .append(" | DNI ").append(e.dni())
                    .append(" | ").append(e.edad()).append(" años")
                    .append(" | ").append(e.genero().getEtiqueta())
                    .append(" | ").append(e.ciudadResidencia())
                    .append("\n");
        }
    }
}
