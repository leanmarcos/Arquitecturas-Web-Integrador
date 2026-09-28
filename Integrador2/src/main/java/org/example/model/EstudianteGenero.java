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

    private final String etiqueta;
    private final String valorCsv;

    EstudianteGenero(String etiqueta, String valorCsv) {
        this.etiqueta = etiqueta;
        this.valorCsv = valorCsv;
    }

    /**
     * Nombre en español para mostrar (ej: "Femenino").
     */
    public String getEtiqueta() {
        return etiqueta;
    }

    /**
     * Convierte un texto (ej: "Female", "femenino") al género correspondiente, sin distinguir mayúsculas.
     * <p>
     * Acepta el valor en inglés del CSV y también la etiqueta en español, así una búsqueda por
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
                .filter(genero -> genero.valorCsv.equalsIgnoreCase(buscado)
                        || genero.etiqueta.equalsIgnoreCase(buscado))
                .findFirst();
    }
}
