package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.repository.EstudianteCarreraRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EstudianteCarreraService {

    private final EstudianteCarreraRepository repository;
}
