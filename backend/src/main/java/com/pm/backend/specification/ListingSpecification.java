package com.pm.backend.specification;

import com.pm.backend.dto.ListingFilterCriteriaDTO;
import com.pm.backend.model.Listing;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class ListingSpecification {

    public static Specification<Listing> withFilters(ListingFilterCriteriaDTO filters) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Price range filtering
            if (filters.getMinPrice() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), filters.getMinPrice()));
            }
            if (filters.getMaxPrice() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), filters.getMaxPrice()));
            }

            // Area range filtering
            if (filters.getMinArea() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("area"), filters.getMinArea()));
            }
            if (filters.getMaxArea() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("area"), filters.getMaxArea()));
            }

            // Cities filtering
            if (filters.getCities() != null && !filters.getCities().isEmpty()) {
                predicates.add(root.get("addressCity").in(filters.getCities()));
            }

            // Zip codes filtering
            if (filters.getZipCodes() != null && !filters.getZipCodes().isEmpty()) {
                predicates.add(root.get("addressZipcode").in(filters.getZipCodes()));
            }

            // Bedrooms filtering
            if (filters.getBeds() != null) {
                predicates.add(criteriaBuilder.equal(root.get("beds"), filters.getBeds()));
            }

            // Bathrooms filtering
            if (filters.getBaths() != null) {
                predicates.add(criteriaBuilder.equal(root.get("baths"), filters.getBaths()));
            }

            // Availability date filtering (available by specified date)
            if (filters.getAvailableBy() != null) {
                predicates.add(criteriaBuilder.or(
                    criteriaBuilder.isNull(root.get("availabilityDate")),
                    criteriaBuilder.lessThanOrEqualTo(root.get("availabilityDate"), filters.getAvailableBy())
                ));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
