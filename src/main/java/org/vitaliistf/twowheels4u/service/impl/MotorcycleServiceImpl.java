package org.vitaliistf.twowheels4u.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.vitaliistf.twowheels4u.model.Motorcycle;
import org.vitaliistf.twowheels4u.repository.MotorcycleRepository;
import org.vitaliistf.twowheels4u.service.MotorcycleService;

@Service
@RequiredArgsConstructor
public class MotorcycleServiceImpl implements MotorcycleService {

    private final MotorcycleRepository motorcycleRepository;

    @Override
    public Motorcycle save(Motorcycle motorcycle) {
        return motorcycleRepository.save(motorcycle);
    }

    @Override
    public List<Motorcycle> findAllByParams(Map<String, String> params) {
        //TODO: complete this method
        return Collections.emptyList();
    }

    @Override
    public Page<Motorcycle> findAll(Pageable pageable) {
        return motorcycleRepository.findAllByDeletedFalse(pageable);
    }

    @Override
    public Motorcycle findById(Long id) {
        return motorcycleRepository.findByIdAndDeletedFalse(id).orElseThrow(()
                -> new NoSuchElementException("Can't find motorcycle with id: " + id));
    }

    @Override
    public Motorcycle update(Long id, Motorcycle motorcycle) {
        Motorcycle motorcycleFromDb = findById(id);
        motorcycleFromDb.setType(motorcycle.getType());
        motorcycleFromDb.setManufacturer(motorcycle.getManufacturer());
        motorcycleFromDb.setInventory(motorcycle.getInventory());
        motorcycleFromDb.setFee(motorcycle.getFee());
        motorcycleFromDb.setModel(motorcycle.getModel());
        motorcycleFromDb.setDeleted(motorcycle.isDeleted());
        return motorcycleRepository.save(motorcycleFromDb);
    }

    @Override
    public void delete(Long id) {
        motorcycleRepository.safeDelete(id);
    }

    @Override
    public Motorcycle addMotorcycleToInventory(Long id) {
        Motorcycle motorcycle = findById(id);
        motorcycle.setInventory(motorcycle.getInventory() + 1);
        return motorcycleRepository.save(motorcycle);
    }

    @Override
    public Motorcycle removeMotorcycleFromInventory(Long id) {
        Motorcycle motorcycle = findById(id);
        if (motorcycle.getInventory() > 0) {
            motorcycle.setInventory(motorcycle.getInventory() - 1);
            return motorcycleRepository.save(motorcycle);
        }
        throw new NoSuchElementException("Can't remove motorcycle with id: " + id);
    }
}