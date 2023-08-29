package org.vitaliistf.twowheels4u.repository.specification.motorcycle;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.vitaliistf.twowheels4u.model.Motorcycle;
import org.vitaliistf.twowheels4u.repository.specification.SpecificationProvider;

@Component
public class MotorcycleModelInSpecification implements SpecificationProvider<Motorcycle> {
    private static final String FIELD_NAME = "model";

    @Override
    public Specification<Motorcycle> getSpecification(String[] models) {
        return ((root, query, cb) -> {
            CriteriaBuilder.In<String> predicate = cb.in(root.get(FIELD_NAME));
            for (String value : models) {
                predicate.value(value);
            }
            return cb.and(predicate, predicate);
        });
    }

    @Override
    public String getFilter() {
        return FIELD_NAME;
    }
}
