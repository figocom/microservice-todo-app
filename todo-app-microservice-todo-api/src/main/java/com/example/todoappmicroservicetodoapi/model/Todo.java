package com.example.todoappmicroservicetodoapi.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class Todo extends Auditable {
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean completed;
    @Column(nullable = false)
    private Long createdBy;
}
