package org.vitaliistf.twowheels4u.repository.specification.motorcycle;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.vitaliistf.twowheels4u.model.Motorcycle;
import org.vitaliistf.twowheels4u.repository.specification.SpecificationProvider;

@Component
public class MotorcycleTypeInSpecification implements SpecificationProvider<Motorcycle> {
    private static final String FIELD_NAME = "type";

    @Override
    public Specification<Motorcycle> getSpecification(String[] motorcycleTypes) {
        return ((root, query, cb) -> {
            CriteriaBuilder.In<Motorcycle.Type> predicate = cb.in(root.get(FIELD_NAME));
            for (String value : motorcycleTypes) {
                predicate.value(Motorcycle.Type.valueOf(value));
            }
            return cb.and(predicate);
        });
    }

    @Override
    public String getFilter() {
        return FIELD_NAME;
    }
}
