package org.example.taskmanagementapi.repositories.specifications;

import org.example.taskmanagementapi.entities.Task;
import org.example.taskmanagementapi.entities.User;
import org.springframework.data.jpa.domain.Specification;

public class TaskSpecification {
    public static Specification<Task> assignToEquals(Long userId) {
        return (root,query,criteriaBuilder) -> {
            if (userId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("user").get("id"), userId);
        };
    }

    public static Specification<Task> statusEquals(String status) {
        return (root,query,criteriaBuilder) -> {
            if (status == null || status.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("status"), status.toUpperCase());
        };
    }

    public static Specification<Task> priorityEquals(String priority) {
        return (root,query,criteriaBuilder) -> {
            if (priority == null || priority.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("priority"), priority.toUpperCase());
        };
    }
}
