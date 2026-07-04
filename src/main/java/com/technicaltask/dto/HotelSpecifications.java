package com.technicaltask.dto;

import com.technicaltask.model.Hotel;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public final class HotelSpecifications {

    private HotelSpecifications() {
    }

    public static Specification<Hotel> byFilters(String name, String brand, String city, String country, List<String> amenities) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            addLikePredicate(predicates, criteriaBuilder.lower(root.get("name")), name, criteriaBuilder);
            addLikePredicate(predicates, criteriaBuilder.lower(root.get("brand")), brand, criteriaBuilder);
            addLikePredicate(predicates, criteriaBuilder.lower(root.get("address").get("city")), city, criteriaBuilder);
            addLikePredicate(predicates, criteriaBuilder.lower(root.get("address").get("country")), country, criteriaBuilder);
            if (amenities != null && !amenities.isEmpty()) {
                if (query != null) {
                    query.distinct(true);
                }
                Join<Hotel, String> amenitiesJoin = root.join("amenities", JoinType.INNER);
                Expression<String> amenityValue = criteriaBuilder.lower(amenitiesJoin);
                List<Predicate> amenityPredicates = amenities.stream()
                        .filter(value -> value != null && !value.isBlank())
                        .map(value -> criteriaBuilder.like(amenityValue, contains(value)))
                        .toList();
                if (!amenityPredicates.isEmpty()) {
                    predicates.add(criteriaBuilder.or(amenityPredicates.toArray(Predicate[]::new)));
                }
            }
            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        };
    }

    private static void addLikePredicate(List<Predicate> predicates, Expression<String> expression, String value, jakarta.persistence.criteria.CriteriaBuilder criteriaBuilder) {
        if (value != null && !value.isBlank()) {
            predicates.add(criteriaBuilder.like(expression, contains(value)));
        }
    }

    private static String contains(String value) {
        return "%" + value.trim().toLowerCase() + "%";
    }
}
