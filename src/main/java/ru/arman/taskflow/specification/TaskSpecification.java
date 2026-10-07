package ru.arman.taskflow.specification;

import org.springframework.data.jpa.domain.Specification;
import ru.arman.taskflow.model.Task;
import ru.arman.taskflow.model.TaskStatus;


public class TaskSpecification {

    private TaskSpecification() {
    }

    public static Specification<Task> hasStatus(TaskStatus status) {
        return (root, query, criteriaBuilder) -> {
            if (status == null) return null;
            return criteriaBuilder.equal(root.get("status"), status);
        };
    }

    public static Specification<Task> titleContains(String keyword) {
        return (root, query, criteriaBuilder) -> {
            if (keyword == null || keyword.isBlank()) return null;
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + keyword.toLowerCase() + "%");
        };
    }

    public static Specification<Task> hasAssignee(Long assigneeId) {
        return (root, query, criteriaBuilder) -> {
            if (assigneeId == null) return null;
            return criteriaBuilder.equal(root.get("assignee").get("id"), assigneeId);
        };
    }
}
