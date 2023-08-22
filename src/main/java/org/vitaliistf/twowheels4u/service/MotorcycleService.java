package org.vitaliistf.twowheels4u.service;

import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.vitaliistf.twowheels4u.models.Motorcycle;

public interface MotorcycleService {
    Motorcycle save(Motorcycle motorcycle);

    List<Motorcycle> findAllByParams(Map<String, String> params);

    Page<Motorcycle> findAll(Pageable pageable);

    Motorcycle findById(Long id);

    Motorcycle update(Long id, Motorcycle motorcycle);

    void delete(Long id);

    Motorcycle addMotorcycleToInventory(Long id);

    Motorcycle removeMotorcycleFromInventory(Long id);
}
