package com.example.rd.autocode.assessment.appliances.manufacturer.manage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ManageManufacturerService {
    private final ManufacturerRepository repository;
    public void create(String name) {
        repository.save(Manufacturer.create(name));
    }

    public void edit(Long id, String name) {
        Manufacturer manufacturer = repository.findById(id).orElseThrow(ManufacturerNotFound::new);
        manufacturer.setName(name);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public List<Manufacturer> findAll() {
        return repository.findAll();
    }

    public Manufacturer findById(Long id) {
        return repository.findById(id).orElseThrow(ManufacturerNotFound::new);
    }
}
