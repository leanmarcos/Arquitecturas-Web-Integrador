package org.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz base que define métodos generales que cada repositorio puede o debería tener
 * @param <T>
 * @param <ID>
 */
public interface RepositorioBase<T,ID extends Serializable> extends JpaRepository<T, ID> {
    List<T> findAll();

    Optional<T> findById(ID id);

    boolean existsById(ID id);

    void insert(T object);

    void update(T object);

    void delete(T object);

}
