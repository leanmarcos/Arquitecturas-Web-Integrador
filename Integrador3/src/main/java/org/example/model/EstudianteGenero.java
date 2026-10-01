package org.example.model;

import java.util.Arrays;
import java.util.Optional;

public enum EstudianteGenero {
    MALE("Masculino", "Male"),
    FEMALE("Femenino", "Female"),
    POLYGENDER("Poligénero", "Polygender"),
    GENDERFLUID("Género fluido", "Genderfluid"),
    AGENDER("Agénero", "Agender"),
    BIGENDER("Bigénero", "Bigender"),
    NON_BINARY("No binario", "Non-binary");

    /** Nombre en español, el que se muestra. */
    private final String nombre;
    /** Nombre en inglés, como viene en el CSV. */
    private final String nombreEnIngles;

    EstudianteGenero(String nombre, String nombreEnIngles) {
        this.nombre = nombre;
        this.nombreEnIngles = nombreEnIngles;
    }

    public String getNombre() {
        return nombre;
    }

    /**
     * Convierte un texto (ej: "Female", "femenino") al género correspondiente, sin distinguir mayúsculas.
     * <p>
     * Acepta el nombre en inglés del CSV y el nombre en español, así una búsqueda por "Femenino" encuentra a los
     * estudiantes cargados como "Female".
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
                .filter(genero -> genero.nombre.equalsIgnoreCase(buscado)
                        || genero.nombreEnIngles.equalsIgnoreCase(buscado))
                .findFirst();
    }
}
