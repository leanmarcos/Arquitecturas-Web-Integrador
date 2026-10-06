package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.exceptions.CarreraNotFoundException;
import org.example.model.Carrera;
import org.example.repository.CarreraRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CarreraService {

    private final CarreraRepository repository;

    public Carrera findEntityById(Long id){
        return repository.findById(id)
                .orElseThrow(() -> new CarreraNotFoundException(id));
    }
}
