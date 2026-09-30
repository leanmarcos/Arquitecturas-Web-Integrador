package org.example.utils;

import org.hibernate.exception.ConstraintViolationException;

public class PersistenceUtils {

    private PersistenceUtils() {
    }

    /**
     * Indica si el error vino de una restricción unique (o de una clave primaria repetida).
     * <p>
     * Busca en la cadena de causas porque, según cómo la convierta el {@code EntityManager}, la excepción de
     * Hibernate puede llegar envuelta en una {@code PersistenceException}.
     */
    public static boolean esViolacionDeUnique(Throwable e) {
        for (Throwable causa = e; causa != null; causa = causa.getCause()) {
            if (causa instanceof ConstraintViolationException cve
                    && cve.getKind() == ConstraintViolationException.ConstraintKind.UNIQUE) {
                return true;
            }
        }
        return false;
    }
}
