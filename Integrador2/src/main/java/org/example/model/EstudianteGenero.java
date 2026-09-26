package org.example.model;

import java.util.Arrays;
import java.util.Optional;

public enum EstudianteGenero {
    MALE("Male", "Masculino"),
    FEMALE("Female", "Femenino"),
    POLYGENDER("Polygender"),
    GENDERFLUID("Genderfluid"),
    AGENDER("Agender"),
    BIGENDER("Bigender"),
    NON_BINARY("Non-binary");

    private final String[] valores;

    EstudianteGenero(String... valores) {
        this.valores = valores;
    }

    /**
     * Convierte un texto (ej: "Female", "femenino") al género correspondiente, sin distinguir mayúsculas.
     * <p>
     * Acepta los valores en inglés del CSV y también "Masculino" / "Femenino", así una búsqueda por
     * "Femenino" encuentra a los estudiantes cargados como "Female".
     *
     * @param valor texto a convertir
     * @return el género, o {@link Optional#empty()} si el texto no corresponde a ninguno
     */
    public static Optional<EstudianteGenero> from(String valor) {
        if (valor == null) {
            return Optional.empty();
        }
        String buscado = valor.trim();
        return Arrays.stream(values())
                .filter(genero -> Arrays.stream(genero.valores).anyMatch(buscado::equalsIgnoreCase))
                .findFirst();
    }
}
