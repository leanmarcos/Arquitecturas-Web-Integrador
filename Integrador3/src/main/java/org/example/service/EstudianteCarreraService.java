package org.example.service;

import org.example.repository.EstudianteCarreraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EstudianteCarreraService {

    @Autowired
    private EstudianteCarreraRepository repository;
}
