package org.vitaliistf.twowheels4u.repository.specification;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.vitaliistf.twowheels4u.model.Motorcycle;

@Component
public class MotorcycleSpecificationManager implements SpecificationManager<Motorcycle> {
    private final Map<String, SpecificationProvider<Motorcycle>> providerMap;

    @Autowired
    public MotorcycleSpecificationManager(List<SpecificationProvider<Motorcycle>> providerList) {
        this.providerMap = providerList.stream()
                .collect(Collectors.toMap(SpecificationProvider::getFilter, Function.identity()));
    }

    @Override
    public Specification<Motorcycle> get(String filterKey, String[] params) {
        if (!providerMap.containsKey(filterKey)) {
            throw new RuntimeException("Key is not supported for data filtering:" + filterKey);
        }
        return providerMap.get(filterKey).getSpecification(params);
    }
}
