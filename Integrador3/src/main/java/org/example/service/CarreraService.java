package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.repository.CarreraRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CarreraService {

    private final CarreraRepository repository;
}
